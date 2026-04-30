package bankcentral.domain;

import bankcentral.enums.TipoCuenta;

public class CuentaAhorros extends Cuenta {

    // Constructor
    public CuentaAhorros() {
        super(TipoCuenta.AHORROS, 0);
    }

    // Retirar con 1.5% de interes
    @Override
    public String retirar(double monto) {
        if (monto <= 0) return "ERROR: El monto debe ser mayor a cero.";

        double interes = monto * 0.015;
        double total   = monto + interes;

        if (total > getSaldo()) {
            return "ERROR: Saldo insuficiente. Recuerda que se cobra 1.5% de interes. " +
                   "Limite: $" + String.format("%,.0f", Math.floor(getSaldo() / 1.015));
        }

        setSaldo(getSaldo() - total);
        registrarMovimiento("Retiro Ahorros (interes 1.5%: $" + String.format("%,.0f", interes) + ")", monto);
        return "Retiro exitoso. Interes cobrado: $" + String.format("%,.0f", interes) +
               ". Nuevo saldo: $" + String.format("%,.0f", getSaldo());
    }
}
