%{
import EtapaLexico.Semantica.*;
import EtapaLexico.AnalisisLexico;
import EtapaLexico.Lexema;
import EtapaLexico.Tokens.Token;
import EtapaLexico.TablaSimbolos;
import GeneracionCodigo.ArbolBinario;
import GeneracionCodigo.NodoArbol;
import GeneracionCodigo.Pila;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
%}

%token ID CTE CADENA IF THEN ELSE ENDIF OUT FUN RETURN BREAK WHEN WHILE MAYOR_IGUAL MENOR_IGUAL ASIGNACION DISTINTO ENTERO DOUBLE CONST DEFER

%left '+' '-'
%left '*' '/'

%start program

%% // declaracion del programa principal

program: header_program '{' ejecucion '}' ';' {
            $$ = new ParserVal(new ArbolBinario((NodoArbol)$3.obj));
            ab = new ArbolBinario((NodoArbol)$3.obj);
            addEstructura("fin programa");
            ambitos.removePila();
            System.out.println("TERMINO PROGRAMA, ambito: " + ambitos.getAmbito());
        } // aca sacar el ambito del "main"
        | header_program {addEstructura("programa sin ejecucion");}
        | header_program '{' ejecucion {agregarError(errores_sintacticos,"Error","Se esperaba un '}' al final del programa");}
        | header_program '{' '}' {agregarError(errores_sintacticos,"Error","Se esperaba una sentencia de ejecucion");}
;

header_program: ID {
        addEstructura("inicio programa");
        ambitos = new Pila($1.sval);
        System.out.println("EMPEZO EL PROGRAMA, ambito: " + ambitos.getAmbito());
    } // aca agregar el ambito del "main"
    //| nombre_programa declaracion_funcion
;

//reglas de declaraciones y bloques de sentencias

declaracion_variables: tipo lista_variables ';' {
            asignarTipos($1.sval);
        }
        | definicion_constante ';' {addEstructura("declaracion constantes");}
        //| declaracion_variables lista_variables ';' // raro
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

sentencia_declarable: declaracion_variables {addEstructura("declaracion variables");}
        | funcion
        | diferimiento {addEstructura("fin diferimiento");}
        //| lista_asignaciones {addEstrictura("lista de asignaciones constantes");}
;

funcion: header_funcion ejecucion_funcion {// aca sacar el ambito
            ambitos.removePila();
            ab = new ArbolBinario((NodoArbol)$2.obj);
            funciones.put($1.sval,ab);
        }
        | header_funcion {agregarError(errores_sintacticos,"Error","Se espera una ejecucion_funcion");}
;

header_funcion: FUN ID '(' lista_parametros ')' ':' tipo  {
            addEstructura("declaracion funcion"); // aca agregar el ambito
            //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + ambitos.getAmbito());
            if (TablaSimbolos.obtenerSimbolo($2.sval+ ambitos.getAmbito()) == null) {
                TablaSimbolos.modifySimbolo($2.sval, $2.sval + ambitos.getAmbito());
                String ptr1 = chequeoAmbito($2.sval + ambitos.getAmbito());
                TablaSimbolos.agregarAtributo(ptr1,"tipo",$7.sval);
                TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
                ambitos.addPila($2.sval);
                procesarParametros(ptr1);
                $$.sval = ptr1;
            } else {
                agregarError(errores_semanticos,"Error","Redeclaraion de la funcion " + $2.sval+ ambitos.getAmbito());
            }
        }
        | FUN ID '(' ')' ':' tipo  {
            addEstructura("declaracion funcion");
            //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + ambitos.getAmbito());
            if (TablaSimbolos.obtenerSimbolo($2.sval+ ambitos.getAmbito()) == null) {
                TablaSimbolos.modifySimbolo($2.sval, $2.sval + ambitos.getAmbito());
                String ptr1 = chequeoAmbito($2.sval + ambitos.getAmbito());
                TablaSimbolos.agregarAtributo(ptr1,"tipo",$6.sval);
                TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
                TablaSimbolos.agregarAtributo(ptr1,"cantidad_parametros","0");
                ambitos.addPila($2.sval);
                $$.sval = ptr1;
            } else {
                agregarError(errores_semanticos,"Error","Redeclaraion de la funcion " + $2.sval+ ambitos.getAmbito());
            }
        }
        | FUN ID '(' error ')' ':' tipo {agregarError(errores_sintacticos,"Error","Maximo 2 parametros en la funcion");}
        | FUN '(' lista_parametros ')' ':' tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
        | FUN '(' ')' ':' tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
        | FUN ID '(' lista_parametros ')' tipo {agregarError(errores_sintacticos,"Error","Se espera el ':' luego de asignar los parametros");}
;

lista_parametros: parametro
        | parametro ',' parametro
        // avisar que el maximo es 2
;

parametro: tipo ID {
            //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + ambitos.getAmbito());
            String ptr1 = chequeoAmbito($2.sval + ambitos.getAmbito());
            TablaSimbolos.agregarAtributo(ptr1,"tipo",$1.sval);
            TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de parametro");
            par_aux.add(ptr1);
        }
        //ERRORES
        | ID {agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
        | tipo {agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
;

tipo: ENTERO {
        $$.sval = "entero";
    }
    | DOUBLE {
        $$.sval = "double";
    }
    //faltaria ver el manejo de las tablas
;

ejecucion_funcion: '{' bloque_funcion /*RETURN '(' expresion ')' ';' */'}' ';' {
            $$ = $2;
        }
        //| '{' RETURN '(' expresion ')' ';' '}' ';'
        //| '{' bloque_funcion RETURN '(' expresion ')' ';' bloque_funcion '}' ';' {agregarError(errores_sintacticos,"Error", "El RETURN debe ser la ultima sentencia de la funcion");}
;

bloque_funcion: bloque_funcion sentencia /*sentencia_funcion*/ {
            if ($2.sval.equals("ejecutable")) {
                $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,(NodoArbol)$2.obj));
            } else {
                $$ = $1;
            }
        }
        | sentencia /*sentencia_funcion*/ {
            if ($1.sval.equals("ejecutable")) {
                $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,null));
            }
        }
;

diferimiento: DEFER sentencia_ejecutable {
        //$$ = new ParserVal(crearNodo("DEFER",(NodoArbol)$2.obj,null)); ??
    }

;

ejecucion_control: ejecucion_control sentencia_ejecutable {
            $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,(NodoArbol)$2.obj));
        }
        | sentencia_ejecutable {
            $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,null));
        }
        | error {agregarError(errores_sintacticos,"Error","No se puede declarar nada en una sentencia de control");}
;
ejecucion: ejecucion sentencia {
            if ($2.sval.equals("ejecutable")) {
                $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,(NodoArbol)$2.obj));
            } else {
                $$ = $1;
            }
        }
        | sentencia {
            if ($1.sval.equals("ejecutable")) {
                $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,null));
            }
        }
;

sentencia: sentencia_ejecutable {
            $$ = $1;
            $$.sval = "ejecutable";
        }
        | sentencia_declarable {// y aca?
            $$.sval = "declarable";
        }
;
//;
sentencia_ejecutable: asignacion ';' {addEstructura("asignacion");
                    $$ = $1;
                }
                | seleccion ';' {addEstructura("fin if");
                    $$ = $1;
                }
                | impresion ';' {addEstructura("fin impresion");
                    $$ = $1;
                } // impresion?asignacion
                | seleccion_when ';' {addEstructura("fin when");
                    $$ = $1;
                }
                | iteracion_while {addEstructura("fin while");
                    $$ = $1;
                }
                | RETURN '(' expresion ')' ';' {
                    $$ = new ParserVal(crearNodo("RETURN",(NodoArbol)$3.obj,null));
                }
                //| error ';' {addEstructura("error");}
;

seleccion_when: WHEN condicion_salto_if then_seleccion/*cuerpo_if*/ {
                //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambitos.getAmbito());
                //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval + Parser.ambitos.getAmbito());
                $$ = new ParserVal(crearNodo("WHEN",(NodoArbol)$2.obj,(NodoArbol)$3.obj));
            }
            //| WHEN condicion_salto_if THEN sentencia_ejecutable
            // me dice que tengo que incorporar en lista de palabras reservadas a la palabra const
            // faltan los errores
            | WHEN condicion_salto_if {agregarError(errores_sintacticos,"Error","Se espera un cuerpo then en el when");}
            | WHEN then_seleccion {agregarError(errores_sintacticos,"Error","Se espera una condicion del when");}
            //| WHEN condicion_salto_if cuerpo_if {agregarError(errores_sintacticos,"Error","Se espera un THEN luego de la comparacion_bool");}
            //| WHEN comparacion_bool THEN '{' ejecucion_control  '}' {agregarError(errores_sintacticos,"Error","Se espera que la comparacion_bool se encuentre encerrada con '(' ')' ");}
            //| WHEN THEN '{' ejecucion_control  '}' {agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool encerrado entre '(' ')' ");}
;

iteracion_while:  WHILE condicion_salto_if ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {
                    //agregarAsignacionWhile((NodoArbol)$8.obj,(NodoArbol)$5.obj);
                    $$ = new ParserVal(crearNodo("WHILE",(NodoArbol)$2.obj,crearNodo("Cuerpo while",(NodoArbol)$8.obj,(NodoArbol)$5.obj)));
                }
                | WHILE condicion_salto_if ':' '(' asignacion ')' sentencia_ejecutable {
                    //agregarAsignacionWhile((NodoArbol)$7.obj,(NodoArbol)$5.obj);
                    $$ = new ParserVal(crearNodo("WHILE",(NodoArbol)$2.obj,crearNodo("Cuerpo while",(NodoArbol)$7.obj,(NodoArbol)$5.obj)));
                }
                | ID ':' WHILE condicion_salto_if ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' { // procesar etiquetas para agregarle el ambito, uso, etc
                    //agregarAsignacionWhile((NodoArbol)$10.obj,(NodoArbol)$7.obj);
                    $$ = new ParserVal(crearNodo("WHILE ETIQUETADO",crearHoja($1.sval),crearNodo("WHILE",(NodoArbol)$4.obj,crearNodo("Cuerpo while",(NodoArbol)$10.obj,(NodoArbol)$7.obj))));
                }
                | ID ':' WHILE condicion_salto_if ':' '(' asignacion ')' sentencia_ejecutable  {
                    //agregarAsignacionWhile((NodoArbol)$9.obj,(NodoArbol)$7.obj);
                    $$ = new ParserVal(crearNodo("WHILE ETIQUETADO",crearHoja($1.sval),crearNodo("WHILE",(NodoArbol)$4.obj,crearNodo("Cuerpo while",(NodoArbol)$9.obj,(NodoArbol)$7.obj))));
                }
                // desarrollando los errores
                /*| WHILE ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una condicion en el while");}
                | WHILE condicion_salto_if '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera ':' luego de la comparacion_bool");}
                | WHILE condicion_salto_if ':' '(' asignacion ')' '{' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una ejecucion luego de la ASIGNACION");}
                //| WHILE '(' ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool dentro de los '(' ')' ");}
                | WHILE condicion_salto_if ':' '(' ')' '{' ejecucion_iteracion '}' ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
                | WHILE condicion_salto_if '(' asignacion ')' sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera un ':' luego de la comparacion_bool");}
                | WHILE condicion_salto_if ':' sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion luego del ':' ");}
                | WHILE condicion_salto_if ':' '(' ')' sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera una asignacion entre los parentesis");}
                | WHILE condicion_salto_if ':' asignacion sentencia_ejecutable ';' {agregarError(errores_sintacticos,"Error","Se espera que la asignacion se encuentre entre parentesis");}*/
;

seleccion_iteracion: IF condicion_salto_if cuerpo_if_iteracion ENDIF {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambitos.getAmbito());
        //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval + Parser.ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("IF",(NodoArbol)$2.obj,(NodoArbol)$3.obj));
    }
    | IF condicion_salto_if then_seleccion_iteracion {agregarError(errores_sintacticos,"Error","Se espera un ENDIF al final del IF");}
    | IF condicion_salto_if ENDIF {agregarError(errores_sintacticos,"Error","Se espera que haya cuerpo para la sentencia if");}
    | IF cuerpo_if_iteracion ENDIF {agregarError(errores_sintacticos,"Error","Se espera que haya una condicion para el if");}
    //| IF else_seleccion_iteracion ENDIF {agregarError(errores_sintacticos,"Error","Se espera que haya una condicion_salto_if");}
;

then_seleccion_iteracion: THEN '{' ejecucion_iteracion '}' {
        $$ = new ParserVal(crearNodo("THEN",(NodoArbol)$3.obj,null));
        //FALTA NODO CONTROL, iria un nodo de arbol con un solo hijo? o como se hace esa sola linea del dibujo?
    }
    | THEN sentencia_iteracion  {
        $$ = new ParserVal(crearNodo("THEN",(NodoArbol)$2.obj,null));
    }
    | THEN ejecucion_iteracion '}'  {agregarError(errores_sintacticos,"Error","Se espera que antes de la ejecucucion_iteracion haya una { ");}
    | THEN '{' ejecucion_iteracion  {agregarError(errores_sintacticos,"Error","Se espera que luego de la ejecucion_iteracion haya una llave");}
;

else_seleccion_iteracion: ELSE '{' ejecucion_iteracion '}' {
        $$ = new ParserVal(crearNodo("ELSE",(NodoArbol)$3.obj,null));
        //FALTA NODO CONTROL, iria un nodo de arbol con un solo hijo? o como se hace esa sola linea del dibujo?
    }
    | ELSE sentencia_iteracion {
        $$ = new ParserVal(crearNodo("ELSE",(NodoArbol)$2.obj,null));
    }
    // ERRRORES
    /*| ELSE RETURN '(' ')' ';' {agregarError(errores_sintacticos,"Error","Se espera que haya una expresion entre los parentesis");}
    | ELSE RETURN '(' expresion ')' {agregarError(errores_sintacticos,"Error","Se espera que haya un ';' luego de la expresion ");}
    | ELSE RETURN ';' {agregarError(errores_sintacticos,"Error","Se espera que luego del return haya una expresion entre parentesis");}
    | ELSE RETURN {agregarError(errores_sintacticos,"Error","Se espera que luego del return haya una expresion entre parentesis y un ';'al final");}*/
    //| ELSE '{' ejecucion_iteracion '}' {agregarError(errores_sintacticos,"Error","Se espera que haya un ';' luego de '}' ");}
    | ELSE '{' ejecucion_iteracion {agregarError(errores_sintacticos,"Error","Se espera que haya una '}' y un ';' ");}
    | ELSE ejecucion_iteracion '}' {agregarError(errores_sintacticos,"Error","Se espera que haya que la ejecucion_iteracion se encuentre entre { }");}
;

seleccion_when_iteracion: WHEN condicion_salto_if then_seleccion_iteracion /*cuerpo_if_iteracion*/ ';' {
                    //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambitos.getAmbito());
                    //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval + Parser.ambitos.getAmbito());
                    $$ = new ParserVal(crearNodo("WHEN",(NodoArbol)$2.obj,(NodoArbol)$3.obj));
                }
                | WHEN condicion_salto_if ';' {agregarError(errores_sintacticos,"Error","Se espera un cierpo en el when");}
                | WHEN cuerpo_if_iteracion ';' {agregarError(errores_sintacticos,"Error","Se espera que haya una condicion para el when");}
                //| WHEN '(' ')' THEN break ';' {agregarError(errores_sintacticos,"Error","Se espera que haya una comparacion_bool entre parentesis");}
                //| WHEN comparacion_bool THEN break ';' {agregarError(errores_sintacticos,"Error","Se espera que la comparacion_bool se encuentre entre parentesis");}
;

cuerpo_if_iteracion: then_seleccion_iteracion {
        //String ptr = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("Cuerpo",(NodoArbol)$1.obj,null));
    }
    | then_seleccion_iteracion else_seleccion_iteracion {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambitos.getAmbito());
        //String ptr2 = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("Cuerpo",(NodoArbol)$1.obj,(NodoArbol)$2.obj));
    }
;
ejecucion_iteracion: ejecucion_iteracion sentencia_iteracion {
                    $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,(NodoArbol)$2.obj));
                }
                |sentencia_iteracion {
                    $$ = new ParserVal(crearNodo("S",(NodoArbol)$1.obj,null));
                }
;

sentencia_iteracion: asignacion ';' {addEstructura("asignacion en iteracion");
                    $$ = $1;
                }
                | seleccion_iteracion ';' {addEstructura("fin if en iteracion");
                    $$ = $1;   
                }
                | impresion ';' {addEstructura("impresion");
                    $$ = $1;
                } // impresion?
                | seleccion_when_iteracion {addEstructura("fin when en iteracion");
                    $$ = $1;
                }
                | iteracion_while {addEstructura("fin while");
                    $$ = $1;
                }
                | break ';' {addEstructura("break");
                    $$ = $1;
                } // break?
;

break: BREAK { // salta al final de la estructura
        $$ = new ParserVal(crearHoja("BREAK"));
    }
    | BREAK ID {// salta al ID, chequear que sea etiqueta
        String ptr = chequeoAmbito($2.sval + ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("BREAK",crearHoja(ptr),null));
    }
;

seleccion: IF condicion_salto_if cuerpo_if ENDIF {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambitos.getAmbito());
        //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval + Parser.ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("IF",(NodoArbol)$2.obj,(NodoArbol)$3.obj));
    }
   // MODIFICAR ERRORES
    | IF condicion_salto_if '{' ejecucion_control '}' else_seleccion ENDIF {agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
    | IF condicion_salto_if then_seleccion '{' ejecucion_control '}' ENDIF {agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
    | IF condicion_salto_if THEN ENDIF {agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
    | IF condicion_salto_if then_seleccion ELSE ENDIF {agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
    | IF condicion_salto_if THEN else_seleccion ENDIF {agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
;

cuerpo_if: then_seleccion {
        //String ptr = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("Cuerpo",(NodoArbol)$1.obj,null));
    }
    | then_seleccion else_seleccion{
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambitos.getAmbito());
        //String ptr2 = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambitos.getAmbito());
        $$ = new ParserVal(crearNodo("Cuerpo",(NodoArbol)$1.obj,(NodoArbol)$2.obj));
    }
;

then_seleccion: THEN '{' ejecucion_control '}'{
        $$ = new ParserVal(crearNodo("THEN",(NodoArbol)$3.obj,null));
        //FALTA NODO CONTROL, iria un nodo de arbol con un solo hijo? o como se hace esa sola linea del dibujo?
    }
    | THEN sentencia_ejecutable {
        $$ = new ParserVal(crearNodo("THEN",(NodoArbol)$2.obj,null));
        //FALTA NODO CONTROL
    }
    // ERRORES
    | THEN '{' ejecucion_control {agregarError(errores_sintacticos,"Error","Se espera un '}' al de las sentencias del THEN");}
    | THEN '{' '}' {agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del THEN");}
    | THEN ejecucion_control '}' {agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
;

else_seleccion: ELSE '{' ejecucion_control '}'{
        $$ = new ParserVal(crearNodo("ELSE",(NodoArbol)$3.obj,null));
        //FALTA NODO CONTROL
    }
    | ELSE sentencia_ejecutable {
        $$ = new ParserVal(crearNodo("ELSE",(NodoArbol)$2.obj,null));
        //FALTA NODO CONTROL
    }
  // ERRORES
    | ELSE '{' '}' ';' {agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
    | ELSE ejecucion_control'}' ';' {agregarError(errores_sintacticos,"Error","Se espera un '{' luego del ELSE");}
;

condicion_salto_if: '(' comparacion_bool ')'{
        $$ = new ParserVal((NodoArbol)crearNodo("Cond",(NodoArbol)$2.obj,null));
    }
   // ERRORES
    | comparacion_bool ')' {agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
    | '(' comparacion_bool {agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion_bool");}
    | comparacion_bool {agregarError(errores_sintacticos,"Error","Se espera que la comparacion_bool se encuentre entre parentesis");}
    | '(' ')' {agregarError(errores_sintacticos,"Error","Se espera una condicion");}
;

comparacion_bool: expresion comparador expresion {
        //System.out.println("ANTES: " + $1.sval + ", " + $3.sval);
        String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        //System.out.println("AA " + ptr1 + ", BB " + ptr2);
        //if (TablaSimbolos.getAtributo(ptr1,"tipo").equals(TablaSimbolos.getAtributo(ptr2,"tipo"))) {
        if ($1.sval.equals($3.sval)) {
            System.out.println("COINCIDEN TIPOS EN COMPARACION");
            $$ = new ParserVal(crearNodo($2.sval,(NodoArbol)$1.obj,(NodoArbol)$3.obj));
        } else {
            agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        }
        //$$ = new ParserVal(crearNodo($2.sval,(NodoArbol)$1.obj,(NodoArbol)$3.obj));
    }
    //ERRORES
    | expresion comparador {agregarError(errores_sintacticos,"Error","Se espera una expresion luego del comparador");}
    | comparador expresion {agregarError(errores_sintacticos,"Error","Se espera una expresion antes del comparador");}
    | comparador {agregarError(errores_sintacticos,"Error","se espera expresiones para poder realizar las comparaciones");}
;

comparador: '>' {
        $$.sval = ">";
    }
    | '<' {
        $$.sval = "<";
    }
    | '=' {
        $$.sval = "=";
    }
    | MAYOR_IGUAL {
        $$.sval = ">=";
    }
    | MENOR_IGUAL {
        $$.sval = "<=";
    }
    | DISTINTO {
        $$.sval = "=!";
    } //DUDAS
;

lista_asignaciones: lista_asignaciones ',' asignacion{
        String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval); // y esto que sval tendria?
        String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval); // y este?
        TablaSimbolos.agregarAtributo(ptr2,"uso","constante");
        $$ = new ParserVal(crearNodo(",",(NodoArbol)$1.obj,(NodoArbol)$3.obj)); // DUDA. esta bien?
        // se estaria haciendo el chequeo de tipo de la asignacion?
     }
    | lista_asignaciones asignacion {agregarError(errores_sintacticos,"Error","Lista de asignaciones sin coma");}
    | asignacion {
        String ptr = TablaSimbolos.obtenerSimbolo($1.sval);
        TablaSimbolos.agregarAtributo(ptr,"uso","constante");
        //TablaSimbolos.agregarAtributo(ptr,"valor",??); // FALTA VALOR, COMO LO CONSIGO?
        $$ = $1;
        
    }
;
definicion_constante: CONST lista_asignaciones{
    
}
;

asignacion: ID ASIGNACION expresion {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval + ambitos.getAmbito());
        String ptr1 = chequeoAmbito($1.sval + ambitos.getAmbito());
        //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        //if (TablaSimbolos.getAtributo(ptr1,"tipo").equals(TablaSimbolos.getAtributo(ptr2,"tipo"))) {
        if (TablaSimbolos.getAtributo(ptr1,"uso").equals("constante")) {
            agregarError(errores_semanticos,"Error","Se esta queriendo modificar la constante " + ptr1);
        } else {
            if (TablaSimbolos.getAtributo(ptr1,"tipo").equals($3.sval)) {
                //System.out.println("COINCIDEN TIPO ASIGNACION");
                $$ = new ParserVal(crearNodo("=:",crearHoja(ptr1),(NodoArbol)$3.obj)); //DUDA
                $$.sval = ptr1;
            } else {
                agregarError(errores_semanticos,"Error","Tipos no compatibles en la asignacion.");
            }
        }
        //$$ = new ParserVal(crearNodo("=:",crearHoja(ptr1),(NodoArbol)$3.obj)); //DUDA
    }
;

expresion: expresion '+' termino {
        /*String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        TablaSimbolos.agregarSimbolo($1.sval+"+"+$3.sval, new Lexema($1.sval+"+"+$3.sval));
        String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"+"+$3.sval);
        TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");*/
        //if (TablaSimbolos.getAtributo(ptr1,"tipo").equals(TablaSimbolos.getAtributo(ptr2,"tipo"))) {
        if ($1.sval.equals($3.sval)) {
            //TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.getAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
            $$ = new ParserVal(crearNodo("+",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
            $$.sval = $3.sval;
            // y aca que dibujamos? $$.sval = ptr?????
        } else {
            agregarError(errores_semanticos,"Error","Tipos no compatibles en la suma.");
        }
        //$$ = new ParserVal(crearNodo("+",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
    }
    | expresion '-' termino {
        /*String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        TablaSimbolos.agregarSimbolo($1.sval+"-"+$3.sval, new Lexema($1.sval+"-"+$3.sval));
        String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambitos.getAmbito() +"-"+$3.sval + Parser.ambitos.getAmbito());
        TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");*/

        //if (TablaSimbolos.getAtributo(ptr1,"tipo").equals(TablaSimbolos.getAtributo(ptr2,"tipo"))) {
        if ($1.sval.equals($3.sval)) {
            //TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.getAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
            $$.obj = crearNodo("-",(NodoArbol)$1.obj,(NodoArbol)$3.obj);
            $$.sval = $3.sval;
            // y aca que dibujamos? $$.sval = ptr?????
        } else {
            agregarError(errores_semanticos,"Error","Tipos no compatibles en la resta.");
        }
        //$$ = new ParserVal(crearNodo("-",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
    }
    | termino {
        //String ptr = TablaSimbolos.obtenerSimbolo($1.sval);
        $$ = $1;
        //$$.sval = ptr;
        $$.sval = $1.sval;
    }
    // ERRORES
    | tipo '(' expresion '+' termino ')' {agregarError(errores_sintacticos,"Error","Conversion explicita no permitida");}
    | tipo '(' expresion '-' termino ')' {agregarError(errores_sintacticos,"Error","Conversion explicita no permitida");}
    | tipo '(' termino ')' {agregarError(errores_sintacticos,"Error","Conversion explicita no permitida");}
    | tipo '(' expresion '+' ')' {agregarError(errores_sintacticos,"Error","Se espera una termino luego del signo '+' y conversion explicita no permitida");}
    | tipo '(' expresion '-' ')' {agregarError(errores_sintacticos,"Error","Se espera una termino luego del signo '-' y conversion explicita no permitida");}
    // duda con el tipo de la expresion
;

termino: termino '*' factor {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        //TablaSimbolos.agregarSimbolo($1.sval+"*"+$3.sval, new Lexema($1.sval+"*"+$3.sval));
        //String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"*"+$3.sval );
        //TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");

        //if (TablaSimbolos.getAtributo(ptr1,"tipo").equals(TablaSimbolos.getAtributo(ptr2,"tipo"))) {
        if ($1.sval.equals($3.sval)) {
            //TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.getAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
            $$ = new ParserVal(crearNodo("*",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
            $$.sval = $3.sval;
            // y aca que dibujamos? $$.sval = ptr?????
        } else {
            agregarError(errores_semanticos,"Error","Tipos no compatibles en la multiplicacion.");
        }
        //$$ = new ParserVal(crearNodo("*",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
    }
    | termino '/' factor {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval);
        //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);

        //TablaSimbolos.agregarSimbolo($1.sval+"/"+$3.sval, new Lexema($1.sval+"/"+$3.sval));
        //String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"/"+$3.sval);
        //TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");

        //if (TablaSimbolos.getAtributo(ptr1,"tipo").equals(TablaSimbolos.getAtributo(ptr2,"tipo"))) {
        if ($1.sval.equals($3.sval)) {
            //TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.getAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar
            $$ = new ParserVal(crearNodo("/",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
            $$.sval = $3.sval;
            // y aca que dibujamos? $$.sval = ptr?????
        } else {
            agregarError(errores_semanticos,"Error","Tipos no compatibles en la division.");
        }
        //$$ = new ParserVal(crearNodo("/",(NodoArbol)$1.obj,(NodoArbol)$3.obj));
    }
    | factor {
        //String ptr = TablaSimbolos.obtenerSimbolo($1.sval); // aca tambien? o solamente en combinacion terminales? y en factor?
        $$ = $1;
        //$$.sval = ptr;
        $$.sval = $1.sval;
    }
;

combinacion_terminales : ID {
        //String ptr = TablaSimbolos.obtenerSimbolo($1.sval + ambitos.getAmbito());
        String ptr = chequeoAmbito($1.sval + ambitos.getAmbito());
        if (ptr != null) {
            $$ = new ParserVal(crearHoja(ptr));
            $$.sval = ptr;
            //$$.sval = TablaSimbolos.getAtributo(ptr,"tipo");
        } else {
            agregarError(errores_semanticos,"Error","Variable " + $1.sval + "no encontrada");
        }
    }
    | CTE {
        String ptr = TablaSimbolos.obtenerSimbolo($1.sval);
        $$ = new ParserVal(crearHoja(ptr));
        $$.sval = ptr;
        //$$.sval = TablaSimbolos.getAtributo(ptr,"tipo");
    }
 	|'-' CTE {
            String ptr = TablaSimbolos.obtenerSimbolo($2.sval);
            negarConstante($2.sval);
            $$ = new ParserVal(crearHoja("-"+ptr));
            $$.sval = ptr;
            //$$.sval = TablaSimbolos.getAtributo(ptr,"tipo");
    }
;

factor: combinacion_terminales { // aca tambien? o solamente en combinacion terminales?
        //String ptr = TablaSimbolos.obtenerSimbolo($1.sval);
        String ptr = chequeoAmbito($1.sval);
        $$ = $1;
        //$$.sval = ptr;
        $$.sval = TablaSimbolos.getAtributo(ptr,"tipo");

    }
    | ID '(' combinacion_terminales ',' combinacion_terminales ')' {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval + ambitos.getAmbito());
        String ptr1 = chequeoAmbito($1.sval + ambitos.getAmbito());
        String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        String ptr3 = TablaSimbolos.obtenerSimbolo($5.sval);
        if (ptr1 != null) {
            boolean esFuncion = TablaSimbolos.getAtributo(ptr1,"uso").equals("nombre de funcion");
            if (esFuncion) {
                boolean cantidadParametrosCorrectos = TablaSimbolos.getAtributo(ptr1,"cantidad_parametros").equals("2");
                if (cantidadParametrosCorrectos) {
                    boolean tipoParametro1Correcto = TablaSimbolos.getAtributo(ptr2,"tipo").equals(TablaSimbolos.getAtributo(ptr1,"tipo_parametro1"));
                    if (tipoParametro1Correcto) {
                            boolean tipoParametro2Correcto = TablaSimbolos.getAtributo(ptr3,"tipo").equals(TablaSimbolos.getAtributo(ptr1,"tipo_parametro2"));
                            if (tipoParametro2Correcto) {
                                // crear hoja ?? $$ = $ ???
                                $$ = new ParserVal(crearNodo("INVOCACION",crearHoja(ptr1),crearNodo("PARAMETROS",(NodoArbol)$3.obj,(NodoArbol)$5.obj)));
                                $$.sval = TablaSimbolos.getAtributo(ptr1,"tipo");
                            } else {
                                agregarError(errores_semanticos,"Error","No concuerda el tipo del segundo parametro de la invocacion con el de la funcion.");
                            }
                    } else {
                        agregarError(errores_semanticos,"Error","No concuerda el tipo del primer parametro de la invocacion con el de la funcion.");
                    }
                } else {
                    agregarError(errores_semanticos,"Error","Cantidad de parametros incorrectos en la funcion.");
                }
            } else {
                agregarError(errores_semanticos,"Error","Funcion no encontrada.");
            }
        } else {
            agregarError(errores_semanticos,"Error","Funcion no encontrada.");
        }
    }
    | ID '(' combinacion_terminales ')' {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval + ambitos.getAmbito());
        String ptr1 = chequeoAmbito($1.sval + ambitos.getAmbito());
        String ptr2 = chequeoAmbito($3.sval);
        //String ptr2 = TablaSimbolos.obtenerSimbolo($3.sval);
        if (ptr1 != null) {
            boolean esFuncion = TablaSimbolos.getAtributo(ptr1,"uso").equals("nombre de funcion");
            if (esFuncion) {
                boolean cantidadParametrosCorrectos = TablaSimbolos.getAtributo(ptr1,"cantidad_parametros").equals("1");
                if (cantidadParametrosCorrectos) {
                    boolean tipoParametro1Correcto = TablaSimbolos.getAtributo(ptr2,"tipo").equals(TablaSimbolos.getAtributo(ptr1,"tipo_parametro1"));
                    if (tipoParametro1Correcto) {
                        // crear hoja ?? $$ = $ ???
                        $$ = new ParserVal(crearNodo("INVOCACION",crearHoja(ptr1),crearNodo("PARAMETROS",(NodoArbol)$3.obj,null)));
                        $$.sval = TablaSimbolos.getAtributo(ptr1,"tipo");
                    } else {
                        agregarError(errores_semanticos,"Error","No concuerda el tipo del parametro de la invocacion con el de la funcion.");
                    }
                } else {
                    agregarError(errores_semanticos,"Error","Cantidad de parametros incorrectos en la funcion.");
                }
            } else {
                agregarError(errores_semanticos,"Error","Funcion no encontrada.");
            }
        } else {
            agregarError(errores_semanticos,"Error","Funcion no encontrada.");
        }
    }
    | ID '(' ')' {
        //String ptr1 = TablaSimbolos.obtenerSimbolo($1.sval + ambitos.getAmbito());
        String ptr1 = chequeoAmbito($1.sval + ambitos.getAmbito());
        if (ptr1 != null) {
        boolean esFuncion = TablaSimbolos.getAtributo(ptr1,"uso").equals("nombre de funcion");
        if (esFuncion) {
            boolean cantidadParametrosCorrectos = TablaSimbolos.getAtributo(ptr1,"cantidad_parametros").equals("0");
            if (cantidadParametrosCorrectos) {
                // crear hoja ?? $$ = $ ???
                $$ = new ParserVal(crearNodo("INVOCACION",crearHoja($1.sval),crearHoja("PARAMETROS")));
                $$.sval = TablaSimbolos.getAtributo(ptr1,"tipo");
            } else {
                agregarError(errores_semanticos,"Error","Cantidad de parametros incorrectos en la funcion.");
            }
        } else {
            agregarError(errores_semanticos,"Error","Funcion no encontrada.");
        }
        } else {
            agregarError(errores_semanticos,"Error","Funcion no encontrada.");
        }
    }
;

impresion: OUT '(' CADENA ')' {
        $$ = new ParserVal(crearNodo("OUT",crearHoja($3.sval),null));
    }
    | OUT '(' ')' {agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
    | OUT {agregarError(errores_sintacticos,"Error","Se espera () con una cadena dentro");}
    | OUT CADENA {agregarError(errores_sintacticos,"Error","Se espera un que la CADENA se encuentre entre parentesis");}
;

%%

public static ArbolBinario ab;

public static Pila ambitos;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";

public static List<String> errores_sintacticos = new ArrayList<>();
public static List<Character> buffer = new ArrayList<>();
public static List<String> estructura = new ArrayList<>();
public static AnalisisLexico AL;
public static boolean errores_compilacion = false;

public static List<String> var_aux = new ArrayList();
public static List<String> par_aux = new ArrayList();
private static List<String> errores_semanticos = new ArrayList<>();

public static Map<String,ArbolBinario> funciones = new HashMap<>();

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}
public void addEstructura(String s){
    estructura.add(s+"-linea: "+AL.getLineaActual());
}

public List<String> getEstructura() {
    return estructura;
}

public List<String> getErrores() {
    return errores_sintacticos;
}

public List<String> getErroresSemanticos() {
    return errores_semanticos;
}

public static void agregarEstructura(String s){
    estructura.add(s);
}

public static void agregarError(List<String> errores, String tipo, String error) {
        if (tipo.equals("ERROR")) {
                errores_compilacion = true;
        }

        int linea_actual = AnalisisLexico.getLineaActual();

        errores.add(tipo + " (Linea " + linea_actual + "): " + error);
}

public static void agregarErrorSemantico(List<String> errores, String tipo, String error) {
        if (tipo.equals("ERROR")) {
                errores_compilacion = true;
        }

        int linea_actual = AnalisisLexico.getLineaActual();

        errores.add(tipo + " (Linea " + linea_actual + "): " + error);
}


int yylex() {
    int tok = 0;
    //System.out.println("YYLEX, " + buffer.get(0) + " - ");
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
    //System.out.println("YYLEX, " + tok);
    return tok;
}

public Double getDouble(String d){
    if (d.contains("D")){
        var w = d.split("D");
        return Math.pow(Double.valueOf(w[0]),Double.valueOf(w[1]));
    } else {
        return Double.valueOf(d);
    }
}

public String negarConstante(String c) { // AHORA?
    String ptr = TablaSimbolos.obtenerSimbolo(c);
    String nuevo = '-' + c;
    if (c.contains(".")) {
        Double d = getDouble(c);
        if ((d < Math.pow(-1.7976931348623157,308) && d > Math.pow(-2.2250738585072014,-308))){
            if (TablaSimbolos.obtenerSimbolo(nuevo) == null){ // si no es null es porque ya se agrego anteriormente la misma constante.
                Lexema lexema = new Lexema(d*-1);
                TablaSimbolos.agregarSimbolo(nuevo,lexema); // falta chequear que no se este usando la misma constante positiva y asi borrarla.
            }
        } else {
            agregarError(errores_sintacticos, "ERROR", "El numero " + c + " esta fuera de rango.");
            nuevo = "";
        }
    } else {
        agregarError(errores_sintacticos, "ERROR", "El numero entero " + c + " no puede ser negativo.");
        //nuevo = "";
        //TablaSimbolos.truncarEntero(ptr,nuevo);
        AL.deleteToken();
    }
    return nuevo;
}

public void setSintactico(List<Character> buffer, AnalisisLexico AL) {
    this.AL = AL;
    this.buffer = buffer;
}

public NodoArbol crearNodo(String a, NodoArbol b, NodoArbol c){
    return new NodoArbol(a,b,c);
}

public NodoArbol crearHoja(String s){
    return new NodoArbol(s,null,null);
}

private void asignarTipos(String tipo) {
    for (int i = 0; i < var_aux.size(); i++) {
        String ptr = TablaSimbolos.obtenerSimbolo(var_aux.get(i));
        String ptr2 = ptr+ambitos.getAmbito();
        if (TablaSimbolos.obtenerSimbolo(ptr2) == null) {
            TablaSimbolos.modifySimbolo(ptr,ptr2);
            TablaSimbolos.agregarAtributo(ptr2,"tipo",tipo);
            TablaSimbolos.agregarAtributo(ptr2,"uso","variable");
        } else {
            agregarError(errores_semanticos,"Error","Redeclaracion de la variable " + ptr2);
        }
    }
    var_aux.clear();
}

private void agregarAsignacionWhile(NodoArbol n, NodoArbol toAdd) {
    if (n.getIzquierda() == null) {
        n.setIzquierda(toAdd);
    } else {
        agregarAsignacionWhile(n.getIzquierda(),toAdd);
    }
}

private void procesarParametros(String nombre_funcion) {
    if (par_aux.size() == 2) {
        TablaSimbolos.agregarAtributo(nombre_funcion,"cantidad_parametros","2");
        TablaSimbolos.agregarAtributo(nombre_funcion,"tipo_parametro1",TablaSimbolos.getAtributo(par_aux.get(0),"tipo"));
        TablaSimbolos.agregarAtributo(nombre_funcion,"tipo_parametro2",TablaSimbolos.getAtributo(par_aux.get(1),"tipo"));
    }
    if (par_aux.size() == 1) {
        TablaSimbolos.agregarAtributo(nombre_funcion,"cantidad_parametros","1");
        TablaSimbolos.agregarAtributo(nombre_funcion,"tipo_parametro1",TablaSimbolos.getAtributo(par_aux.get(0),"tipo"));
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