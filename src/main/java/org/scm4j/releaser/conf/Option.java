package org.scm4j.releaser.conf;

import java.util.HashMap;
import java.util.Map;

public enum Option {
	DELAYED_TAG("--delayed-tag"), STACK_TRACE("--stacktrace");
	
	private static final Map<String, Option> map = new HashMap<>();

	static {
		for (Option option : Option.values()) {
			map.put(option.strValue, option);
		}
	}
	
	private final String strValue;
	
	Option(String strValue) {
		this.strValue = strValue;
	}
	
	public static Map<String, Option> getArgsMap() {
		return map;
	}
	
	public String getStrValue() {
		return strValue;
	}

	public static boolean isValid(String strValue) {
		return map.containsKey(strValue);
	}
}
