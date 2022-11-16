package ACCIONES_SEMANTICAS;
import java.util.List;

import AL.Token;

public class AS9 implements Accion_Semantica {
    
	public AS9(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        token = new StringBuilder();
        return null;
    }

    @Override
    public String toString() {
        return "AS9";
    }
}
