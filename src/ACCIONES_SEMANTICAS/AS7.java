package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.Token;

public class AS7 implements Accion_Semantica {

    public AS7(){

    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        token.append(c);
        String simbolo = token.toString();
        if(simbolo.equals("=:")){
            return new Token(272,null);
        }
        if(simbolo.equals("=!")){
            return new Token(273,null);
        }
        if(simbolo.equals(">=")){
            return new Token(270,null);
        }
        if(simbolo.equals("<=")){
            return new Token(270,null);
        }
        return null;
    }

    @Override
    public String toString() {
        return "AS7";
    }

}
