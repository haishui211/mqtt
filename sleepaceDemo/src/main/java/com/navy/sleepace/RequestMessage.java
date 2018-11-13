package com.navy.sleepace;

import java.util.Map;


public class RequestMessage {
	private long messageId;
	private Map<String,Object> data;
	
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}


	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	
	
	
}
