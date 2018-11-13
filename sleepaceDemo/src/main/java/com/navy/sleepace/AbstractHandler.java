package com.navy.sleepace;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public abstract class AbstractHandler {
	protected static AtomicInteger messageId = new AtomicInteger(); 
	private MqttConnectClient connect;
	private String channelId;
	public AbstractHandler(MqttConnectClient connect){
		this.connect = connect;
		this.channelId = connect.getChannelId();
	}
	protected void publish(String topic,String message) throws MqttPersistenceException, MqttException{
		connect.publish(topic, message, false);
		
	}
	protected void subscribe(String topic) throws MqttException {
		connect.subscribe(topic);
	}
	
	public MqttConnectClient getConnect() {
		return connect;
	}
	public void setConnect(MqttConnectClient connect) {
		this.connect = connect;
	}
	
	protected String requestTopic(String deviceId,int deviceType,int leftRight,String apiKey){
		return SleepaceConstants.PUB_TOPIC_REQ+channelId+"/"+deviceType+"/"+deviceId+"/"+leftRight+"/"+apiKey;
	}
	protected String responseTopic(String deviceId,int deviceType,int leftRight,String apiKey){
		return SleepaceConstants.PUB_TOPIC_RESP+channelId+"/"+deviceType+"/"+deviceId+"/"+leftRight+"/"+apiKey;
	}	

}
