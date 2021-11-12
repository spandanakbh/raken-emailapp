package com.raken.email.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raken.email.controller.EmailController;

/**
 * @author spandana k
 *
 */
@SpringBootTest
public class EmailApplicationTest {
	
	@Autowired
	private EmailController emailController;

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	@Test
	void contextLoads() {
		assertThat(emailController).isNotNull();
	}

}
