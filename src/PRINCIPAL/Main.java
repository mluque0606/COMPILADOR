package PRINCIPAL;
public class Main {

	public static void main(String[] args) {
		
		Compilador c = new Compilador();
		c.ejecutarCompilador("src/Testeos/Programa_8.txt");
		
		
		/*if(args.length != 0) {
			Compilador c = new Compilador();
			c.ejecutarCompilador("src/program_1.txt");
		}
		else {
			System.out.println("No se encontro el archivo a compilar");
		}*/
	}
}