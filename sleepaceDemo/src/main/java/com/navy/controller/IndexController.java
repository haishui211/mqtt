package com.navy.controller;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.navy.sleepace.SleepaceConstants;
import com.navy.component.SleepaceMqttComponent;
import com.navy.sleepace.Device;
import com.navy.sleepace.MqttConnectClient;
import com.navy.sleepace.RequestHandler;

@RestController
public class IndexController {
	
	@Autowired
	private MqttConnectClient mqttConnectClient;
	
	@Autowired
	private SleepaceMqttComponent sleepaceMqttComponent;
	
	String deviceId = "ipof3rdnvto65";
	
	@GetMapping("/queryBindInfo")
	public Object queryBindInfo(String userCode) {
		return sleepaceMqttComponent.queryBindInfo(userCode);
	}
	
	@GetMapping("/bind")
	public Object bind(String userCode) {
		return sleepaceMqttComponent.bindSleepaceDevice(userCode, deviceId);
	}
	
	@GetMapping("/unbind")
	public Object unbind(String userCode) {
		return sleepaceMqttComponent.unBindSleepaceDevice(userCode, deviceId);
	}
	
	@GetMapping("/queryDeviceConnectStatus")
	public void queryDeviceConnectStatus() throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId("ipof3rdnvto65");
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.DEVICE_CONNECTION_STATUS, device, null);
	}
	
	@GetMapping("/queryDeviceWorkStatus")
	public void queryDeviceWorkStatus() throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId("ipof3rdnvto65");
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.WORK_STATUS, device, null);
	}
	
	@GetMapping("/viewRealTimeData")
	public void viewRealTimeData() throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId("ipof3rdnvto65");
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.VIEW_REALTIME_DATA, device, null);
	}
	
	@GetMapping("/startMonitor")
	public void startMonitor() throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId("ipof3rdnvto65");
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.START_MONITOR, device, null);
	}
	
	@GetMapping("/startMonitorAuto")
	public void startMonitorAuto() throws MqttPersistenceException, MqttException {
		sleepaceMqttComponent.startMonitorAuto(deviceId);
	}
	
	@GetMapping("/stopMonitor")
	public void stopMonitor() throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId("ipof3rdnvto65");
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.STOP_MONITOR, device, null);
	}
	
	@GetMapping("/analysisData")
	public void analysisData() throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId("ipof3rdnvto65");
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.ANALYSIS_DATA, device, null);
	}
	
	@GetMapping("/analysisData2")
	public void analysisData2() throws MqttPersistenceException, MqttException {
		mqttConnectClient.subscribe("sleepace/req/50500/27/ipof3rdnvto65/0/analysis_data");
	}
}
