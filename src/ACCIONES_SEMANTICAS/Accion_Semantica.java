package ACCIONES_SEMANTICAS;
import java.util.List;
import AL.Token;

public interface Accion_Semantica {

	Token ejecutar(Character c, List<Character> buffer, StringBuilder token);
    String toString();
}
