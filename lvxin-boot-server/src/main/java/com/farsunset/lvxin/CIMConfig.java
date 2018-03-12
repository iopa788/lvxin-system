package com.farsunset.lvxin;

import java.io.IOException;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.farsunset.cim.sdk.server.handler.CIMNioSocketAcceptor;
import com.farsunset.lvxin.cim.handler.BindHandler;
import com.farsunset.lvxin.cim.handler.LocationHandler;
import com.farsunset.lvxin.cim.handler.ModifyLogoHandler;
import com.farsunset.lvxin.cim.handler.ModifyProfileHandler;
import com.farsunset.lvxin.cim.handler.SessionClosedHandler;

@Configuration
public class CIMConfig {
	private CIMNioSocketAcceptor nioSocketAcceptor;
	@Value("${cim.server.port}")
	private int port;

	@Autowired
	private BindHandler bindHandler;
	@Autowired
	private SessionClosedHandler closedHandler;
	@Autowired
	private LocationHandler locationHandler;
	@Autowired
	private ModifyProfileHandler profileHandler;
	@Autowired
	private ModifyLogoHandler logoHandler;

	@PostConstruct
	private void initCimService() throws IOException {
		nioSocketAcceptor = new CIMNioSocketAcceptor();
		nioSocketAcceptor.setPort(port);
		nioSocketAcceptor.addHandler("client_bind", bindHandler);
		nioSocketAcceptor.addHandler("client_closed", closedHandler);
		nioSocketAcceptor.addHandler("client_cycle_location", locationHandler);
		nioSocketAcceptor.addHandler("client_modify_profile", profileHandler);
		nioSocketAcceptor.addHandler("client_modify_logo", logoHandler);
		nioSocketAcceptor.bind();
	}

	@Bean
	public CIMNioSocketAcceptor getCimSocketAcceptor() {
		return nioSocketAcceptor;
	}

}