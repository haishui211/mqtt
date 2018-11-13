package com.navy.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.navy.sleepace.SleepaceConstants;
import com.alibaba.fastjson.TypeReference;
import com.navy.sleepace.BindInfo;
import com.navy.sleepace.Device;
import com.navy.sleepace.HttpAuth;
import com.navy.sleepace.HttpReponse;
import com.navy.sleepace.MqttConnectClient;
import com.navy.sleepace.RequestHandler;

/**
 * sleepace mqtt 服务组件
 * @author haishui211
 */
@Component
public class SleepaceMqttComponent {
	
	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 启用自动监测
	 */
	private final byte AUTO_FLAG = 1;
	/**
	 * 自动监测时间
	 */
	private final String AUTO_MONITOR_TIME = "18:00";
	/**
	 * 自动监测重复标志位
	 */
	private final byte AUTO_WORK_DAY = 127;
	
	@Autowired
	private MqttConnectClient mqttConnectClient;
	
	/**
	 * 查询用户绑定信息
	 * @param userCode
	 * @return
	 */
	public String queryBindInfo(String userCode) {
		String bindInfoUrl = SleepaceConstants.httpUrlPre + SleepaceConstants.HTTP_BINDINFO_INFO_API + "/" + SleepaceConstants.channelId + "/"+ getSleepaceToken() + "/" + userCode;
		HttpReponse<List<BindInfo>> bindInfoResponse = HttpAuth.httpGet(bindInfoUrl, new TypeReference<HttpReponse<List<BindInfo>>>() {});
		if (bindInfoResponse.getCode() != 0) {
			logger.info("get bind info fail");
			return null;
		}
		List<BindInfo> bindInfos = (List<BindInfo>) bindInfoResponse.getData();
		if (bindInfos != null) {
			for (BindInfo bd : bindInfos) {
				return bd.getDeviceId();
			}
		}
		return null;
	}
	
	/**
	 * 绑定sleepace设备
	 * @param userCode
	 * @param deviceId
	 */
	@SuppressWarnings("rawtypes")
	public boolean bindSleepaceDevice(String userCode, String deviceId) {
		String bindUrl = SleepaceConstants.httpUrlPre + SleepaceConstants.HTTP_BIND_API + "/" + SleepaceConstants.channelId + "/" + getSleepaceToken();
		Map<String, String> bindInfo = new HashMap<String, String>();
		bindInfo.put("channelId", SleepaceConstants.channelId);
		bindInfo.put("token", getSleepaceToken());
		bindInfo.put("userId", userCode);
		bindInfo.put("deviceId", deviceId);
		bindInfo.put("leftRight", 0 + "");
		bindInfo.put("timeZone", 28800 + "");
		bindInfo.put("registerIp", "192.168.66.229");
		HttpReponse bindResponse = HttpAuth.httpPost(bindUrl, bindInfo, new TypeReference<HttpReponse<Map<String, String>>>() {});
		if (bindResponse.getCode() != 0) {
			logger.info(" bind device  fail");
					return false;
		} else {
			logger.info(" bind device success");
			return true;
		}
	}
	
	/**
	 * 解绑sleepace设备
	 * @param userCode
	 * @param deviceId
	 */
	@SuppressWarnings("rawtypes")
	public boolean unBindSleepaceDevice(String userCode, String deviceId) {
		String bindUrl = SleepaceConstants.httpUrlPre + SleepaceConstants.HTTP_UNBIND_API + "/" + SleepaceConstants.channelId + "/" + getSleepaceToken() + "/" + userCode + "/" + deviceId;
		HttpReponse bindResponse = HttpAuth.httpDelete(bindUrl, new TypeReference<HttpReponse<Map<String, Object>>>() {});
		if (bindResponse.getCode() != 0) {
			logger.info("unbind device  fail");
					return false;
		} else {
			logger.info("unbind device success");
			return true;
		}
	}
	
	/**
	 * 给指定设备设定自动监测时间
	 * @param deviceId
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void startMonitorAuto(String deviceId) throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceType(SleepaceConstants.deviceType);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("flag", AUTO_FLAG);
		params.put("checkTime", AUTO_MONITOR_TIME);
		params.put("workDay", AUTO_WORK_DAY);
		handler.handlerPublish(SleepaceConstants.AUTO_START_CONFIG, device, params);
	}
	
	/**
	 * 开始监测
	 * @param deviceId
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void startMonitor(String deviceId) throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.START_MONITOR, device, null);
	}
	
	/**
	 * 停止监测
	 * @param deviceId
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void stopMonitor(String deviceId) throws MqttPersistenceException, MqttException {
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.STOP_MONITOR, device, null);
	}
	
	/**
	 * 查看实时数据
	 * @param deviceId
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void viewRealTimeData(String deviceId) throws MqttPersistenceException, MqttException{
		RequestHandler handler = new RequestHandler(mqttConnectClient);
		Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceType(SleepaceConstants.deviceType);
		handler.handlerPublish(SleepaceConstants.VIEW_REALTIME_DATA, device, null);
	}
	
	/**
	 * 订阅睡眠分析数据
	 * 在睡眠结束时返回分析数据
	 * @param deviceId
	 * @throws MqttException
	 */
	public void subscribeAnalysisData(String deviceId) throws MqttException {
		String topic = getAnalysisDataTopic(deviceId);
		mqttConnectClient.subscribe(topic);
	}
	
	private String getSleepaceToken() {
		return mqttConnectClient.getToken();
	}
	
	private String getAnalysisDataTopic(String deviceId) {
		String result = SleepaceConstants.PUB_TOPIC_REQ 
						+ "/" + SleepaceConstants.channelId 
						+ "/" + SleepaceConstants.deviceType 
						+ "/" + deviceId
						+ "/0/analysis_data";
		return result;
	}
}
