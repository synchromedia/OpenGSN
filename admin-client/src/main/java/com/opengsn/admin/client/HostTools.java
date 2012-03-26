package com.opengsn.admin.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

public class HostTools implements AdminTool {
	CouchDbClient dbClient = new CouchDbClient();

	@Override
	public void create() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "engine-instance");
		Response resp = dbClient.save(map);
		System.out.println(resp.getId());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void list() {
		// TODO Auto-generated method stub
		
	}
}
