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
        double[] velocidadesMaximas = new double[24];

        recorrerMonitoreos(horasConVientoBajo,
                horasConVientoSuficiente,
                velocidadMinDeVientoNecesaria);

        agregarVelocidadesBajas(velocidadesMaximas);

        StringBuilder resultado = new StringBuilder();

        for (int hora = 0; hora < 24; hora++) {

            if (horasConVientoBajo[hora] && !horasConVientoSuficiente[hora]) {
            	
            	resultado.append(" ");

                if (hora < 10) {
                    resultado.append("0");
                }

                resultado.append(hora);
                resultado.append(": ");
                
                if (velocidadesMaximas[hora] == (int) velocidadesMaximas[hora]) {
                    resultado.append((int) velocidadesMaximas[hora]);
                } else {
                    resultado.append(velocidadesMaximas[hora]);
                }
                
                resultado.append(" m/s\n");
            }
        }

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

	    resultado.append("- ");
	    resultado.append(nombrePlanta());
	    resultado.append(": ");
	    
	    if (velocidadMinDeVientoNecesaria == (int) velocidadMinDeVientoNecesaria) {
	        resultado.append((int) velocidadMinDeVientoNecesaria);
	    } else {
	        resultado.append(velocidadMinDeVientoNecesaria);
	    }
	    
	    resultado.append(" m/s\n");

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
