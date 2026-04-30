package bankcentral.config;

import bankcentral.repository.ClienteRepository;
import bankcentral.service.ClienteServiceImpl;
import bankcentral.userinterface.MenuApp;
import bankcentral.view.ClienteView;

public class Config {

    public static MenuApp createMenuApp() {

        // Repository (nuestra base de datos en memoria)
        ClienteRepository clienteRepository = new ClienteRepository();

        // Service (logica de negocio)
        ClienteServiceImpl clienteServiceImpl = new ClienteServiceImpl(clienteRepository);

        // View (capa de presentacion)
        ClienteView clienteView = new ClienteView(clienteServiceImpl);

        return new MenuApp(clienteView, clienteServiceImpl);
    }
}
