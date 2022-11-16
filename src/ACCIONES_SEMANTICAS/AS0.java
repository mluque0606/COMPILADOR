package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.AnalizadorLexico;
import AL.Token;

public class AS0 implements Accion_Semantica{

    public AS0(){
    }
    
    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        if (ch == '\n') {
            AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual()+1);
        }
        return null;
    }

    @Override
    public String toString() {
        return "AS0";
    }
}
