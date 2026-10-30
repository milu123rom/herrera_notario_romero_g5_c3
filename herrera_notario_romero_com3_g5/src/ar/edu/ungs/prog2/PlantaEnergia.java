package ar.edu.ungs.prog2;

public class PlantaEnergia implements Comparable<PlantaEnergia>{
	private Planta planta; 
	
	public PlantaEnergia(Planta planta) {
		this.planta = planta; 
	}
	
	@Override
	public int compareTo(PlantaEnergia otra) {
		//Orden descendente (de mayor a menor energia generada)
//		return Double.compare(otra.planta.totalGenerado(), this.planta.totalGenerado()); 
		return (int) (otra.planta.totalGenerado() - this.planta.totalGenerado());
	}
	
	@Override
	public String toString() {
		return planta.nombrePlanta() + ": " + planta.totalGenerado();
	}
}
