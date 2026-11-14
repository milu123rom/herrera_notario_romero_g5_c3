package ar.edu.ungs.prog2;
import java.time.LocalDateTime;

public class MonitoreoEolico extends Monitoreo {
 
    private double velocidadDelVientoPromedio;


    public MonitoreoEolico(Planta planta, LocalDateTime fechaHora,
            double totalEnergiaGenHora, int cantIncidentesPorPeriodo, double velocidadDelVientoPromedio) {

        super(planta, fechaHora, totalEnergiaGenHora, cantIncidentesPorPeriodo);

        if (velocidadDelVientoPromedio < 0
                || !Double.isFinite(velocidadDelVientoPromedio)) {
            throw new IllegalArgumentException(
                    "La velocidad del viento no puede ser negativa ni inválida");
        }

        this.velocidadDelVientoPromedio = velocidadDelVientoPromedio;
    }

    public double consultarVelocidadDelVientoPromedio() {
        return velocidadDelVientoPromedio;
    }
    
    @Override
    public boolean cumpleCuotaEolica(PlantaEolica planta) {
        return correspondeA(planta) && consultarEnergiaGenerada() >= planta.capacidadMinimaCuota();
    }
    
    @Override
    public boolean admitidoComoComun() {
        return false; //Esto permite que las plantas hidráulicas solo acepten monitoreos comunes.
    }
    
    @Override
    public boolean admitidoComoEolico() {
        return true;
    }
    
    @Override
    public void registrarEstadoViento(boolean[] horasConVientoBajo, boolean[] horasConVientoSuficiente, double velocidadMinima) {
        int hora = consultarFechaHora().getHour();

        if (velocidadDelVientoPromedio < velocidadMinima) {
            horasConVientoBajo[hora] = true;
        } else {
            horasConVientoSuficiente[hora] = true;
        }
    }
    
    @Override
    @Override
    public void agregarVelocidadBaja(StringBuilder resultado,
            double velocidadMinima,
            boolean[] horasConVientoSuficiente) {

        int hora = fechaHora.getHour();

        if (velocidadDelVientoPromedio < velocidadMinima
                && !horasConVientoSuficiente[hora]) {

            if (hora < 10) {
                resultado.append("0");
            }

            resultado.append(hora);
            resultado.append(": ");
            resultado.append(velocidadDelVientoPromedio);
            resultado.append(" m/s\n");
        }
    }
    
    //Imprime la informacion como se la desea  
    @Override
    public String toString() {
    	StringBuilder texto = new StringBuilder(super.toString()); 
    	
    	texto.append(", velocidad promedio del viento: "); 
    	texto.append(velocidadDelVientoPromedio); 
    	texto.append(" m/s"); 
    	
        return texto.toString(); 
    }
}

