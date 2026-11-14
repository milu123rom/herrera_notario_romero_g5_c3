package ar.edu.ungs.prog2;

import java.time.LocalDateTime;

public class PlantaTermica extends Planta {

    private String tipoCombustible;
    private double objetivoEmisionesCo2PorHora;
    private double capacidadAlmacenamientoCombustible;

    public PlantaTermica(double capacidadMax, String direccion, String nombre, boolean activaEstado, LocalDateTime fechaHoraPuestaEnFuncionamiento,
            String tipoCombustible, double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {

        super(capacidadMax, direccion, nombre, activaEstado, fechaHoraPuestaEnFuncionamiento);
        
        if (tipoCombustible == null || tipoCombustible.isBlank()) {
            throw new IllegalArgumentException("El tipo de combustible no puede estar vacío");
        }

        if (objetivoEmisionesCo2PorHora < 0) {
            throw new IllegalArgumentException("El objetivo de emisiones no puede ser negativo");
        }

        if (capacidadAlmacenamientoCombustible < 0) {
            throw new IllegalArgumentException("La capacidad de almacenamiento no puede ser negativa");
        }

        this.tipoCombustible = tipoCombustible;
        this.objetivoEmisionesCo2PorHora = objetivoEmisionesCo2PorHora;
        this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
    }

    @Override
    public double capacidadMinimaCuota() {
        return capacidadMaximaParaCalculo() / 2.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo monitoreo) {
        return monitoreo != null && monitoreo.cumpleCuotaTermica(this, objetivoEmisionesCo2PorHora);
    }
    
    @Override
    public boolean admiteMonitoreo(Monitoreo monitoreo) {
        return monitoreo != null && monitoreo.admitidoComoTermico();
    }

    @Override
    public String toString() {
        return "Planta térmica: " + DataDePlantas()
                + ", combustible: " + tipoCombustible
                + ", objetivo de emisiones CO2: "
                + objetivoEmisionesCo2PorHora
                + ", capacidad de almacenamiento: "
                + capacidadAlmacenamientoCombustible;
    }
}
