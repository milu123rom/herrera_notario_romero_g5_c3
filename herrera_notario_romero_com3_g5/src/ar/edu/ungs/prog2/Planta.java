package ar.edu.ungs.prog2;
import java.time.format.DateTimeFormatter;
import java.util.Iterator; 
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public abstract class Planta {
    private double capacidadMax;
    private String direccion;
    private String nombre;
    private List<Monitoreo> registro;
    private boolean activaEstado;
    private LocalDateTime fechaHoraPuestaEnFuncionamiento; // Nota: Si es FechaHora, considera ajustar el tipo
    private List<PeriodoInactividad> periodosInactivos;

    public Planta(double capacidadMax, String direccion, String nombre, boolean activaEstado,  LocalDateTime fechaHoraPuestaEnFuncionamiento) {
        this.capacidadMax = capacidadMax;
        this.direccion = direccion;
        this.nombre = nombre;
        this.activaEstado = activaEstado;
        this.fechaHoraPuestaEnFuncionamiento = fechaHoraPuestaEnFuncionamiento;
        
        this.registro = new ArrayList<>();
        this.periodosInactivos = new ArrayList<>();
    }
    
    public String DataDePlantas() {
    	String estadoDePlantaString; 
    	if(this.activaEstado) {
    		estadoDePlantaString = "ACTIVA"; 
    	} else {
    		estadoDePlantaString = "INACTIVA"; 
    	}
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String fechaExacta = this.fechaHoraPuestaEnFuncionamiento.format(formato);
        
        return this.nombre + " (" + estadoDePlantaString + "): " + fechaExacta;
    }
    
    
    public void actualizarEstado(boolean nuevoEstado, LocalDateTime fechaHora) {

        if (fechaHora == null ||
                fechaHora.isBefore(fechaHoraPuestaEnFuncionamiento)) {
            throw new RuntimeException("Fecha inválida");
        }

        if (activaEstado == nuevoEstado) {
            throw new RuntimeException("La planta ya está en ese estado");
        }

        if (!nuevoEstado) {

            if (!periodosInactivos.isEmpty()) {
                PeriodoInactividad ultimoPeriodo =
                        periodosInactivos.get(periodosInactivos.size() - 1);

                if (!ultimoPeriodo.permiteNuevoPeriodo(fechaHora)) {
                    throw new RuntimeException("Fecha anterior al último período");
                }
            }

            periodosInactivos.add(new PeriodoInactividad(fechaHora, null));

        } else {

            PeriodoInactividad ultimoPeriodo =
                    periodosInactivos.get(periodosInactivos.size() - 1);

            ultimoPeriodo.cerrarPeriodo(fechaHora);
        }

        activaEstado = nuevoEstado;
    }
    
  
    
    public abstract double capacidadMinimaCuota();
    public abstract boolean cumpleCuota(Monitoreo monitoreo);

    
    public boolean estuvoInactivaEn(LocalDateTime fechaHora) { //Recorre los períodos de inactividad y pregunta si la fecha consultada pertenece a alguno.
        for (PeriodoInactividad periodo : periodosInactivos) {
            if (periodo.contiene(fechaHora)) {
                return true;
            }
        }
        return false;
    }
    
    

    /*Verifica que el monitoreo corresponda a esa planta, comprueba que la planta esté activa,
    rechaza fechas anteriores a su puesta en funcionamiento, comprueba que la planta no haya estado inactiva en esa fecha y evita 
    registrar dos monitoreos con la misma fecha y hora.*/
    public boolean registrarMonitoreo(Monitoreo m) { 

        if (m == null || !m.correspondeA(this)) {
            return false;
        }

        LocalDateTime fechaHora = m.consultarFechaHora();

        if (fechaHora == null
                || !activaEstado
                || fechaHora.isBefore(fechaHoraPuestaEnFuncionamiento)
                || estuvoInactivaEn(fechaHora)) {
            return false;
        }

        for (Monitoreo existente : registro) {
            if (existente.consultarFechaHora().equals(fechaHora)) {
                return false;
            }
        }

        registro.add(m);
        return true;
    }
    
    protected double capacidadMaximaParaCalculo() {
        return capacidadMax;
    }
    
    //Requerimiento 11: Elimina los monitoreos cuya energia e incidencias sean 0 
    public int limpiarRegistrosSinDatos() {
    	int eliminados = 0; 
    	Iterator<Monitoreo> iterador = registro.iterator(); 
    	
    	while(iterador.hasNext()) {
    		Monitoreo m = iterador.next(); 
    		
    		if(m.sinDatos()) {
    			iterador.remove();
    			eliminados++; 
    		}
    	}
    	return eliminados; 
    }
    
    //Recorre todos los monitoreos registrados en esa planta y suma la energia generada de cada uno
    public double totalGenerado() {
    	double total = 0; 
    	for(Monitoreo m : registro) {
    		total += m.consultarEnergiaGenerada(); 
    	}
    	return total; 
    }
    
    public int totalIncidencias() { //suma las incidencias
        int total = 0;

        for (Monitoreo m : registro) {
            total += m.consultarIncidentes();
        }

        return total;
    }

	public int[] incidenciasPorHora() {
	
	    int[] incidencias = new int[24];
	
	    for (Monitoreo monitoreo : registro) {
	        int hora = monitoreo.consultarFechaHora().getHour();
	
	        incidencias[hora] += monitoreo.consultarIncidentes();
	    }
	
	    return incidencias;
	}
	

	public int[] histogramaCumplimiento(LocalDateTime desde,
	        LocalDateTime hasta) {
	
	    int[] histograma = new int[24];  //24 horas del dia (0 a 23)
	
	    for (Monitoreo m : registro) {
	    	if(m.consultarFechaHora().isEqual(desde) || m.consultarFechaHora().isAfter(desde) && (m.consultarFechaHora().isEqual(hasta) || m.consultarFechaHora().isBefore(hasta))){
	    		if(this.cumpleCuota(m)) {
	    			int hora = m.consultarFechaHora().getHour(); //Devuelve un avlor de 0 a 23
	    			histograma[hora]++; 
	    		}
	    	}
	    }
	
	    return histograma;
	}


	public boolean algunaVezCumplioCuota() {
	
	    for (Monitoreo monitoreo : registro) {
	        if (cumpleCuota(monitoreo)) {
	            return true;
	        }
	    }
	
	    return false;
	}

	protected void recorrerMonitoreosEolicos(boolean[] horasConVientoBajo, boolean[] horasConVientoSuficiente,
	                                        double velocidadMinima) {
	
	    for (Monitoreo monitoreo : registro) {
	
	        if (monitoreo instanceof MonitoreoEolico) {
	
	            MonitoreoEolico eolico = (MonitoreoEolico) monitoreo;
	            int hora = eolico.consultarFechaHora().getHour();
	
	            if (eolico.consultarVelocidadDelVientoPromedio() < velocidadMinima) {
	                horasConVientoBajo[hora] = true;
	            } else {
	                horasConVientoSuficiente[hora] = true;
	            }
	        }
	    }
	}


    public String nombrePlanta() {
    	return this.nombre; 
    }
    
    public boolean estaActiva() {
    	return activaEstado; 
    }

    @Override
    public abstract String toString();
}
