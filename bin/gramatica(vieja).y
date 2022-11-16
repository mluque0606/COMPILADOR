%{
import ACCIONES_SEMANTICAS.*;
import AL.AnalizadorLexico;
import AL.Lexema;
import AL.Token;
import AL.TablaSimbolos;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
%}

//Declaracion de tokens a recibir del Analizador Lexico
%token ID CTE CADENA IF THEN ELSE ENDIF OUT FUN RETURN BREAK CONTINUE WHILE MAYOR_IGUAL MENOR_IGUAL ASIGNACION DISTINTO I32 F32 TOF32 DISCARD

%left '+' '-'
%left '*' '/'

%start program

%% //Declaracion del programa principal

program: header_program '{' ejecucion '}' ';' {addEstructura("programa");}
        | header_program {addEstructura("programa sin ejecucion");}
        
        | header_program '{' ejecucion {agregarError(errores_sintacticos,"Error","Se esperaba un '}' al final del programa");}
        | header_program '{' '}' {agregarError(errores_sintacticos,"Error","Se esperaba una sentencia de ejecucion");}
;

header_program: ID {cambiarAmbito($1.val);
					agregarToken (":START");
					TablaSimbolos.agregarSimbolo(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
					}
;

//Reglas de declaraciones y bloques de sentencias

declaracion_variables: tipo lista_variables ';'

        | lista_variables ';' {agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
        | lista_variables {agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
;

//QUE PASA SI EL STRING SIMB DEVUELVE NULL PORQUE NO ESTA??
lista_variables: lista_variables ',' ID { 
                String simb = TablaSimbolos.obtenerSimbolo($3.sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable"); }
        | ID { 
                String simb = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable");
                }
;

sentencia_declarable: declaracion_variables {addEstructura("declaracion variables");}
        | funcion {addEstructura("declaracion funcion");}
;

funcion: header_funcion ejecucion_funcion {agregarToken(nombreFuncion());
										   salirAmbito();
										   agregarToken("\\ENDP"); }

        | header_funcion {agregarError(errores_sintacticos,"Error","Se esperan sentencias ejecutables para la funcion");}
;

//Puede tener o no parametros
header_funcion: FUN ID '(' lista_parametros ')' ':' tipo {
						String simb = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb, "tipo", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "uso", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "retorno", tipo);

                        TablaSimbolos.agregarSimbolo("@ret@" + $2.sval + Parser.ambito.toString());
                        String simb2 = TablaSimbolos.obtenerSimbolo("@ret@" + $3.sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb2, "tipo", tipo);
                        TablaSimbolos.agregarAtributo(simb2, "uso", "variable");
						
						cambiarAmbito($2.sval);
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
						
        | FUN ID '(' ')' ':' tipo {
						String simb = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb, "tipo", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "uso", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "retorno", tipo);

                        TablaSimbolos.agregarSimbolo("@ret@" + $2.sval + Parser.ambito.toString());
                        String simb2 = TablaSimbolos.obtenerSimbolo("@ret@" + $3.sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb2, "tipo", tipo);
                        TablaSimbolos.agregarAtributo(simb2, "uso", "variable");
						
						cambiarAmbito($2.sval);
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
      
      	//| FUN ID '(' lista_parametros ')' ':'  {agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
      	| FUN ID '(' lista_parametros ')'  {agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
        | FUN '(' lista_parametros ')' ':' tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
        | FUN '(' ')' ':' tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
        | FUN ID  lista_parametros ':' tipo {agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
        | FUN ID ':' tipo {agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
;

//Maximo 2 parametros
lista_parametros: parametro
        | parametro ',' parametro
;

parametro: tipo ID {    
						String simb = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());
                        int primerSeparador = Parser.ambito.toString().indexOf(NAME_MANGLING_CHAR);
                        int ultimoSeparador = Parser.ambito.toString().lastIndexOf(NAME_MANGLING_CHAR);
                        String nombre_funcion = Parser.ambito.substring(ultimoSeparador + 1) + Parser.ambito.substring(primerSeparador, ultimoSeparador);
                        String simbFunc = TablaSimbolos.obtenerSimbolo(nombre_funcion);
                        
                        TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                        TablaSimbolos.agregarAtributo(simb, "uso", "parametro");
                        TablaSimbolos.agregarAtributo(simbFunc, "tipo_parametro", tipo);
                   }

        | ID {agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
        | tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
;

tipo: I32 {tipo = TablaTipos.LONG_TYPE;}
    | F32 {tipo = TablaTipos.FLOAT_TYPE;}
    //| FUNC {tipo = TablaTipos.FUNC_TYPE;}
;

//Puede ser una funcion que solo retorne algo o, con sentencias ejecutables
//HACER METODO OBTENERATRIBUTO()?????????
ejecucion_funcion: '{' bloque_funcion RETURN '(' expresion ')' ';' '}' ';' {
                                                                        String simbFunc = TablaSimbolos.obtenerSimbolo(nombreFuncion());

                                                                        if (TablaSimbolos.obtenerAtributo(punt_funcion, "retorno").equals(TablaTipos.FUNC_TYPE)) {
                                                                                TablaSimbolos.agregarAtributo(punt_funcion, "nombre_retorno", funcion_a_asignar);
                                                                                funcion_a_asignar = "";  
                                                                        }
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
       
        | '{' RETURN '(' expresion ')' ';' '}' ';' {
        											String simbFunc = TablaSimbolos.obtenerSimbolo(nombreFuncion());
        																
                                                    if (TablaSimbolos.obtenerAtributo(punt_funcion, "retorno").equals(TablaTipos.FUNC_TYPE)) {
                                                    	TablaSimbolos.agregarAtributo(punt_funcion, "nombre_retorno", funcion_a_asignar);
                                                        funcion_a_asignar = "";  
                                                    }
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
        
        | '{' bloque_funcion RETURN '(' expresion ')' ';' bloque_funcion '}' ';' {agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
;

//Una funcion puede tener una sentencia o muchas
bloque_funcion: bloque_funcion sentencia_funcion
        | sentencia_funcion
;

//Los diferentes tipos de sentencias que puede haber en el bloque de una funcion
sentencia_funcion: sentencia
        | seleccion_funcion ';' {addEstructura("if en funcion");}
        | iteracion_while_funcion {addEstructura("while en funcion");}
;

seleccion_funcion: IF condicion_salto_if then_seleccion_funcion_sin_else ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}
									
    | IF condicion_salto_if then_seleccion_funcion else_seleccion_funcion ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}
									
    | IF condicion_salto_if then_seleccion else_seleccion_funcion ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}
									
    | IF condicion_salto_if then_seleccion_funcion else_seleccion ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}

	| IF condicion_salto_if then_seleccion_funcion_sin_else {agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
	| IF condicion_salto_if then_seleccion_funcion else_seleccion_funcion{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
	| IF condicion_salto_if then_seleccion else_seleccion_funcion{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
	| IF condicion_salto_if then_seleccion_funcion else_seleccion {agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
;

then_seleccion_funcion: THEN '{' ejecucion_control RETURN '(' expresion ')' ';' '}' ';' {
								polaca.add(pila.pop(), polaca.size()+2);
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
								
    | THEN RETURN '(' expresion ')' ';' {
								polaca.add(pila.pop(), polaca.size()+2);
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
								
	| '{' ejecucion_control RETURN '(' expresion ')' ';' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes de las { ");}
	| RETURN '(' expresion ')' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes del RETURN ");}
	
;

then_seleccion_funcion_sin_else: THEN '{' ejecucion_control RETURN '(' expresion ')' ';' '}' ';'
	| THEN RETURN '(' expresion ')' ';'
	
	| '{' ejecucion_control RETURN '(' expresion ')' ';' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes de la { ");}
	| RETURN '(' expresion ')' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes del RETURN ");}
	
;

else_seleccion_funcion: ELSE '{' ejecucion_control RETURN '(' expresion ')' ';' '}' ';'
    | ELSE RETURN '(' expresion ')' ';'
;

iteracion_while_funcion: WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN '(' expresion ')' ';' '}' ';'
                | ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN '(' expresion ')' ';' '}' ';'

                | WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN '(' expresion ')' '}' {agregarError(errores_sintacticos,"Error","Se espera un ';' luego de '}' ");}
                | WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una expresion luego del RETURN");}
                | WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN expresion {agregarError(errores_sintacticos,"Error","Falta los parentesis en la expresion, falta '}' y un ';'");}
                | WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN expresion ';'{agregarError(errores_sintacticos,"Error","Falta los parentesis en la expresion y un '}' para el cierre");}
                | ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN '(' expresion ')' '}' {agregarError(errores_sintacticos,"Error","Se espera un ';' luego de '}' ");}
                | ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN expresion {agregarError(errores_sintacticos,"Error","Falta los parentesis en la expresion, falta '}' y un ';'");}
                | ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion RETURN expresion ';'{agregarError(errores_sintacticos,"Error","Falta los parentesis en la expresion y un '}' para el cierre");}
;

ejecucion_control: ejecucion_control sentencia_ejecutable
        | sentencia_ejecutable
;

ejecucion: ejecucion sentencia
        | sentencia
;

sentencia: sentencia_ejecutable
        | sentencia_declarable
;

sentencia_ejecutable: asignacion ';' 
                | seleccion ';' {addEstructura("if");}
                | impresion ';' {addEstructura("impresion");}
                | iteracion_while {addEstructura("while");}
                | error ';' {addEstructura("error");}
;

iteracion_while:  WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'
                | WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' sentencia_ejecutable
                | ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'
                | ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' sentencia_ejecutable 

                | WHILE ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool antes del ':' ");}
                | WHILE '(' comparacion_bool ')' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera ':' luego de la comparacion_bool");}
                | WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una ejecucion luego de la ASIGNACION");}
                | WHILE '(' ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool dentro de los '(' ')' ");}
                | WHILE '(' comparacion_bool ')' ':' '(' ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
                | WHILE '(' comparacion_bool ')' '(' asignacion ')' sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera un ':' luego de la comparacion_bool");}
                | WHILE '(' comparacion_bool ')' ':' sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion luego del ':' ");}
                | WHILE '(' comparacion_bool ')' ':' '(' ')' sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion entre los parentesis");}
                | WHILE '(' comparacion_bool ')' ':' asignacion sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera que la asignacion se encuentre entre parentesis");}
;

seleccion_iteracion: IF condicion_salto_if THEN '{' ejecucion_iteracion '}' ';' ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}
									
    | IF condicion_salto_if THEN '{' ejecucion_iteracion '}' ';' else_seleccion_iteracion ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}
    
    | IF condicion_salto_if ENDIF {agregarError(errores_sintacticos,"Error","Se espera que haya then_seleccion_iteracion ");}
    //| IF then_seleccion_iteracion ENDIF {agregarError(errores_sintacticos,"Error","Se espera que haya una condicion_salto_if");}
    | IF else_seleccion_iteracion ENDIF {agregarError(errores_sintacticos,"Error","Se espera que haya una condicion_salto_if");}
	//| IF condicion_salto_if then_seleccion_iteracion_sin_else {agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
    //| IF condicion_salto_if then_seleccion_iteracion else_seleccion_iteracion ENDIF {agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
;

else_seleccion_iteracion: ELSE '{' ejecucion_iteracion '}' ';'
    
    | ELSE RETURN '(' ')' ';' {agregarError(errores_sintacticos,"Error","Se espera que haya una expresion entre los parentesis");}
    | ELSE RETURN '(' expresion ')' {agregarError(errores_sintacticos,"Error","Se espera que haya un ';' luego de la expresion ");}
    | ELSE RETURN ';' {agregarError(errores_sintacticos,"Error","Se espera que luego del return haya una expresion entre parentesis");}
    | ELSE RETURN {agregarError(errores_sintacticos,"Error","Se espera que luego del return haya una expresion entre parentesis y un ';'al final");}
    | ELSE '{' ejecucion_iteracion '}' {agregarError(errores_sintacticos,"Error","Se espera que haya un ';' luego de '}' ");}
    | ELSE '{' ejecucion_iteracion {agregarError(errores_sintacticos,"Error","Se espera que haya una '}' y un ';' ");}
    | ELSE ejecucion_iteracion ';' {agregarError(errores_sintacticos,"Error","Se espera que haya que la ejecucion_iteracion se encuentre entre { }");}
;

ejecucion_iteracion: ejecucion_iteracion sentencia_iteracion
                |sentencia_iteracion
;

sentencia_iteracion: asignacion ';'
                | seleccion_iteracion ';' {addEstructura("if en iteracion");}
                | impresion ';' {addEstructura("impresion");}
                | iteracion_while {addEstructura("while");}
                | break ';' {addEstructura("break");}
                | continue ';' {addEstructura("continue");}
;

continue: CONTINUE
	|CONTINUE ':' ID
;

break:
	|BREAK CTE
	|BREAK '-' CTE
;

seleccion: IF condicion_salto_if then_seleccion_sin_else ENDIF {
									polaca.add(pila.pop(), polaca.size());
									}
									
    | IF condicion_salto_if then_seleccion else_seleccion ENDIF {
									polaca.add(pila.pop(), polaca.size());
									} 

    | IF condicion_salto_if '{' ejecucion_control '}' else_seleccion ENDIF {agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
    | IF condicion_salto_if then_seleccion '{' ejecucion_control '}' ENDIF {agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
    | IF condicion_salto_if THEN ENDIF {agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
    | IF condicion_salto_if then_seleccion ELSE ENDIF {agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
    | IF condicion_salto_if THEN else_seleccion ENDIF {agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
	| IF condicion_salto_if then_seleccion_sin_else {agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
    | IF condicion_salto_if then_seleccion else_seleccion {agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
;

then_seleccion: THEN '{' ejecucion_control '}' ';' {
								polaca.add(pila.pop(), polaca.size()+2);
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
								
    | THEN sentencia_ejecutable {
								polaca.add(pila.pop(), polaca.size()+2);
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
								
	| '{' ejecucion_control '}' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
 	| sentencia_ejecutable {agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
    | THEN '{' ejecucion_control {agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
    | THEN '{' '}' {agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del THEN");}
    | THEN ejecucion_control '}' {agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
;

then_seleccion_sin_else: THEN '{' ejecucion_control '}' ';' 
    | THEN sentencia_ejecutable

	| '{' ejecucion_control '}' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
 	| sentencia_ejecutable {agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
    | THEN '{' ejecucion_control {agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
    | THEN '{' '}' {agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del THEN");}
    | THEN ejecucion_control '}' {agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
;

else_seleccion: ELSE '{' ejecucion_control '}' ';'
    | ELSE sentencia_ejecutable

    | ELSE '{' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
    | ELSE ejecucion_control'}' ';' {agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
;

condicion_salto_if: '(' comparacion_bool ')' {
								apilar();
								agregarToken("SI");	
								agregarToken("BF");			 
								}

    | comparacion_bool ')' {agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
    | '(' comparacion_bool {agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
    | comparacion_bool {agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
    | '(' ')' {agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
;

comparacion_bool: expresion comparador expresion {addEstructura("comparacion");
												  agregarToken($2.sval);}
;

//Tipos de comparadores aceptados por el lenguaje
comparador: '>'
    | '<'
    | '='
    | MAYOR_IGUAL
    | MENOR_IGUAL
    | DISTINTO
;

//ACOMODAR FUNCIONES DE LA TABLA DE SIMBOLOS?????
asignacion: ID ASIGNACION expresion {addEstructura($1.sval + " asignacion " + $3.sval);
												String punt1 = TablaSimbolos.obtenerSimboloAmbito($1.sval + Parser.ambito.toString());
                                                String punt3 = TablaSimbolos.obtenerSimboloAmbito($3.sval + Parser.ambito.toString());

                                                agregarToken(punt1); 
                                                agregarToken($2.sval);
                                                //crearPunteroFuncion(punt1, punt3);
                                                }
                                                
	| ID ASIGNACION iteracion_while else_asignacion_iteracion {addEstructura($1.sval + " asignacion " + $3.sval);}
	
	| ID ASIGNACION iteracion_while {agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
;

else_asignacion_iteracion: ELSE CTE
;

expresion: expresion '+' termino {agregarToken("+");}
    | expresion '-' termino {agregarToken("-");}
    | termino
    | TOF32 '(' expresion '+' termino ')'  {agregarToken("+");}
    | TOF32 '(' expresion '-' termino ')' {agregarToken("-");}
    | TOF32 '(' termino ')'
;

termino: termino '*' factor {agregarToken("*");}
    | termino '/' factor {agregarToken("/");}
    | factor
;

//Hay que tener en cuenta el caso de las constantes negativas
//HACER FUNCIONES PARA EL CASO DE ID??????
combinacion_terminales : ID { 
			String punt1 = TablaSimbolos.obtenerSimboloAmbito($1.sval + Parser.ambito.toString());
            agregarToken(punt1);
             
            if (TablaSimbolos.obtenerAtributo(punt1, "tipo").equals(TablaTipos.FUNC_TYPE))
               funcion_a_asignar = punt1;
            }
            
    | CTE {
			String simb = TablaSimbolos.obtenerSimbolo($1.sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            agregarToken($1.sval);
            }
                
 	|'-' CTE {
 			String simb = TablaSimbolos.obtenerSimbolo($2.sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            String simbNeg = negarConstante($2.sval);
            agregarToken(simbNeg);
    		 }
;

//Llamado a funcion con DISCARD, puede tener 0, 1 o 2 parametros
//FIJARSE COMO HACER CUANDO HAY 2 PARAMETROS Y CUANDO NO HAY
factor: combinacion_terminales
    | DISCARD ID '(' combinacion_terminales ',' combinacion_terminales ')'
    | DISCARD ID '(' combinacion_terminales ')' { 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito($2.sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito($4.sval + Parser.ambito.toString());
                          accionSemanticaFuncion(punt4, punt2); 
                          }   
    | DISCARD ID '(' ')'
    
    | ID '(' combinacion_terminales ',' combinacion_terminales ')' {agregarError(errores_sintacticos,"Error","Se espera la palabra DISCARD antes del nombre de la funcion");}
    | ID '(' combinacion_terminales ')' {agregarError(errores_sintacticos,"Error","Se espera la palabra DISCARD antes del nombre de la funcion");}
    | ID '(' ')' {agregarError(errores_sintacticos,"Error","Se espera la palabra DISCARD antes del nombre de la funcion");}
;

impresion: OUT'(' CADENA ')' {       
                                String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                String valor = $3.sval;
                                String tipo = "string";
                                TablaSimbolos.agregarSimbolo(nombre);
                                String simb = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(simb, "valor", valor);
                                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                                agregarToken(nombre);    //agregamos a la polaca el simbolo, junto identificador de cadenas, a la polaca 
                                contador_cadenas++; 
                                }
                                
    | OUT '(' ')' {agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
    | OUT {agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
    | OUT CADENA {agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
;


%%


public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = '@';

public static String funcion_a_asignar = "";
public static StringBuilder ambito = new StringBuilder();
public static final List<Integer> posicionesPolaca = new ArrayList<>();
public static final List<String> polaca = new ArrayList<>();
public static final Stack pila = new Stack();
private static String tipo;

public static List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static List<Character> buffer = new ArrayList<>();
public static List<String> estructura = new ArrayList<>();
public static AnalizadorLexico AL;
public static boolean errores_compilacion = false;

private static int contador_cadenas = 0;
public static final String STRING_CHAR = "&";

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

public void addEstructura(String s){
    estructura.add(s);
}

public List<String> getEstructura() {
    return estructura;
}

public List<String> getErrores() {
    List<String> aux = new ArrayList<>();
    for(String es: errores_sintacticos)
    	aux.add(es);
    return aux;
}

public static void agregarEstructura(String s){
    estructura.add(s);
}

public static void agregarError(List<String> errores, String tipo, String error) {
        if (tipo.equals("ERROR")) {
                errores_compilacion = true;
        }
        int linea_actual = AnalizadorLexico.getLineaActual();
        errores.add(tipo + " (Linea " + linea_actual + "): " + error);
}

int yylex() {
    int tok = 0;
    Token t = AL.getToken(buffer);
    if (t != null) {
        if (t.getId() == 0) {
                return 0;
        }
        tok = t.getId();
        if (t.getAtributo() != null) {
            yylval = new ParserVal(t.getAtributo());
        }
    }
    return tok;
}

public Double getDouble(String numero){
    if (numero.contains("F")){
        var w = numero.split("F");
        return Math.pow(Double.valueOf(w[0]),Double.valueOf(w[1]));
    } else {
        return Double.valueOf(numero);
    }

}

public String negarConstante(String c) {
    String ptr = TablaSimbolos.obtenerSimbolo(c);
    String nuevo = '-' + c;
    if (c.contains(".")) {
        Double d = getDouble(nuevo);
        if ((d < Math.pow(-1.17549435, -38) && d > Math.pow(-3.40282347, 38))){
            if (TablaSimbolos.obtenerSimbolo(nuevo) == null){
                Lexema lexema = new Lexema(d);
                TablaSimbolos.agregarSimbolo(nuevo,lexema);
            }
        } else {
            agregarError(errores_sintacticos, "ERROR", "El numero " + c + " esta fuera de rango.");
            nuevo = "";
        }
    } else {
        Integer i = getInteger(nuevo);
        if ((d > Math.pow(-2, 31) && d < Math.pow(2, 31)-1)){
            if (TablaSimbolos.obtenerSimbolo(nuevo) == null){
                Lexema lexema = new Lexema(d);
                TablaSimbolos.agregarSimbolo(nuevo,lexema);
            }
        } else {
            agregarError(errores_sintacticos, "ERROR", "El numero " + c + " esta fuera de rango.");
            nuevo = "";
        }
    }
    return nuevo;
}

public void setSintactico(List<Character> buffer, AnalizadorLexico AL) {
    this.AL = AL;
    this.buffer = buffer;
}

//Funcion recursiva para controlar si un simbolo se encuentra en la tabla de simbolos, teniendo en cuenta su ambito
public static boolean pertenece(String simbolo){
	if(!simbolo.contains(NAME_MANGLING_CHAR)){    //si no esta
		return false;
	}
	else if(TablaSimbolos.obtenerSimbolo(simbolo) != null){  //si lo encontro en la TS
		return true;
	}
	else{  //caso contrario, hay que recursar, quitando el ultimo ambito
		int index = simbolo.lastIndexOf(NAME_MANGLING_CHAR);
		simbolo = simbolo.substring(0, index);
		return pertenece(simbolo);
	}
}

//Funcion para indicar que se entro en un nuevo ambito
private static void cambiarAmbito(String nuevo_ambito){
	ambito.append(NAME_MANGLING_CHAR).append(nuevo_ambito);
}

//Funcion para indicar que se salio de un ambito, le borra todo hasta el ultimo identificador del Name_mangling
private static void salirAmbito(){
	int index = ambito.lastIndexOf(NAME_MANGLING_CHAR);
	ambito.delete(index, ambito.length());
}

//Funcion que chequea si el tipo de parametro es valido para la funcion
public static boolean chequearParametro(String parametro, String funcion){
	//Obtengo el tipo de parametro
	//Obtengo el tipo de parametro que acepta funcion
	
	//return tipoParametro == tipoFuncion;
}

Verifica el parametro en una llamada a funcion, agrega error en caso de ser incompatibles
public static void accionSemanticaFuncion(String parametro, String funcion){
	if(chequearParametro(parametro, funcion)){
		agregarToken(funcion_a_asignar);
		agregarToken(funcion);
		agregarToken(parametro);
		agregarToken("#CALL");
	}
	else{
		agregarError(errores_semanticos, ERROR, "El tipo del parametro es distinto al provisto");
	}
}

//Funcion para agregar tokens en la Polaca
public static void agregarToken(String token){
	polaca.add(token);
	posicionesPolaca.add(AnalizadorLexico.getLineaActual());
}

//Funcion que agrega una nueva posicion a la pila, correspondiente a lo ultimo que se encontro en la Polaca
public static void apilar(){
	pila.push(polaca.size());
}

private static String nombreFuncion(){
	int ultimo_nmc = ambito.lastIndexOf(NAME_MANGLING_CHAR);
	String nombre_funcion = ambito.substring(ultimo_nmc + 1);
	return nombre_funcion + ambito.substring(0, ultimo_nmc);
}

public static void crearPunteroFuncion(String puntero_funcion, String funcion_llamada) {
        //tomo el tipo de dato de funcion_asignada y funcion de la tabla de simbolos
        String puntero_funcion_asignada = TablaSimbolos.obtenerSimbolo(puntero_funcion);
        String puntero_funcion_llamada = TablaSimbolos.obtenerSimbolo(funcion_llamada);

        String tipo_puntero = TablaSimbolos.obtenerAtributo(puntero_funcion_asignada, "tipo");
        String retorno_funcion_llamada = TablaSimbolos.obtenerAtributo(puntero_funcion_llamada, "retorno");
        
        boolean retorna_funciones = funcion_a_asignar.equals("") && retorno_funcion_llamada.equals(TablaTipos.FUNC_TYPE);
        boolean es_funcion = !funcion_llamada.equals("");
        
        //pregunto si ninguno de ellos es distinto del tipo string
        if (tipo_puntero.equals(TablaTipos.FUNC_TYPE) && (es_funcion || retorna_funciones)) {
                //verifico que el atributo 'uso' del simbolo puntero sea: PUNTERO_FUNCION
        	int puntero_funcion_a_copiar;

                if (retorna_funciones) {
                        String lexema_a_copiar = TablaSimbolos.obtenerAtributo(puntero_funcion_llamada, "nombre_retorno");
                        puntero_funcion_a_copiar = TablaSimbolos.obtenerSimbolo(lexema_a_copiar);
                } else {
                        puntero_funcion_a_copiar = TablaSimbolos.obtenerSimbolo(funcion_a_asignar);
                }

                String uso_puntero = TablaSimbolos.obtenerAtributo(puntero_funcion_asignada, "uso");
                
                if (uso_puntero.equals("variable")) {
                        //agrego a los atributos de puntero_funcion todos los atributos de funcion en la tabla de simbolos, con excepcion del atributo 'uso' y 'lexema'
                        Map<String,String> atributos = TablaSimbolos.obtenerAtributos(puntero_funcion_a_copiar);
                        assert atributos != null;

                        TablaSimbolos.agregarAtributo(puntero_funcion_asignada, "funcion_asignada", atributos.get("lexema"));

                        for (String atributo : atributos.keySet()) {
                                if (atributo.equals("uso") || atributo.equals("lexema")) continue;  //no agrego el atributo uso
                                
                                TablaSimbolos.agregarAtributo(puntero_funcion_asignada, atributo, atributos.get(atributo));
                        }
                }

                funcion_a_asignar = "";   // reiniciamos la funcion a asignar           
        }
}