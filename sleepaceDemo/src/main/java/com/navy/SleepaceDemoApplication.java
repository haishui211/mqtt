package com.navy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.navy.sleepace.MqttConnectClient;

@SpringBootApplication
public class SleepaceDemoApplication implements CommandLineRunner{
	
	@Autowired
	private MqttConnectClient mqttConnectClient;
	
	public static void main(String[] args) {
		SpringApplication.run(SleepaceDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					mqttConnectClient.connect();
					new MqttRetryConnectTimmer().retryMqttClient(mqttConnectClient);
				} 
				catch (Exception e) {
					//
				}
			}
		};
		new Thread(runnable).start();
	}
}
