%{
import AL.AnalizadorLexico;
import AL.Lexema;
import AL.Token;
import AL.TablaSimbolos;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import AL.TablaTipos;
import java.util.Map;
import java.util.Arrays;
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

header_program: ID {cambiarAmbito($1.sval);
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
					}
;

//Reglas de declaraciones y bloques de sentencias

declaracion_variables: tipo lista_variables ';' {
					asignarTipos($1.sval);
					}

        | lista_variables ';' {agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
        | lista_variables {agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
;

lista_variables: lista_variables ',' ID { 
				var_aux.add($3.sval);
                }
        | ID { 
        		var_aux.add($1.sval);
               }
;

sentencia_declarable: declaracion_variables {addEstructura("declaracion variables");
											 }
        | funcion {addEstructura("declaracion funcion");
        		   }
;

funcion: header_funcion ejecucion_funcion {agregarToken(nombreFuncion());
										   salirAmbito();
										   agregarToken("\\ENDP"); }
		| header_funcion {agregarError(errores_sintacticos,"Error","Se espera una ejecucion_funcion");}
;

//Puede tener o no parametros
header_funcion: FUN ID '(' lista_parametros ')' ':' tipo {						
						if (TablaSimbolos.obtenerSimbolo($2.sval+ Parser.ambito.toString()) == null) {
                			TablaSimbolos.modifySimbolo($2.sval, $2.sval + Parser.ambito.toString());
                			String ptr1 = chequeoAmbito($2.sval + Parser.ambito.toString());
                			TablaSimbolos.agregarAtributo(ptr1,"tipo",$7.sval);
                			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
							cambiarAmbito($2.sval);                			
							procesarParametros(ptr1);
							$$.sval = ptr1;
                			agregarToken("!" + nombreFuncion().replace(':', '/'));
            			} else {
                			agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Redeclaracion de la funcion " + $2.sval+ Parser.ambito.toString());
            			}
						}
						
        | FUN ID '(' ')' ':' tipo {						
						if (TablaSimbolos.obtenerSimbolo($2.sval+ Parser.ambito.toString()) == null) {
                			TablaSimbolos.modifySimbolo($2.sval, $2.sval + Parser.ambito.toString());
                			String ptr1 = chequeoAmbito($2.sval + Parser.ambito.toString());
                			TablaSimbolos.agregarAtributo(ptr1,"tipo",$6.sval);
                			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
							cambiarAmbito($2.sval);     
							$$.sval = ptr1;           			
                			agregarToken("!" + nombreFuncion().replace(':', '/'));
            			} else {
                			agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Redeclaracion de la funcion " + $2.sval+ Parser.ambito.toString());
            			}
						}
      
      	//| FUN ID '(' lista_parametros ')' ':'  {agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
      	//| FUN ID '(' ')' ':'  {agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
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
           		String ptr1 = chequeoAmbito($2.sval + Parser.ambito.toString());
            	TablaSimbolos.agregarAtributo(ptr1,"tipo",$1.sval);
            	TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de parametro");
            	par_aux.add(ptr1);
                   }

        | ID {agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
        | tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
;

tipo: I32 {$$.sval = "Entero";} 
    | F32 {$$.sval = "Float";} 
;

//Puede ser una funcion que solo retorne algo o, con sentencias ejecutables
ejecucion_funcion: '{' bloque_funcion RETURN '(' expresion ')' ';' '}' ';' {
																		//$$ = $2;
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
       
        | '{' RETURN '(' expresion ')' ';' '}' ';' {
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
        
        | '{' bloque_funcion RETURN '(' expresion ')' ';' bloque_funcion '}' ';' {agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
		| '{' bloque_funcion RETURN ';' '}' ';'  {agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
		| '{' RETURN ';' '}' ';' {agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
		| '{' bloque_funcion RETURN '('  ')' ';' '}' ';' {agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
		| '{' RETURN '('  ')' ';' '}' ';'  {agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
;

//Una funcion puede tener una sentencia o muchas
bloque_funcion: bloque_funcion sentencia
        | sentencia
;

ejecucion_control: ejecucion_control sentencia_ejecutable
        | sentencia_ejecutable
;

ejecucion: ejecucion sentencia
        | sentencia
;

sentencia: sentencia_ejecutable  {$$ = $1; $$.sval = "ejecutable";}
        | sentencia_declarable   {$$.sval = "declarable";}
;

sentencia_ejecutable: asignacion ';' {$$ = $1;}
                | seleccion ';' {addEstructura("if"); $$ = $1;}
                | impresion ';' {addEstructura("impresion"); $$ = $1;}
                | iteracion_while {addEstructura("while"); $$ = $1;}
                | invocacion_con_d {addEstructura("invocacion con discard"); $$ = $1;}
                | error ';' {addEstructura("error"); $$ = $1;}
;

//DESCARTAR VALOR DE RETORNO DE LA FUNCION?
invocacion_con_d: DISCARD invocacion
;

iteracion_while:  inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';' { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
  				| inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion break '}' ';'{ 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
																												
				| inicio_while condicion_salto_while '(' asignacion ')' '{' break '}' ';'{ 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
  				| inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion continue '}' ';' { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}		
																												
				| inicio_while condicion_salto_while '(' asignacion ')' '{' continue '}' ';' { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}		
                | inicio_while condicion_salto_while '(' asignacion ')' sentencia_ejecutable { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
                | ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';' { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
                | ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion break '}' ';' { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}     
                | ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion continue '}' ';' { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}          
                | ID ':' inicio_while condicion_salto_while '(' asignacion ')' sentencia_ejecutable { 
 																												//DESAPILO+COMPLETO PASO INCOMPLETO
																												//DESAPILO PASO DE INICIO
																												//GENERAR BI AL INICIO
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}

                | inicio_while  '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool ");}
                | inicio_while condicion_salto_while '(' ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
;

inicio_while: WHILE { 
					//APILAR PASO INICIAL
					apilar(); }
;

condicion_salto_while: '(' comparacion_bool ')' ':' { 
					//GENERO BF INCOMPLETA Y APILO PASO INCOMPLETO
					apilar();
					agregarToken("SI");	
					agregarToken("#BF");			 
					}
;

ejecucion_iteracion: ejecucion_iteracion sentencia_ejecutable 
                | sentencia_ejecutable 
;

continue: CONTINUE ';' {$$ = $1; agregarToken("CONTINUE");}
	|CONTINUE ':' ID ';' {$$ = $1;
					agregarToken("CONTINUE");
					agregarToken("ETIQUETA " + $3.sval);
					String ptr = chequeoAmbito($3.sval + Parser.ambito.toString());
					}
;

break: BREAK CTE ';' {$$ = $1;
					agregarToken("BREAK");
					agregarToken("ETIQUETA " + $2.sval);
					TablaSimbolos.agregarAtributo("asignacion while", "break", $2.sval);}
	|BREAK '-' CTE  ';' {$$ = $1;
					agregarToken("BREAK");
					agregarToken("ETIQUETA "+"-"+$3.sval);
					TablaSimbolos.agregarAtributo("asignacion while", "break", "-"+$3.sval);}
;

seleccion: IF condicion_salto_if then_seleccion_sin_else ENDIF {
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
									
    | IF condicion_salto_if then_seleccion else_seleccion ENDIF {
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
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
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("#BI");
								}
								
    | THEN sentencia_ejecutable {
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}

	| THEN break {
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
								
	| THEN '{' ejecucion_control break '}' ';' {
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
								
	| '{' ejecucion_control '}' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
 	| sentencia_ejecutable {agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
    | THEN '{' ejecucion_control {agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
    | THEN ejecucion_control '}' ';' {agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
;

then_seleccion_sin_else: THEN '{' ejecucion_control '}' ';' 
    | THEN sentencia_ejecutable
    | THEN break 			
	| THEN '{' ejecucion_control break '}' ';'

	| '{' ejecucion_control '}' ';' {agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
 	| sentencia_ejecutable {agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
    | THEN '{' ejecucion_control {agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
    | THEN ejecucion_control '}' ';' {agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
;

else_seleccion: ELSE '{' ejecucion_control '}' ';'
    | ELSE sentencia_ejecutable

    | ELSE '{' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
    | ELSE ejecucion_control'}' ';' {agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
;

condicion_salto_if: '(' comparacion_bool ')' {
								apilar();
								agregarToken("SI");	
								agregarToken("#BF");			 
								}

    | comparacion_bool ')' {agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
    | '(' comparacion_bool {agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
    | comparacion_bool {agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
    | '(' ')' {agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
;

//CUANDO AGREGO EL COMPARADOR A LA POLACA NO SE AGREGA BIEN
comparacion_bool: expresion '>' expresion {
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        						//COMO CONTROLO CONVERSIONES EXPLICITAS
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken(">");
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						//}
        						}
        		
        		| expresion '<' expresion {
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        						//COMO CONTROLO CONVERSIONES EXPLICITAS
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken("<");
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						//}
        						}
        		| expresion '=' expresion {
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        						//COMO CONTROLO CONVERSIONES EXPLICITAS
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken("=");
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						//}
        						}
        		| expresion MAYOR_IGUAL expresion {
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        						//COMO CONTROLO CONVERSIONES EXPLICITAS
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken(">=");
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						//}
        						}
        		| expresion MENOR_IGUAL expresion {
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        						//COMO CONTROLO CONVERSIONES EXPLICITAS
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken("<=");
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						//}
        						}
        		| expresion DISTINTO expresion {
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        						//COMO CONTROLO CONVERSIONES EXPLICITAS
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken("=!");
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						//}
        						}
        						
;

//Tipos de comparadores aceptados por el lenguaje
/*comparador: '>'
    | '<'
    | '='
    | MAYOR_IGUAL 
    | MENOR_IGUAL 
    | DISTINTO
;*/

//AGREGAR ACCIONES EN LA POLACA PARA EL CASO DE ASIGNACION POR EXPRESION
asignacion: ID ASIGNACION expresion {addEstructura($1.sval + " asignacion " + $3.sval);
 						String ptr1 = chequeoAmbito($1.sval + Parser.ambito.toString());
        				if (TablaSimbolos.obtenerAtributo(ptr1,"uso") == "constante") {
            				agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Se esta queriendo modificar la constante " + ptr1);
        				} else {
							agregarToken(ptr1); 
            				agregarToken("=:");
            				$$.sval = ptr1;
        				}
                        }
                                                
	| ID ASIGNACION iteracion_while else_asignacion_iteracion {
						addEstructura($1.sval + " asignacion " + $3.sval);
						String ptr1 = chequeoAmbito($1.sval + Parser.ambito.toString());
						if (TablaSimbolos.obtenerSimbolo("asignacion while") == null){
							TablaSimbolos.agregarSimbolo("asignacion while", new Lexema("asignacion while"));
						}
						if((!TablaSimbolos.obtenerAtributo(ptr1, "tipo").equals(TablaSimbolos.obtenerAtributo("asignacion while", "break"))) || (!TablaSimbolos.obtenerAtributo(ptr1, "tipo").equals(TablaSimbolos.obtenerAtributo("asignacion while", "else")))) {
							{agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Tipos incompatibles en la asignacion por while");}
						}
						agregarToken(ptr1); 
            			agregarToken("=:");
            			$$.sval = ptr1;
						}
	
	| ID ASIGNACION iteracion_while {agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
	//| ID ASIGNACION  {agregarError(errores_sintacticos,"Error","Se espera una expresion luego de la asignacion");}
	//| ASIGNACION expresion {agregarError(errores_sintacticos,"Error","Se espera una expresion antes de la asignacion");}
	//| ASIGNACION {agregarError(errores_sintacticos,"Error","Se espera expresion antes y despues de la asignacion");}
;

else_asignacion_iteracion: ELSE CTE {TablaSimbolos.agregarAtributo("asignacion while", "else", $2.sval);}
	| ELSE '-' CTE {TablaSimbolos.agregarAtributo("asignacion while", "else", "-"+$3.sval);}
	
	| ELSE {agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
;

expresion: expresion '+' termino {
								String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        						String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        						//TablaSimbolos.agregarSimbolo($1.sval+"+"+$3.sval, new Lexema($1.sval+"+"+$3.sval));
       							//String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"+"+$3.sval);
        						//TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        						//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            						//TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
									agregarToken("+");
									$$.sval = $3.sval;
        						//} else {
            					//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la suma.");
        						//}
								}
    | expresion '-' termino {
    						String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        					//TablaSimbolos.agregarSimbolo($1.sval+"-"+$3.sval, new Lexema($1.sval+"-"+$3.sval));
       						//String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"-"+$3.sval);
        					//TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            					//TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
								agregarToken("-");
								$$.sval = $3.sval;
        					//} else {
            				//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la resta.");
        					//}
    						}
    | termino {
    	$$ = $1;
        $$.sval = $1.sval;
    }
     
    | TOF32 '(' expresion '+' termino ')'  {
    						String ptr1 = TablaSimbolos.obtenerSimbolo($3.sval);
    						String ptr2 = TablaSimbolos.obtenerSimbolo($5.sval);
    						
    						TablaSimbolos.agregarSimbolo($3.sval+"+"+$5.sval, new Lexema($3.sval+"+"+$5.sval));
    						String ptr3 = TablaSimbolos.obtenerSimbolo($3.sval+"+"+$5.sval);
    						TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
    						TablaSimbolos.agregarAtributo(ptr3, "tipo", TablaTipos.FLOAT_TYPE);
    						
    						agregarToken("+");
    						}
    | TOF32 '(' expresion '-' termino ')' {
    						String ptr1 = TablaSimbolos.obtenerSimbolo($3.sval);
    						String ptr2 = TablaSimbolos.obtenerSimbolo($5.sval);
    						
    						TablaSimbolos.agregarSimbolo($3.sval+"-"+$5.sval, new Lexema($3.sval+"-"+$5.sval));
    						String ptr3 = TablaSimbolos.obtenerSimbolo($3.sval+"-"+$5.sval);
    						TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
    						TablaSimbolos.agregarAtributo(ptr3, "tipo", TablaTipos.FLOAT_TYPE);
    						
    						agregarToken("-");
    						}
    | TOF32 '(' termino ')' {
    						String ptr1 = TablaSimbolos.obtenerSimbolo($3.sval);
    						TablaSimbolos.agregarAtributo(ptr1, "tipo", TablaTipos.FLOAT_TYPE);
    						}
;

termino: termino '*' factor {
							String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        					//TablaSimbolos.agregarSimbolo($1.sval+"*"+$3.sval, new Lexema($1.sval+"*"+$3.sval));
       						//String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"*"+$3.sval);
        					//TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            					//TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
								agregarToken("*");
								$$.sval = $3.sval;
        					//} else {
            				//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la multiplicacion.");
        					//}
							}
    | termino '/' factor {
    					String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        				String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        				//TablaSimbolos.agregarSimbolo($1.sval+"/"+$3.sval, new Lexema($1.sval+"/"+$3.sval));
       					//String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"/"+$3.sval);
        				//TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        				//if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            				//TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
							agregarToken("/");
							$$.sval = $3.sval;
        				//} else {
            			//	agregarError(errores_semanticos,"Error","Tipos no compatibles en la division.");
        				//}
    					}
    | factor {
    	$$ = $1;
        $$.sval = $1.sval;
    }
;

combinacion_terminales : ID {
			String ptr = chequeoAmbito($1.sval + Parser.ambito.toString());
			if (ptr != null) {
				agregarToken(ptr);
				$$.sval = ptr;
            } else {
            	agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Variable " + $1.sval + "no encontrada");
            }
            }
            
    | CTE {
    		String ptr = TablaSimbolos.obtenerSimbolo($1.sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
			agregarToken(ptr);
			$$.sval = ptr;
            }
                
 	|'-' CTE {
 			String ptr = TablaSimbolos.obtenerSimbolo($2.sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
    		String simb = negarConstante(ptr);
    		agregarToken(simb);
    		$$.sval = ptr;
    		}
;

//para cuando llamo a la funcion con el objetivo de utilizar el valor de retorno
invocacion: ID '(' combinacion_terminales ',' combinacion_terminales ')'  { 
							String ptr1 = chequeoAmbito($1.sval + Parser.ambito.toString());
        					String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        					String ptr3 = TablaSimbolos.obtenerSimbolo($5.sval);
        					if(ptr1 != null){
        						boolean esFuncion = TablaSimbolos.obtenerAtributo(ptr1,"uso").equals("nombre de funcion");
        						if (esFuncion) {
            						boolean cantidadParametrosCorrectos = TablaSimbolos.obtenerAtributo(ptr1,"cantidad_parametros").equals("2");
            						if (cantidadParametrosCorrectos) {
                						boolean tipoParametro1Correcto = TablaSimbolos.obtenerAtributo(ptr2,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr1,"tipo_parametro1"));
                						if (tipoParametro1Correcto) {
                        					boolean tipoParametro2Correcto = TablaSimbolos.obtenerAtributo(ptr3,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr1,"tipo_parametro2"));
                        					if (tipoParametro2Correcto) {
                            					agregarToken(ptr1);
												agregarToken(ptr2);
												agregarToken(ptr3);
												agregarToken("#CALL");
												$$.sval = TablaSimbolos.obtenerAtributo(ptr1,"tipo");
                        					} else {
                            					agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"No concuerda el tipo del segundo parametro de la invocacion con el de la funcion.");
                        					}
                						} else {
                    						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"No concuerda el tipo del primer parametro de la invocacion con el de la funcion.");
                						}
            						} else {
                						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Cantidad de parametros incorrectos en la funcion.");
            						}
        						} else {
            						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Funcion no encontrada.");
        						}
        					} else {
            					agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Funcion no encontrada.");
        					}
        				
                        	}	 
                          
    | ID '(' combinacion_terminales ')' { 
    						String ptr1 = chequeoAmbito($1.sval + Parser.ambito.toString());
        					String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        					if (ptr1 != null) {
        						boolean esFuncion = TablaSimbolos.obtenerAtributo(ptr1,"uso").equals("nombre de funcion");
        						if (esFuncion) {
            						boolean cantidadParametrosCorrectos = TablaSimbolos.obtenerAtributo(ptr1,"cantidad_parametros").equals("1");
            						if (cantidadParametrosCorrectos) {
                						boolean tipoParametro1Correcto = TablaSimbolos.obtenerAtributo(ptr2,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr1,"tipo_parametro1"));
                						if (tipoParametro1Correcto) {
                    						agregarToken(ptr1);
											agregarToken(ptr2);
											agregarToken("#CALL");
											$$.sval = TablaSimbolos.obtenerAtributo(ptr1,"tipo");
                						} else {
                    						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"No concuerda el tipo del parametro de la invocacion con el de la funcion.");
                						}
            						} else {
                						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Cantidad de parametros incorrectos en la funcion.");
            						}
        						} else {
            						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Funcion no encontrada.");
        						}
        					} else {
            					agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Funcion no encontrada.");
        					}
                            }   
                          
    | ID '(' ')' { 
    
    						String ptr1 = chequeoAmbito($1.sval + Parser.ambito.toString());
        					if (ptr1 != null) {
        						boolean esFuncion = TablaSimbolos.obtenerAtributo(ptr1,"uso").equals("nombre de funcion");
        						if (esFuncion) {
            						boolean cantidadParametrosCorrectos = TablaSimbolos.obtenerAtributo(ptr1,"cantidad_parametros").equals("0");
            						if (cantidadParametrosCorrectos) {
                						agregarToken(ptr1);
										agregarToken("#CALL");
										$$.sval = TablaSimbolos.obtenerAtributo(ptr1,"tipo");
            						} else {
                						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Cantidad de parametros incorrectos en la funcion.");
            						}
        						} else {
            						agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Funcion no encontrada.");
        						}
        					} else {
            					agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Funcion no encontrada.");
        					}        						
                            }
                            
	//| DISCARD ID '(' combinacion_terminales ',' combinacion_terminales ')' {agregarError(errores_sintacticos,"Error","No puede contener DISCARD, al estar en una asignacion");}
	//| DISCARD ID '(' combinacion_terminales ')' {agregarError(errores_sintacticos,"Error","No puede contener DISCARD, al estar en una asignacion");}
	//| DISCARD ID '(' ')'  {agregarError(errores_sintacticos,"Error","No puede contener DISCARD, al estar en una asignacion");}
;

factor: combinacion_terminales {
		String ptr = chequeoAmbito($1.sval);
        $$ = $1;
        $$.sval = TablaSimbolos.obtenerAtributo(ptr,"tipo");
		}	
	| invocacion
;

impresion: OUT'(' CADENA ')' {       
                                String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                TablaSimbolos.agregarSimbolo(nombre, new Lexema(nombre));
                                String simb = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(simb, "valor", $3.sval);
                                TablaSimbolos.agregarAtributo(simb, "tipo", "String");
                                agregarToken(nombre);    //agregamos a la polaca el simbolo, junto identificador de cadenas
                                contador_cadenas++; 
                                }
                                
    | OUT '(' ')' {agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
    | OUT {agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
    | OUT CADENA {agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
;


%%

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = "@";
public static final String nombreVariableContrato = "@contrato";

public static StringBuilder ambito = new StringBuilder();

public static final List<Integer> posicionesPolaca = new ArrayList<>();
public static final List<String> polaca = new ArrayList<>();
public static final Stack pila = new Stack();

public static List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();

public static List<Character> buffer = new ArrayList<>();
public static List<String> estructura = new ArrayList<>();
public static AnalizadorLexico AL;
public static boolean errores_compilacion = false;

private static int contador_cadenas = 0;
public static final String STRING_CHAR = "&";

public static List<String> var_aux = new ArrayList();
public static List<String> par_aux = new ArrayList();

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

public List<String> getPolaca(){
	return polaca;
}

public List<String> getErroresSemanticos() {
    List<String> aux = new ArrayList<>();
    for(String es: errores_semanticos)
    	aux.add(es);
    return aux;
}

public List<String> getErroresSintacticos() {
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

public static void agregarErrorSemantico(int linea, String error){
        errores_compilacion = true;
        errores_semanticos.add(Parser.ERROR + " (Linea " + linea + "): " + error);
}

int yylex() {
    int tok = 0;
    Token t = AL.getToken(buffer);
    if (t != null) {
        if (t.getIdentificador() == 0) {
                return 0;
        }
        tok = t.getIdentificador();
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
                Lexema lexema = new Lexema(d*-1);
                TablaSimbolos.agregarSimbolo(nuevo,lexema);
            }
        } else {
            agregarError(errores_sintacticos, "ERROR", "El numero " + c + " esta fuera de rango.");
            nuevo = "";
        }
    } else {
        Integer i = Integer.parseInt(nuevo);
        if ((i > Math.pow(-2, 31) && i < Math.pow(2, 31)-1)){
            if (TablaSimbolos.obtenerSimbolo(nuevo) == null){
                Lexema lexema = new Lexema(i*-1);
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

/*
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
*/

//Funcion para asignar tipos a las variables
private void asignarTipos(String tipo) {
    for (int i = 0; i < var_aux.size(); i++) {
        String ptr = TablaSimbolos.obtenerSimbolo(var_aux.get(i));
        String ptr2 = ptr+Parser.ambito.toString();
        if (TablaSimbolos.obtenerSimbolo(ptr2) == null) {
        	TablaSimbolos.modifySimbolo(ptr,ptr2);
        	TablaSimbolos.agregarAtributo(ptr2,"tipo",tipo);
        	TablaSimbolos.agregarAtributo(ptr,"uso","variable");
    	}
    	else{
    		agregarErrorSemantico(AnalizadorLexico.getLineaActual(), "Redeclaracion de la variable " + ptr2);
    	}
    }
    var_aux.clear();
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

private void procesarParametros(String nombre_funcion) {
    if (par_aux.size() == 2) {
        TablaSimbolos.agregarAtributo(nombre_funcion,"cantidad_parametros","2");
        TablaSimbolos.agregarAtributo(nombre_funcion,"tipo_parametro1",TablaSimbolos.obtenerAtributo(par_aux.get(0),"tipo"));
        TablaSimbolos.agregarAtributo(nombre_funcion,"tipo_parametro2",TablaSimbolos.obtenerAtributo(par_aux.get(1),"tipo"));
    }
    if (par_aux.size() == 1) {
        TablaSimbolos.agregarAtributo(nombre_funcion,"cantidad_parametros","1");
        TablaSimbolos.agregarAtributo(nombre_funcion,"tipo_parametro1",TablaSimbolos.obtenerAtributo(par_aux.get(0),"tipo"));
    }
    par_aux.clear();
}

private String auxChequeoAmbito(List<String> amb) {
    String s = amb.get(0);
    for (int i = 1; i < amb.size()-1; i++) {
        s = s + "@" + amb.get(i);
    }
    return s;
}

private String chequeoAmbito(String ptr1) {
    String s = ptr1;
    if (TablaSimbolos.obtenerSimbolo(ptr1) == null) {
        while (true) {
            List<String> ls = Arrays.asList(s.split("@"));
            s = auxChequeoAmbito(ls);
            if (TablaSimbolos.obtenerSimbolo(s) == null) {
                if (ls.size() == 1) {
                    return null;
                }
            } else {
                return s;
            }
        }
    } else {
        return ptr1;
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
