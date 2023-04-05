package com.mqtt.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mqtt.test.model.Message;
import com.mqtt.test.service.MQTTServiceCallback;


@RestController
public class MQTTController {
	
	@Autowired
	private MQTTServiceCallback mqttService;
	
	@PostMapping("/publish")
	public HttpStatus publish(@RequestBody Message message) throws JsonProcessingException {
		mqttService.publish("topic", message);
		return HttpStatus.OK;
	}


}
