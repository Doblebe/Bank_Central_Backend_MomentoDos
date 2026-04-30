package bankcentral.repository;

import bankcentral.domain.Cliente;
import bankcentral.enums.TipoCuenta;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    // Lista que funciona como nuestra base de datos en memoria
    List<Cliente> clientes = new ArrayList<>();

    // Datos iniciales
    public ClienteRepository() {
        clientes.add(new Cliente(1, "1001234567", "Carlos",   "Lopez",   "3001234567", "carlos",   "1234", TipoCuenta.AHORROS));
        clientes.add(new Cliente(2, "1007654321", "Maria",    "Gonzalez","3107654321", "maria",    "5678", TipoCuenta.CORRIENTE));
        clientes.add(new Cliente(3, "1009876543", "Andres",   "Perez",   "3209876543", "andres",   "9012", TipoCuenta.CREDITO));

        // Agregar saldos iniciales de ejemplo
        clientes.get(0).getCuenta().consignar(2_000_000);
        clientes.get(1).getCuenta().consignar(1_500_000);
    }

    // CREATE - Agregar un cliente
    public Cliente createClienteRepository(Cliente cliente) {
        clientes.add(cliente);
        System.out.println("Cliente creado: " + cliente.getNombre() + " " + cliente.getApellido());
        return cliente;
    }

    // READ - Buscar cliente por id
    public Cliente getClienteById(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    // READ - Buscar cliente por usuario
    public Cliente getClienteByUsuario(String usuario) {
        for (Cliente cliente : clientes) {
            if (cliente.getUsuario().equalsIgnoreCase(usuario)) {
                return cliente;
            }
        }
        return null;
    }

    // READ - Obtener todos los clientes
    public List<Cliente> getAllClientes() {
        for (Cliente cliente : clientes) {
            System.out.println(cliente.toString());
        }
        return clientes;
    }

    // UPDATE - Devuelve el objeto para que el service lo modifique
    public Cliente updateClienteRepository(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    // DELETE - Eliminar cliente por id
    public void deleteClienteRepository(int id) {
        Cliente toDelete = null;
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                toDelete = cliente;
                break;
            }
        }
        if (toDelete != null) {
            clientes.remove(toDelete);
            System.out.println("Cliente eliminado correctamente.");
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }
}
