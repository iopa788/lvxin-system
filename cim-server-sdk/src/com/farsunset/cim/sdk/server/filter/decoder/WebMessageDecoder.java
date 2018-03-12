/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.cim.sdk.server.filter.decoder;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.cim.sdk.server.model.HeartbeatResponse;
import com.farsunset.cim.sdk.server.model.SentBody;
import com.google.gson.Gson;

/**
 * 服务端接收消息解码
 */
public class WebMessageDecoder extends MessageDecoderAdapter {
	public static final Pattern SEC_KEY_PATTERN = Pattern.compile("^(Sec-WebSocket-Key:).+",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	protected final Logger logger = Logger.getLogger(WebMessageDecoder.class.getName());

	@Override
	public MessageDecoderResult decodable(IoSession arg0, IoBuffer iobuffer) {
		if (iobuffer.remaining() < 2) {
			return NEED_DATA;
		}

		 /**
	     * 原生SDK只会发送2种类型消息 1个心跳类型 另一个是sendbody，报文的第一个字节为消息类型
	     * 如果非原生sdk发出的消息，则认为是websocket发送的消息
	     * websocket发送的消息 第一个字节不可能等于C_H_RS或者SENTBODY
	     */
		byte conetnType = iobuffer.get();
		if (conetnType == CIMConstant.ProtobufType.C_H_RS || conetnType == CIMConstant.ProtobufType.SENTBODY) {
			return NOT_OK;
		}
		return OK;
	}

	
	public boolean tryWebSockeHandShake(IoSession session, IoBuffer in, ProtocolDecoderOutput out) {
		 try{
			    byte[] array = new byte[in.limit()];
			    in.get(array);
	            String payLoadMsg = new String(array);
	            String socketKey = null;
	            Matcher m = SEC_KEY_PATTERN.matcher(payLoadMsg);
	    		if (m.find()) {
	    			String foundstring = m.group();
	    			socketKey=foundstring.split(":")[1].trim();
	    		}
	            if(socketKey == null || socketKey.length() <= 0){
	                return false;
	            }
	            
	            SentBody body = new SentBody();
	    		body.setKey(CIMNioSocketAcceptor.WEBSOCKET_HANDLER_KEY);
    			body.put("key", socketKey);

	    		out.write(body);
	            return true;
	        }
	        catch(Exception e){
	            return false;
	        }        
		
	}
	@Override
	public MessageDecoderResult decode(IoSession iosession, IoBuffer in, ProtocolDecoderOutput out)throws Exception {
		in.mark();
		boolean isHandshake = tryWebSockeHandShake(iosession,in,out);
		if(isHandshake) {
			return OK;
		}
		in.reset();
		IoBuffer dataFrame = decodeDataFrame(in,iosession);
		if(dataFrame==null) {
			in.reset();
			return NEED_DATA;
		}
		handleSentBodyAndHeartPing(dataFrame.array(),out);
		return OK;
	}
	
	 private  IoBuffer decodeDataFrame(IoBuffer in, IoSession session) {

	        IoBuffer resultBuffer = null;
	        do{
	            in.get();            
	            int frameLen = (in.get() & (byte) 0x7F);
	            if(frameLen == 126){
	                frameLen = in.getShort();
	            }
	            
	            // Validate if we have enough data in the buffer to completely
	            // parse the WebSocket DataFrame. If not return null.
	            if(frameLen+4 > in.remaining()){                
	                return null;
	            }
	            byte mask[] = new byte[4];
	            for (int i = 0; i < 4; i++) {
	                mask[i] = in.get();
	            }

	            /*  now un-mask frameLen bytes as per Section 5.3 RFC 6455
	                Octet i of the transformed data ("transformed-octet-i") is the XOR of
	                octet i of the original data ("original-octet-i") with octet at index
	                i modulo 4 of the masking key ("masking-key-octet-j"):

	                j                   = i MOD 4
	                transformed-octet-i = original-octet-i XOR masking-key-octet-j
	            * 
	            */
	             
	            byte[] unMaskedPayLoad = new byte[frameLen];
	            for (int i = 0; i < frameLen; i++) {
	                byte maskedByte = in.get();
	                unMaskedPayLoad[i] = (byte) (maskedByte ^ mask[i % 4]);
	            }
	            
	            if(resultBuffer == null){
	                resultBuffer = IoBuffer.wrap(unMaskedPayLoad);
	                resultBuffer.position(resultBuffer.limit());
	                resultBuffer.setAutoExpand(true);
	            }
	            else{
	                resultBuffer.put(unMaskedPayLoad);
	            }
	        }
	        while(in.hasRemaining());
	        
	        resultBuffer.flip();
	        return resultBuffer;

	    }     
	
	public void handleSentBodyAndHeartPing(byte[] data,ProtocolDecoderOutput out) throws UnsupportedEncodingException   {
		String message = new String(data, "UTF-8");
	 
		/**
		 * 只处理心跳响应以及，sentbody消息
		 */
		if (HeartbeatResponse.CMD_HEARTBEAT_RESPONSE.equals(message)) {
			HeartbeatResponse response = HeartbeatResponse.getInstance();
			logger.info(response.toString());
			out.write(response);
		}else if(data.length > 2)
		{
			SentBody body = new Gson().fromJson(message, SentBody.class);
			logger.info(body.toString());
			out.write(body);
		}
 
	}
}
