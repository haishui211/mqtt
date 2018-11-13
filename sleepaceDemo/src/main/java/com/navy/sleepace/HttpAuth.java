package com.navy.sleepace;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class HttpAuth {
	public static <T> T httpPost(String url,Map<String,String> params,TypeReference<T> dataType){
		Map<String,String> header = new HashMap<String,String>();
		header.put("lang", "zh");
		String response = HttpClientUtil.httpPost(url, header, params);
		return JSON.parseObject(response, dataType,null);
	}
	
	public static <T> T httpGet(String url,TypeReference<T> dataType){
		Map<String,String> header = new HashMap<String,String>();
		header.put("lang", "zh");
		String response = HttpClientUtil.httpGet(url, header,null);
		return JSON.parseObject(response, dataType, null);
	}
	
	public static <T> T httpDelete(String url,TypeReference<T> dataType){
		Map<String,String> header = new HashMap<String,String>();
		header.put("lang", "zh");
		String response = HttpClientUtil.httpDeleteCustomize(url, header,null);
		return JSON.parseObject(response, dataType,null);
	}
	
	
}
