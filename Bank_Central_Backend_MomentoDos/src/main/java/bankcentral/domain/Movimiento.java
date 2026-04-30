package bankcentral.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {

    // POJO - Plain Old Java Object

    // Atributos
    private String fecha;
    private String tipo;
    private double valor;

    // Constructor
    public Movimiento(String tipo, double valor) {
        this.fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.tipo  = tipo;
        this.valor = valor;
    }

    // Getters y Setters

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return fecha + " | " + tipo + " | $" + String.format("%,.0f", valor);
    }
}
