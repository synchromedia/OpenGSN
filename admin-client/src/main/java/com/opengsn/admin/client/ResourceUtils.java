package com.opengsn.admin.client;

import java.util.HashMap;
import java.util.Map;

import org.lightcouch.Response;

public class ResourceUtils {
	public static Map<String, Object> createBaseResource(String name, String desc) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("type", "engine-instance");
		map.put("description", desc);
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("reconnection_delay", "10");
		settings.put("refresh_execution", "Refresh");
		settings.put("polling_interval", "180");
		map.put("settings",settings);
		return map;
	}
}
