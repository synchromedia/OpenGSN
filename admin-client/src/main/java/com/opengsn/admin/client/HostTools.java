package com.opengsn.admin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;

public class HostTools implements AdminTool {
	CouchDbClient dbClient = new CouchDbClient();

	@Override
	public void create() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("What is the name for the host:");
			String name = br.readLine();
			System.out.println("Enter the host description:");
			String desc = br.readLine();
			Map<String, Object> map = ResourceUtils.createBaseResource(name, desc);
			map.put("typeId", "libvirt");
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("os", "ubuntu");
			System.out.println("Enter the host location:");
			String location = br.readLine();
			model.put("location", location);
			model.put("rootStoragePath", "/storage");
			map.put("model",model);
			Map<String,Object> module = new HashMap<String,Object>();
			module.put("name", "remoteShell");
			module.put("moduleId", "http://www.iaasframework.com/RemoteShell");
			Map<String,Object> modSettings = new HashMap<String,Object>();
			System.out.println("Enter the host username:");
			String uname = br.readLine();
			modSettings.put("username",uname);
			System.out.println("Enter the host password:");
			String pass = br.readLine();
			modSettings.put("password", pass);
			System.out.println("Enter the host ip:");
			String ip = br.readLine();
			modSettings.put("host", ip);
			modSettings.put("port", "22");
			modSettings.put("transport", "ssh");
			System.out.println("Enter the host default prompt:");
			String prompt = br.readLine();
			modSettings.put("prompts", prompt);
			module.put("settings", modSettings);
			List<Object> list = new ArrayList<Object>();
			map.put("modules",list);
			Response resp = dbClient.save(map);
		} catch (IOException exp) {

		}
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
