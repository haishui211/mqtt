package com.navy.sleepace;

public class SleepaceConstants {
	//合作方 channelId 
	public static String channelId = "50500";
	//合作 方接入 安全码
	//j3CUdreIdGhS
	public static String channelKey = "j3CUdreIdGhS";
	//http服务url
	public static String httpUrlPre = "http://mqttauthtest.sleepace.com/mqtt/service/";
	//public static String httpUrlPre = "https://mqttcn.sleepace.com/mqtt/service/";
	//接入的设备类型
	public static int deviceType = 27;
	//mqtt服务心跳时间间隔
	public static int heartBeat = 120;


	public static String DEVICE_BIND = "device_bind";
	public static String DEVICE_CONNECTION_STATUS = "device_connection_status";
	public static String WORK_STATUS = "work_status";
	public static String START_MONITOR = "start_monitor";
	public static String STOP_MONITOR = "stop_monitor";
	public static String AUTO_START_CONFIG = "auto_start_config";
	public static String VIEW_REALTIME_DATA = "view_realtime_data";
	public static String STOP_REALTIME_DATA = "stop_realtime_data";
	public static String DEVICE_CONNECTION_EVENT = "device_connection_event";
	public static String SMART_ALARM_EVENT ="smart_alarm_event";
	public static String REALTIME_DATA = "realtime_data";
	public static String ANALYSIS_DATA = "analysis_data";
	
	public static String SUB_TOPIC_REQ = "sleepace/req/";
	public static String SUB_TOPIC_RESP = "sleepace/resp/";
	
	public static String PUB_TOPIC_REQ = "partner/req/";
	public static String PUB_TOPIC_RESP = "partner/resp/";
	
	public static String HTTP_TOKEN_API = "token";
	public static String HTTP_BIND_API = "bind";
	public static String HTTP_BINDINFO_INFO_API = "bindinfo";
	public static String HTTP_UNBIND_API = "unbind";
}
