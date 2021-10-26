package com.fabio.estudopadroesprojeto.service.impl;

import java.util.Optional;
import com.fabio.estudopadroesprojeto.model.Cliente;
import com.fabio.estudopadroesprojeto.model.ClienteRepository;
import com.fabio.estudopadroesprojeto.model.Endereco;
import com.fabio.estudopadroesprojeto.model.EnderecoRepository;
import com.fabio.estudopadroesprojeto.service.ClienteService;
import com.fabio.estudopadroesprojeto.service.ViaCepService;


public class ClienteServiceImpl implements ClienteService{
	
	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;
	
	
	public ClienteServiceImpl(final ClienteRepository clienteRepository, 
			final EnderecoRepository enderecoRepository,
			final ViaCepService viaCepService) {
		super();
		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
		this.viaCepService = viaCepService;
	}

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			novoEndereco.setNumber(cliente.getEndereco().getNumber());
			novoEndereco.setComplemento(cliente.getEndereco().getComplemento());
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		endereco.setNumber(cliente.getEndereco().getNumber());
		endereco.setComplemento(cliente.getEndereco().getComplemento());
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}

	
}
