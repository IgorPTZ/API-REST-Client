package com.example.consumingrest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

@SpringBootApplication
public class ConsumingRestApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumingRestApplication.class, args);
	}
	
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	@SuppressWarnings("unchecked")
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			
			  HttpHeaders headers = new HttpHeaders(); 
			  headers.set("Accept","application/json");
			  headers.set("Content/Type", "text/json");
			    
			  JsonObject jsonObject = new JsonObject(); 
			  jsonObject.addProperty("cpf","999999999");
			  	 
			  HashMap<String, String> bodyParam = new HashMap<>();
			  bodyParam.put("encrypted", AesService.encrypt(jsonObject.toString()));
			   
			  UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8280/solos/services/contato/obter_por_cpf");
			  
			  HttpEntity<?> entity        = new HttpEntity<>(bodyParam, headers);
			  HttpEntity<String> response = restTemplate.exchange( builder.toUriString(), HttpMethod.POST, entity, String.class);
			 
			  ObjectMapper mapper = new ObjectMapper(); 
			  Map<String,Object> map = mapper.readValue(response.getBody(), Map.class);
			  
			  System.out.println("Map" + AesService.decrypt(map.get("encrypted").toString()));
		};
	}
}
