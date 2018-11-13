package com.navy.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.navy.util.ClientMQTT;

@RestController
public class DemoController {
	
	private static ClientMQTT client;
	static {
		client = new ClientMQTT();
		client.start();
	}
	
	@RequestMapping("/disconnect")
	public Object disconnect() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			client.destroy();
			map.put("destroy", "ok");
		}
		catch(Exception e) {
			map.put("error", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping("/status")
	public Object status() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			map.put("status", client.status());
		}
		catch(Exception e) {
			map.put("error", e.getMessage());
		}
		return map;
	}
}
