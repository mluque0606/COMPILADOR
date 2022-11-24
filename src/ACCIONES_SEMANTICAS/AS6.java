package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.AnalizadorLexico;
import AL.Lexema;
import AL.TablaSimbolos;
import AL.Token;


public class AS6 implements Accion_Semantica {

    public AS6(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        String simbolo = token.toString();
        Character ch = buffer.remove(0);
        Double numero = getDouble(simbolo);
        
        if(!dentroRango(numero)){
            token = new StringBuilder();
            AnalizadorLexico.agregarError("lexico","Se produjo un error de rango de " + simbolo + ", esta fuera de rango");
            return null;
        }
        if (TablaSimbolos.obtenerSimbolo(simbolo) != null){
            return new Token(258,TablaSimbolos.obtenerSimbolo(simbolo));
        } 
        else {
            Lexema lexema = new Lexema(numero);
            TablaSimbolos.agregarSimbolo(simbolo,lexema);
            TablaSimbolos.agregarAtributo(simbolo, "tipo", "Float");
            return new Token(258,TablaSimbolos.obtenerSimbolo(simbolo));
        }
    }

    public Double getDouble(String simbolo){
        if (simbolo.contains("F")){
            var w = simbolo.split("F");
            return Math.pow(Double.valueOf(w[0]),Double.valueOf(w[1]));
        } 
        else
            return Double.valueOf(simbolo);
    }

    public boolean dentroRango(Double numero){
        if((numero == 0.0) || (numero > Math.pow(1.17549435,-38) && numero < Math.pow(3.40282347,38)) ||
                (numero > Math.pow(-3.40282347,38) && numero < Math.pow(-1.17549435,-38))) {
            return true;
        } 
        else 
            return false;
    }

    @Override
    public String toString() {
        return "AS6";
    }
}
