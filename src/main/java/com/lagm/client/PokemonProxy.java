package com.lagm.client;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lagm.dto.PokemonListResponse;

@Component
public class PokemonProxy implements IPokemonProxy {

	private static final Logger LOGGER = LoggerFactory.getLogger(PokemonProxy.class);

	@Override
	public PokemonListResponse listaPokemones(String url) {
		PokemonListResponse response = null;
		Gson gson = new GsonBuilder().serializeNulls().create();

		// Configuración SSL
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

		LOGGER.info(responseEntity.getBody());

		response = gson.fromJson(responseEntity.getBody(), PokemonListResponse.class);

		return response;
	}

}