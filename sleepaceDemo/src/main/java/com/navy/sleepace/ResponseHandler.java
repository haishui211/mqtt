package com.navy.sleepace;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.alibaba.fastjson.JSON;

public class ResponseHandler extends AbstractHandler {

	public ResponseHandler(MqttConnectClient connect) {
		super(connect);
	}

	public void handler(String topic,RequestMessage recMsg) throws MqttPersistenceException, MqttException {
		ResponseMessage message = new ResponseMessage();
		message.setMessageId(recMsg.getMessageId());
		String[] topicStrArr = topic.split("/");
		super.publish(super.responseTopic(topicStrArr[4], Integer.valueOf(topicStrArr[3]),Integer.valueOf(topicStrArr[5]),topicStrArr[topicStrArr.length-1]), JSON.toJSONString(message));

	}

}
