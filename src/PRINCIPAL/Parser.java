package PRINCIPAL;

//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica(copia).y"
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
//#line 28 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short CADENA=259;
public final static short IF=260;
public final static short THEN=261;
public final static short ELSE=262;
public final static short ENDIF=263;
public final static short OUT=264;
public final static short FUN=265;
public final static short RETURN=266;
public final static short BREAK=267;
public final static short CONTINUE=268;
public final static short WHILE=269;
public final static short MAYOR_IGUAL=270;
public final static short MENOR_IGUAL=271;
public final static short ASIGNACION=272;
public final static short DISTINTO=273;
public final static short I32=274;
public final static short F32=275;
public final static short TOF32=276;
public final static short DISCARD=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    3,    3,    3,    5,    5,
    6,    6,    7,    7,    8,    8,    8,    8,    8,    8,
    8,   10,   10,   11,   11,   11,    4,    4,    9,    9,
    9,    9,    9,    9,    9,   12,   12,   15,   15,    2,
    2,   14,   14,   16,   16,   16,   16,   16,   16,   21,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   23,   24,   25,   25,   27,   27,   26,   26,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   31,   31,   31,
   31,   31,   31,   31,   30,   30,   30,   30,   30,   30,
   32,   32,   32,   32,   29,   29,   29,   29,   29,   28,
   28,   28,   28,   28,   28,   17,   17,   17,   33,   33,
   33,   13,   13,   13,   13,   13,   13,   34,   34,   34,
   36,   36,   36,   22,   22,   22,   35,   35,   19,   19,
   19,   19,
};
final static short yylen[] = {                            2,
    5,    1,    3,    3,    1,    3,    2,    1,    3,    1,
    1,    1,    2,    1,    7,    6,    5,    6,    5,    5,
    4,    1,    3,    2,    1,    1,    1,    1,    9,    8,
   10,    6,    5,    8,    7,    2,    1,    2,    1,    2,
    1,    1,    1,    2,    2,    2,    1,    1,    2,    2,
    9,    9,    9,    6,   11,   11,   11,    8,    8,    8,
    1,    4,    2,    1,    1,    3,    2,    3,    4,    5,
    7,    7,    4,    5,    5,    3,    4,    5,    2,    2,
    4,    1,    3,    4,    5,    2,    4,    1,    3,    4,
    5,    2,    4,    4,    3,    2,    2,    1,    2,    3,
    3,    3,    3,    3,    3,    3,    4,    3,    2,    3,
    1,    3,    3,    1,    6,    6,    4,    3,    3,    1,
    1,    1,    2,    6,    4,    3,    1,    1,    4,    3,
    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,    0,    0,    0,    0,    0,   61,
   27,   28,    0,    4,    0,   11,    0,    0,   43,   12,
    0,   41,   42,    0,    0,    0,   47,   48,    0,   49,
    0,    0,    0,  122,    0,    0,    0,    0,  128,    0,
    0,    0,  120,  127,  132,    0,    0,    0,    0,   50,
    0,   40,   10,    0,    7,    0,    0,   13,   44,   45,
   46,    0,    0,    0,    0,    0,    0,    0,    0,  123,
   99,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   96,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  130,   25,    0,    0,    0,    0,    0,    0,    0,    1,
    6,    9,    0,    0,   37,    0,    0,    0,    0,    0,
  107,    0,    0,  121,  126,    0,    0,    0,   95,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   73,    0,
    0,    0,    0,   80,    0,    0,   39,   69,    0,    0,
    0,  118,  119,  129,    0,    0,   21,   24,    0,    0,
    0,    0,    0,    0,    0,   36,    0,    0,    0,    0,
    0,  109,    0,    0,    0,  125,    0,    0,  117,    0,
    0,    0,   67,    0,    0,    0,   38,   75,    0,   74,
    0,   70,    0,    0,   20,   23,   19,    0,    0,    0,
    0,    0,    0,    0,   62,    0,    0,  110,    0,    0,
    0,    0,    0,    0,    0,   68,    0,    0,    0,    0,
    0,   16,    0,   18,   33,    0,    0,    0,    0,    0,
   64,    0,    0,    0,   54,    0,  124,  115,  116,   93,
    0,   94,    0,   71,   72,   15,    0,    0,   32,    0,
    0,    0,   63,    0,    0,    0,    0,    0,    0,   58,
   91,   35,    0,    0,    0,   59,   60,    0,    0,    0,
    0,    0,    0,    0,   30,   34,    0,    0,   66,   51,
   52,   53,    0,    0,    0,   29,    0,   55,   56,   57,
   31,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   58,   96,
   97,  104,   38,  105,  171,   23,   24,   25,   26,   27,
   28,   39,   29,   63,  222,  134,  248,  108,   41,   86,
   87,  135,  111,   42,   43,   44,
};
final static short yysindex[] = {                      -228,
    0,    0,  -79,  405,   21,  -45,  -21,  -35,  -24,    0,
    0,    0, -200,    0,  431,    0, -155,   -9,    0,    0,
  -17,    0,    0,   54,   59,   70,    0,    0,   87,    0,
   76, -128,  109,    0,  120, -102,   -5,  322,    0,  128,
  -65,   68,    0,    0,    0,  -11,  -31,   47,  109,    0,
  114,    0,    0,   35,    0,  -78,  580,    0,    0,    0,
    0,   79,  145,   61,  137,  -69,  157,   -7,  102,    0,
    0,  165,  102,  102,  102,    2,    2,  102,  102,  102,
    0,  -45,  269, -194,    0,  -53, -111,    2,    2,  168,
    0,    0,   55,  -85,   15,  230,  256,  281,  275,    0,
    0,    0,   12,  596,    0,  -25,  302,  305,  -34,   -2,
    0,  102,  307,    0,    0,    7,  155,  121,    0,  137,
  137,  137,   68,   68,  137,  137,  137,  383,    0,   10,
 -194,  455,    0,    0,   88,  485,    0,    0,  238, -194,
  132,    0,    0,    0,  298,  350,    0,    0,  -85, -188,
  -85,  340,  274,   27,   19,    0,  282,  355,  147,  303,
  386,    0,  171,  173,   22,    0,    2,    2,    0,  504,
  510,    0,    0,  182,  526,  372,    0,    0,  -22,    0,
  532,    0,  -85,  388,    0,    0,    0,  -85,  394,  395,
   94,  331,   62, -194,    0, -194,  389,    0,  419,  423,
  123,  125,  408,  542,  409,    0,  410,    0,    0,  209,
  210,    0,  -85,    0,    0,  351,  411,  416,  418,  105,
    0,  548,  558,  612,    0,  399,    0,    0,    0,    0,
  421,    0,    0,    0,    0,    0,  424,  357,    0,  361,
  429,  432,    0,  441,  420,  564,  365,  391,  612,    0,
    0,    0,  452,  454,  453,    0,    0,  257,  458,  459,
  460,  574,  402,  412,    0,    0,  476,  469,    0,    0,
    0,    0,  480,  481,  482,    0,  483,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  543,    0,    0,    1,    0,  486,    0,    0,
    0,    0,    0,    0,  544,    0,    0,   33,    0,    0,
   49,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,  202,
    0,   71,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   50,   63,   64,    0,    0,    0,    0,
    0,  364,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  112,  489,    0,    0,    0,    0,
    0,    0,    0,    0,   41,    0,   32,    0,    0,    0,
    0,    0,    0,    0,    0,  141,    0,    0,    0,   84,
    0,    0,    0,    0,    0,    0,    0,  326,    0,  -15,
  140,  146,   93,  116,  164,  178,  188,    0,    0,    0,
    0,    0,  -49,    0,    0,    0,    0,    0,    0,    0,
  492,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  349,    0,    0,  258,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  345,  373,    0,    0,    0,    0,    0,  299,  309,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  320,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  433,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  403,  538,    0,    0,    0,    0,  -16,
  413,  304,  430,   -4,  -42,  353,  -44,    0,    0,  529,
    0,  551,  533,  499, -168, -185,  318,   24,    0,    0,
    0,  -62,    0,  -23,  207,  -54,
};
final static int YYTABLESIZE=889;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   10,  121,  121,  121,   46,  121,  160,  121,   93,   86,
   52,  140,   32,  116,   68,   48,   17,  107,   37,  121,
  121,  121,  121,   36,  141,  103,   94,  223,    1,   91,
   40,   99,    8,  115,   56,   71,  209,   36,  247,   36,
  132,  136,  163,    4,   10,  118,   36,  166,   14,   55,
  165,  154,  123,  124,  174,  246,   49,   84,  193,   10,
   72,    5,   82,  263,  161,    7,   36,  190,   92,    8,
  153,   36,   22,   79,   10,   39,  146,  192,   56,   30,
  262,   26,   13,  121,   26,   11,   12,   98,  175,   22,
  121,  121,  121,  101,  121,  145,  121,  181,   26,  156,
   68,   53,  219,  106,  108,   57,   36,  103,  121,   88,
  200,  114,   59,  114,   89,  114,  210,   60,   32,  199,
   36,  106,  108,   36,  111,   10,   62,  204,   61,  114,
  114,  114,  114,  112,  217,  112,   76,  112,   77,   17,
   10,   17,  111,  201,  202,  241,   36,   76,   68,   77,
  139,  112,  112,  112,  112,   70,  113,    8,  113,   69,
  113,  169,   88,  228,   88,  229,   88,   89,   81,   89,
   88,   89,  100,   14,  113,  113,  113,  113,  102,   76,
  104,   77,  121,  121,  109,  121,  105,  121,   11,   12,
    5,   82,  110,  114,    7,   83,  112,  167,    8,  168,
  121,  121,  121,   10,  100,  119,   39,   39,  144,  138,
   39,   13,   79,   86,   39,  112,  121,  121,  101,   39,
  121,  121,  159,   45,  121,   92,   31,   39,  102,  121,
  121,  121,   47,  121,   82,   33,   34,  121,  113,  128,
  103,  103,   11,   12,  103,  103,   31,   90,  103,  114,
   34,   33,   34,  103,   35,  162,   10,   10,   33,   34,
   10,  103,  104,  156,   10,   10,   10,  173,  105,   10,
   35,  148,   17,   17,   10,   10,   17,   10,  114,   34,
   17,   17,   17,   33,   34,   17,  100,  149,    8,    8,
   17,   17,    8,   17,  142,  143,    8,    8,    8,  150,
  101,    8,   35,   92,   14,   14,    8,    8,   14,    8,
  102,   92,   14,   14,   14,  152,   89,   14,   33,   34,
   11,   12,   14,   14,   98,   14,  114,  114,   11,   12,
  114,  114,   64,   34,  114,  106,   34,   35,  151,  114,
  114,  114,  157,  114,   10,  158,  164,  114,  112,  112,
  178,   35,  112,  112,   35,  183,  112,   90,   33,   34,
  170,  112,  112,  112,   76,  112,   77,   87,  114,  112,
  114,  113,  113,   82,   88,  113,  113,   35,   85,  113,
   83,   79,   80,   78,  113,  113,  113,  112,  113,  112,
  184,  131,  113,   85,  182,  104,  104,  188,  189,  104,
  104,  105,  105,  104,  194,  105,  105,   92,  104,  105,
  121,  121,  195,  121,  105,  113,  104,  113,   31,  100,
  100,   84,  105,  100,  100,  196,  197,  100,  198,  159,
  208,   81,  100,  101,  101,  133,  137,  101,  101,  206,
  100,  101,   78,  102,  102,  213,  101,  102,  102,   95,
   95,  102,  215,  216,  101,  218,  102,   98,   98,  226,
   65,   98,   98,  227,  102,   98,  230,  232,  233,  238,
   98,  234,  235,   39,  239,  237,  240,  258,   98,  251,
  172,  253,  252,  137,  177,  254,   97,  255,  177,  260,
  256,  172,  137,    5,   82,   95,  147,    7,  117,  257,
  180,    8,  120,  121,  122,  170,   10,  125,  126,  127,
  265,  224,  266,  269,   13,  261,  270,  271,  272,   83,
   89,  249,  137,  177,    5,   82,  274,  177,    7,   14,
  128,  129,    8,  177,  276,  130,  275,   10,  278,  279,
  280,  281,    2,    3,  131,   13,  221,   76,  221,  225,
   77,  185,   95,  187,   54,   51,  177,   65,  268,   66,
   84,   90,  186,   50,   67,  113,  264,    0,    0,    0,
   81,   87,    0,    0,  243,  243,  221,  267,  250,  176,
    0,   78,   85,  191,    0,  212,    0,    0,    0,    0,
  214,   73,   74,  277,   75,    0,    0,    0,  243,    0,
    0,  221,    0,    0,   39,   39,    0,    0,   39,  179,
    0,   92,   39,    0,  243,  236,    0,   39,    0,   97,
   97,    0,  220,   97,   97,   39,    0,   97,  203,    0,
    0,    0,   97,    0,  205,    0,    0,    0,    5,   82,
   97,    0,    7,    0,    5,   82,    8,    0,    7,    0,
  207,   10,    8,    0,    5,   82,  211,   10,    7,   13,
    5,    6,    8,    0,    7,   13,  231,   10,    8,    9,
    0,    0,  242,   10,    0,   13,    0,    0,   11,   12,
    0,   13,  244,    0,    0,    0,    5,    6,  259,    0,
    7,    0,    0,    0,    8,    9,    0,    0,  273,   10,
    0,    0,    0,    0,   11,   12,    0,   13,    5,    6,
    5,   82,    7,    0,    7,    0,    8,    9,    8,    0,
    0,   10,    0,   10,    5,    6,   11,   12,    7,   13,
    0,   13,    8,    9,    0,    0,    0,   10,    0,    0,
    5,   82,   11,   12,    7,   13,    0,    0,    8,    0,
    0,    0,    0,   10,    0,    0,    0,    0,    0,    5,
   82,   13,    0,    7,    0,    5,   82,    8,    0,    7,
    0,    0,   10,    8,    0,    0,    0,    0,   10,    0,
   13,    5,   82,    0,    0,    7,   13,    5,   82,    8,
    0,    7,    0,    0,   10,    8,    0,    5,   82,    0,
   10,    7,   13,    5,   82,    8,    0,    7,   13,    0,
   10,    8,    0,    5,   82,    0,   10,    7,   13,    5,
   82,    8,    0,    7,   13,    0,   10,    8,    0,    5,
   82,    0,   10,    7,   13,    5,    6,    8,    0,    7,
   13,    0,   10,    8,    9,  103,    0,    0,   10,    0,
   13,    5,    6,   11,   12,    7,   13,    0,    0,    8,
    9,  155,    0,    0,   10,    0,    0,    5,   82,   11,
   12,    7,   13,    0,    0,    8,    0,    0,  130,  245,
   10,    0,    0,    0,    0,    0,    0,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    0,   41,   42,   43,   40,   45,   41,   47,   40,   59,
   15,  123,   58,   68,   40,   40,    0,   62,   40,   59,
   60,   61,   62,   45,   87,   41,   58,  196,  257,   41,
    7,   48,    0,   41,   44,   41,   59,   45,  224,   45,
   83,   84,   45,  123,   44,   69,   45,   41,    0,   59,
   44,   40,   76,   77,   45,  224,  257,  123,   40,   59,
   37,  256,  257,  249,  109,  260,   45,   41,  257,  264,
   59,   45,   41,  123,  269,  125,   93,   59,   44,   59,
  249,   41,  277,  123,   44,  274,  275,   41,  131,   58,
   41,   42,   43,   59,   45,   41,   47,  140,   58,  104,
   40,  257,   41,   41,   41,  123,   45,  123,   59,   42,
  165,   41,   59,   43,   47,   45,  179,   59,   58,  164,
   45,   59,   59,   45,   41,  125,   40,  170,   59,   59,
   60,   61,   62,   41,   41,   43,   43,   45,   45,  123,
  269,  125,   59,  167,  168,   41,   45,   43,   40,   45,
  262,   59,   60,   61,   62,  258,   41,  125,   43,   40,
   45,   41,   42,   41,   42,   41,   42,   47,   41,   47,
   59,   47,   59,  125,   59,   60,   61,   62,  257,   43,
   41,   45,   42,   43,   40,   45,   41,   47,  274,  275,
  256,  257,  262,  123,  260,  261,   40,   43,  264,   45,
   60,   61,   62,  269,   41,   41,  256,  257,   41,  263,
  260,  277,  262,  263,  264,  123,  256,  257,   41,  269,
  260,  261,  257,  259,  264,  257,  272,  277,   41,  269,
  270,  271,  257,  273,  123,  257,  258,  277,  123,  262,
  256,  257,  274,  275,  260,  261,  272,  259,  264,  257,
  258,  257,  258,  269,  276,  258,  256,  257,  257,  258,
  260,  277,  123,  268,  264,  265,  266,  258,  123,  269,
  276,  257,  256,  257,  274,  275,  260,  277,  257,  258,
  264,  265,  266,  257,  258,  269,  123,   58,  256,  257,
  274,  275,  260,  277,   88,   89,  264,  265,  266,   44,
  123,  269,  276,  257,  256,  257,  274,  275,  260,  277,
  123,  257,  264,  265,  266,   41,   59,  269,  257,  258,
  274,  275,  274,  275,  123,  277,  256,  257,  274,  275,
  260,  261,  257,  258,  264,  257,  258,  276,   58,  269,
  270,  271,   41,  273,  269,   41,   40,  277,  256,  257,
  263,  276,  260,  261,  276,   58,  264,   59,  257,  258,
  123,  269,  270,  271,   43,  273,   45,   59,   43,  277,
   45,  256,  257,  262,  263,  260,  261,  276,   59,  264,
  123,   60,   61,   62,  269,  270,  271,   43,  273,   45,
   41,  123,  277,   41,  263,  256,  257,   58,  125,  260,
  261,  256,  257,  264,  123,  260,  261,   59,  269,  264,
  270,  271,   58,  273,  269,   43,  277,   45,  272,  256,
  257,  123,  277,  260,  261,  123,   41,  264,  258,  257,
   59,  123,  269,  256,  257,   83,   84,  260,  261,  258,
  277,  264,  123,  256,  257,   58,  269,  260,  261,   47,
   48,  264,   59,   59,  277,  125,  269,  256,  257,   41,
   31,  260,  261,   41,  277,  264,   59,   59,   59,   59,
  269,  263,  263,  125,   59,  125,   59,   58,  277,   59,
  128,  125,   59,  131,  132,  125,  123,   59,  136,  125,
   59,  139,  140,  256,  257,   93,   94,  260,   69,   59,
  263,  264,   73,   74,   75,  123,  269,   78,   79,   80,
   59,  123,   59,  257,  277,  125,   59,   59,   59,  262,
  263,  123,  170,  171,  256,  257,  125,  175,  260,  125,
  262,  263,  264,  181,   59,  267,  125,  269,   59,   59,
   59,   59,    0,    0,   59,  277,  194,   59,  196,  197,
   59,  149,  150,  151,   17,  125,  204,  125,  255,   31,
  262,  263,  150,   13,   32,   67,  249,   -1,   -1,   -1,
  262,  263,   -1,   -1,  222,  223,  224,  125,  226,  125,
   -1,  262,  263,  154,   -1,  183,   -1,   -1,   -1,   -1,
  188,  270,  271,  125,  273,   -1,   -1,   -1,  246,   -1,
   -1,  249,   -1,   -1,  256,  257,   -1,   -1,  260,  125,
   -1,  263,  264,   -1,  262,  213,   -1,  269,   -1,  256,
  257,   -1,  193,  260,  261,  277,   -1,  264,  125,   -1,
   -1,   -1,  269,   -1,  125,   -1,   -1,   -1,  256,  257,
  277,   -1,  260,   -1,  256,  257,  264,   -1,  260,   -1,
  125,  269,  264,   -1,  256,  257,  125,  269,  260,  277,
  256,  257,  264,   -1,  260,  277,  125,  269,  264,  265,
   -1,   -1,  125,  269,   -1,  277,   -1,   -1,  274,  275,
   -1,  277,  125,   -1,   -1,   -1,  256,  257,  125,   -1,
  260,   -1,   -1,   -1,  264,  265,   -1,   -1,  125,  269,
   -1,   -1,   -1,   -1,  274,  275,   -1,  277,  256,  257,
  256,  257,  260,   -1,  260,   -1,  264,  265,  264,   -1,
   -1,  269,   -1,  269,  256,  257,  274,  275,  260,  277,
   -1,  277,  264,  265,   -1,   -1,   -1,  269,   -1,   -1,
  256,  257,  274,  275,  260,  277,   -1,   -1,  264,   -1,
   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,   -1,  256,
  257,  277,   -1,  260,   -1,  256,  257,  264,   -1,  260,
   -1,   -1,  269,  264,   -1,   -1,   -1,   -1,  269,   -1,
  277,  256,  257,   -1,   -1,  260,  277,  256,  257,  264,
   -1,  260,   -1,   -1,  269,  264,   -1,  256,  257,   -1,
  269,  260,  277,  256,  257,  264,   -1,  260,  277,   -1,
  269,  264,   -1,  256,  257,   -1,  269,  260,  277,  256,
  257,  264,   -1,  260,  277,   -1,  269,  264,   -1,  256,
  257,   -1,  269,  260,  277,  256,  257,  264,   -1,  260,
  277,   -1,  269,  264,  265,  266,   -1,   -1,  269,   -1,
  277,  256,  257,  274,  275,  260,  277,   -1,   -1,  264,
  265,  266,   -1,   -1,  269,   -1,   -1,  256,  257,  274,
  275,  260,  277,   -1,   -1,  264,   -1,   -1,  267,  268,
  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  277,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","CADENA","IF","THEN","ELSE",
"ENDIF","OUT","FUN","RETURN","BREAK","CONTINUE","WHILE","MAYOR_IGUAL",
"MENOR_IGUAL","ASIGNACION","DISTINTO","I32","F32","TOF32","DISCARD",
};
final static String yyrule[] = {
"$accept : program",
"program : header_program '{' ejecucion '}' ';'",
"program : header_program",
"program : header_program '{' ejecucion",
"program : header_program '{' '}'",
"header_program : ID",
"declaracion_variables : tipo lista_variables ';'",
"declaracion_variables : lista_variables ';'",
"declaracion_variables : lista_variables",
"lista_variables : lista_variables ',' ID",
"lista_variables : ID",
"sentencia_declarable : declaracion_variables",
"sentencia_declarable : funcion",
"funcion : header_funcion ejecucion_funcion",
"funcion : header_funcion",
"header_funcion : FUN ID '(' lista_parametros ')' ':' tipo",
"header_funcion : FUN ID '(' ')' ':' tipo",
"header_funcion : FUN ID '(' lista_parametros ')'",
"header_funcion : FUN '(' lista_parametros ')' ':' tipo",
"header_funcion : FUN '(' ')' ':' tipo",
"header_funcion : FUN ID lista_parametros ':' tipo",
"header_funcion : FUN ID ':' tipo",
"lista_parametros : parametro",
"lista_parametros : parametro ',' parametro",
"parametro : tipo ID",
"parametro : ID",
"parametro : tipo",
"tipo : I32",
"tipo : F32",
"ejecucion_funcion : '{' bloque_funcion RETURN '(' expresion ')' ';' '}' ';'",
"ejecucion_funcion : '{' RETURN '(' expresion ')' ';' '}' ';'",
"ejecucion_funcion : '{' bloque_funcion RETURN '(' expresion ')' ';' bloque_funcion '}' ';'",
"ejecucion_funcion : '{' bloque_funcion RETURN ';' '}' ';'",
"ejecucion_funcion : '{' RETURN ';' '}' ';'",
"ejecucion_funcion : '{' bloque_funcion RETURN '(' ')' ';' '}' ';'",
"ejecucion_funcion : '{' RETURN '(' ')' ';' '}' ';'",
"bloque_funcion : bloque_funcion sentencia",
"bloque_funcion : sentencia",
"ejecucion_control : ejecucion_control sentencia_ejecutable",
"ejecucion_control : sentencia_ejecutable",
"ejecucion : ejecucion sentencia",
"ejecucion : sentencia",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_declarable",
"sentencia_ejecutable : asignacion ';'",
"sentencia_ejecutable : seleccion ';'",
"sentencia_ejecutable : impresion ';'",
"sentencia_ejecutable : iteracion_while",
"sentencia_ejecutable : invocacion_con_d",
"sentencia_ejecutable : error ';'",
"invocacion_con_d : DISCARD invocacion",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' break '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' continue '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' break '}' ';'",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' continue '}' ';'",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : inicio_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' ')' '{' ejecucion_iteracion '}' ';'",
"inicio_while : WHILE",
"condicion_salto_while : '(' comparacion_bool ')' ':'",
"ejecucion_iteracion : ejecucion_iteracion sentencia_ejecutable",
"ejecucion_iteracion : sentencia_ejecutable",
"continue : CONTINUE",
"continue : CONTINUE ':' ID",
"break : BREAK CTE",
"break : BREAK '-' CTE",
"seleccion : IF condicion_salto_if then_seleccion_sin_else ENDIF",
"seleccion : IF condicion_salto_if then_seleccion else_seleccion ENDIF",
"seleccion : IF condicion_salto_if '{' ejecucion_control '}' else_seleccion ENDIF",
"seleccion : IF condicion_salto_if then_seleccion '{' ejecucion_control '}' ENDIF",
"seleccion : IF condicion_salto_if THEN ENDIF",
"seleccion : IF condicion_salto_if then_seleccion ELSE ENDIF",
"seleccion : IF condicion_salto_if THEN else_seleccion ENDIF",
"seleccion : IF condicion_salto_if then_seleccion_sin_else",
"seleccion : IF condicion_salto_if then_seleccion else_seleccion",
"then_seleccion : THEN '{' ejecucion_control '}' ';'",
"then_seleccion : THEN sentencia_ejecutable",
"then_seleccion : THEN break",
"then_seleccion : '{' ejecucion_control '}' ';'",
"then_seleccion : sentencia_ejecutable",
"then_seleccion : THEN '{' ejecucion_control",
"then_seleccion : THEN ejecucion_control '}' ';'",
"then_seleccion_sin_else : THEN '{' ejecucion_control '}' ';'",
"then_seleccion_sin_else : THEN sentencia_ejecutable",
"then_seleccion_sin_else : '{' ejecucion_control '}' ';'",
"then_seleccion_sin_else : sentencia_ejecutable",
"then_seleccion_sin_else : THEN '{' ejecucion_control",
"then_seleccion_sin_else : THEN ejecucion_control '}' ';'",
"else_seleccion : ELSE '{' ejecucion_control '}' ';'",
"else_seleccion : ELSE sentencia_ejecutable",
"else_seleccion : ELSE '{' '}' ';'",
"else_seleccion : ELSE ejecucion_control '}' ';'",
"condicion_salto_if : '(' comparacion_bool ')'",
"condicion_salto_if : comparacion_bool ')'",
"condicion_salto_if : '(' comparacion_bool",
"condicion_salto_if : comparacion_bool",
"condicion_salto_if : '(' ')'",
"comparacion_bool : expresion '>' expresion",
"comparacion_bool : expresion '<' expresion",
"comparacion_bool : expresion '=' expresion",
"comparacion_bool : expresion MAYOR_IGUAL expresion",
"comparacion_bool : expresion MENOR_IGUAL expresion",
"comparacion_bool : expresion DISTINTO expresion",
"asignacion : ID ASIGNACION expresion",
"asignacion : ID ASIGNACION iteracion_while else_asignacion_iteracion",
"asignacion : ID ASIGNACION iteracion_while",
"else_asignacion_iteracion : ELSE CTE",
"else_asignacion_iteracion : ELSE '-' CTE",
"else_asignacion_iteracion : ELSE",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : TOF32 '(' expresion '+' termino ')'",
"expresion : TOF32 '(' expresion '-' termino ')'",
"expresion : TOF32 '(' termino ')'",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"combinacion_terminales : ID",
"combinacion_terminales : CTE",
"combinacion_terminales : '-' CTE",
"invocacion : ID '(' combinacion_terminales ',' combinacion_terminales ')'",
"invocacion : ID '(' combinacion_terminales ')'",
"invocacion : ID '(' ')'",
"factor : combinacion_terminales",
"factor : invocacion",
"impresion : OUT '(' CADENA ')'",
"impresion : OUT '(' ')'",
"impresion : OUT",
"impresion : OUT CADENA",
};

//#line 676 "gramatica(copia).y"

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
//#line 842 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 24 "gramatica(copia).y"
{addEstructura("programa");}
break;
case 2:
//#line 25 "gramatica(copia).y"
{addEstructura("programa sin ejecucion");}
break;
case 3:
//#line 27 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba un '}' al final del programa");}
break;
case 4:
//#line 28 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba una sentencia de ejecucion");}
break;
case 5:
//#line 31 "gramatica(copia).y"
{cambiarAmbito(val_peek(0).sval);
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
					}
break;
case 6:
//#line 40 "gramatica(copia).y"
{
					asignarTipos(val_peek(2).sval);
					}
break;
case 7:
//#line 44 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
break;
case 8:
//#line 45 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
break;
case 9:
//#line 48 "gramatica(copia).y"
{ 
				var_aux.add(val_peek(0).sval);
                }
break;
case 10:
//#line 51 "gramatica(copia).y"
{ 
        		var_aux.add(val_peek(0).sval);
               }
break;
case 11:
//#line 56 "gramatica(copia).y"
{addEstructura("declaracion variables");
											 }
break;
case 12:
//#line 58 "gramatica(copia).y"
{addEstructura("declaracion funcion");
        		   }
break;
case 13:
//#line 62 "gramatica(copia).y"
{agregarToken(nombreFuncion());
										   salirAmbito();
										   agregarToken("\\ENDP"); }
break;
case 14:
//#line 65 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una ejecucion_funcion");}
break;
case 15:
//#line 69 "gramatica(copia).y"
{						
						if (TablaSimbolos.obtenerSimbolo(val_peek(5).sval+ Parser.ambito.toString()) == null) {
                			TablaSimbolos.modifySimbolo(val_peek(5).sval, val_peek(5).sval + Parser.ambito.toString());
                			String ptr1 = chequeoAmbito(val_peek(5).sval + Parser.ambito.toString());
                			TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
                			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
							cambiarAmbito(val_peek(5).sval);                			
							procesarParametros(ptr1);
                			agregarToken("!" + nombreFuncion().replace(':', '/'));
            			} else {
                			agregarError(errores_semanticos,"Error","Redeclaraion de la funcion " + val_peek(5).sval+ Parser.ambito.toString());
            			}
						}
break;
case 16:
//#line 83 "gramatica(copia).y"
{						
						if (TablaSimbolos.obtenerSimbolo(val_peek(4).sval+ Parser.ambito.toString()) == null) {
                			TablaSimbolos.modifySimbolo(val_peek(4).sval, val_peek(4).sval + Parser.ambito.toString());
                			String ptr1 = chequeoAmbito(val_peek(4).sval + Parser.ambito.toString());
                			TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
                			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
							cambiarAmbito(val_peek(4).sval);                			
                			agregarToken("!" + nombreFuncion().replace(':', '/'));
            			} else {
                			agregarError(errores_semanticos,"Error","Redeclaraion de la funcion " + val_peek(4).sval+ Parser.ambito.toString());
            			}
						}
break;
case 17:
//#line 98 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 18:
//#line 99 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 19:
//#line 100 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 101 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 21:
//#line 102 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 24:
//#line 110 "gramatica(copia).y"
{    
           		String ptr1 = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
            	TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(1).sval);
            	TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de parametro");
            	par_aux.add(ptr1);
                   }
break;
case 25:
//#line 117 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 26:
//#line 118 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 29:
//#line 126 "gramatica(copia).y"
{
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
break;
case 30:
//#line 131 "gramatica(copia).y"
{
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
break;
case 31:
//#line 136 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 32:
//#line 137 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 33:
//#line 138 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 34:
//#line 139 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 35:
//#line 140 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 45:
//#line 161 "gramatica(copia).y"
{addEstructura("if");}
break;
case 46:
//#line 162 "gramatica(copia).y"
{addEstructura("impresion");}
break;
case 47:
//#line 163 "gramatica(copia).y"
{addEstructura("while");}
break;
case 48:
//#line 164 "gramatica(copia).y"
{addEstructura("invocacion con discard");}
break;
case 49:
//#line 165 "gramatica(copia).y"
{addEstructura("error");}
break;
case 51:
//#line 172 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
break;
case 52:
//#line 180 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
break;
case 53:
//#line 188 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
break;
case 54:
//#line 196 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
break;
case 55:
//#line 204 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));	
																												agregarToken("#BI");	
																												}
break;
case 56:
//#line 212 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
break;
case 57:
//#line 220 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
break;
case 58:
//#line 228 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
break;
case 59:
//#line 237 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool ");}
break;
case 60:
//#line 238 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 61:
//#line 241 "gramatica(copia).y"
{ 
					/*APILAR PASO INICIAL*/
					apilar(); }
break;
case 62:
//#line 246 "gramatica(copia).y"
{ 
					/*GENERO BF INCOMPLETA Y APILO PASO INCOMPLETO*/
					apilar();
					agregarToken("SI");	
					agregarToken("#BF");			 
					}
break;
case 66:
//#line 259 "gramatica(copia).y"
{
					String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
					}
break;
case 67:
//#line 264 "gramatica(copia).y"
{TablaSimbolos.agregarAtributo("asignacion while", "break", val_peek(0).sval);}
break;
case 68:
//#line 265 "gramatica(copia).y"
{TablaSimbolos.agregarAtributo("asignacion while", "break", "-"+val_peek(0).sval);}
break;
case 69:
//#line 268 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 70:
//#line 272 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
									}
break;
case 71:
//#line 276 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 72:
//#line 277 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 73:
//#line 278 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 74:
//#line 279 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 75:
//#line 280 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 76:
//#line 281 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 77:
//#line 282 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 78:
//#line 285 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("#BI");
								}
break;
case 79:
//#line 292 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 80:
//#line 299 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 81:
//#line 306 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 82:
//#line 307 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 83:
//#line 308 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 84:
//#line 309 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 87:
//#line 315 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 88:
//#line 316 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 89:
//#line 317 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 90:
//#line 318 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 93:
//#line 324 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 94:
//#line 325 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 95:
//#line 328 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("#BF");			 
								}
break;
case 96:
//#line 334 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 97:
//#line 335 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 98:
//#line 336 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 99:
//#line 337 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 100:
//#line 341 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
									agregarToken(">");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");*/
        						/*}*/
        						}
break;
case 101:
//#line 353 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
									agregarToken("<");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");*/
        						/*}*/
        						}
break;
case 102:
//#line 364 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
									agregarToken("=");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");*/
        						/*}*/
        						}
break;
case 103:
//#line 375 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
									agregarToken(">=");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");*/
        						/*}*/
        						}
break;
case 104:
//#line 386 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
									agregarToken("<=");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");*/
        						/*}*/
        						}
break;
case 105:
//#line 397 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
									agregarToken("=!");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");*/
        						/*}*/
        						}
break;
case 106:
//#line 421 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
 						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        				if (TablaSimbolos.obtenerAtributo(ptr1,"uso") == "constante") {
            				agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Se esta queriendo modificar la constante " + ptr1);
        				} else {
							agregarToken(ptr1); 
            				agregarToken("=:");
        				}
                        }
break;
case 107:
//#line 431 "gramatica(copia).y"
{
						addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);
						String ptr1 = chequeoAmbito(val_peek(3).sval + Parser.ambito.toString());
						if (TablaSimbolos.obtenerSimbolo("asignacion while") == null){
							TablaSimbolos.agregarSimbolo("asignacion while", new Lexema("asignacion while"));
						}
						if((!TablaSimbolos.obtenerAtributo(ptr1, "tipo").equals(TablaSimbolos.obtenerAtributo("asignacion while", "break"))) || (!TablaSimbolos.obtenerAtributo(ptr1, "tipo").equals(TablaSimbolos.obtenerAtributo("asignacion while", "else")))) {
							{agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Tipos incompatibles en la asignacion por while");}
						}
						}
break;
case 108:
//#line 442 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 109:
//#line 448 "gramatica(copia).y"
{TablaSimbolos.agregarAtributo("asignacion while", "else", val_peek(0).sval);}
break;
case 110:
//#line 449 "gramatica(copia).y"
{TablaSimbolos.agregarAtributo("asignacion while", "else", "-"+val_peek(0).sval);}
break;
case 111:
//#line 451 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
break;
case 112:
//#line 454 "gramatica(copia).y"
{
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        						String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        						TablaSimbolos.agregarSimbolo(val_peek(2).sval+"+"+val_peek(0).sval, new Lexema(val_peek(2).sval+"+"+val_peek(0).sval));
       							String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"+"+val_peek(0).sval);
        						TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            						TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
									agregarToken("+");
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la suma.");*/
        						/*}*/
								}
break;
case 113:
//#line 468 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					TablaSimbolos.agregarSimbolo(val_peek(2).sval+"-"+val_peek(0).sval, new Lexema(val_peek(2).sval+"-"+val_peek(0).sval));
       						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"-"+val_peek(0).sval);
        					TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            					TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
								agregarToken("-");
        					/*} else {*/
            				/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la resta.");*/
        					/*}*/
    						}
break;
case 115:
//#line 484 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(3).sval);
    						String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
    						
    						TablaSimbolos.agregarSimbolo(val_peek(3).sval+"+"+val_peek(1).sval, new Lexema(val_peek(3).sval+"+"+val_peek(1).sval));
    						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(3).sval+"+"+val_peek(1).sval);
    						TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
    						TablaSimbolos.agregarAtributo(ptr3, "tipo", TablaTipos.FLOAT_TYPE);
    						
    						agregarToken("+");
    						}
break;
case 116:
//#line 495 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(3).sval);
    						String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
    						
    						TablaSimbolos.agregarSimbolo(val_peek(3).sval+"-"+val_peek(1).sval, new Lexema(val_peek(3).sval+"-"+val_peek(1).sval));
    						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(3).sval+"-"+val_peek(1).sval);
    						TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
    						TablaSimbolos.agregarAtributo(ptr3, "tipo", TablaTipos.FLOAT_TYPE);
    						
    						agregarToken("-");
    						}
break;
case 117:
//#line 506 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
    						TablaSimbolos.agregarAtributo(ptr1, "tipo", TablaTipos.FLOAT_TYPE);
    						}
break;
case 118:
//#line 512 "gramatica(copia).y"
{
							String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					TablaSimbolos.agregarSimbolo(val_peek(2).sval+"*"+val_peek(0).sval, new Lexema(val_peek(2).sval+"*"+val_peek(0).sval));
       						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"*"+val_peek(0).sval);
        					TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            					TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
								agregarToken("*");
        					/*} else {*/
            				/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la multiplicacion.");*/
        					/*}*/
							}
break;
case 119:
//#line 526 "gramatica(copia).y"
{
    					String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        				String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        				TablaSimbolos.agregarSimbolo(val_peek(2).sval+"/"+val_peek(0).sval, new Lexema(val_peek(2).sval+"/"+val_peek(0).sval));
       					String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"/"+val_peek(0).sval);
        				TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        				/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            				TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
							agregarToken("/");
        				/*} else {*/
            			/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la division.");*/
        				/*}*/
    					}
break;
case 121:
//#line 543 "gramatica(copia).y"
{
			String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
			if (ptr != null) {
				agregarToken(ptr);
            } else {
            	agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Variable " + val_peek(0).sval + "no encontrada");
            }
            }
break;
case 122:
//#line 552 "gramatica(copia).y"
{
    		String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
			agregarToken(ptr);
            }
break;
case 123:
//#line 558 "gramatica(copia).y"
{
 			String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
    		String simb = negarConstante(ptr);
    		agregarToken(simb);
    		}
break;
case 124:
//#line 567 "gramatica(copia).y"
{ 
							String ptr1 = chequeoAmbito(val_peek(5).sval + Parser.ambito.toString());
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(3).sval);
        					String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
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
break;
case 125:
//#line 602 "gramatica(copia).y"
{ 
    						String ptr1 = chequeoAmbito(val_peek(3).sval + Parser.ambito.toString());
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
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
break;
case 126:
//#line 629 "gramatica(copia).y"
{ 
    
    						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        					if (ptr1 != null) {
        						boolean esFuncion = TablaSimbolos.obtenerAtributo(ptr1,"uso").equals("nombre de funcion");
        						if (esFuncion) {
            						boolean cantidadParametrosCorrectos = TablaSimbolos.obtenerAtributo(ptr1,"cantidad_parametros").equals("0");
            						if (cantidadParametrosCorrectos) {
                						agregarToken(ptr1);
										agregarToken("#CALL");
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
break;
case 129:
//#line 659 "gramatica(copia).y"
{       
                                String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                TablaSimbolos.agregarSimbolo(nombre, new Lexema(nombre));
                                String simb = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(simb, "valor", val_peek(1).sval);
                                TablaSimbolos.agregarAtributo(simb, "tipo", "String");
                                agregarToken(nombre);    /*agregamos a la polaca el simbolo, junto identificador de cadenas*/
                                contador_cadenas++; 
                                }
break;
case 130:
//#line 669 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 131:
//#line 670 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 132:
//#line 671 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1808 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
