package ar.edu.ungs.prog2;

import java.time.LocalDateTime;

public class Monitoreo {

    private Planta planta;
    protected LocalDateTime fechaHora;
    protected double totalEnergiaGenHora;
    protected int cantIncidentesPorPeriodo;

    public Monitoreo(Planta planta, LocalDateTime fechaHora, double totalEnergiaGenHora, int cantIncidentesPorPeriodo) {

        if (planta == null || fechaHora == null) {
            throw new IllegalArgumentException(
                    "La planta y la fecha no pueden ser nulas");
        }

        if (totalEnergiaGenHora < 0 || cantIncidentesPorPeriodo < 0) {
            throw new IllegalArgumentException(
                    "La energia y las incidencias no pueden ser negativas");
        }

        this.planta = planta;
        this.fechaHora = fechaHora;
        this.totalEnergiaGenHora = totalEnergiaGenHora;
        this.cantIncidentesPorPeriodo = cantIncidentesPorPeriodo;
    }

    public LocalDateTime consultarFechaHora() {
        return fechaHora;
    }

    public double consultarEnergiaGenerada() {
        return totalEnergiaGenHora;
    }

    public int consultarIncidentes() {
        return cantIncidentesPorPeriodo;
    }

    public boolean sinDatos() {
        return totalEnergiaGenHora == 0
                && cantIncidentesPorPeriodo == 0;
    }

    boolean correspondeA(Planta planta) {
        return this.planta == planta;
    }
    
    public boolean cumpleCuotaEolica(PlantaEolica planta) {
        return false; //Devuelve false porque un monitoreo común no contiene los datos específicos de un monitoreo eólico.
    }
    
    public boolean cumpleCuotaTermica(PlantaTermica planta, double limiteCO2) {
        return false; //El monitoreo común devuelve false porque no contiene información sobre emisiones de CO2
    }
    
    public boolean admitidoComoComun() {
        return true;
    }
    
    public boolean admitidoComoEolico() {
        return false;
    }

    public boolean admitidoComoTermico() {
        return false;
    }
    
    public void registrarEstadoViento(boolean[] horasConVientoBajo, boolean[] horasConVientoSuficiente, double velocidadMinima) {
        // Un monitoreo común no registra velocidad del viento.
    }
    
    public void agregarVelocidadBaja(double[] velocidadesMaximas) {
        // Las otras clases de monitoreo no registran viento.
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();

        texto.append("Fecha y Hora: ").append(fechaHora);
        texto.append(" , energia generada: ")
                .append(totalEnergiaGenHora);
        texto.append(", incidencias: ")
                .append(cantIncidentesPorPeriodo);

        return texto.toString();
    }
}
