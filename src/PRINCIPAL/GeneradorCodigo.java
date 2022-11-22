package PRINCIPAL;
import java.util.Stack;

import AL.TablaSimbolos;
import AL.TablaTipos;

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
                case "#BT": //QUE ES #BT???
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
	
//Utilizada para generar el codigo necesario para todos los datos del programa, presentes en la TS
	private static void generarCodigoDatos(StringBuilder cabecera) {
		
	}
	
	public static void generarOperador(String operador) {
		String op2 = pila_tokens.pop();
		String op1 = pila_tokens.pop();
		
		if(operador.equals("=:")) {
			String aux = op1;
			op1 = op2;
			op2 = aux;
		}
		
		String tipo = TablaTipos.getTipoAbarcativo(op1, op2, operador);
		switch (tipo) {
			case TablaTipos.LONG_TYPE:
				generarOperacionEnteros(op1, op2, operador);
				break;
			case TablaTipos.FLOAT_TYPE:
				generarOperacionFlotantes(op1, op2, operador);
				break;
			case TablaTipos.FUNC_TYPE:
				generarOperacionFuncion(op1, op2);
				break;
			default: 
				System.out.println("Hay error");
				//TablaSimbolos.imprimirTabla();
		}
	}
	
	private static void generarOperacionEnteros(String op1, String op2, String operador) {
		op1 = renombre(op1);
		op2 = renombre(op2);
		
		String aux;
		
		switch(operador) {
			case "+":
				codigo.append("MOV ECX, ").append(op1).append("\n");
				codigo.append("ADD ECX, ").append(op2).append("\n");
				aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
				codigo.append("MOV ").append(aux).append(", ECX\n");
				pila_tokens.push(aux);
				break;
			case "-":
				codigo.append("MOV ECX, ").append(op1).append("\n");
				codigo.append("SUB ECX, ").append(op2).append("\n");
				aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
				codigo.append("MOV ").append(aux).append(", ECX\n");
				pila_tokens.push(aux);
				break;
			case "*":
				codigo.append("MOV EAX, ").append(op1).append("\n");
				codigo.append("MUL ").append(op2).append("\n");
				aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
				generarErrorOverflow(aux);
				codigo.append("MOV ").append(aux).append(", EAX\n");
				pila_tokens.push(aux);
				break;
			case "=:":
				codigo.append("MOV ECX, ").append(op2).append("\n");
				codigo.append("MOV ").append(op1).append(", ECX\n");
				break;
			case "/":
				aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
				codigo.append("CMP ").append(op2).append(", 00h\n");
				generarErrorDivCero(aux);
				codigo.append("MOV EAX, ").append(op1).append("\n");
				codigo.append("DIV ").append(op2).append("\n");
				codigo.append("MOV ").append(aux).append(", EAX\n");
				pila_tokens.push(aux);
				break;
			case "=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); 
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); 
                codigo.append("JE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); 
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                ultimaComparacion = "JNE";
                break;
			case "=!":
				codigo.append("MOV ECX, ").append(op2).append("\n"); 
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); 
                codigo.append("JNE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); 
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                ultimaComparacion = "JE";
                break;
			case ">=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JAE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                ultimaComparacion = "JB";
                break;
			case ">":
				codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JA ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                ultimaComparacion = "JBE";
                break;
			case "<=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JBE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                ultimaComparacion = "JA";
                break;
			case "<":
				codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JB " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                ultimaComparacion = "JAE";
                break;
            default:
            	codigo.append("ERROR, se entro a default en operacion de enteros").append("\n");
				break;
		}
	}
	
	private static void generarOperacionFlotantes(String op1, String op2, String operador) { 
		op1 = renombre(op1);
		op2 = renombre(op2);
		
		String aux;
		
		//Los que sean LONG ya deben venir convertidos a FLOAT
		
		switch(operador) {
			case "+":
				codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");

                codigo.append("FADD\n");
                aux = ocuparAuxiliar(TablaTipos.FLOAT_TYPE);
                codigo.append("FSTP ").append(aux).append("\n");
                pila_tokens.push(aux);
                break;
			case "-":
				codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");

                codigo.append("FSUB\n");
                aux = ocuparAuxiliar(TablaTipos.FLOAT_TYPE);
                codigo.append("FSTP ").append(aux).append("\n");
                pila_tokens.push(aux);
                break;
			case "*":
				codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");
                
                codigo.append("FMUL\n");
                aux = ocuparAuxiliar(TablaTipos.FLOAT_TYPE);
                codigo.append("FSTP ").append(aux).append("\n");
                pila_tokens.push(aux);
                break;
			case "=:":
				codigo.append("FLD ").append(op2).append("\n");
	            codigo.append("FSTP ").append(op1).append("\n");
	             break;
			case "/":
				aux = ocuparAuxiliar(TablaTipos.FLOAT_TYPE);
                codigo.append("FLD ").append(op2).append("\n"); //cargo el operando dos para luego compararlo con cero
                
                //guardar 00h en una variable auxiliar
                String _cero = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV ").append(_cero).append(", 00h\n");
                codigo.append("FCOM " + _cero + "\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                generarErrorDivCero(aux);

                codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FDIV\n");
                codigo.append("FSTP ").append(aux).append("\n");
                pila_tokens.push(aux);
                break;
			case "=":
				codigo.append("FLD ").append(op1).append("\n"); 
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); 
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                break;
			case "=!":
				codigo.append("FLD ").append(op1).append("\n"); 
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JNE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); 
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                break;
			case ">=":
				codigo.append("FLD ").append(op1).append("\n"); 
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JAE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); 
                codigo.append(aux.substring(1) + ":\n"); 
                pila_tokens.push(aux);
                break;
			case">":
				codigo.append("FLD ").append(op1).append("\n"); 
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n"); 
                codigo.append("JA " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); 
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                pila_tokens.push(aux);
                break;
			case "<=":
				codigo.append("FLD ").append(op1).append("\n"); 
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JBE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); 
                codigo.append(aux.substring(1) + ":\n"); 
                pila_tokens.push(aux);
                break;
			case "<":
				codigo.append("FLD ").append(op1).append("\n"); 
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.LONG_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JB " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); 
                codigo.append(aux.substring(1) + ":\n"); 
                pila_tokens.push(aux);
                break;
			default:
				codigo.append("ERROR, se entro en default para las operaciones de flotantes");
				break;
		}
	}
	
	public static void generarOperacionFuncion(String op1, String op2) {
		String punt_op2 = TablaSimbolos.obtenerSimbolo(op2);
		String uso = TablaSimbolos.obtenerAtributo(punt_op2, "uso");
		
		op1 = renombre(op1);
		
		if(uso.equals("variable"))
			op2 = renombre(op2);
		
		codigo.append("MOV EAX, ").append(op2).append("\n");
		codigo.append("MOV ").append(op1).append(", EAX\n");
	}
	
	private static String renombre(String token) {
		char caracter = token.charAt(0);
		String puntToken = TablaSimbolos.obtenerSimbolo(token);
		
		if(TablaSimbolos.obtenerAtributo(puntToken, "uso").equals("constante")) {
			return "@" + token.replace('.', '@').replace('-', '@').replace('-', '@');
		} else if(Character.isLowerCase(caracter) || Character.isUpperCase(caracter) || caracter == '_') {
			return "_" + token;
		} else {
			return token;
		}
	}
	
	private static String ocuparAuxiliar(String tipo) {
		String retorno = "@aux" + auxiliarDisponible;
		++auxiliarDisponible;
		
		//Se agrega la auxiliar a la tabla de simbolos
		TablaSimbolos.agregarSimb(retorno);
		TablaSimbolos.agregarAtributo(retorno, "tipo", tipo);
		return retorno;
	}

	//Genera codigo necesario ante un error por overflow en multiplicacion de enteros
	private static void generarErrorOverflow(String aux){
		codigo.append("JNO ").append(aux.substring(1)).append("\n");    
        codigo.append("invoke MessageBox, NULL, addr @ERROR_OVERFLOW_PRODUCTO, addr @ERROR_OVERFLOW_PRODUCTO, MB_OK\n");
        codigo.append("invoke ExitProcess, 0\n");
        codigo.append(aux.substring(1)).append(":\n"); //declaro una label 
	}
	
	//Genera codigo necesario ante un error de division por cero
	private static void generarErrorDivCero(String aux){
		codigo.append("JNE ").append(aux.substring(1)).append("\n");
        codigo.append("invoke MessageBox, NULL, addr @ERROR_DIVISION_POR_CERO, addr @ERROR_DIVISION_POR_CERO, MB_OK\n");
        codigo.append("invoke ExitProcess, 0\n");
        codigo.append(aux.substring(1)).append(":\n"); //declaro una label        
	}
	
	private static void generarSalto(String salto) {
		String direccion = pila_tokens.pop();    

        if (!salto.equals("JMP") && ultimaComparacion.equals("")) {
            String valor = pila_tokens.pop();
            String punt_valor = TablaSimbolos.obtenerSimbolo(valor);
            String uso = TablaSimbolos.obtenerAtributo(punt_valor, "uso");
            
            if (uso.equals("variable"))
                valor = renombre(valor);

            codigo.append("MOV ECX, ").append(valor).append("\n");
            codigo.append("OR ECX, 0\n");
            codigo.append("JE L").append(direccion).append("\n");
        } else {
            codigo.append(salto).append(" L").append(direccion).append("\n");
        }

        ultimaComparacion = "";
	}
	
	private static String negacion(String comparacion) {
		switch (comparacion) {
			case "JE": return "JNE";
			case "JNE": return "JE";
			case "JG": return "JLE";
			case "JLE": return "JG";
			case "JL": return "JGE";
			case "JGE": return "JL";
        	default: return comparacion;
		}
	}
	
	private static void generarLlamadoFuncion() {

	}
	
    private static void generarCodigoRetorno() {
    	generarOperador("=:");
    	codigo.append("RET\n");
    }
    
    private static void generarCodigoFinalFuncion(String token) {
    	String nombre_funcion = pila_tokens.pop();
    	codigo.append(nombre_funcion).append(" ").append(token.substring(1)).append("\n");
    }
    
    private static void generarCabeceraFuncion(String token) {
    	codigo.append(token.substring(1)).append(" PROC\n");
    }
}
