package ar.edu.ungs.prog2;

public class PlantaEnergia implements Comparable<PlantaEnergia>{
	private Planta planta; 
	
	public PlantaEnergia(Planta planta) {
		this.planta = planta; 
	}
	
	/*@Override
	public int compareTo(PlantaEnergia otra) {
		//Orden descendente (de mayor a menor energia generada)
//		return Double.compare(otra.planta.totalGenerado(), this.planta.totalGenerado()); 
		return (int) (otra.planta.totalGenerado() - this.planta.totalGenerado());  
		
		//puede generar un orden incorrecto porque convertís 
		la diferencia de dos números decimales a int
	}*/
	
	@Override //conserva el orden de mayor a menor sin perder los decimales
	public int compareTo(PlantaEnergia otra) {
	    return Double.compare(otra.planta.totalGenerado(),this.planta.totalGenerado() );
	}
	
	@Override
	public String toString() {
		return planta.nombrePlanta() + ": " + planta.totalGenerado();
	}
}
