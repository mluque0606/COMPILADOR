package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.Token;


public class AS2 implements Accion_Semantica {

    public AS2(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        if(ch.equals('(')){
            return new Token(40,null);
        }
        if(ch.equals(')')){
            return new Token(41,null);
        }
        if(ch.equals('*')){
            return new Token(42,null);
        }
        if(ch.equals('+')){
            return new Token(43,null);
        }
        if(ch.equals(',')){
            return new Token(44,null);
        }
        if(ch.equals('-')){
            return new Token(45,null);
        }
        if(ch.equals('/')){
            return new Token(47,null);
        }
        if(ch.equals(':')){
            return new Token(58,null);
        }
        if(ch.equals(';')){
            return new Token(59,null);
        }
        if(ch.equals('{')){
            return new Token(123,null);
        }
        if(ch.equals('}')){
            return new Token(125,null);
        }
        return null;
    }

    @Override
    public String toString() {
        return "AS2";
    }
}
