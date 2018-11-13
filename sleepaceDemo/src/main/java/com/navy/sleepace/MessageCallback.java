package com.navy.sleepace;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
/**  
 *   
 * 必须实现MqttCallback的接口并实现对应的相关接口方法  
 *      ◦CallBack 类将实现 MqttCallBack。每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。在回调中，将它用来标识已经启动了该回调的哪个实例。  
 *  ◦必须在回调类中实现三个方法：  
 *   
 *  public void messageArrived(MqttTopic topic, MqttMessage message)  
 *  接收已经预订的发布。  
 *   
 *  public void connectionLost(Throwable cause)  
 *  在断开连接时调用。  
 *   
 *  public void deliveryComplete(MqttDeliveryToken token))  
 *     发布的 QoS 1 或 QoS 2 消息成功后调用。  
 *  ◦由 MqttClient.connect 激活此回调。  
 *  
 *  @author sleepace
 *   
 */    
@Component
public class MessageCallback implements MqttCallback {  
	
	private Logger logger = Logger.getLogger(getClass());
	
	private MqttConnectClient connect ;
	
	public MessageCallback(MqttConnectClient mqttConnectClient){
		this.connect = mqttConnectClient;
	}

	/**
   * 连接断开时重连
   */
    public void connectionLost(Throwable cause) {  
    	logger.info("connection is losted");
    	connect.connect();
    }  
  
    /**
     * 发布的 QoS 1 或 QoS 2 消息成功后调用
     */
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			logger.debug(token.getMessage().toString()+"-"+token.getMessageId());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
/**
 * 接收已经预订的发布
 */
	@Override
	public void messageArrived(final String topic, final MqttMessage message) throws Exception {
		String msgStr = new String(message.getPayload());
		logger.info("topic="+topic);
		logger.info("msg="+msgStr);
		if(topic.startsWith(SleepaceConstants.SUB_TOPIC_REQ)){
			ResponseHandler handler = new ResponseHandler(this.connect);
			handler.handler(topic,JSON.parseObject(msgStr, RequestMessage.class));
		}
	}
}  