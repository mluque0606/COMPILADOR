package AL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ACCIONES_SEMANTICAS.AS0;
import ACCIONES_SEMANTICAS.AS1;
import ACCIONES_SEMANTICAS.AS2;
import ACCIONES_SEMANTICAS.AS3;
import ACCIONES_SEMANTICAS.AS4;
import ACCIONES_SEMANTICAS.AS5;
import ACCIONES_SEMANTICAS.AS6;
import ACCIONES_SEMANTICAS.AS7;
import ACCIONES_SEMANTICAS.AS8;
import ACCIONES_SEMANTICAS.AS9;
import ACCIONES_SEMANTICAS.ASE;
import ACCIONES_SEMANTICAS.ASa;
import ACCIONES_SEMANTICAS.Accion_Semantica;

public class FileAux {
	
//METODO QUE CARGA UNA MATRIZ DE ACCIONES SEMANTICAS CON LOS DATOS DEL ARCHIVO DADO
    public static Accion_Semantica[][] cargarASdeArchivo(String direc, int filas, int columnas) {
        Accion_Semantica[][] action_matriz = new Accion_Semantica[filas][columnas]; //inicializo matriz para las AS
        try {
            File archivo = new File(direc);
            Scanner scanner = new Scanner(archivo);
            for (int i = 0; i < filas; ++i) {  //para toda la matriz agrego los valores que leo en el archivo
                for (int j = 0; j < columnas; ++j) {
                    action_matriz[i][j] = crearAccion(scanner.nextLine());   //crea una accion semantica
                }
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {     //excepcion en caso de que no se pueda leer el archivo
            System.out.println("No fue posible leer el archivo: " + direc); 
            excepcion.printStackTrace();
        }
        return action_matriz;
    }

//METODO QUE CARGA UNA MATRIZ DE ENTEROS(transicion de estados) CON LOS DATOS DEL ARCHIVO DADO POR PARAMETRO
    public static int[][] cargarEstadosDeArchivo(String direc, int filas, int columnas) {
        int[][] matriz = new int[filas][columnas];       //inicializo una matriz para la transicion de estados
        try {
            File archivo = new File(direc);
            Scanner scanner = new Scanner(archivo);
            for (int i = 0; i < filas; ++i) {            //para toda la matriz voy agregando los valores leido del archivo
                for (int j = 0; j < columnas; ++j) {
                    matriz[i][j] = Integer.parseInt(scanner.nextLine());
                }
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {      //excepcion en caso de que no se pueda leer el archivo
            System.out.println("No fue posible leer el archivo: " + direc); 
            excepcion.printStackTrace();
        }
        return matriz;
    }

//METODO QUE CARGA UN MAP DE ID Y PR CON LOS DATOS DEL ARCHIVO DADO
    public static Map<String, Integer> cargarMapArchivo(String direc) {
        Map<String, Integer> map = new HashMap<>();
        try {
            File archivo = new File(direc);
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNext()) {     //Leo todo el arhcivo y voy cargando el mapa
                String pr = scanner.next();
                int id = scanner.nextInt();
                map.put(pr, id);
            }
            scanner.close();
        } catch (FileNotFoundException excepcion) {   //excepcion en caso de que no se pueda leer el archivo
            System.out.println("No fue posible leer el archivo: " + direc);
            excepcion.printStackTrace();
        }
        return map;
    }  

//METODO QUE DEVUELVE EL PROXIMO CARACTER
    public static char getProximoChar(Reader reader) throws IOException {
        reader.mark(1);
        char prox_caracter = (char) reader.read();
        reader.reset();
        return prox_caracter;
    }
    
//METODO QUE DICE SI SE LLEGO AL FINAL DEL ARCHIVO
    public static boolean endOfFile(Reader reader) throws IOException {
        reader.mark(1);
        int value = reader.read();
        reader.reset();
        return value < 0;
    }

//METODO QUE CREA LA ACCION SEMANTICA CORRESPONDIENTE DE ACUERDO AL NOMBRE DADO POR PARAMETRO
    private static Accion_Semantica crearAccion(String nombreAS) {
        switch (nombreAS) {
            case "AS0":
                return new AS0();
            case "AS1":
                return new AS1();
            case "AS2":
                return new AS2();
            case "AS3":
                return new AS3();
            case "AS4":
                return new AS4();
            case "AS5":
                return new AS5();
            case "AS6":
                return new AS6();
            case "AS7":
                return new AS7();
            case "AS8":
                return new AS8();
            case "AS9":
                return new AS9();
            case "ASa":
                return new ASa();
            case "ASE":
                return new ASE();
            default:
                return null;
        }
    }

	public static void wtiteProgram(String a, String s) {
		File file = new File(a);
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(a);
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
