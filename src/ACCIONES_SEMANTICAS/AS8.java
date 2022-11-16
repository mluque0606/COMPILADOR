package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.Token;

public class AS8 implements Accion_Semantica {

    public AS8(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        String simbolo = token.toString();
        if(simbolo.equals("=")){
            return new Token(61,null);
        }
        if(simbolo.equals(">")){
            return new Token(62,null);
        }
        if(simbolo.equals("<")){
            return new Token(60,null);
        }
        return null;
    }

    @Override
    public String toString() {
        return "AS8";
    }

}
