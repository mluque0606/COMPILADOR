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
   20,   20,   23,   24,   25,   25,   27,   27,   26,   26,
   18,   18,   18,   18,   18,   18,   18,   18,   18,   31,
   31,   31,   31,   31,   31,   31,   31,   30,   30,   30,
   30,   30,   30,   30,   30,   32,   32,   32,   32,   29,
   29,   29,   29,   29,   28,   28,   28,   28,   28,   28,
   17,   17,   17,   33,   33,   33,   13,   13,   13,   13,
   13,   13,   34,   34,   34,   36,   36,   36,   22,   22,
   22,   35,   35,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    5,    1,    3,    3,    1,    3,    2,    1,    3,    1,
    1,    1,    2,    1,    7,    6,    5,    6,    5,    5,
    4,    1,    3,    2,    1,    1,    1,    1,    9,    8,
   10,    6,    5,    8,    7,    2,    1,    2,    1,    2,
    1,    1,    1,    2,    2,    2,    1,    1,    2,    2,
    9,   10,    9,   10,    9,    6,   11,   12,   12,    8,
    8,    8,    1,    4,    2,    1,    2,    4,    3,    4,
    4,    5,    7,    7,    4,    5,    5,    3,    4,    5,
    2,    2,    6,    4,    1,    3,    4,    5,    2,    2,
    6,    4,    1,    3,    4,    5,    2,    4,    4,    3,
    2,    2,    1,    2,    3,    3,    3,    3,    3,    3,
    3,    4,    3,    2,    3,    1,    3,    3,    1,    6,
    6,    4,    3,    3,    1,    1,    1,    2,    6,    4,
    3,    1,    1,    4,    3,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,    0,    0,    0,    0,    0,   63,
   27,   28,    0,    4,    0,   11,    0,    0,   43,   12,
    0,   41,   42,    0,    0,    0,   47,   48,    0,   49,
    0,    0,    0,  127,    0,    0,    0,    0,  133,    0,
    0,    0,  125,  132,  137,    0,    0,    0,    0,   50,
    0,   40,   10,    0,    7,    0,    0,   13,   44,   45,
   46,    0,    0,    0,    0,    0,    0,    0,    0,  128,
  104,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  101,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  135,   25,    0,    0,    0,    0,    0,    0,    0,    1,
    6,    9,    0,    0,   37,    0,    0,    0,    0,    0,
  112,    0,    0,  126,  131,    0,    0,    0,  100,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   75,    0,
    0,    0,    0,    0,    0,    0,   39,   71,    0,    0,
    0,  123,  124,  134,    0,    0,   21,   24,    0,    0,
    0,    0,    0,    0,    0,   36,    0,    0,    0,    0,
    0,  114,    0,    0,    0,  130,    0,    0,  122,    0,
    0,    0,    0,    0,    0,    0,   38,   77,    0,   76,
    0,   72,    0,    0,   20,   23,   19,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,  115,    0,    0,
    0,    0,    0,    0,    0,   69,    0,    0,    0,    0,
    0,    0,    0,   16,    0,   18,   33,    0,    0,    0,
    0,    0,   66,    0,    0,    0,   56,    0,  129,  120,
  121,   98,    0,   99,   70,    0,    0,   73,   74,   15,
    0,    0,   32,    0,    0,    0,   65,    0,    0,    0,
    0,    0,    0,   60,   96,    0,   35,    0,    0,    0,
   61,   62,   67,    0,    0,    0,    0,    0,    0,    0,
   30,   34,    0,    0,    0,   51,    0,    0,   53,   55,
    0,    0,    0,   29,    0,   68,   52,   54,   57,    0,
    0,   31,   58,   59,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   58,   96,
   97,  104,   38,  105,  171,   23,   24,   25,   26,   27,
   28,   39,   29,   63,  224,  134,  252,  108,   41,   86,
   87,  135,  111,   42,   43,   44,
};
final static short yysindex[] = {                      -225,
    0,    0,  -70,  438,   18,  -44,  -20,  -34,  -40,    0,
    0,    0, -200,    0,  460,    0, -187,   20,    0,    0,
  -25,    0,    0,   31,   54,   59,    0,    0,   68,    0,
  146, -128,  108,    0,  116,  -86,   -5,  324,    0,  149,
  323,   58,    0,    0,    0,  -29,  -30,   55,  108,    0,
  145,    0,    0,   42,    0,  -51,  654,    0,    0,    0,
    0,  101,  170,   15,   79,  -50,  171,    2,  150,    0,
    0,  188,  150,  150,  150,   38,   38,  150,  150,  150,
    0,  -44,  -64, -206,    0,  -27, -105,   38,   38,  208,
    0,    0,   63,  -88,    5,  253,  273,  263,  284,    0,
    0,    0,   21,  670,    0,  -24,  293,  298,  -33,   -4,
    0,  150,  303,    0,    0,   37,   82,  103,    0,   79,
   79,   79,   58,   58,   79,   79,   79,  406,    0,   22,
 -206,  562,    0,    0,   83,  572,    0,    0,  370, -206,
   84,    0,    0,    0,  287,  319,    0,    0,  -88, -181,
  -88,  313,  236,   11,   28,    0,  249,  315,  104,  258,
  342,    0,  132,  135,   98,    0,   38,   38,    0,  586,
  596,    0,  340,  148,  543,  350,    0,    0,  -28,    0,
  602,    0,  -88,  356,    0,    0,    0,  -88,  362,  368,
  106,  305,   43, -206,    0, -206,  422,    0,  387,  390,
  134,  160,  373,  618,  375,    0,  376,  379,  314,    0,
    0,  179,  182,    0,  -88,    0,    0,  318,  388,  389,
  391,  119,    0,  629,  640,  686,    0,  424,    0,    0,
    0,    0,  392,    0,    0,    0,  395,    0,    0,    0,
  399,  339,    0,  344,  413,  414,    0,  419,  130,  515,
  355,  360, -206,    0,    0,    0,    0,  429,  435,  476,
    0,    0,    0,  224,  437,  372,  377,  441,  442,  529,
    0,    0,  446,  492,  452,    0,  454,  456,    0,    0,
  457,  396,  409,    0,  465,    0,    0,    0,    0,  466,
  471,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  512,    0,    0,    1,    0,  477,    0,    0,
    0,    0,    0,    0,  535,    0,    0,   33,    0,    0,
   49,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,    0,    0,    0,  381,
    0,   71,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  519,   48,   51,    0,    0,    0,    0,
    0,  400,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -48,  478,    0,    0,    0,    0,
    0,    0,    0,    0,   62,    0,    7,    0,    0,    0,
    0,    0,    0,    0,    0,  123,    0,    0,    0,   56,
    0,    0,    0,    0,    0,    0,    0,   92,    0,  -14,
  141,  156,   93,  118,  180,  199,  210,    0,    0,    0,
    0,    0,  352,  161,    0,    0,    0,    0,    0,    0,
  479,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  226,    0,    0,  229,    0,    0,    0,    0,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  124,  128,    0,    0,    0,    0,    0,    0,    0,  245,
  257,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  277,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  306,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  405,  448,    0,    0,    0,    0,  -19,
  393,  281,  453,   -2,  -49,  378,  -47,    0,    0,  511,
    0,  531,  514,  483, -154, -131, -204,   32,    0,    0,
    0,  -68,    0,  -39,  120,  -42,
};
final static int YYTABLESIZE=963;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   10,   22,  126,  126,  126,   46,  126,  160,  126,   93,
   93,   91,   52,   32,  107,   68,   17,  140,  141,   37,
  126,  126,  126,  126,   36,  116,  108,   94,   99,  118,
  211,    1,    8,  132,  136,   71,  123,  124,   40,   36,
  163,  225,  115,  209,   10,  267,   36,   22,   14,    5,
   82,  190,    4,    7,   68,   36,   49,    8,  131,   10,
  154,  161,   10,   56,   22,  283,  174,  193,   72,   53,
   13,  250,   32,  146,   85,   92,   30,  166,   55,  153,
  165,  175,   36,  221,  126,   56,  192,   36,  111,   59,
  181,  113,   11,   12,  251,   98,  116,   57,  270,   88,
  101,  156,   26,  145,   89,   26,  111,   62,  108,  113,
  212,  119,   60,  119,  116,  119,  199,   61,  266,   26,
  204,   76,  200,   77,  167,   10,  168,  201,  202,  119,
  119,  119,  119,  117,  119,  117,  119,  117,  282,   17,
   10,   17,   36,  169,   88,   36,  219,   68,   76,   89,
   77,  117,  117,  117,  117,   69,  139,    8,  118,  245,
  118,   76,  118,   77,  126,  126,  117,  126,  117,  126,
  118,   70,  118,   14,  230,   88,  118,  118,  118,  118,
   89,  109,  126,  126,  126,   11,   12,  264,  263,   81,
   36,    5,   82,  119,   36,    7,  110,  128,  129,    8,
  231,   88,  130,  100,   10,  102,   89,  142,  143,  109,
  112,  110,   13,   85,   93,  117,   47,  126,  126,   90,
  105,  126,  126,  159,   45,  126,   92,   31,  119,   90,
  126,  126,  126,  128,  126,  138,   33,   34,  126,  106,
  118,  108,  108,   11,   12,  108,  108,   31,  144,  108,
  107,   33,   34,  162,  108,   35,   10,   10,  114,   34,
   10,  148,  108,  109,   10,   10,   10,   33,   34,   10,
   35,  156,   17,   17,   10,   10,   17,   10,  110,  173,
   17,   17,   17,   82,   97,   17,   35,   94,    8,    8,
   17,   17,    8,   17,   33,   34,    8,    8,    8,   33,
   34,    8,  105,   95,   14,   14,    8,    8,   14,    8,
  149,   92,   14,   14,   14,   92,  150,   14,   35,   92,
  151,  106,   14,   14,  152,   14,  119,  119,   11,   12,
  119,  119,  107,  157,  119,   88,   11,   12,  158,  119,
  119,  119,  164,  119,  183,  178,  182,  119,  117,  117,
   39,   86,  117,  117,  114,   34,  117,  106,   34,  184,
  189,  117,  117,  117,   91,  117,   76,   87,   77,  117,
  188,  194,  195,  118,  118,   31,   35,  118,  118,   84,
  196,  118,  197,   79,   80,   78,  118,  118,  118,  198,
  118,  159,  126,  126,  118,  126,  109,  109,  206,   80,
  109,  109,   64,   34,  109,  207,   33,   34,  210,  109,
   89,  110,  110,  215,   10,  110,  110,  109,   85,  110,
  217,   35,   82,   90,  110,   35,  218,  228,   83,  220,
  229,  232,  110,  234,  235,  105,  105,  236,  237,  105,
  105,  238,  241,  105,  239,   84,  242,  243,  105,  244,
  255,   95,   95,  256,  106,  106,  105,  257,  106,  106,
  133,  137,  106,  258,   54,  107,  107,  106,  259,  107,
  107,  260,  261,  107,   81,  106,   39,  262,  107,  268,
  275,   39,   39,   65,  269,   39,  107,  271,   97,   39,
   86,   94,  170,  272,   39,  276,  277,   95,  147,  279,
  280,  278,   39,  103,  284,  172,   87,   95,  137,  177,
  286,    2,  287,  177,  288,  289,  172,  137,   84,   92,
  290,  117,  102,  292,  293,  120,  121,  122,  170,  294,
  125,  126,  127,  291,    3,  136,   78,   79,   80,   88,
  274,   66,  186,   50,  226,   67,  253,  137,  177,  113,
    0,    0,  177,  185,   95,  187,    0,    0,  177,  126,
  126,  126,   14,  126,    0,  126,    0,   83,   91,    0,
    0,  223,    0,  223,  227,    0,    0,  126,    5,   82,
    0,  177,    7,   83,   51,    0,    8,  214,    0,    0,
    0,   10,  216,   73,   74,    0,   75,    0,    0,   13,
  273,  247,  247,  223,    0,  254,  191,   39,   39,    0,
    0,   39,    0,   81,   89,   39,  285,    0,    0,  240,
   39,    0,    0,    0,    0,    5,   82,  247,   39,    7,
  223,    0,  180,    8,    0,    0,  103,  103,   10,  265,
  103,  103,    0,    0,  103,  222,   13,  247,    0,  103,
    0,    0,    0,  281,    0,  102,  102,  103,    0,  102,
  102,    5,   82,  102,    0,    7,    0,  208,  102,    8,
    0,    0,    0,    0,   10,    0,  102,    5,   82,    5,
   82,    7,   13,    7,    0,    8,  176,    8,    0,    0,
   10,    0,   10,    5,    6,    0,  179,    7,   13,    0,
   13,    8,    9,    0,    0,    0,   10,    0,    0,    0,
  203,   11,   12,    0,   13,    5,    6,    0,    0,    7,
  205,    0,    0,    8,    9,    0,  213,    0,   10,    0,
    0,    5,    6,   11,   12,    7,   13,    0,    0,    8,
    9,    0,  233,    0,   10,    0,    0,    5,    6,   11,
   12,    7,   13,  246,    0,    8,    9,    0,    0,    0,
   10,    0,    0,    0,  248,   11,   12,    0,   13,    0,
    5,   82,    0,    0,    7,    0,    0,    0,    8,    0,
    0,  130,  249,   10,    5,   82,    0,    0,    7,    0,
    0,   13,    8,    0,    0,  130,  249,   10,    5,   82,
    0,    0,    7,    0,    0,   13,    8,    0,    0,  130,
    0,   10,    0,    0,    0,    0,    0,    5,   82,   13,
    0,    7,    0,    0,    0,    8,    0,    5,   82,    0,
   10,    7,    0,    0,    0,    8,    0,    0,   13,    0,
   10,    5,   82,    0,    0,    7,    0,    0,   13,    8,
    0,    5,   82,    0,   10,    7,    0,    5,   82,    8,
    0,    7,   13,    0,   10,    8,    0,    0,    0,    0,
   10,    0,   13,    5,   82,    0,    0,    7,   13,    0,
    0,    8,    0,    0,    5,   82,   10,    0,    7,    0,
    0,    0,    8,    0,   13,    5,   82,   10,    0,    7,
    0,    0,    0,    8,    0,   13,    0,    0,   10,    5,
    6,    0,    0,    7,    0,    0,   13,    8,    9,  103,
    0,    0,   10,    0,    0,    5,    6,   11,   12,    7,
   13,    0,    0,    8,    9,  155,    0,    0,   10,    0,
    0,    5,   82,   11,   12,    7,   13,    0,    0,    8,
    0,    0,  130,  249,   10,    0,    0,    0,    0,    0,
    0,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    4,   41,   42,   43,   40,   45,   41,   47,   40,
   59,   41,   15,   58,   62,   40,    0,  123,   87,   40,
   59,   60,   61,   62,   45,   68,   41,   58,   48,   69,
   59,  257,    0,   83,   84,   41,   76,   77,    7,   45,
   45,  196,   41,  175,   44,  250,   45,   41,    0,  256,
  257,   41,  123,  260,   40,   45,  257,  264,  123,   59,
   40,  109,  269,   44,   58,  270,   45,   40,   37,  257,
  277,  226,   58,   93,  123,  257,   59,   41,   59,   59,
   44,  131,   45,   41,  123,   44,   59,   45,   41,   59,
  140,   41,  274,  275,  226,   41,   41,  123,  253,   42,
   59,  104,   41,   41,   47,   44,   59,   40,  123,   59,
  179,   41,   59,   43,   59,   45,  164,   59,  250,   58,
  170,   43,  165,   45,   43,  125,   45,  167,  168,   59,
   60,   61,   62,   41,   43,   43,   45,   45,  270,  123,
  269,  125,   45,   41,   42,   45,   41,   40,   43,   47,
   45,   59,   60,   61,   62,   40,  262,  125,   41,   41,
   43,   43,   45,   45,   42,   43,   43,   45,   45,   47,
   43,  258,   45,  125,   41,   42,   59,   60,   61,   62,
   47,   41,   60,   61,   62,  274,  275,   58,   59,   41,
   45,  256,  257,  123,   45,  260,   41,  262,  263,  264,
   41,   42,  267,   59,  269,  257,   47,   88,   89,   40,
   40,  262,  277,  262,  263,  123,  257,  256,  257,   59,
   41,  260,  261,  257,  259,  264,  257,  272,   41,  259,
  269,  270,  271,  262,  273,  263,  257,  258,  277,   41,
  123,  256,  257,  274,  275,  260,  261,  272,   41,  264,
   41,  257,  258,  258,  269,  276,  256,  257,  257,  258,
  260,  257,  277,  123,  264,  265,  266,  257,  258,  269,
  276,  274,  256,  257,  274,  275,  260,  277,  123,  258,
  264,  265,  266,  123,   59,  269,  276,   59,  256,  257,
  274,  275,  260,  277,  257,  258,  264,  265,  266,  257,
  258,  269,  123,   59,  256,  257,  274,  275,  260,  277,
   58,  257,  264,  265,  266,   59,   44,  269,  276,  257,
   58,  123,  274,  275,   41,  277,  256,  257,  274,  275,
  260,  261,  123,   41,  264,   59,  274,  275,   41,  269,
  270,  271,   40,  273,   58,  263,  263,  277,  256,  257,
  125,  123,  260,  261,  257,  258,  264,  257,  258,   41,
  125,  269,  270,  271,   59,  273,   43,  123,   45,  277,
   58,  123,   58,  256,  257,  272,  276,  260,  261,  123,
  123,  264,   41,   60,   61,   62,  269,  270,  271,  258,
  273,  257,  270,  271,  277,  273,  256,  257,   59,  123,
  260,  261,  257,  258,  264,  258,  257,  258,   59,  269,
   59,  256,  257,   58,  269,  260,  261,  277,   41,  264,
   59,  276,  262,  263,  269,  276,   59,   41,  123,  125,
   41,   59,  277,   59,   59,  256,  257,   59,  125,  260,
  261,  263,  125,  264,  263,  123,   59,   59,  269,   59,
   59,   47,   48,   59,  256,  257,  277,   59,  260,  261,
   83,   84,  264,  125,   17,  256,  257,  269,  125,  260,
  261,   59,   59,  264,  123,  277,  125,   59,  269,  125,
  257,  256,  257,   31,  125,  260,  277,   59,  263,  264,
  262,  263,  123,   59,  269,   59,  125,   93,   94,   59,
   59,  125,  277,  123,   59,  128,  262,  263,  131,  132,
   59,    0,   59,  136,   59,   59,  139,  140,  262,  263,
  125,   69,  123,   59,   59,   73,   74,   75,  123,   59,
   78,   79,   80,  125,    0,   59,   59,   59,  262,  263,
  260,   31,  150,   13,  123,   32,  123,  170,  171,   67,
   -1,   -1,  175,  149,  150,  151,   -1,   -1,  181,   41,
   42,   43,  125,   45,   -1,   47,   -1,  262,  263,   -1,
   -1,  194,   -1,  196,  197,   -1,   -1,   59,  256,  257,
   -1,  204,  260,  261,  125,   -1,  264,  183,   -1,   -1,
   -1,  269,  188,  270,  271,   -1,  273,   -1,   -1,  277,
  125,  224,  225,  226,   -1,  228,  154,  256,  257,   -1,
   -1,  260,   -1,  262,  263,  264,  125,   -1,   -1,  215,
  269,   -1,   -1,   -1,   -1,  256,  257,  250,  277,  260,
  253,   -1,  263,  264,   -1,   -1,  256,  257,  269,  125,
  260,  261,   -1,   -1,  264,  193,  277,  270,   -1,  269,
   -1,   -1,   -1,  125,   -1,  256,  257,  277,   -1,  260,
  261,  256,  257,  264,   -1,  260,   -1,  125,  269,  264,
   -1,   -1,   -1,   -1,  269,   -1,  277,  256,  257,  256,
  257,  260,  277,  260,   -1,  264,  125,  264,   -1,   -1,
  269,   -1,  269,  256,  257,   -1,  125,  260,  277,   -1,
  277,  264,  265,   -1,   -1,   -1,  269,   -1,   -1,   -1,
  125,  274,  275,   -1,  277,  256,  257,   -1,   -1,  260,
  125,   -1,   -1,  264,  265,   -1,  125,   -1,  269,   -1,
   -1,  256,  257,  274,  275,  260,  277,   -1,   -1,  264,
  265,   -1,  125,   -1,  269,   -1,   -1,  256,  257,  274,
  275,  260,  277,  125,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,   -1,   -1,  125,  274,  275,   -1,  277,   -1,
  256,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,   -1,
   -1,  267,  268,  269,  256,  257,   -1,   -1,  260,   -1,
   -1,  277,  264,   -1,   -1,  267,  268,  269,  256,  257,
   -1,   -1,  260,   -1,   -1,  277,  264,   -1,   -1,  267,
   -1,  269,   -1,   -1,   -1,   -1,   -1,  256,  257,  277,
   -1,  260,   -1,   -1,   -1,  264,   -1,  256,  257,   -1,
  269,  260,   -1,   -1,   -1,  264,   -1,   -1,  277,   -1,
  269,  256,  257,   -1,   -1,  260,   -1,   -1,  277,  264,
   -1,  256,  257,   -1,  269,  260,   -1,  256,  257,  264,
   -1,  260,  277,   -1,  269,  264,   -1,   -1,   -1,   -1,
  269,   -1,  277,  256,  257,   -1,   -1,  260,  277,   -1,
   -1,  264,   -1,   -1,  256,  257,  269,   -1,  260,   -1,
   -1,   -1,  264,   -1,  277,  256,  257,  269,   -1,  260,
   -1,   -1,   -1,  264,   -1,  277,   -1,   -1,  269,  256,
  257,   -1,   -1,  260,   -1,   -1,  277,  264,  265,  266,
   -1,   -1,  269,   -1,   -1,  256,  257,  274,  275,  260,
  277,   -1,   -1,  264,  265,  266,   -1,   -1,  269,   -1,
   -1,  256,  257,  274,  275,  260,  277,   -1,   -1,  264,
   -1,   -1,  267,  268,  269,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  277,
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
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion break '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' break '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion continue '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' '{' continue '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion break '}' ';'",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' '{' ejecucion_iteracion continue '}' ';'",
"iteracion_while : ID ':' inicio_while condicion_salto_while '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : inicio_while '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : inicio_while condicion_salto_while '(' ')' '{' ejecucion_iteracion '}' ';'",
"inicio_while : WHILE",
"condicion_salto_while : '(' comparacion_bool ')' ':'",
"ejecucion_iteracion : ejecucion_iteracion sentencia_ejecutable",
"ejecucion_iteracion : sentencia_ejecutable",
"continue : CONTINUE ';'",
"continue : CONTINUE ':' ID ';'",
"break : BREAK CTE ';'",
"break : BREAK '-' CTE ';'",
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
"then_seleccion : THEN '{' ejecucion_control break '}' ';'",
"then_seleccion : '{' ejecucion_control '}' ';'",
"then_seleccion : sentencia_ejecutable",
"then_seleccion : THEN '{' ejecucion_control",
"then_seleccion : THEN ejecucion_control '}' ';'",
"then_seleccion_sin_else : THEN '{' ejecucion_control '}' ';'",
"then_seleccion_sin_else : THEN sentencia_ejecutable",
"then_seleccion_sin_else : THEN break",
"then_seleccion_sin_else : THEN '{' ejecucion_control break '}' ';'",
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

//#line 738 "gramatica(copia).y"

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
//#line 866 "Parser.java"
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
							yyval.sval = ptr1;
                			agregarToken("!" + nombreFuncion().replace(':', '/'));
            			} else {
                			agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Redeclaracion de la funcion " + val_peek(5).sval+ Parser.ambito.toString());
            			}
						}
break;
case 16:
//#line 84 "gramatica(copia).y"
{						
						if (TablaSimbolos.obtenerSimbolo(val_peek(4).sval+ Parser.ambito.toString()) == null) {
                			TablaSimbolos.modifySimbolo(val_peek(4).sval, val_peek(4).sval + Parser.ambito.toString());
                			String ptr1 = chequeoAmbito(val_peek(4).sval + Parser.ambito.toString());
                			TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
                			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
							cambiarAmbito(val_peek(4).sval);     
							yyval.sval = ptr1;           			
                			agregarToken("!" + nombreFuncion().replace(':', '/'));
            			} else {
                			agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Redeclaracion de la funcion " + val_peek(4).sval+ Parser.ambito.toString());
            			}
						}
break;
case 17:
//#line 100 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 18:
//#line 101 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 19:
//#line 102 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 103 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 21:
//#line 104 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 24:
//#line 112 "gramatica(copia).y"
{    
           		String ptr1 = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
            	TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(1).sval);
            	TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de parametro");
            	par_aux.add(ptr1);
                   }
break;
case 25:
//#line 119 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 26:
//#line 120 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 27:
//#line 123 "gramatica(copia).y"
{yyval.sval = "Entero";}
break;
case 28:
//#line 124 "gramatica(copia).y"
{yyval.sval = "Float";}
break;
case 29:
//#line 128 "gramatica(copia).y"
{
																		/*$$ = $2;*/
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
break;
case 30:
//#line 134 "gramatica(copia).y"
{
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
break;
case 31:
//#line 139 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 32:
//#line 140 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 33:
//#line 141 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 34:
//#line 142 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 35:
//#line 143 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 42:
//#line 159 "gramatica(copia).y"
{yyval = val_peek(0); yyval.sval = "ejecutable";}
break;
case 43:
//#line 160 "gramatica(copia).y"
{yyval.sval = "declarable";}
break;
case 44:
//#line 163 "gramatica(copia).y"
{yyval = val_peek(1);}
break;
case 45:
//#line 164 "gramatica(copia).y"
{addEstructura("if"); yyval = val_peek(1);}
break;
case 46:
//#line 165 "gramatica(copia).y"
{addEstructura("impresion"); yyval = val_peek(1);}
break;
case 47:
//#line 166 "gramatica(copia).y"
{addEstructura("while"); yyval = val_peek(0);}
break;
case 48:
//#line 167 "gramatica(copia).y"
{addEstructura("invocacion con discard"); yyval = val_peek(0);}
break;
case 49:
//#line 168 "gramatica(copia).y"
{addEstructura("error"); yyval = val_peek(1);}
break;
case 51:
//#line 175 "gramatica(copia).y"
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
//#line 183 "gramatica(copia).y"
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
//#line 192 "gramatica(copia).y"
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
//#line 200 "gramatica(copia).y"
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
//#line 209 "gramatica(copia).y"
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
//#line 217 "gramatica(copia).y"
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
//#line 225 "gramatica(copia).y"
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
//#line 233 "gramatica(copia).y"
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
//#line 241 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
break;
case 60:
//#line 249 "gramatica(copia).y"
{ 
 																												/*DESAPILO+COMPLETO PASO INCOMPLETO*/
																												/*DESAPILO PASO DE INICIO*/
																												/*GENERAR BI AL INICIO*/
																												polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
																												agregarToken(Integer.toString((int) pila.pop()+1));
																												agregarToken("#BI");	
																												}
break;
case 61:
//#line 258 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool ");}
break;
case 62:
//#line 259 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 63:
//#line 262 "gramatica(copia).y"
{ 
					/*APILAR PASO INICIAL*/
					apilar(); }
break;
case 64:
//#line 267 "gramatica(copia).y"
{ 
					/*GENERO BF INCOMPLETA Y APILO PASO INCOMPLETO*/
					apilar();
					agregarToken("SI");	
					agregarToken("#BF");			 
					}
break;
case 67:
//#line 279 "gramatica(copia).y"
{yyval = val_peek(1); agregarToken("CONTINUE");}
break;
case 68:
//#line 280 "gramatica(copia).y"
{yyval = val_peek(3);
					agregarToken("CONTINUE");
					agregarToken("ETIQUETA " + val_peek(1).sval);
					String ptr = chequeoAmbito(val_peek(1).sval + Parser.ambito.toString());
					}
break;
case 69:
//#line 287 "gramatica(copia).y"
{yyval = val_peek(2);
					agregarToken("BREAK");
					agregarToken("ETIQUETA " + val_peek(1).sval);
					TablaSimbolos.agregarAtributo("asignacion while", "break", val_peek(1).sval);}
break;
case 70:
//#line 291 "gramatica(copia).y"
{yyval = val_peek(3);
					agregarToken("BREAK");
					agregarToken("ETIQUETA "+"-"+val_peek(1).sval);
					TablaSimbolos.agregarAtributo("asignacion while", "break", "-"+val_peek(1).sval);}
break;
case 71:
//#line 297 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 72:
//#line 301 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
									}
break;
case 73:
//#line 305 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 74:
//#line 306 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 75:
//#line 307 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 76:
//#line 308 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 77:
//#line 309 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 78:
//#line 310 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 79:
//#line 311 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 80:
//#line 314 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("#BI");
								}
break;
case 81:
//#line 321 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 82:
//#line 328 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 83:
//#line 335 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 84:
//#line 342 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 85:
//#line 343 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 86:
//#line 344 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 87:
//#line 345 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 92:
//#line 353 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 93:
//#line 354 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 94:
//#line 355 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 95:
//#line 356 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 98:
//#line 362 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 99:
//#line 363 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 100:
//#line 366 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("#BF");			 
								}
break;
case 101:
//#line 372 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 102:
//#line 373 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 103:
//#line 374 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 104:
//#line 375 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 105:
//#line 379 "gramatica(copia).y"
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
case 106:
//#line 391 "gramatica(copia).y"
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
case 107:
//#line 402 "gramatica(copia).y"
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
case 108:
//#line 413 "gramatica(copia).y"
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
case 109:
//#line 424 "gramatica(copia).y"
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
case 110:
//#line 435 "gramatica(copia).y"
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
case 111:
//#line 459 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
 						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        				if (TablaSimbolos.obtenerAtributo(ptr1,"uso") == "constante") {
            				agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Se esta queriendo modificar la constante " + ptr1);
        				} else {
							agregarToken(ptr1); 
            				agregarToken("=:");
            				yyval.sval = ptr1;
        				}
                        }
break;
case 112:
//#line 470 "gramatica(copia).y"
{
						addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);
						String ptr1 = chequeoAmbito(val_peek(3).sval + Parser.ambito.toString());
						if (TablaSimbolos.obtenerSimbolo("asignacion while") == null){
							TablaSimbolos.agregarSimbolo("asignacion while", new Lexema("asignacion while"));
						}
						if((!TablaSimbolos.obtenerAtributo(ptr1, "tipo").equals(TablaSimbolos.obtenerAtributo("asignacion while", "break"))) || (!TablaSimbolos.obtenerAtributo(ptr1, "tipo").equals(TablaSimbolos.obtenerAtributo("asignacion while", "else")))) {
							{agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Tipos incompatibles en la asignacion por while");}
						}
						agregarToken(ptr1); 
            			agregarToken("=:");
            			yyval.sval = ptr1;
						}
break;
case 113:
//#line 484 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 114:
//#line 490 "gramatica(copia).y"
{TablaSimbolos.agregarAtributo("asignacion while", "else", val_peek(0).sval);}
break;
case 115:
//#line 491 "gramatica(copia).y"
{TablaSimbolos.agregarAtributo("asignacion while", "else", "-"+val_peek(0).sval);}
break;
case 116:
//#line 493 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
break;
case 117:
//#line 496 "gramatica(copia).y"
{
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        						String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        						/*TablaSimbolos.agregarSimbolo($1.sval+"+"+$3.sval, new Lexema($1.sval+"+"+$3.sval));*/
       							/*String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"+"+$3.sval);*/
        						/*TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");*/
        						/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            						/*TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar*/
									agregarToken("+");
									yyval.sval = val_peek(0).sval;
        						/*} else {*/
            					/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la suma.");*/
        						/*}*/
								}
break;
case 118:
//#line 511 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					/*TablaSimbolos.agregarSimbolo($1.sval+"-"+$3.sval, new Lexema($1.sval+"-"+$3.sval));*/
       						/*String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"-"+$3.sval);*/
        					/*TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");*/
        					/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            					/*TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar*/
								agregarToken("-");
								yyval.sval = val_peek(0).sval;
        					/*} else {*/
            				/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la resta.");*/
        					/*}*/
    						}
break;
case 119:
//#line 526 "gramatica(copia).y"
{
    	yyval = val_peek(0);
        yyval.sval = val_peek(0).sval;
    }
break;
case 120:
//#line 531 "gramatica(copia).y"
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
case 121:
//#line 542 "gramatica(copia).y"
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
case 122:
//#line 553 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
    						TablaSimbolos.agregarAtributo(ptr1, "tipo", TablaTipos.FLOAT_TYPE);
    						}
break;
case 123:
//#line 559 "gramatica(copia).y"
{
							String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					/*TablaSimbolos.agregarSimbolo($1.sval+"*"+$3.sval, new Lexema($1.sval+"*"+$3.sval));*/
       						/*String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"*"+$3.sval);*/
        					/*TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");*/
        					/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            					/*TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar*/
								agregarToken("*");
								yyval.sval = val_peek(0).sval;
        					/*} else {*/
            				/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la multiplicacion.");*/
        					/*}*/
							}
break;
case 124:
//#line 574 "gramatica(copia).y"
{
    					String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        				String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        				/*TablaSimbolos.agregarSimbolo($1.sval+"/"+$3.sval, new Lexema($1.sval+"/"+$3.sval));*/
       					/*String ptr3 = TablaSimbolos.obtenerSimbolo($1.sval+"/"+$3.sval);*/
        				/*TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");*/
        				/*if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {*/
            				/*TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); // le agrego el tipo a la variable auxiliar*/
							agregarToken("/");
							yyval.sval = val_peek(0).sval;
        				/*} else {*/
            			/*	agregarError(errores_semanticos,"Error","Tipos no compatibles en la division.");*/
        				/*}*/
    					}
break;
case 125:
//#line 589 "gramatica(copia).y"
{
    	yyval = val_peek(0);
        yyval.sval = val_peek(0).sval;
    }
break;
case 126:
//#line 595 "gramatica(copia).y"
{
			String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
			if (ptr != null) {
				agregarToken(ptr);
				yyval.sval = ptr;
            } else {
            	agregarErrorSemantico(AnalizadorLexico.getLineaActual(),"Variable " + val_peek(0).sval + "no encontrada");
            }
            }
break;
case 127:
//#line 605 "gramatica(copia).y"
{
    		String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
			agregarToken(ptr);
			yyval.sval = ptr;
            }
break;
case 128:
//#line 612 "gramatica(copia).y"
{
 			String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
    		String simb = negarConstante(ptr);
    		agregarToken(simb);
    		yyval.sval = ptr;
    		}
break;
case 129:
//#line 622 "gramatica(copia).y"
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
												yyval.sval = TablaSimbolos.obtenerAtributo(ptr1,"tipo");
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
case 130:
//#line 658 "gramatica(copia).y"
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
											yyval.sval = TablaSimbolos.obtenerAtributo(ptr1,"tipo");
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
case 131:
//#line 686 "gramatica(copia).y"
{ 
    
    						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        					if (ptr1 != null) {
        						boolean esFuncion = TablaSimbolos.obtenerAtributo(ptr1,"uso").equals("nombre de funcion");
        						if (esFuncion) {
            						boolean cantidadParametrosCorrectos = TablaSimbolos.obtenerAtributo(ptr1,"cantidad_parametros").equals("0");
            						if (cantidadParametrosCorrectos) {
                						agregarToken(ptr1);
										agregarToken("#CALL");
										yyval.sval = TablaSimbolos.obtenerAtributo(ptr1,"tipo");
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
case 132:
//#line 713 "gramatica(copia).y"
{
		String ptr = chequeoAmbito(val_peek(0).sval);
        yyval = val_peek(0);
        yyval.sval = TablaSimbolos.obtenerAtributo(ptr,"tipo");
		}
break;
case 134:
//#line 721 "gramatica(copia).y"
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
case 135:
//#line 731 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 136:
//#line 732 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 137:
//#line 733 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1934 "Parser.java"
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
