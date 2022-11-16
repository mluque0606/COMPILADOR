package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.AnalizadorLexico;
import AL.Lexema;
import AL.TablaPR;
import AL.TablaSimbolos;
import AL.Token;


public class AS3 implements Accion_Semantica {
	
    public AS3(){
    }

	@Override
	public Token ejecutar(Character ch, List<Character> buffer, StringBuilder token) {
		String simbolo = token.toString();
	    Character c = buffer.remove(0);
	    Integer idPalabraReservada = TablaPR.getIdentificador(simbolo);
	        
	    if (idPalabraReservada != null) 
	    	return new Token(idPalabraReservada,null); 
	        
	    else {
	    	if (simbolo.length() > AnalizadorLexico.LONG_ID) {
	    		AnalizadorLexico.agregarError("lexico","Se produjo un warning debido a que el identificador " + simbolo + ", contiene mas de " +AnalizadorLexico.LONG_ID+ " caracteres.");
	            String nuevoToken = token.substring(0, AnalizadorLexico.LONG_ID - 1);
	            token = new StringBuilder();
	            token.append(nuevoToken);
	            simbolo = token.toString();
	        }
	            
	        if (TablaSimbolos.obtenerSimbolo(simbolo) != null) { //Si se encuentra en la TS
	           return new Token(257,TablaSimbolos.obtenerSimbolo(simbolo));
	        } 
	            
	        else {            //Si no esta en la TS
	           Lexema lexema = new Lexema(simbolo);
	           TablaSimbolos.agregarSimbolo(simbolo,lexema);
	           return new Token(257,TablaSimbolos.obtenerSimbolo(simbolo));
	        }
	    }
	}

	@Override
	public String toString() {
	    return "AS3";
	}

}
