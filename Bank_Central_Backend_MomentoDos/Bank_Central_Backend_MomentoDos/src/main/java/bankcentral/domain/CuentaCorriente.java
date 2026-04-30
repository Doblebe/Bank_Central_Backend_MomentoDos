package bankcentral.domain;

import bankcentral.enums.TipoCuenta;

public class CuentaCorriente extends Cuenta {

    // Constructor
    public CuentaCorriente() {
        super(TipoCuenta.CORRIENTE, 0);
    }

    // Retirar con 20% de sobregiro permitido
    @Override
    public String retirar(double monto) {
        if (monto <= 0) return "ERROR: El monto debe ser mayor a cero.";

        double limite = getSaldo() * 1.20;

        if (monto > limite) {
            return "ERROR: Supera el limite. Puedes retirar hasta $" +
                   String.format("%,.0f", limite) + " (incluye 20% de sobregiro).";
        }

        setSaldo(getSaldo() - monto);
        registrarMovimiento("Retiro Corriente", monto);
        return "Retiro exitoso. Nuevo saldo: $" + String.format("%,.0f", getSaldo());
    }
}
