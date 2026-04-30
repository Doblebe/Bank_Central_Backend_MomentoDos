package bankcentral;

import bankcentral.config.Config;
import bankcentral.userinterface.MenuApp;

public class Main {

    public static void main(String[] args) {

        MenuApp menuApp = Config.createMenuApp();
        menuApp.mainMenu();

    }
}
