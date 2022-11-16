package AL;
import java.util.Map;

public class TablaPR {
	    private final static String FILE_PR = "src/tablaPR.txt";
	    private final static Map<String, Integer> palabras_reservadas = FileAux.cargarMapArchivo(FILE_PR);

		public static Integer getIdentificador(String simbolo) {
            return palabras_reservadas.getOrDefault(simbolo, null);
		}
}
