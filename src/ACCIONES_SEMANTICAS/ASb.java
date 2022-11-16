package ACCIONES_SEMANTICAS;

import java.util.List;

import AL.Token;

public class ASb implements Accion_Semantica {

    public ASb(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        buffer.add(0,c);
        return null;
    }

    @Override
    public String toString() {
        return "ASb";
    }
}
