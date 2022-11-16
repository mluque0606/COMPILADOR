package AL;

public class Lexema {
	Integer atributoInt;
    Double atributoDou;
    String atributoStr;

    public Lexema(int atributoInt) {
        this.atributoInt = atributoInt;
        this.atributoDou = null;
        this.atributoStr = null;
    }

    public Lexema(double atributoDou) {
        this.atributoDou = atributoDou;
        this.atributoStr = null;
        this.atributoInt = null;
    }

    public Lexema(String atributoStr) {
        this.atributoStr = atributoStr;
        this.atributoDou = null;
        this.atributoInt = null;
    }

    public String getTipo() {
        if(atributoStr != null)
            return "string";
        if(atributoDou != null)
            return "double";
        if(atributoInt != null)
            return "entero";
        return null;
    }

    @Override
    public String toString() {
        if(atributoStr != null)
            return atributoStr;
        if(atributoDou != null)
            return atributoDou.toString();
        if(atributoInt != null)
            return atributoInt.toString();
        return null;
    }
}
