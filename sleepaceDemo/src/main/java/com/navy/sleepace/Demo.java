package com.navy.sleepace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.alibaba.fastjson.TypeReference;

public class Demo {
	
	private  static Logger logger = Logger.getLogger(Demo.class);
	private static MqttConnectClient client;
	//临时测试用设备id
	private static String deviceId = "ipof3rdnvto65";
	//合作方用户的唯一标识
	private static String userId = "zhansan211";
	
	public static void main(String[] args) throws MqttException, InterruptedException{
		
		client = new MqttConnectClient();
		//mqtt服务连接
		String token = client.connect();
		final RequestHandler handler = new RequestHandler(client);
		
		final Device device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceType(SleepaceConstants.deviceType);
		
		//查询用户绑定信息
		String bindInfoUrl = SleepaceConstants.httpUrlPre+SleepaceConstants.HTTP_BINDINFO_INFO_API+"/"+SleepaceConstants.channelId+"/"+token+"/"+userId;
		HttpReponse<List<BindInfo>> bindInfoResponse = HttpAuth.httpGet(bindInfoUrl,new TypeReference<HttpReponse<List<BindInfo>>>(){});
		if(bindInfoResponse.getCode() != 0){
			logger.info("get bind info fail");
			return; 
		}
		List<BindInfo> bindInfos = (List<BindInfo>)bindInfoResponse.getData();
		if(bindInfos != null){
			for(BindInfo bd:bindInfos){
				logger.info(bd.getDeviceId());
			}
		}
		
		// 在线状态查询
		handler.handlerPublish(SleepaceConstants.DEVICE_CONNECTION_STATUS, device, null);
		// 开始监测
		handler.handlerPublish(SleepaceConstants.START_MONITOR, device, null);
		Thread.sleep(60*1000);
		// 工作状态查询
		handler.handlerPublish(SleepaceConstants.WORK_STATUS, device, null);
		// 查看实时数据
		handler.handlerPublish(SleepaceConstants.VIEW_REALTIME_DATA, device, null);
	}
	
	public void auth(String channelId,String secretId){
		
	}
	
	
}
