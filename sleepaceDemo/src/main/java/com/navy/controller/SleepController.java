package com.navy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.navy.util.SleepaceDataUtil;

@RestController
public class SleepController {
	
	@RequestMapping("/sleep/get")
	public Object get() throws Exception {
		return SleepaceDataUtil.getSleepList();
	}
}
