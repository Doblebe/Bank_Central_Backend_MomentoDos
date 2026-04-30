package bankcentral.service;

import bankcentral.domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    // Contratos - solo la firma de los metodos

    public Cliente createClienteService();
    public Cliente updateClienteService(int id);
    public Optional<Cliente> getClienteById(int id);
    public List<Cliente> getAllClientes();
    public void deleteCliente(int id);
    public Cliente login(String usuario, String contrasena);
}
