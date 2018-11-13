package com.navy.sleepace;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.alibaba.fastjson.JSON;

public class RequestHandler extends AbstractHandler {

	public RequestHandler(MqttConnectClient connect) {
		super(connect);
	}
	
	/**
	 * 往mqtt发布消息
	 * @param apiKey
	 * @param device
	 * @param data
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void handlerPublish(String apiKey,Device device,Map<String,Object> data) throws MqttPersistenceException, MqttException {
		RequestMessage message = new RequestMessage();
		message.setMessageId(super.messageId.getAndIncrement());
		message.setData(data);
		super.publish(super.requestTopic(device.getDeviceId(), device.getDeviceType(),device.getLeftRight(),apiKey),  JSON.toJSONString(message));
	}
	
	/**
	 * 从mqtt订阅消息
	 * @param apiKey
	 * @param device
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void handlerSubscribe(String apiKey,Device device) throws MqttPersistenceException, MqttException {
		String subscribeTopic = super.requestTopic(device.getDeviceId(), device.getDeviceType(),device.getLeftRight(),apiKey);
		super.subscribe(subscribeTopic);
	}
}
