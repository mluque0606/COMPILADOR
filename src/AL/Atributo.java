package AL;

import java.util.HashMap;
import java.util.Map;

public class Atributo {
	Lexema lexema;
    Map<String,String> mas = new HashMap<>();

    public Atributo(Lexema lexema) {
        this.lexema = lexema;
    }

    public Atributo() {
	}

	public void setLexema(Lexema lexema){
        this.lexema = lexema;
    }

    public String getTipo(){
        return lexema.getTipo();
    }

    public void aggAtributo(String key, String value){
        mas.put(key,value);
    }
    
    public HashMap<String, String> getMas(){
    	return (HashMap<String, String>) mas;
    }
    
    @Override
    public String toString() {
        return "Atributo{" +
                "lexema=" + lexema + mas +
                '}';
    }
}
