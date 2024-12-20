package org.alura;

public class Moneda {
    public Double conversion_rate;

    public Moneda(Double conversion_rate) {
        this.conversion_rate = conversion_rate;
    }

    public Double getConversion_rate() {
        return conversion_rate;
    }

    public void setConversion_rate(Double conversion_rate) {
        this.conversion_rate = conversion_rate;
    }
}
