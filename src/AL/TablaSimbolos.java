package AL;
import java.util.HashMap;
import java.util.Map;

import PRINCIPAL.Parser;


public class TablaSimbolos {
    private static Map<String,Atributo> tabla = new HashMap<>();
    
    public TablaSimbolos() {
    }

    public static void agregarSimbolo(String token, Lexema lexema) {
        Atributo atributo = new Atributo(lexema);
        tabla.put(token,atributo);
    }
    
    public static void agregarSimb(String token) {
        Atributo atributo = new Atributo();
        tabla.put(token,atributo);
    }

    public static String obtenerSimbolo(String simbolo){
        if (tabla.containsKey(simbolo)) {
            return simbolo;
        }
        return null;
    }
    
    public static void agregarAtributo(String s, String atributo, String valor) {
        if(tabla.containsKey(s)) {
        	var a = tabla.get(s);
        	a.aggAtributo(atributo, valor);
        	tabla.put(s, a);
    	}
        //sino esta, se debe agregar el simbolo y crear el hash con los atributos dados
    }
    
    public static void truncarEntero(String ptr, String constante) {
        Lexema lexema = new Lexema(Integer.valueOf(constante));
        var a = tabla.get(ptr);
        a.setLexema(lexema);
        tabla.put(ptr,a);
    }
    
    public static Double getDouble(String numero){
        if (numero.contains("F")){
            var w = numero.split("F");
            return Math.pow(Double.valueOf(w[0]),Double.valueOf(w[1]));
        } else {
            return Double.valueOf(numero);
        }
    }
    
    public static void negarConstante(String s, String constante){
        if (tabla.containsKey(s)) {
            var a = tabla.get(s);
            Lexema nuevo = new Lexema(getDouble(constante));
            a.setLexema(nuevo);
            tabla.put(s,a);
        }
    }
    
    public static Atributo getAtr(String s) {
        if (tabla.containsKey(s)) {
            return tabla.get(s);
        }
        return null;
    }
    
    public static boolean isInt(String s){
        if (tabla.containsKey(s)) {
            return (tabla.get(s).lexema.atributoInt != null);
        }
        return false;
    }
    
    public static void imprimirTabla() {
        System.out.println("\nTablaSimbolos:");
        for (Map.Entry<String, Atributo> entrada: tabla.entrySet()) {
            System.out.print(entrada.getKey() + ": ");
            System.out.print(entrada.getValue().toString());
            System.out.println();
        }
    }

	public static void eliminarSimbolo(String c) {
		tabla.remove(c);
	}

	public static String obtenerAtributo(String simb, String atributo) {
		Atributo aux = getAtr(simb);
        for (Map.Entry<String, String> entrada: aux.getMas().entrySet()) {
        	if(entrada.getKey().equals(atributo))
        		return entrada.getValue();        }
		return null;
	}
	
	public static String obtenerSimboloAmbito(String simbolo) {
		return simbolo;
        /*//Dado un lexema, retorna su puntero a la tabla de simbolos
        String simb = TablaSimbolos.obtenerSimbolo(simbolo);

        if (simb != null) {
            return simb;
        } else if (simbolo.contains(Parser.NAME_MANGLING_CHAR)) {
            int index = simbolo.lastIndexOf(Parser.NAME_MANGLING_CHAR);
            simb = simb.substring(0, index);
            return obtenerSimboloAmbito(simb);
        } else {
            return null;
        }*/
    }

	public static void modifySimbolo(String ptr, String string) {
		Atributo a = tabla.get(ptr);
		eliminarSimbolo(ptr);
		tabla.put(string, a);
	}
	
}
