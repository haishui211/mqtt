package com.navy.sleepace;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;
/**
 * 在连接mqtt服务器前，需通过权限认证“pro/mqtt/service/token”
 * 
 * 
 * @author sleepace
 *
 */
@Component
public class MqttConnectClient{ 
	
	private Logger logger = Logger.getLogger(getClass());
	private static AtomicInteger mqttMsgId = new AtomicInteger();
    private MqttClient client = null;
    
    @Autowired
    private MessageCallback messageCallback;
    
    /**
     * mqtt 服务地址，由http权限认证接口“pro/mqtt/service/token”返回
     */
    private String mqttAddress = "";

    /**
     * 心跳间隔 120s
     */
    /**
     * 由sleepace提供
    */
    private String channelId =SleepaceConstants.channelId;
   
	/**
     *必须唯一， clientId = channelId+"_"+编号。编号可以接入方生成，用于区分多条mqtt连接（正常情况下，一条连接足够了）
     */
	private String clientId ;
	private String authUrl;
	
    /**
     * sleepace主动发送消息的topic
     */
    private String subRequestTopic;
    /**
     * sleepace回复合作方消息的topic
     */
    private String subResponseTopic;
    
    
    private Timer reconnectTimer;
    private ConnectTask task;
	private Object responseLock = new Object();
    private int reconnectTime = 2000;
    private String token = "";
    
    
    public MqttConnectClient() throws MqttException {
    
//    	this.clientId = channelId;
    	subResponseTopic =  SleepaceConstants.SUB_TOPIC_RESP+channelId+"/#";
    	subRequestTopic = SleepaceConstants.SUB_TOPIC_REQ+channelId+"/#";
    	
    }
    /**
     * 连接mqtt服务
     * @param token
     */
	public String connect(){ 
		synchronized(responseLock){
			if(client!=null){//重连时需要将原来的client关闭
				try {
					closed();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
	        reconnectTimer = new Timer("MQTT connect" );
	        task = new ConnectTask();
	    	reconnectTimer.schedule(task, reconnectTime);   
	    	try {
				responseLock.wait();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
	    	return token;
		}
		
    } 
    
    /**
     * 发布消息  
     * @param topic
     * @param json
     * @param retain
     * @throws MqttPersistenceException
     * @throws MqttException
     */
	public void publish(String topic, String json, boolean retain)
			throws MqttPersistenceException, MqttException {
		MqttTopic mTopic = client.getTopic(topic);  
    	MqttMessage message = new MqttMessage();  
        message.setQos(0);  
        message.setRetained(retain);
        message.setId(mqttMsgId.getAndIncrement());
        message.setPayload(json.getBytes());  
       mTopic.publish(message);		
	}
	
	public void subscribe(String topic) throws MqttException {
		client.subscribe(topic);
	}
	
	private class ConnectTask extends TimerTask {
		public void run() {
			logger.info("connect....");
			try {
				long timeStamp = System.currentTimeMillis();
		    	String encrySecretKey  = MD5Util.md5(SleepaceConstants.channelId+SleepaceConstants.channelKey+timeStamp);
				authUrl = SleepaceConstants.httpUrlPre+SleepaceConstants.HTTP_TOKEN_API+"/"+SleepaceConstants.channelId+"/"+encrySecretKey+"/"+timeStamp;
				//权限认证，取mqtt地址服务与token
				HttpReponse<Map<String, String>> authResponse = HttpAuth.httpGet(authUrl,new TypeReference<HttpReponse<Map<String, String>>>() {});
				if (authResponse.getCode() != 0) {
					logger.info("auth fail");
					reconnectTimer.schedule(new ConnectTask(), reconnectTime);
					return;
				}
				Map<String, String> authData = (Map<String, String>) authResponse.getData();
				token = authData.get("token");
				mqttAddress = authData.get("mqttAddress");
				client = new MqttClient(mqttAddress, clientId);
//				messageCallback = new MessageCallback(MqttConnectClient.this);
				final MqttConnectOptions options = new MqttConnectOptions();
				options.setCleanSession(false);
				options.setUserName(channelId);
				options.setPassword(token.toCharArray());
				// 设置超时时间
				options.setConnectionTimeout(40);
				// 设置会话心跳时间
				options.setKeepAliveInterval(SleepaceConstants.heartBeat);
				// 设置为不自动重连。因为连接前需要先通过http认证接口拿到token与mqtt地址，所以需要在自定义实现重连
				options.setAutomaticReconnect(false);
				client.setCallback(messageCallback);
				client.connect(options);
				logger.info("connect success");
				client.subscribe(new String[] { subRequestTopic,subResponseTopic });
				reconnectTimer.cancel();
				synchronized(responseLock){
					responseLock.notify();
				}
			} catch (Exception e) {
				logger.error("connect error",e);
		    	reconnectTimer.schedule(new ConnectTask(), reconnectTime);
			}
		}
	}
	
	public void closed() throws MqttException {
		try {
			client.disconnect();
			client.close();
		}
		catch(Exception e) {
			logger.error("mqtt closed error" + e.getMessage());
		}
	}
	
	 public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
} 