package bankcentral.domain;

import bankcentral.enums.TipoCuenta;

import java.util.ArrayList;
import java.util.List;

public class Cuenta {

    // POJO - Plain Old Java Object

    // Atributos
    private TipoCuenta tipo;
    private double saldo;
    private List<Movimiento> movimientos;

    // Constructor
    public Cuenta(TipoCuenta tipo, double saldoInicial) {
        this.tipo         = tipo;
        this.saldo        = saldoInicial;
        this.movimientos  = new ArrayList<>();
    }

    // Getters y Setters

    public TipoCuenta getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuenta tipo) {
        this.tipo = tipo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    // Metodo para registrar movimientos
    protected void registrarMovimiento(String tipo, double valor) {
        movimientos.add(0, new Movimiento(tipo, valor));
    }

    // Consignar - disponible para todos los tipos de cuenta
    public String consignar(double monto) {
        if (monto <= 0) return "ERROR: El monto debe ser mayor a cero.";
        this.saldo += monto;
        registrarMovimiento("Consignacion", monto);
        return "Consignacion exitosa. Nuevo saldo: $" + String.format("%,.0f", this.saldo);
    }

    // Retirar - cada subclase lo implementa a su manera
    public String retirar(double monto) {
        return "ERROR: Metodo no implementado.";
    }

    @Override
    public String toString() {
        return tipo.name() + " | Saldo: $" + String.format("%,.0f", saldo);
    }
}
