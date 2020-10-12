package com.mi6.utils;

import java.text.SimpleDateFormat;

public class Util {
	
	private static String formatCommonDateTime = "HH:mm:ss, dd-MM-yyyy";

	public static SimpleDateFormat getCommonDateTimeFormat() {
		return new SimpleDateFormat(formatCommonDateTime);
	}

}
