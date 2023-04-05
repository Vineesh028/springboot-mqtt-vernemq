package com.mqtt.test.service;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mqtt.test.model.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MQTTServiceCallback implements MqttCallbackExtended {

	@Value("${mqtt.url}")
	private String mqttUrl;

	public static MQTTServiceCallback callback;

	private static MqttClient client;

	private static MqttConnectOptions mOptions;

	@PostConstruct
	public void init() throws MqttException {
		callback = this;
		connectToMqttClient();
	}

	private void connectToMqttClient() throws MqttException {
		log.info("Connecting to MQTT server");
		getMqttClient(mqttUrl).setCallback(new MQTTServiceCallback());
		log.debug("initial client id: " + client.getClientId());
		log.debug("MQTT connect options: " + getMqttOptions().toString());
		client.connect(getMqttOptions());
	}

	public static MqttConnectOptions getMqttOptions() {
		if (mOptions == null) {
			mOptions = new MqttConnectOptions();
			mOptions.setUserName("user");
			mOptions.setPassword(new String("password").toCharArray());
			mOptions.setAutomaticReconnect(true);
		}
		return mOptions;
	}

	public static MqttClient getMqttClient(String mqttUrl) throws MqttException {
		if (client == null) {
			client = new MqttClient(mqttUrl, MqttClient.generateClientId());
		}
		return client;
	}

	@Override
	public void connectionLost(Throwable cause) {
		log.info("connectionLost :" + cause.getMessage() + " :" + cause.toString());

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {

			log.info("messageArrived from topic :" + topic + " message :" + message.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			log.info("deliveryCompleted :" + token.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		if (reconnect) {
			log.info("Reconnected to MQTT Server");
		}
		try {
			getMqttClient(mqttUrl).subscribe("topic");
			log.info("Subscribed to topic: " + "topic");

		} catch (MqttException e) {
			log.error("Failed to subscribe to the topic:" + "topic");
		}

	}

	public void publish(String topic, Message message2) throws JsonProcessingException {
		MqttMessage message = new MqttMessage();
		message.setPayload(message2.toString().getBytes());
		message.setQos(2);
		try {

			if (client.isConnected()) {
				log.info("Connection Status :" + client.isConnected());
			}
			client.publish(topic, message);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
