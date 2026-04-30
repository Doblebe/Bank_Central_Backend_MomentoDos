package bankcentral.domain;

import bankcentral.enums.TipoCuenta;
import bankcentral.enums.TipoEstado;

public class Cliente {

    // POJO - Plain Old Java Object

    // Atributos
    private int id;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String celular;
    private String usuario;
    private String contrasena;
    private TipoEstado estado;
    private int intentosFallidos;
    private Cuenta cuenta;

    // Constructores

    public Cliente() {}

    public Cliente(int id, String identificacion, String nombre, String apellido,
                   String celular, String usuario, String contrasena, TipoCuenta tipoCuenta) {
        this.id                = id;
        this.identificacion    = identificacion;
        this.nombre            = nombre;
        this.apellido          = apellido;
        this.celular           = celular;
        this.usuario           = usuario;
        this.contrasena        = contrasena;
        this.estado            = TipoEstado.ACTIVO;
        this.intentosFallidos  = 0;
        asignarCuenta(tipoCuenta);
    }

    // Metodo para asignar el tipo de cuenta correcta
    private void asignarCuenta(TipoCuenta tipoCuenta) {
        switch (tipoCuenta) {
            case AHORROS:
                this.cuenta = new CuentaAhorros();
                break;
            case CORRIENTE:
                this.cuenta = new CuentaCorriente();
                break;
            case CREDITO:
                this.cuenta = new TarjetaCredito(5_000_000);
                break;
        }
    }

    // Autenticar contrasena
    public boolean autenticar(String clave) {
        return this.contrasena.equals(clave);
    }

    // Cambiar contrasena
    public String cambiarContrasena(String actual, String nueva, String confirmar) {
        if (!this.contrasena.equals(actual))  return "ERROR: La contrasena actual es incorrecta.";
        if (!nueva.equals(confirmar))          return "ERROR: La nueva contrasena y la confirmacion no coinciden.";
        if (nueva.length() < 4)               return "ERROR: La contrasena debe tener al menos 4 caracteres.";
        this.contrasena = nueva;
        return "Contrasena cambiada exitosamente.";
    }

    // Getters y Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public TipoEstado getEstado() { return estado; }
    public void setEstado(TipoEstado estado) { this.estado = estado; }

    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }

    @Override
    public String toString() {
        return id + " | " + nombre + " " + apellido + " | Usuario: " + usuario +
               " | " + cuenta.getTipo() + " | Estado: " + estado;
    }
}
