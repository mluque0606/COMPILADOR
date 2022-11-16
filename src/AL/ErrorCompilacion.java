package AL;

public class ErrorCompilacion {
	private String tipoError;
    private String mensajeError;
    private int linea;

    public ErrorCompilacion(String tipoError, String mensajeError, int linea) {
        this.tipoError = tipoError;
        this.mensajeError = mensajeError;
        this.linea = linea;
    }

    @Override
    public String toString() {
        return "Se detecto error de compilacion en la etapa: " + tipoError + ", linea: " + linea + ", detalle: " + mensajeError;
    }
}
