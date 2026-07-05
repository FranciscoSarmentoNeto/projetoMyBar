package br.com.mybar.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import br.com.mybar.project.util.CpfValidator;
import br.com.mybar.project.model.Actors.Client;
import br.com.mybar.project.repository.CustomerRepositoryInterface;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepositoryInterface interfaceCliente;

    public List<Client> listarClientes() {
        return interfaceCliente.findAll();
    }

    public Client buscarPorId(Long id) {
        return interfaceCliente.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    public Client incluirCliente(Client cliente) {
        String cpfLimpo = CpfValidator.limpar(cliente.getCpf());

        if (!CpfValidator.isValid(cpfLimpo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido.");
        }

        cliente.setCpf(cpfLimpo);

        if (interfaceCliente.findByCpf(cpfLimpo).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um cliente cadastrado com este CPF.");
        }
        return interfaceCliente.save(cliente);
    }

    public Client editarCliente(Long id, Client clienteAtualizado) {
        Client clienteExistente = buscarPorId(id);

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setSexo(clienteAtualizado.getSexo());

        return interfaceCliente.save(clienteExistente);
    }

    public void deletarCliente(Long id) {
        Client cliente = buscarPorId(id);

        interfaceCliente.delete(cliente);
    }

    public Optional<Client> findCpf(String cpf) {
        return interfaceCliente.findByCpf(cpf);
    }
}