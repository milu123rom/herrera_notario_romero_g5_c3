package ar.edu.ungs.prog2;

import java.time.LocalDateTime;

public class PlantaEolica extends PlantasRenovables {

    private int cantidadDeAerogeneradores;
    private double alturaDeLasTorres;
    private double velocidadMinDeVientoNecesaria;

    public PlantaEolica(double capacidadMax, String direccion,
            String nombre, boolean activaEstado,
            LocalDateTime fechaHoraPuestaEnFuncionamiento,
            String fuentePrimaria, double eficienciaPromedio,
            int cantidadDeAerogeneradores,
            double alturaDeLasTorres,
            double velocidadMinDeVientoNecesaria) {

        super(capacidadMax, direccion, nombre, activaEstado,
                fechaHoraPuestaEnFuncionamiento,
                fuentePrimaria, eficienciaPromedio);
        
        if (cantidadDeAerogeneradores < 0) {
            throw new IllegalArgumentException("La cantidad de aerogeneradores no puede ser negativa");
        }

        if (alturaDeLasTorres < 0) {
            throw new IllegalArgumentException("La altura de las torres no puede ser negativa");
        }

        if (velocidadMinDeVientoNecesaria < 0) {
            throw new IllegalArgumentException("La velocidad mínima del viento no puede ser negativa");
        }
        

        this.cantidadDeAerogeneradores = cantidadDeAerogeneradores;
        this.alturaDeLasTorres = alturaDeLasTorres;
        this.velocidadMinDeVientoNecesaria =
                velocidadMinDeVientoNecesaria;
    }

    @Override
    public double capacidadMinimaCuota() {
        return calcularCuotaRenovable();
    }

    @Override
    public boolean cumpleCuota(Monitoreo monitoreo) {
        return monitoreo != null && monitoreo.cumpleCuotaEolica(this);
    }

    public String horasSinVelocidadMinima() {

        boolean[] horasConVientoBajo = new boolean[24];
        boolean[] horasConVientoSuficiente = new boolean[24];

        recorrerMonitoreos(horasConVientoBajo, horasConVientoSuficiente, velocidadMinDeVientoNecesaria);

        StringBuilder resultado = new StringBuilder();

        agregarVelocidadesBajas(resultado, velocidadMinDeVientoNecesaria, horasConVientoSuficiente);

        return resultado.toString();
    }
	
	@Override
	public boolean admiteMonitoreo(Monitoreo monitoreo) {
	    return monitoreo != null && monitoreo.admitidoComoEolico();
	}
	
	@Override
	public String informeHorasViento() {

	    String horas = horasSinVelocidadMinima();

	    if (horas.isEmpty()) {
	        return "";
	    }

	    StringBuilder resultado = new StringBuilder();

	    resultado.append(nombrePlanta());
	    resultado.append(": ");
	    resultado.append(horas);

	    return resultado.toString();
	}


    @Override
    public String toString() {
        return "Planta eólica: "
                + DataDePlantas()
                + ", aerogeneradores: " + cantidadDeAerogeneradores
                + ", altura de torres: " + alturaDeLasTorres
                + ", velocidad mínima de viento: "
                + velocidadMinDeVientoNecesaria;
    }
}
