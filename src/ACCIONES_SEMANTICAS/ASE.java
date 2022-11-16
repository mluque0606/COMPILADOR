package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.AnalizadorLexico;
import AL.Token;

public class ASE implements Accion_Semantica {

    public ASE(){

    }

    @Override
    public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        System.out.println(token.toString());
        Character ch = buffer.remove(0);
        token = new StringBuilder();
        AnalizadorLexico.agregarError("lexico","Se produjo un error de semantica.");
        if (ch == AnalizadorLexico.NL) {
            AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
        }
        return null;
    }

    @Override
    public String toString() {
        return "ASE";
    }
}
