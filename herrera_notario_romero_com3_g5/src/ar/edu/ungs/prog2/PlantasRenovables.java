package ar.edu.ungs.prog2;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class PlantasRenovables extends Planta {

    private String fuentePrimaria;
    private double eficienciaPromedio;

    public PlantasRenovables(double capacidadMax, String direccion, String nombre, boolean activaEstado, LocalDateTime fechaHoraPuestaEnFuncionamiento,
                            String fuentePrimaria,
                            double eficienciaPromedio) {

        super(capacidadMax, direccion, nombre,
              activaEstado, fechaHoraPuestaEnFuncionamiento);
        
        if (eficienciaPromedio < 0) {
            throw new IllegalArgumentException("La eficiencia promedio no puede ser negativa");
        }
        
        this.eficienciaPromedio = eficienciaPromedio;
        this.fuentePrimaria = fuentePrimaria;
    }
    //no volver a agregar actualizarEstado(), cumpleCuota() ni capacidadMinimaCuota() porque los hereda de Planta.


    // Cálculo compartido por las plantas renovables
    protected double calcularCuotaRenovable() {
        return capacidadMaximaParaCalculo() * eficienciaPromedio;
    }

    // Permite comprobar la fuente sin exponer el atributo
    public boolean usaFuentePrimaria(String fuente) {
        return fuente != null
                && fuentePrimaria.equalsIgnoreCase(fuente);
    }

	public String consultarFuentePrimaria() {
	    return fuentePrimaria;
	}
	
	@Override
	public void contarFuenteRenovable(Map<String, Integer> resultado) {

	    if (resultado.containsKey(fuentePrimaria)) {
	        int cantidad = resultado.get(fuentePrimaria);
	        resultado.put(fuentePrimaria, cantidad + 1);
	    } else {
	        resultado.put(fuentePrimaria, 1);
	    }
	}
	

    @Override
    public abstract String toString();
}
