package PRINCIPAL;
import java.util.Stack;

public class GeneradorCodigo {
	public static StringBuilder codigo = new StringBuilder();
	private static final Stack<String> pila_tokens = new Stack<>();
	private static String ultimaComparacion = "";
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
        for (String token : Parser.polaca) {
            switch (token) {
                case "*":
                case "+":
                case "=:":  
                case "-":
                case "/":
                case ">=":
                case ">":   
                case "<=":
                case "<":
                case "=!":
                case "=":
                    generarOperador(token);
                    break;
                case "#BI":
                    generarSalto("JMP");
                    break;
                case "#BF":
                    generarSalto(ultimaComparacion);
                    break;
                case "#BT":
                    generarSalto(negacion(ultimaComparacion));
                    break;
                case "#CALL":
                    generarLlamadoFuncion();
                    break;
                case "\\RET":
                    generarCodigoRetorno();
                    break;
                case "\\ENDP":
                    generarCodigoFinalFuncion(token);
                    break;
                default:
                    if (token.startsWith(Parser.STRING_CHAR)) { //encontramos una cadena
                        token = token.substring(1);
                        codigo.append("invoke MessageBox, NULL, addr ").append(token).append(", addr ").append(token).append(", MB_OK \n");
                    } else if (token.startsWith(":")) {   //entramos un label
                        codigo.append(token.substring(1)).append(":\n");
                    } else if (token.startsWith("!")) {   // Encontramos el comienzo de una funcion
                        generarCabeceraFuncion(token);
                    } else {
                        pila_tokens.push(token);
                    }

                    break;
            }

            ++posicionActualPolaca;
            //Impresion por pantalla para debuggear el codigo
            //System.out.println("Se leyo el token: " + token + ", la pila actual es: " + pila_tokens);
        }

        codigo.append("invoke ExitProcess, 0\n")
              .append("end START");

        generarCabecera();	
	}
//Funcion encargada de generar la cabecera del codigo
	private static void generarCabecera() {
		
	}
	
	public static void generarOperador(String operador) {
		
	}
	
	private static void generarSalto(String salto) {
		
	}
	
	private static String negacion(String comparacion) {
		return null;
	}
	
	private static void generarLlamadoFuncion() {
		
	}
	
    private static void generarCodigoRetorno() {
    	
    }
    
    private static void generarCodigoFinalFuncion(String token) {
    	
    }
    
    private static void generarCabeceraFuncion(String token) {
    	
    }
}
