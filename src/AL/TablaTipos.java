package AL;
import PRINCIPAL.GeneradorCodigo;
import PRINCIPAL.Parser;

public class TablaTipos {
    public static final int LONG = 0;
    public static final int FLOAT = 1;
    public static final int FUNC = 2;
    
    public static final String FLOAT_TYPE = "Float";
    public static final String LONG_TYPE = "Entero";
    public static final String FUNC_TYPE = "Funcion";
    public static final String ERROR_TYPE = "Error";
    
    private static final String[][] tiposSumaResta = { { LONG_TYPE, FLOAT_TYPE, ERROR_TYPE },
                                                       { FLOAT_TYPE, FLOAT_TYPE, ERROR_TYPE },
                                                       { ERROR_TYPE, ERROR_TYPE, ERROR_TYPE} };
    private static final String[][] tiposMultDiv = { { LONG_TYPE, FLOAT_TYPE, ERROR_TYPE },
                                                     { FLOAT_TYPE, FLOAT_TYPE, ERROR_TYPE },
                                                     { ERROR_TYPE, ERROR_TYPE, ERROR_TYPE} };
    private static final String[][] tiposComparadores = { { LONG_TYPE, FLOAT_TYPE, ERROR_TYPE }, 
                                                          { FLOAT_TYPE, FLOAT_TYPE, ERROR_TYPE },
                                                          { ERROR_TYPE, ERROR_TYPE, ERROR_TYPE } };
	private static final String[][] tiposAsig = { { LONG_TYPE, ERROR_TYPE, ERROR_TYPE }, 
	                                              { FLOAT_TYPE, FLOAT_TYPE, ERROR_TYPE },
                                                  { ERROR_TYPE, ERROR_TYPE, FUNC_TYPE } };	
	
	private static String tipoResultante(String op1, String op2, String operador) {
        int fil = getNumeroTipo(op1);
        int col = getNumeroTipo(op2);

        switch (operador) {
            case ("+"):
            case ("-"):
                return tiposSumaResta[fil][col];
            case ("*"):
            case ("/"):
                return tiposMultDiv[fil][col];
            case ("=:"):
                return tiposAsig[fil][col];
            case ("<="):
            case ("<"):
            case (">="):
            case (">"):
            case ("!="):
            case ("="):
                return tiposComparadores[fil][col];
            default:
                return ERROR_TYPE;
        }
    }
	
    private static int getNumeroTipo(String tipo) {
        if (tipo == LONG_TYPE) 
        	return LONG;
        else 
        	if (tipo == FLOAT_TYPE) 
        		return FLOAT;
        else 
        	return FUNC;
    }
    
    public static String getTipo(String op) {
        String puntOp = TablaSimbolos.obtenerSimboloAmbito(op);

        String tipo = TablaSimbolos.obtenerAtributo(puntOp, "tipo");

        if (tipo == FUNC_TYPE) {
            String uso = TablaSimbolos.obtenerAtributo(puntOp, "uso");
            if(uso == FUNC_TYPE)
                return uso;
        }
        return tipo; 
    }
    
    
    public static String getTipoAbarcativo(String op1, String op2, String operador){
        String tipoOp1 = getTipo(op1);
        String tipoOp2 = getTipo(op2);

        String tipoFinal = tipoResultante(tipoOp1, tipoOp2, operador);

        if (tipoFinal.equals(ERROR_TYPE)) {
            Parser.agregarErrorSemantico( GeneradorCodigo.posicionActualPolaca, "No se puede realizar la operacion " + operador + " entre los tipos " + tipoOp1 + " y " + tipoOp2);
        }

        return tipoFinal;
    }
}
