package PRINCIPAL;
import java.util.Stack;

public class GeneradorCodigo {
	public static StringBuilder codigo = new StringBuilder();
	private static final Stack<String> pila_tokens = new Stack<>();
	private String ultimaComparacion = "";
	private static int auxiliarDisponible = 0;
	public static int posicionActualPolaca = 0;
	public static String ultimaFuncionLlamada;
	private static final String AUX_CONTRATO = "@contrato";
	private static String nombreAux2bytes = "aux2bytes";

//String de errores fijos posibles al generar el codigo.
	private static final String ERROR_DIVISION_POR_CERO = "ERROR: Division por cero";
	private static final String ERROR_OVERFLOW_PRODUCTO = "ERROR: Overflow en operacion de producto";   
	private static final String ERROR_INVOCACION = "ERROR: Invocacion de funcion a si misma no permitida";

//Funcion que genera el codigo del programa, utilizando los tokens de la polaca y simbolos de la tabla
	public static void generarCodigo() {
		
	}
//Funcion encargada de generar la cabecera del codigo
	private static void generarCabecera() {
		
	}
}
