package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.AnalizadorLexico;
import AL.Token;

public class AS4 implements Accion_Semantica {

	public AS4() {
	}

	@Override
	public Token ejecutar(Character c, List<Character> buffer, StringBuilder token) {
        Character ch = buffer.remove(0);
        if (ch == AnalizadorLexico.NL) {   //si es un salto de linea, seteo la linea actual de lectura
            AnalizadorLexico.setLineaActual(AnalizadorLexico.getLineaActual() + 1);
        }
        int ultima_barra = token.lastIndexOf("/");
        token.delete(ultima_barra, token.length());
		return null;
	}
	
	public String toString() {
		return "AS4";
	}
}
