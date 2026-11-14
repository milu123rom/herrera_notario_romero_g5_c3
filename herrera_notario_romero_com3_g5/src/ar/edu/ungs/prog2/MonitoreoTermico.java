package ar.edu.ungs.prog2;

import java.time.LocalDateTime;

public class MonitoreoTermico extends Monitoreo {

    private double cantidadDeCO2Generado;
    private double cantDeCombustible;

    public MonitoreoTermico(Planta planta, LocalDateTime fechaHora, double totalEnergiaGenHora, int cantIncidentesPorPeriodo,
            double cantidadDeCO2Generado, double cantDeCombustible) {

        super(planta, fechaHora, totalEnergiaGenHora, cantIncidentesPorPeriodo);

        if (cantidadDeCO2Generado < 0 || cantDeCombustible < 0 || !Double.isFinite(cantidadDeCO2Generado)
                || !Double.isFinite(cantDeCombustible)) {
            throw new IllegalArgumentException("El CO2 y el combustible deben tener valores válidos");
        }

        this.cantidadDeCO2Generado = cantidadDeCO2Generado;
        this.cantDeCombustible = cantDeCombustible;
    }

    public double consultarCO2Generado() {
        return cantidadDeCO2Generado;
    }

    public double consultarCombustibleUtilizado() {
        return cantDeCombustible;
    }
    
    @Override
    public boolean cumpleCuotaTermica(
            PlantaTermica planta, double limiteCO2) {

        return correspondeA(planta)
                && consultarEnergiaGenerada() >= planta.capacidadMinimaCuota()
                && cantidadDeCO2Generado < limiteCO2;
    }
    
    @Override
    public boolean admitidoComoComun() {
        return false; //Esto permite que las plantas solares acepten únicamente monitoreos comunes.
    }
    
    @Override
    public boolean admitidoComoTermico() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder(super.toString());

        texto.append(", CO2 generado: ");
        texto.append(cantidadDeCO2Generado);

        texto.append(", combustible utilizado: ");
        texto.append(cantDeCombustible);

        return texto.toString();
    }
}
