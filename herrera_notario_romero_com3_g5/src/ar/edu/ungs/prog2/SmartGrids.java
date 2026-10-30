package ar.edu.ungs.prog2;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;  

public class SmartGrids implements ISmartGrids {
	private Map<String, Planta> plantas;
	public SmartGrids() {
	    this.plantas = new LinkedHashMap<>();
	}


	@Override
	public void registrarPlantaSolar(String nombre, String direccion,
	        double capacidadMaxima,
	        String fechaHoraPuestaEnFuncionamiento,
	        String fuentePrimaria, double eficienciaPromedio,
	        double superficieCubierta, int cantidadPaneles,
	        double horasRadiacionDiaria) {
	
	    if (nombre == null || nombre.isBlank()
	            || plantas.containsKey(nombre)) {
	        throw new RuntimeException("Nombre de planta inválido o repetido");
	    }
	
	    LocalDateTime fecha =
	            LocalDateTime.parse(fechaHoraPuestaEnFuncionamiento);
	
	    PlantaSolar solar = new PlantaSolar(capacidadMaxima, direccion, nombre, true, fecha,
	            fuentePrimaria, eficienciaPromedio, superficieCubierta, cantidadPaneles, horasRadiacionDiaria);
	
	    plantas.put(nombre, solar);
	}



	@Override
	public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
	        String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
	        int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
	
	    if (nombre == null || nombre.isBlank()
	            || plantas.containsKey(nombre)) {
	        throw new RuntimeException(
	                "Nombre de planta inválido o repetido");
	    }
	
	    LocalDateTime fecha =
	            LocalDateTime.parse(fechaHoraPuestaEnFuncionamiento);
	
	    PlantaEolica eolica = new PlantaEolica(capacidadMaxima, direccion, nombre,
	            true, fecha, fuentePrimaria, eficienciaPromedio, cantidadAerogeneradores, alturaTorres, velocidadMinimaViento);
	
	    plantas.put(nombre, eolica);
	}



	@Override
	public void registrarPlantaHidraulica(String nombre,
	        String direccion, double capacidadMaxima,
	        String fechaHoraPuestaEnFuncionamiento,
	        String fuentePrimaria, double eficienciaPromedio,
	        int cantidadTurbinas, double capacidadEmbalse) {
	
	    if (nombre == null || nombre.isBlank() || plantas.containsKey(nombre)) {
	        throw new RuntimeException("Nombre de planta inválido o repetido");
	    }
	
	    LocalDateTime fecha = LocalDateTime.parse(fechaHoraPuestaEnFuncionamiento);
	
	    PlantaHidraulica hidraulica = new PlantaHidraulica(capacidadMaxima, direccion, nombre, true, fecha, fuentePrimaria, 
	    		eficienciaPromedio, cantidadTurbinas, capacidadEmbalse);

	    plantas.put(nombre, hidraulica);
	}



	@Override
	public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima, String fechaHoraPuestaEnFuncionamiento,
	        String tipoCombustible, double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
	
	    if (nombre == null || nombre.isBlank() || plantas.containsKey(nombre)) {
	        throw new RuntimeException(
	                "Nombre de planta inválido o repetido");
	    }
	
	    LocalDateTime fecha = LocalDateTime.parse(fechaHoraPuestaEnFuncionamiento);
	
	    PlantaTermica termica = new PlantaTermica(capacidadMaxima, direccion, nombre, true, fecha,
	            tipoCombustible, objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
	
	    plantas.put(nombre, termica);
	}


	@Override
	public List<String> consultarPlantas() {
		//este metodo ya esta hecho, cree en planta un metodo DataDePlantas el cual llamo
		List<String> listaConsultarPlantas = new ArrayList<>();
		for (Planta p : this.plantas.values()) {
			String textoDeLaPlanta = p.DataDePlantas();//aca llame a el metodo DataDePlantas que hize en clase Planta
			listaConsultarPlantas.add(textoDeLaPlanta); 
	    
		}
		return listaConsultarPlantas;
	}


	@Override
	public double capacidadMinimaCuota(String nombre) {
		if(nombre == null || !plantas.containsKey(nombre)) {
			throw new RuntimeException("La planta" + nombre + "no existe"); 
		}
		
		//Buscamos la planta y delegamos el calculo por polimorfismo
	    Planta planta = plantas.get(nombre);
	
	    return planta.capacidadMinimaCuota();
	}


	@Override
	public void activarPlanta(String nombre, String fechaHora) {
	    Planta planta = plantas.get(nombre);

	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }

	    planta.actualizarEstado(true, LocalDateTime.parse(fechaHora));
	}
	
	

	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
	    Planta planta = plantas.get(nombre);
	
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }
	
	    planta.actualizarEstado(false, LocalDateTime.parse(fechaHora));
	}

//	private Planta obtenerPlantaOExcepcion(String nombre) {
//	    Planta planta = this.plantas.get(nombre);
//	    if (planta == null) {
//	        throw new RuntimeException("La planta no existe: " + nombre);
//	    }
//	    return planta;
//	}
	
	@Override
	public boolean estaActiva(String nombre) {
	    Planta planta = plantas.get(nombre); 
	    
	    if(planta == null) {
	    	throw new RuntimeException("La planta no existe"); 
	    }
	    return planta.estaActiva(); 
	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora,
	        double energiaGenerada, int incidencias) {

	    Planta planta = plantas.get(nombre);

	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }

	    if (!(planta instanceof PlantaSolar)
	            && !(planta instanceof PlantaHidraulica)) {
	        throw new RuntimeException("Tipo de planta incorrecto");
	    }

	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);

	    Monitoreo monitoreo = new Monitoreo(
	            planta, fecha, energiaGenerada, incidencias);

	    if (!planta.registrarMonitoreo(monitoreo)) {
	        throw new RuntimeException("No se pudo registrar el monitoreo");
	    }
	}


	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
	
	    Planta planta = plantas.get(nombre);
	
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }
	
	    if (!(planta instanceof PlantaEolica)) {
	        throw new RuntimeException("La planta no es eólica");
	    }
	
	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);
	
	    MonitoreoEolico monitoreo = new MonitoreoEolico(
	            planta, fecha, energiaGenerada, incidencias,
	            velocidadViento);
	
	    if (!planta.registrarMonitoreo(monitoreo)) {
	        throw new RuntimeException("No se pudo registrar el monitoreo");
	    }
	}



	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
	        double co2Emitido, double combustibleUtilizado) {
	
	    Planta planta = plantas.get(nombre);
	
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }
	
	    if (!(planta instanceof PlantaTermica)) {
	        throw new RuntimeException("La planta no es térmica");
	    }
	
	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);
	
	    MonitoreoTermico monitoreo = new MonitoreoTermico(planta, fecha, energiaGenerada, incidencias,
	    		co2Emitido, combustibleUtilizado);
	
	    if (!planta.registrarMonitoreo(monitoreo)) {
	        throw new RuntimeException("No se pudo registrar el monitoreo");
	    }
	}



	@Override
	public double totalGenerado(String nombre) {
	    Planta planta = plantas.get(nombre); 
	    
	    if(planta == null) {
	    	throw new RuntimeException("La planta no existe"); 
	    }
	    return planta.totalGenerado(); 
	}



	@Override
	public int[] incidenciasPorHora(String nombre) {
	
	    Planta planta = plantas.get(nombre);
	
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }
	
	    return planta.incidenciasPorHora();
	}



	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
	
	    Planta planta = plantas.get(nombre);
	
	    if (planta == null) {
	        throw new RuntimeException("La planta no existe");
	    }
	
//	    LocalDateTime desde = LocalDateTime.parse(fechaDesde);
//	    LocalDateTime hasta = LocalDateTime.parse(fechaHasta);
	    
	    LocalDateTime desde = fechaDesde.contains("T") ? LocalDateTime.parse(fechaDesde) : LocalDate.parse(fechaDesde).atStartOfDay(); 
	    LocalDateTime hasta = fechaHasta.contains("T") ? LocalDateTime.parse(fechaHasta) : LocalDate.parse(fechaHasta).atTime(23, 59, 59); 
	
	    if (desde.isAfter(hasta)) {
	        throw new RuntimeException("El rango de fechas es inválido");
	    }
	
	    return planta.histogramaCumplimiento(desde, hasta);
	}

	

	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
	
//	    Map<String, Integer> resultado = new LinkedHashMap<>();
	    Map<String, Integer> resultado = new TreeMap<>(); 
	    
	    for (Planta planta : plantas.values()) {
	
	        if (planta instanceof PlantasRenovables) {
	
	            PlantasRenovables renovable = (PlantasRenovables) planta;
	            String fuente = renovable.consultarFuentePrimaria();
	
//	            if (resultado.containsKey(fuente)) {
//	                int cantidad = resultado.get(fuente);
//	                resultado.put(fuente, cantidad + 1);
//	            } else {
//	                resultado.put(fuente, 1);
//	            }
	            //Sumamos 1 a la fuente correspondiente usando getOrDefault
	            resultado.put(fuente, resultado.getOrDefault(fuente, 0) + 1); 
	        }
	    }
	
	    return resultado;
	}



	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
	
	    List<String> resultado = new ArrayList<>();
	
//	    for (String nombre : plantas.keySet()) {
//	
//	        Planta planta = plantas.get(nombre);
//	
//	        if (!planta.algunaVezCumplioCuota()) {
//	            resultado.add(nombre);
//	        }
//	    }
	    for(Planta planta : plantas.values()) {
	    	if(!planta.algunaVezCumplioCuota()) {
	    		resultado.add(planta.nombrePlanta()); 
	    	}
	    }
	
	    return resultado;
	}


	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
	
	    StringBuilder resultado = new StringBuilder();
	
	    for (Planta planta : plantas.values()) {
	
	        if (planta instanceof PlantaEolica) {
	
	            PlantaEolica eolica = (PlantaEolica) planta;
	            String horas = eolica.horasSinVelocidadMinima();
	
	            if (!horas.isEmpty()) {
	
	                if (resultado.length() > 0) {
	                    resultado.append("\n");
	                }
	
	                resultado.append(eolica.nombrePlanta());
	                resultado.append(": ");
	                resultado.append(horas);
	            }
	        }
	    }
	
	    return resultado.toString();
	}


	@Override
	public int limpiarMonitoreosSinDatos() {
	
	    int totalEliminados = 0;
	
	    for (Planta planta : plantas.values()) {
	        totalEliminados += planta.limpiarRegistrosSinDatos();
	    }
	
	    return totalEliminados;
	}



	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
	
//	    List<String> resultado = new ArrayList<>();
//	    List<String> nombres = new ArrayList<>(plantas.keySet());
	    List<PlantaEnergia> auxiliares = new ArrayList<>(); 
	    
	    for(Planta planta : plantas.values()) {
	    	auxiliares.add(new PlantaEnergia(planta)); 
	    }
	    
	    //Ordenamos con Collections.sort usando el Comparable
	    java.util.Collections.sort(auxiliares); 
	    
	    //Psamos los objetos auxiliares a String
	    List<String> resultado = new ArrayList<>(); 
	    for(PlantaEnergia p : auxiliares) {
	    	resultado.add(p.toString());; 
	    }
	    return resultado; 
	}



	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
	
	    List<String> resultado = new ArrayList<>();
	
	    LocalDateTime fecha = LocalDateTime.parse(fechaHora);
	
	    for (Planta planta : plantas.values()) {
	        if (planta.estuvoInactivaEn(fecha)) {
	            resultado.add(planta.nombrePlanta());
	        }
	    }
	
	    return resultado;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append("SMARTGRIDS\n"); 
		sb.append("Cantidad de plantas registradas en smartgrids: ").append(plantas.size()).append("\n"); 
		sb.append("Detalle de Plantas-\n"); 
		
		for(Planta p : plantas.values()) {
			sb.append("- ").append(p.toString()).append("\n"); 
		}
		return sb.toString(); 
	}


}
