package AL;

public class Token {
	int identificador;
	String atributo;
	
	public Token(int identificador, String atributo) {
		this.atributo = atributo;
		this.identificador = identificador;
	}
	
	public String getAtributo() {
		return atributo;
	}

	public int getIdentificador() {
		return identificador;
	}
	
    @Override
    public String toString() {
        if (atributo != null)
            return "T="+identificador+"/"+atributo;
        else
            return "T="+identificador;
    }
}
