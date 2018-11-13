package com.navy;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.navy.sleepace.MqttConnectClient;

/**
 * mqtt重连操作
 * @author haishui211
 */
public class MqttRetryConnectTimmer {
	
	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 每隔20小时重连一次
	 */
//	private final long durations = 1000*60*60*20;
	
	private final long durations = 1000*20;
	
	public void retryMqttClient(MqttConnectClient mqttConnectClient) {
		Timer reconnectTimer = new Timer("MQTT reconnect" );
		TimerTask task = new RetryConnectTask(mqttConnectClient);
    	reconnectTimer.schedule(task, durations);
	}
	
	private class RetryConnectTask extends TimerTask{
		
		private MqttConnectClient mqttConnectClient;
		
		public RetryConnectTask(MqttConnectClient mqttConnectClient) {
			this.mqttConnectClient = mqttConnectClient;
		}
		
		@Override
		public void run() {
			logger.info("retryMqttClient once...");
//			this.mqttConnectClient.connect();
		}
	}
}
