package bankcentral.service;

import bankcentral.domain.Cliente;
import bankcentral.domain.TarjetaCredito;
import bankcentral.enums.TipoCuenta;
import bankcentral.enums.TipoEstado;
import bankcentral.repository.ClienteRepository;
import bankcentral.util.TypeValidator;

import java.util.List;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository; // Inyeccion de dependencias
    }

    @Override
    public Cliente createClienteService() {

        Cliente cliente = new Cliente();

        cliente.setId(TypeValidator.validateInt("Ingrese el id del cliente"));
        cliente.setIdentificacion(TypeValidator.validateString("Ingrese la identificacion"));
        cliente.setNombre(TypeValidator.validateString("Ingrese el nombre"));
        cliente.setApellido(TypeValidator.validateString("Ingrese el apellido"));
        cliente.setCelular(TypeValidator.validateString("Ingrese el celular"));
        cliente.setUsuario(TypeValidator.validateString("Ingrese el usuario"));
        cliente.setContrasena(TypeValidator.validateString("Ingrese la contrasena (minimo 4 caracteres)"));
        cliente.setEstado(TipoEstado.ACTIVO);
        cliente.setIntentosFallidos(0);

        System.out.println("Seleccione el tipo de cuenta:");
        System.out.println("1. AHORROS   2. CORRIENTE   3. CREDITO");
        int opcion = TypeValidator.validateInt("Opcion: ");

        TipoCuenta tipoCuenta;
        switch (opcion) {
            case 2:  tipoCuenta = TipoCuenta.CORRIENTE; break;
            case 3:  tipoCuenta = TipoCuenta.CREDITO;   break;
            default: tipoCuenta = TipoCuenta.AHORROS;   break;
        }

        // Crear la cuenta correcta segun el tipo seleccionado
        Cliente nuevoCliente = new Cliente(
            cliente.getId(),
            cliente.getIdentificacion(),
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getCelular(),
            cliente.getUsuario(),
            cliente.getContrasena(),
            tipoCuenta
        );

        return clienteRepository.createClienteRepository(nuevoCliente);
    }

    @Override
    public Cliente updateClienteService(int id) {

        Cliente cliente = clienteRepository.getClienteById(id);

        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return null;
        }

        System.out.println("Seleccione el dato a actualizar:");
        System.out.println("1. Nombre");
        System.out.println("2. Apellido");
        System.out.println("3. Identificacion");
        System.out.println("4. Celular");
        System.out.println("5. Usuario");
        System.out.println("6. Estado (ACTIVO/BLOQUEADO)");

        int option = TypeValidator.validateInt("Opcion: ");

        switch (option) {
            case 1:
                cliente.setNombre(TypeValidator.validateString("Nuevo nombre"));
                break;
            case 2:
                cliente.setApellido(TypeValidator.validateString("Nuevo apellido"));
                break;
            case 3:
                cliente.setIdentificacion(TypeValidator.validateString("Nueva identificacion"));
                break;
            case 4:
                cliente.setCelular(TypeValidator.validateString("Nuevo celular"));
                break;
            case 5:
                cliente.setUsuario(TypeValidator.validateString("Nuevo usuario"));
                break;
            case 6:
                String est = TypeValidator.validateString("Nuevo estado (ACTIVO / BLOQUEADO)");
                cliente.setEstado(est.equalsIgnoreCase("BLOQUEADO") ? TipoEstado.BLOQUEADO : TipoEstado.ACTIVO);
                break;
            default:
                System.out.println("Seleccione una opcion valida.");
        }

        return cliente;
    }

    @Override
    public Optional<Cliente> getClienteById(int id) {

        Cliente cliente = clienteRepository.getClienteById(id);

        if (cliente != null) {
            System.out.println("Id: "              + cliente.getId());
            System.out.println("Identificacion: "  + cliente.getIdentificacion());
            System.out.println("Nombre: "          + cliente.getNombre() + " " + cliente.getApellido());
            System.out.println("Celular: "         + cliente.getCelular());
            System.out.println("Usuario: "         + cliente.getUsuario());
            System.out.println("Estado: "          + cliente.getEstado());
            System.out.println("Cuenta: "          + cliente.getCuenta());
        } else {
            System.out.println("Cliente no encontrado.");
        }

        return Optional.ofNullable(cliente);
    }

    @Override
    public List<Cliente> getAllClientes() {
        return clienteRepository.getAllClientes();
    }

    @Override
    public void deleteCliente(int id) {
        System.out.println("Estoy en el service de Cliente");
        clienteRepository.deleteClienteRepository(id);
    }

    // Login: valida usuario, contrasena y bloqueo de cuenta
    @Override
    public Cliente login(String usuario, String contrasena) {

        Cliente cliente = clienteRepository.getClienteByUsuario(usuario);

        if (cliente == null) {
            System.out.println("Usuario no encontrado.");
            return null;
        }

        if (cliente.getEstado() == TipoEstado.BLOQUEADO) {
            System.out.println("Cuenta bloqueada. Contacta con soporte.");
            return null;
        }

        if (!cliente.autenticar(contrasena)) {
            cliente.setIntentosFallidos(cliente.getIntentosFallidos() + 1);
            int restantes = 3 - cliente.getIntentosFallidos();

            if (cliente.getIntentosFallidos() >= 3) {
                cliente.setEstado(TipoEstado.BLOQUEADO);
                System.out.println("Cuenta bloqueada por demasiados intentos fallidos.");
            } else {
                System.out.println("Contrasena incorrecta. Intentos restantes: " + restantes);
            }
            return null;
        }

        // Login exitoso: resetear intentos fallidos
        cliente.setIntentosFallidos(0);
        return cliente;
    }

    // Consignar en la cuenta del cliente
    public String consignar(Cliente cliente, double monto) {
        String resultado = cliente.getCuenta().consignar(monto);
        System.out.println(resultado);
        return resultado;
    }

    // Retirar de la cuenta del cliente
    public String retirar(Cliente cliente, double monto) {
        String resultado = cliente.getCuenta().retirar(monto);
        System.out.println(resultado);
        return resultado;
    }

    // Transferir entre dos clientes
    public String transferir(Cliente origen, String usuarioDestino, double monto) {

        Cliente destino = clienteRepository.getClienteByUsuario(usuarioDestino);

        if (destino == null)
            return "ERROR: Usuario destino no encontrado.";
        if (origen.getUsuario().equalsIgnoreCase(usuarioDestino))
            return "ERROR: No puedes transferirte a ti mismo.";
        if (origen.getCuenta().getTipo() == bankcentral.enums.TipoCuenta.CREDITO)
            return "ERROR: La tarjeta de credito no puede hacer transferencias.";
        if (monto > origen.getCuenta().getSaldo())
            return "ERROR: Saldo insuficiente.";

        origen.getCuenta().setSaldo(origen.getCuenta().getSaldo() - monto);
        origen.getCuenta().getMovimientos().add(0,
            new bankcentral.domain.Movimiento("Transferencia a " + usuarioDestino, monto));

        destino.getCuenta().setSaldo(destino.getCuenta().getSaldo() + monto);
        destino.getCuenta().getMovimientos().add(0,
            new bankcentral.domain.Movimiento("Transferencia recibida de " + origen.getUsuario(), monto));

        return "Transferencia exitosa. Nuevo saldo: $" + String.format("%,.0f", origen.getCuenta().getSaldo());
    }

    // Comprar con tarjeta de credito
    public String comprarCredito(Cliente cliente, double monto, int cuotas) {
        if (!(cliente.getCuenta() instanceof TarjetaCredito)) {
            return "ERROR: El cliente no tiene tarjeta de credito.";
        }
        TarjetaCredito tc = (TarjetaCredito) cliente.getCuenta();
        return tc.comprar(monto, cuotas);
    }

    // Pagar deuda de tarjeta de credito
    public String pagarCredito(Cliente cliente, double monto) {
        if (!(cliente.getCuenta() instanceof TarjetaCredito)) {
            return "ERROR: El cliente no tiene tarjeta de credito.";
        }
        TarjetaCredito tc = (TarjetaCredito) cliente.getCuenta();
        return tc.pagarDeuda(monto);
    }

    // Cambiar contrasena del cliente
    public String cambiarContrasena(Cliente cliente, String actual, String nueva, String confirmar) {
        return cliente.cambiarContrasena(actual, nueva, confirmar);
    }
}
