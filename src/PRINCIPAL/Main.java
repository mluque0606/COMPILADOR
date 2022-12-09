package PRINCIPAL;
public class Main {

	public static void main(String[] args) {
		
		Compilador c = new Compilador();
		c.ejecutarCompilador("src/Testeos/Programa_1.txt", "src/Resultado.asm");
		
		/*
		if(args.length > 1) {
			Compilador c = new Compilador();
			c.ejecutarCompilador(args[0], args[1]);
		}
		else {
			System.out.println("No se encontro el archivo a compilar");
		}*/
	}
}