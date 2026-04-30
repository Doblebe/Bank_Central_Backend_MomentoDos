package bankcentral.domain;

import bankcentral.enums.TipoCuenta;

public class TarjetaCredito extends Cuenta {

    // Atributos propios de la tarjeta
    private double cupo;

    // Constructor
    public TarjetaCredito(double cupo) {
        super(TipoCuenta.CREDITO, 0);
        this.cupo = cupo;
    }

    // Getters propios
    public double getCupo() {
        return cupo;
    }

    public double getCupoDisponible() {
        return cupo - getSaldo(); // getSaldo() aqui representa la deuda
    }

    // Calcular tasa segun numero de cuotas
    private double calcularTasa(int cuotas) {
        if (cuotas <= 2) return 0.0;
        if (cuotas <= 6) return 0.019;  // 1.9% mensual
        return 0.023;                    // 2.3% mensual
    }

    // Calcular cuota mensual con formula de amortizacion
    private double calcularCuota(double capital, int cuotas) {
        double tasa = calcularTasa(cuotas);
        if (tasa == 0) return capital / cuotas;
        return (capital * tasa) / (1 - Math.pow(1 + tasa, -cuotas));
    }

    // Comprar con la tarjeta
    public String comprar(double monto, int cuotas) {
        if (monto <= 0) return "ERROR: El monto debe ser mayor a cero.";
        if (monto > getCupoDisponible())
            return "ERROR: Cupo insuficiente. Disponible: $" + String.format("%,.0f", getCupoDisponible());

        double cuotaMensual = calcularCuota(monto, cuotas);
        setSaldo(getSaldo() + monto);
        registrarMovimiento("Compra " + cuotas + " cuotas - Cuota: $" +
                            String.format("%,.0f", cuotaMensual), monto);
        return "Compra registrada. Cuota mensual: $" + String.format("%,.0f", cuotaMensual) +
               ". Deuda total: $" + String.format("%,.0f", getSaldo());
    }

    // Pagar deuda
    public String pagarDeuda(double monto) {
        if (monto <= 0) return "ERROR: El monto debe ser mayor a cero.";
        if (monto > getSaldo())
            return "ERROR: El pago supera la deuda ($" + String.format("%,.0f", getSaldo()) + ").";

        setSaldo(getSaldo() - monto);
        registrarMovimiento("Pago tarjeta de credito", monto);
        return "Pago realizado. Deuda restante: $" + String.format("%,.0f", getSaldo());
    }

    // Los retiros no estan permitidos en tarjeta de credito
    @Override
    public String retirar(double monto) {
        return "ERROR: La tarjeta de credito no permite retiros en efectivo.";
    }

    @Override
    public String toString() {
        return "CREDITO | Cupo: $" + String.format("%,.0f", cupo) +
               " | Deuda: $" + String.format("%,.0f", getSaldo()) +
               " | Disponible: $" + String.format("%,.0f", getCupoDisponible());
    }
}
