package com.fabio.estudopadroesprojeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fabio.estudopadroesprojeto.model.ClienteRepository;
import com.fabio.estudopadroesprojeto.model.EnderecoRepository;
import com.fabio.estudopadroesprojeto.service.ViaCepService;
import com.fabio.estudopadroesprojeto.service.impl.ClienteServiceImpl;

@Configuration
public class BeanConfiguration {
	
	@Bean
	ClienteServiceImpl clienteServiceImpl(ClienteRepository clienteRepository,
			EnderecoRepository enderecoRepository, ViaCepService viaCepService) {
		
		return new ClienteServiceImpl(clienteRepository,enderecoRepository,viaCepService);
	}
	

}
