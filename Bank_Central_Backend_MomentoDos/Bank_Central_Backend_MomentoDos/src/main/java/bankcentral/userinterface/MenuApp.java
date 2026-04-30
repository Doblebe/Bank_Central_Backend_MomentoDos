package bankcentral.userinterface;

import bankcentral.domain.Cliente;
import bankcentral.domain.Movimiento;
import bankcentral.domain.TarjetaCredito;
import bankcentral.service.ClienteServiceImpl;
import bankcentral.util.TypeValidator;
import bankcentral.view.ClienteView;

import java.util.List;
import java.util.Scanner;

public class MenuApp {

    Scanner sc = new Scanner(System.in);

    private final ClienteView clienteView;
    private final ClienteServiceImpl clienteServiceImpl;

    public MenuApp(ClienteView clienteView, ClienteServiceImpl clienteServiceImpl) {
        this.clienteView        = clienteView;
        this.clienteServiceImpl = clienteServiceImpl;
    }


    // -------------------------------------------------------
    // MENU PRINCIPAL
    // -------------------------------------------------------
    public void mainMenu() {

        System.out.println("Presione 1 para iniciar BankCentral");
        int init = sc.nextInt();
        sc.nextLine();

        while (init != 0) {
            System.out.println("\n==============================");
            System.out.println("     BIENVENIDO A BANKCENTRAL");
            System.out.println("==============================");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar Sesion");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    System.out.println("\n--- Registro de Nuevo Cliente ---");
                    clienteView.createCliente();
                    break;
                case 2:
                    System.out.println("\n--- Inicio de Sesion ---");
                    iniciarSesion();
                    break;
                case 3:
                    System.out.println("Gracias por usar BankCentral. Hasta pronto.");
                    init = 0;
                    break;
                default:
                    System.out.println("Ingrese una opcion valida.");
            }
        }
    }


    // -------------------------------------------------------
    // INICIO DE SESION
    // -------------------------------------------------------
    private void iniciarSesion() {

        String usuario    = TypeValidator.validateString("Usuario: ");
        String contrasena = TypeValidator.validateString("Contrasena: ");

        Cliente cliente = clienteView.login(usuario, contrasena);

        if (cliente == null) {
            System.out.println("No se pudo iniciar sesion. Volviendo al menu principal.");
            return;
        }

        System.out.println("\nBienvenido, " + cliente.getNombre() + " " + cliente.getApellido() + "!");

        // Verificar si es admin para mostrar opciones adicionales
        if (cliente.getUsuario().equalsIgnoreCase("admin")) {
            menuAdmin(cliente);
        } else {
            menuDashboard(cliente);
        }
    }


    // -------------------------------------------------------
    // DASHBOARD DEL CLIENTE (equivalente al dashboard.html)
    // -------------------------------------------------------
    private void menuDashboard(Cliente cliente) {

        boolean activo = true;

        while (activo) {
            System.out.println("\n==============================");
            System.out.println("  DASHBOARD - " + cliente.getNombre().toUpperCase());
            System.out.println("==============================");
            System.out.println("Cuenta: " + cliente.getCuenta().getTipo());

            if (cliente.getCuenta() instanceof TarjetaCredito) {
                TarjetaCredito tc = (TarjetaCredito) cliente.getCuenta();
                System.out.println("Deuda: $" + String.format("%,.0f", tc.getSaldo()));
                System.out.println("Cupo disponible: $" + String.format("%,.0f", tc.getCupoDisponible()));
            } else {
                System.out.println("Saldo: $" + String.format("%,.0f", cliente.getCuenta().getSaldo()));
            }

            System.out.println("------------------------------");
            System.out.println("1. Consignar");
            System.out.println("2. Retirar");
            System.out.println("3. Transferir");
            System.out.println("4. Movimientos");
            System.out.println("5. Tarjeta de Credito");
            System.out.println("6. Mi Perfil");
            System.out.println("7. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");

            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    menuConsignar(cliente);
                    break;
                case 2:
                    menuRetirar(cliente);
                    break;
                case 3:
                    menuTransferir(cliente);
                    break;
                case 4:
                    menuMovimientos(cliente);
                    break;
                case 5:
                    menuCredito(cliente);
                    break;
                case 6:
                    menuPerfil(cliente);
                    break;
                case 7:
                    System.out.println("Sesion cerrada. Hasta pronto, " + cliente.getNombre() + ".");
                    activo = false;
                    break;
                default:
                    System.out.println("Seleccione una opcion valida.");
            }
        }
    }


    // -------------------------------------------------------
    // CONSIGNAR
    // -------------------------------------------------------
    private void menuConsignar(Cliente cliente) {
        System.out.println("\n--- Consignar ---");
        double monto = TypeValidator.validateDouble("Ingrese el monto a consignar: $");
        String resultado = clienteServiceImpl.consignar(cliente, monto);
        System.out.println(resultado);
    }


    // -------------------------------------------------------
    // RETIRAR
    // -------------------------------------------------------
    private void menuRetirar(Cliente cliente) {
        System.out.println("\n--- Retirar ---");
        double monto = TypeValidator.validateDouble("Ingrese el monto a retirar: $");
        String resultado = clienteServiceImpl.retirar(cliente, monto);
        System.out.println(resultado);
    }


    // -------------------------------------------------------
    // TRANSFERIR
    // -------------------------------------------------------
    private void menuTransferir(Cliente cliente) {
        System.out.println("\n--- Transferir ---");
        String usuarioDestino = TypeValidator.validateString("Ingrese el usuario destino: ");
        double monto          = TypeValidator.validateDouble("Ingrese el monto a transferir: $");
        String resultado      = clienteServiceImpl.transferir(cliente, usuarioDestino, monto);
        System.out.println(resultado);
    }


    // -------------------------------------------------------
    // MOVIMIENTOS
    // -------------------------------------------------------
    private void menuMovimientos(Cliente cliente) {
        System.out.println("\n--- Historial de Movimientos ---");
        List<Movimiento> movimientos = cliente.getCuenta().getMovimientos();

        if (movimientos.isEmpty()) {
            System.out.println("No tienes movimientos registrados.");
            return;
        }

        System.out.printf("%-22s %-45s %15s%n", "Fecha", "Tipo", "Valor");
        System.out.println("-".repeat(84));
        for (Movimiento m : movimientos) {
            System.out.printf("%-22s %-45s %15s%n",
                m.getFecha(),
                m.getTipo(),
                "$" + String.format("%,.0f", m.getValor()));
        }
    }


    // -------------------------------------------------------
    // TARJETA DE CREDITO
    // -------------------------------------------------------
    private void menuCredito(Cliente cliente) {

        if (!(cliente.getCuenta() instanceof TarjetaCredito)) {
            System.out.println("No tienes tarjeta de credito.");
            return;
        }

        TarjetaCredito tc = (TarjetaCredito) cliente.getCuenta();
        boolean activo = true;

        while (activo) {
            System.out.println("\n--- Tarjeta de Credito ---");
            System.out.println("Cupo total:       $" + String.format("%,.0f", tc.getCupo()));
            System.out.println("Deuda actual:     $" + String.format("%,.0f", tc.getSaldo()));
            System.out.println("Cupo disponible:  $" + String.format("%,.0f", tc.getCupoDisponible()));
            System.out.println("------------------------------");
            System.out.println("1. Realizar Compra");
            System.out.println("2. Pagar Deuda");
            System.out.println("3. Volver");
            System.out.print("Seleccione una opcion: ");

            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    double monto  = TypeValidator.validateDouble("Monto de la compra: $");
                    int    cuotas = TypeValidator.validateInt("Numero de cuotas: ");
                    System.out.println(clienteServiceImpl.comprarCredito(cliente, monto, cuotas));
                    break;
                case 2:
                    double pago = TypeValidator.validateDouble("Monto a pagar: $");
                    System.out.println(clienteServiceImpl.pagarCredito(cliente, pago));
                    break;
                case 3:
                    activo = false;
                    break;
                default:
                    System.out.println("Seleccione una opcion valida.");
            }
        }
    }


    // -------------------------------------------------------
    // PERFIL DEL CLIENTE
    // -------------------------------------------------------
    private void menuPerfil(Cliente cliente) {

        boolean activo = true;

        while (activo) {
            System.out.println("\n--- Mi Perfil ---");
            System.out.println("Nombre:         " + cliente.getNombre() + " " + cliente.getApellido());
            System.out.println("Identificacion: " + cliente.getIdentificacion());
            System.out.println("Celular:        " + cliente.getCelular());
            System.out.println("Usuario:        " + cliente.getUsuario());
            System.out.println("Estado:         " + cliente.getEstado());
            System.out.println("------------------------------");
            System.out.println("1. Actualizar Nombre");
            System.out.println("2. Actualizar Celular");
            System.out.println("3. Cambiar Contrasena");
            System.out.println("4. Volver");
            System.out.print("Seleccione una opcion: ");

            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    cliente.setNombre(TypeValidator.validateString("Nuevo nombre: "));
                    System.out.println("Nombre actualizado correctamente.");
                    break;
                case 2:
                    cliente.setCelular(TypeValidator.validateString("Nuevo celular: "));
                    System.out.println("Celular actualizado correctamente.");
                    break;
                case 3:
                    String actual    = TypeValidator.validateString("Contrasena actual: ");
                    String nueva     = TypeValidator.validateString("Nueva contrasena: ");
                    String confirmar = TypeValidator.validateString("Confirmar nueva contrasena: ");
                    System.out.println(clienteServiceImpl.cambiarContrasena(cliente, actual, nueva, confirmar));
                    break;
                case 4:
                    activo = false;
                    break;
                default:
                    System.out.println("Seleccione una opcion valida.");
            }
        }
    }


    // -------------------------------------------------------
    // PANEL DE ADMINISTRACION (solo para usuario "admin")
    // -------------------------------------------------------
    private void menuAdmin(Cliente admin) {

        boolean activo = true;

        while (activo) {
            System.out.println("\n==============================");
            System.out.println("     PANEL DE ADMINISTRACION");
            System.out.println("==============================");
            System.out.println("1. Ver todos los clientes");
            System.out.println("2. Buscar cliente por Id");
            System.out.println("3. Crear nuevo cliente");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");

            int opt = sc.nextInt();
            sc.nextLine();

            switch (opt) {
                case 1:
                    System.out.println("\n--- Todos los Clientes ---");
                    clienteView.getAllClientes();
                    break;
                case 2:
                    clienteView.getClienteById(TypeValidator.validateInt("Ingrese el id del cliente"));
                    break;
                case 3:
                    System.out.println("\n--- Crear Nuevo Cliente ---");
                    clienteView.createCliente();
                    break;
                case 4:
                    System.out.println("\n--- Actualizar Cliente ---");
                    clienteView.updateCliente(TypeValidator.validateInt("Ingrese el id del cliente a actualizar"));
                    break;
                case 5:
                    System.out.println("\n--- Eliminar Cliente ---");
                    clienteView.deleteCliente(TypeValidator.validateInt("Ingrese el id del cliente a eliminar"));
                    break;
                case 6:
                    System.out.println("Sesion cerrada. Hasta pronto, " + admin.getNombre() + ".");
                    activo = false;
                    break;
                default:
                    System.out.println("Seleccione una opcion valida.");
            }
        }
    }
}
