package AL;
import java.util.ArrayList;
import java.util.List;
import ACCIONES_SEMANTICAS.*;

import ACCIONES_SEMANTICAS.Accion_Semantica;

public class AnalizadorLexico {
	 public final static char TAB = '\t';
	 public final static char BLANCO = ' ';
	 public final static char NL = '\n';
	 public final static char COMILLA_SIMPLE = '\'';
	 public final static int IDENTIFICADOR = 257;
	 public final static int CONSTANTE = 258;
	 public final static int CADENA = 259;
	 public final static int LONG_ID = 25;
	 public final static long MAX_VALOR_LONG = (long) Math.pow(2, 31) - 1;
	 public final static long MIN_VALOR_LONG = (long) Math.pow(-2, 31);
	 public final static double MIN_DOUBLE = 1.17549435F-38;
	 public final static double MAX_DOUBLE = 3.40282347F+38;   
	 private final static char DIGITO = '0';
	 private final static char MINUSCULA = 'a';
	 private final static char MAYUSCULA = 'A';
	 private final static int CANT_ESTADOS = 16;
	 private final static int CANT_CARACTERES = 26; 
	 private final static String FILE_PATH_ESTADOS = "src/matrizTransicionEstados.txt";
	 private final static List<Accion_Semantica> matriz_acciones_semanticas[][] = new ArrayList[CANT_ESTADOS][CANT_CARACTERES];
	 private final static int[][] matriz_transicion_estados = FileAux.cargarEstadosDeArchivo(FILE_PATH_ESTADOS, CANT_ESTADOS, CANT_CARACTERES);
	 public static StringBuilder token_actual = new StringBuilder();
	 public static int estado_actual = 0;
	 private static int linea_actual = 1;
	 public static List<Token> listaTokens = new ArrayList<Token>();
	 private static List<ErrorCompilacion> errores = new ArrayList<ErrorCompilacion>();

	 public AnalizadorLexico(List<String> acciones_semanticas){
	        crearTablas(acciones_semanticas);
	        //imprimirMatriz(matriz_acciones_semanticas);
	 }
	 
	 public void crearTablas(List<String> acciones_semanticas){
	        int f = 0;
	        int c = 0;
	        while (!acciones_semanticas.isEmpty()) {
	            if (c > CANT_CARACTERES-1) {
	                f = f+1;
	                c = 0;
	            }
	            String line = acciones_semanticas.remove(0);
	            List<Accion_Semantica> as = new ArrayList<Accion_Semantica>();
	            if (line.equals("AS0")) {
	                as.add(new AS0());
	            }
	            if (line.equals("AS1")) {
	                as.add(new AS1());
	            }
	            if (line.equals("AS2")) {
	                as.add(new AS2());
	            }
	            if (line.equals("AS3")) {
	                as.add(new AS3());
	            }
	            if (line.equals("AS4")) {
	                as.add(new AS4());
	            }
	            if (line.equals("AS5")) {
	                as.add(new AS5());
	            }
	            if (line.equals("AS6")) {
	                as.add(new AS6());
	            }
	            if (line.equals("AS7")) {
	                as.add(new AS7());
	            }
	            if (line.equals("AS8")) {
	                as.add(new AS8());
	            }
	            if (line.equals("AS9")) {
	                as.add(new AS9());
	            }
	            if (line.equals("ASE")) {
	                as.add(new ASE());
	            }
	            if (line.equals("ASa")) {
	                as.add(new ASa());
	            }
	            if (line.equals("AS3/ASb")) {
	                as.add(new AS3());
	                as.add(new ASb());
	            }
	            if (line.equals("AS5/ASb")) {
	                as.add(new AS5());
	                as.add(new ASb());
	            }
	            if (line.equals("AS6/ASb")) {
	                as.add(new AS6());
	                as.add(new ASb());
	            }
	            if (line.equals("AS8/ASb")) {
	                as.add(new AS8());
	                as.add(new ASb());
	            }
	            matriz_acciones_semanticas[f][c] = as;
	            c++;
	        }
	 }
	 
	 public List<String> getErrores() {
		 List<String> ls = new ArrayList<>();
	     for (int i = 0; i < errores.size(); i++) {
	    	 ls.add(errores.get(i).toString());
	     }
	     return ls;
	 }
	 
	 public List<Token> getTokens(){
	     return listaTokens;
	 }
	    
     public static int getLineaActual() {
		 return linea_actual;
	 }

	 public static void setLineaActual(int linea) {
		 linea_actual = linea;
	 }
	 
	 public List<Token> leerCodigo(List<Character> buffer){
	     List<Token> tok = new ArrayList<>();
	     Token t;
	         while (!buffer.isEmpty()) {
	             Character c = buffer.get(0);
	             t = cambiarEstado(c,buffer);
	             if (t != null) {
	                 tok.add(t);
	             }
	         }
	     System.out.println(errores.toString());
	     return tok;
	 }

	 private static char detectarTipo(char caracter) {
		 if (Character.isDigit(caracter))
			 return DIGITO;
	     else if (Character.isLowerCase(caracter))
	         return MINUSCULA;
	     else if (caracter != 'F' && Character.isUpperCase(caracter))
	         return MAYUSCULA;
	     else
	         return caracter;
	 }

	 public static Token cambiarEstado(char caracter, List<Character> buffer) {
		 Token t = null;
		 int caracter_actual;
		 switch (detectarTipo(caracter)) {
	     	case BLANCO:
	     		caracter_actual = 0;
	            break;
	     	case TAB:
	            caracter_actual = 1;
	            break;
	        case NL:
	            caracter_actual = 2;
	            break;
	        case MINUSCULA:
	            caracter_actual = 3;
	            break;
	        case MAYUSCULA:
	            caracter_actual = 4;
	            break;
	        case '_':
	            caracter_actual = 5;
	            break;
	        case DIGITO:
	            caracter_actual = 6;
	            break;
	        case '.':
	            caracter_actual = 7;
	            break;
	        case 'F':
	            caracter_actual = 8;
	            break;
	        case '+':
	            caracter_actual = 9;
	            break;
	        case '-':
	            caracter_actual = 10;
	            break;
	        case '*':
	            caracter_actual = 11;
	            break;
	        case '/':
	            caracter_actual = 12;
	            break;
	         case '(':
	            caracter_actual = 13;
	            break;
	         case ')':
	            caracter_actual = 14;
	            break;
	         case '{':
	            caracter_actual = 15;
	            break;
	         case '}':
	            caracter_actual = 16;
	            break;
	         case ';':
	            caracter_actual = 17;
	            break;
	         case ',':
	            caracter_actual = 18;
	            break;
	         case ':':
	            caracter_actual = 19;
	            break;
	         case '=':
	            caracter_actual = 20;
	            break;
	         case '>':
	            caracter_actual = 21;
	            break;
	         case '<':
	            caracter_actual = 22;
	            break;
	         case COMILLA_SIMPLE:
	            caracter_actual = 23;
	            break;
			 case '!':
	            caracter_actual = 24;
	            break;
	         default:
	            caracter_actual = 25;
	            break;
	        }
		    List<Accion_Semantica> accSemanticas = matriz_acciones_semanticas[estado_actual][caracter_actual];
		    if (accSemanticas.size() == 2) {
	            t = accSemanticas.get(0).ejecutar(caracter, buffer, token_actual);
	            accSemanticas.get(1).ejecutar(caracter, buffer, token_actual);
	        } 
		    else {
	            t = accSemanticas.get(0).ejecutar(caracter, buffer, token_actual);
	        }
	        if (t != null) {
	            estado_actual = 0;
	            token_actual = new StringBuilder();
	        } 
	        else {
	            estado_actual = matriz_transicion_estados[estado_actual][caracter_actual];
	        }
	        if (estado_actual == -2) {
	            estado_actual = 0;
	        }
	        if (estado_actual == -1) { // error?
	            token_actual = new StringBuilder();
	            estado_actual = 0;
	        }
	        return t;
}

	    public static void agregarError(String tipo, String mensaje) {
	        int linea = AnalizadorLexico.getLineaActual();
	        ErrorCompilacion e = new ErrorCompilacion(tipo,mensaje,linea);
	        errores.add(e);
	    }
	    
	    public static Token getToken(List<Character> buffer) {
	        estado_actual = 0;
	        boolean tieneToken = false;
	        Token t = new Token(0,null);
	        while (!tieneToken) {
	            if (!buffer.isEmpty()) {
	                Character c = buffer.get(0);
	                t = cambiarEstado(c,buffer);
	                if (t != null) {
	                    tieneToken = true;
	                    listaTokens.add(t);
	                }
	            } 
	            else {
	                tieneToken = true;
	            }
	        }
	        return t;
	    }
	    
	    public void imprimirMatriz(List<Accion_Semantica>[][] matrizAccionesSemanticas) {
	    	int i = 0; int j = 0;
	    	while(i < 16) {
	    		while(j < 26) {
	    			System.out.print(matrizAccionesSemanticas [i][j] + ", ");
	    			j++;
	    		}
	    		i++;
	    		j = 0;
	    		System.out.println();
	    	}
	    }
}
