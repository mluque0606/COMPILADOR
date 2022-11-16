package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.Lexema;
import AL.TablaSimbolos;
import AL.Token;


public class ASa implements Accion_Semantica {

    public ASa(){

    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        token.deleteCharAt(0);
        String simbolo = token.toString();
        if(TablaSimbolos.obtenerSimbolo(simbolo) != null){
            return new Token(259,TablaSimbolos.obtenerSimbolo(simbolo));
        } else{
            Lexema lexema = new Lexema(simbolo);
            TablaSimbolos.agregarSimbolo(simbolo,lexema);
            return new Token(259,TablaSimbolos.obtenerSimbolo(simbolo));
        }
    }

    @Override
    public String toString() {
        return "ASa";
    }
}
