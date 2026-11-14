package ar.edu.ungs.prog2;
import java.time.LocalDateTime;
public class PlantaSolar extends PlantasRenovables {
    private double superficieCubierta;
    private int cantidadDePaneles;
    private double horasDeRadiacionDiaria;
    
    public PlantaSolar(double capacidadMax, String direccion, String nombre, boolean activaEstado,
            LocalDateTime fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            double superficieCubierta, int cantidadDePaneles, double horasDeRadiacionDiaria) {

        super(capacidadMax, direccion, nombre, activaEstado, fechaHoraPuestaEnFuncionamiento, fuentePrimaria, eficienciaPromedio);
        
        if (superficieCubierta < 0) {
            throw new IllegalArgumentException("La superficie no puede ser negativa");
        }

        if (cantidadDePaneles < 0) {
            throw new IllegalArgumentException("La cantidad de paneles no puede ser negativa");
        }

        if (horasDeRadiacionDiaria < 0 || horasDeRadiacionDiaria > 24) {
            throw new IllegalArgumentException("Las horas de radiación deben estar entre 0 y 24");
        }
        

        this.superficieCubierta = superficieCubierta;
        this.cantidadDePaneles = cantidadDePaneles;
        this.horasDeRadiacionDiaria = horasDeRadiacionDiaria;
    }


    @Override
    public double capacidadMinimaCuota() {
        return calcularCuotaRenovable()
                * horasDeRadiacionDiaria / 24.0;
    }

    @Override
    public boolean cumpleCuota(Monitoreo monitoreo) {
        return monitoreo != null
                && monitoreo.correspondeA(this)
                && monitoreo.consultarEnergiaGenerada()
                        >= capacidadMinimaCuota();
    }
    
    @Override
    public boolean admiteMonitoreo(Monitoreo monitoreo) {
        return monitoreo != null && monitoreo.admitidoComoComun();
    }


    @Override
    public String toString() {
        return "Planta solar: " + DataDePlantas() + ", paneles: " + cantidadDePaneles + ", superficie: " + superficieCubierta + " m²"
                + ", horas de radiación diaria: " + horasDeRadiacionDiaria;
    }
}


