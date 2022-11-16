package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.Token;

public class AS1 implements Accion_Semantica {
    
	public AS1(){
    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        token.append(ch);
        return null;
    }

    @Override
    public String toString() {
        return "AS1";
    }
}
