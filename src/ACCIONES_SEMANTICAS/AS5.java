package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.AnalizadorLexico;
import AL.Lexema;
import AL.TablaSimbolos;
import AL.Token;



public class AS5 implements Accion_Semantica {

    public AS5(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        String simbolo = token.toString();
        Character ch = buffer.remove(0);
        if(Long.parseLong(simbolo) > AnalizadorLexico.MAX_VALOR_LONG){
            token = new StringBuilder();
            AnalizadorLexico.agregarError("lexico","Se produjo un error de rango de " + simbolo + ", es mayor a " + AnalizadorLexico.MAX_VALOR_LONG);
            return null;
        }
        if(Long.parseLong(simbolo) < AnalizadorLexico.MIN_VALOR_LONG){
            token = new StringBuilder();
            AnalizadorLexico.agregarError("lexico","Se produjo un error de rango de " + simbolo + ", es menor a " + AnalizadorLexico.MIN_VALOR_LONG);
            return null;
        }
        if (TablaSimbolos.obtenerSimbolo(simbolo) != null){
            return new Token(258,TablaSimbolos.obtenerSimbolo(simbolo));
        } else {
            Lexema lexema = new Lexema(Integer.parseInt(simbolo));
            TablaSimbolos.agregarSimbolo(simbolo,lexema);
            TablaSimbolos.agregarAtributo(simbolo, "tipo", "Entero");
            return new Token(258,TablaSimbolos.obtenerSimbolo(simbolo));
        }
    }

    @Override
    public String toString() {
        return "AS5";
    }

}
