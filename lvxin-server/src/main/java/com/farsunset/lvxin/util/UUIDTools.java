
package com.farsunset.lvxin.util;

import java.util.UUID;

public class UUIDTools {

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
