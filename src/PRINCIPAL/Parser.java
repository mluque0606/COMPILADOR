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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import AL.TablaTipos;
import java.util.Map;


//#line 29 "Parser.java"




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
   33,   33,   33,   33,   33,   33,   17,   17,   17,   34,
   34,   34,   13,   13,   13,   13,   13,   13,   35,   35,
   35,   37,   37,   37,   22,   22,   22,   36,   36,   19,
   19,   19,   19,
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
    1,    1,    1,    1,    1,    1,    3,    4,    3,    2,
    3,    1,    3,    3,    1,    6,    6,    4,    3,    3,
    1,    1,    1,    2,    6,    4,    3,    1,    1,    4,
    3,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,    0,    0,    0,    0,    0,   61,
   27,   28,    0,    4,    0,   11,    0,    0,   43,   12,
    0,   41,   42,    0,    0,    0,   47,   48,    0,   49,
    0,    0,    0,  123,    0,    0,    0,    0,  129,    0,
    0,    0,  121,  128,  133,    0,    0,    0,    0,   50,
    0,   40,   10,    0,    7,    0,    0,   13,   44,   45,
   46,    0,    0,    0,    0,    0,    0,    0,    0,  124,
   99,    0,  104,  105,  106,    0,    0,  101,  102,  103,
    0,   96,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  131,   25,    0,    0,    0,    0,    0,    0,    0,
    1,    6,    9,    0,    0,   37,    0,    0,    0,    0,
    0,  108,    0,    0,  122,  127,    0,    0,    0,   95,
    0,    0,    0,    0,   73,    0,    0,    0,    0,   80,
    0,    0,   39,   69,    0,    0,    0,  119,  120,  130,
    0,    0,   21,   24,    0,    0,    0,    0,    0,    0,
    0,   36,    0,    0,    0,    0,    0,  110,    0,    0,
    0,  126,    0,    0,  118,    0,    0,    0,   67,    0,
    0,    0,   38,   75,    0,   74,    0,   70,    0,    0,
   20,   23,   19,    0,    0,    0,    0,    0,    0,    0,
   62,    0,    0,  111,    0,    0,    0,    0,    0,    0,
    0,   68,    0,    0,    0,    0,    0,   16,    0,   18,
   33,    0,    0,    0,    0,    0,   64,    0,    0,    0,
   54,    0,  125,  116,  117,   93,    0,   94,    0,   71,
   72,   15,    0,    0,   32,    0,    0,    0,   63,    0,
    0,    0,    0,    0,    0,   58,   91,   35,    0,    0,
    0,   59,   60,    0,    0,    0,    0,    0,    0,    0,
   30,   34,    0,    0,   66,   51,   52,   53,    0,    0,
    0,   29,    0,   55,   56,   57,   31,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   58,   97,
   98,  105,   38,  106,  167,   23,   24,   25,   26,   27,
   28,   39,   29,   63,  218,  130,  244,  109,   41,   87,
   88,  131,   81,  112,   42,   43,   44,
};
final static short yysindex[] = {                      -220,
    0,    0,  -82,  350,  -11,  -16,  -21,  -35,  -30,    0,
    0,    0, -203,    0,  366,    0, -178,   63,    0,    0,
  -42,    0,    0,   44,   82,  104,    0,    0,   78,    0,
   76, -170,   89,    0,  127,  -65,  -29,  121,    0,  155,
  274,  118,    0,    0,    0,  -26,  -31,   46,   89,    0,
  140,    0,    0,  145,    0,  -54,  153,    0,    0,    0,
    0,   43,  162,   40,   74,  -49,  179,   22,   79,    0,
    0,  218,    0,    0,    0,   59,   59,    0,    0,    0,
   79,    0,  -16,  -72, -167,    0,  -43,  -94,   59,   59,
  255,    0,    0,   55, -205,   54,  264,  295,  293,  319,
    0,    0,    0,   12,  549,    0,   -9,  324,  326,  -34,
  -10,    0,   79,  331,    0,    0,  128,  106,  159,    0,
  118,  118,   74,  314,    0,   10, -167,  420,    0,    0,
  120,  443,    0,    0,  202, -167,  125,    0,    0,    0,
  332,  373,    0,    0, -205, -210, -205,  340,  298,   -7,
   13,    0,  308,  362,  164,  316,  399,    0,  183,  192,
  111,    0,   59,   59,    0,  454,  465,    0,    0,  195,
  480,  391,    0,    0,   26,    0,  491,    0, -205,  396,
    0,    0,    0, -205,  397,  401,   70,  336,   -5, -167,
    0, -167,  325,    0,  416,  428,  337,  359,  411,  507,
  413,    0,  414,    0,    0,  213,  214,    0, -205,    0,
    0,  353,  421,  422,  423,   94,    0,  509,  523,  178,
    0,  344,    0,    0,    0,    0,  424,    0,    0,    0,
    0,    0,  425,  360,    0,  361,  429,  435,    0,  436,
  448,  525,  376,  383,  178,    0,    0,    0,  450,  451,
  382,    0,    0,  254,  453,  455,  459,  539,  395,  400,
    0,    0,  462,  398,    0,    0,    0,    0,  463,  467,
  468,    0,  469,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  524,    0,    0,    1,    0,  470,    0,    0,
    0,    0,    0,    0,  532,    0,    0,   33,    0,    0,
   49,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,  292,
    0,   71,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  103,    9,   41,    0,    0,    0,    0,
    0,  303,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   84,  474,    0,    0,    0,
    0,    0,    0,    0,    0,  129,    0,   36,    0,    0,
    0,    0,    0,    0,    0,    0,   14,    0,    0,    0,
   50,    0,    0,    0,    0,    0,    0,    0,  166,    0,
   93,  116,  -15,    0,    0,    0,    0,    0,  236,    0,
    0,    0,    0,    0,    0,    0,  478,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  139,    0,    0,
  112,    0,    0,    0,    0,    0,    0,    0,    0,   17,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  167,  313,    0,    0,
    0,    0,    0,  149,  181,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  284,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  415,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  357,  527,    0,    0,    0,    0,   -2,
  404,  306,   -3,   -4,  -41,  297,  -37,    0,    0,  531,
    0,  541,  526,  498, -162, -206,  328,   25,    0,    0,
    0,  -70,    0,    0,   51,   90,  -55,
};
final static int YYTABLESIZE=826;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   10,  122,  122,  122,   46,  122,  156,  122,   94,   48,
   52,   71,  117,  243,   92,   36,   17,  137,   37,  122,
  122,  122,  122,   36,  108,  100,   95,   65,  136,  219,
   68,   40,    8,  186,  159,  215,    1,   36,  259,   36,
    4,   32,  128,  132,   10,  100,   93,   30,   14,  107,
  127,  150,  189,   49,  170,  122,  122,  242,  122,   10,
  122,   72,  116,   11,   12,  118,   36,  107,   11,   12,
  149,  188,  157,  122,  122,  122,   22,  123,   53,   68,
   57,  109,  258,  122,  205,  171,   99,   36,    5,   83,
  112,  142,    7,   22,  177,  141,    8,   32,   10,  109,
  152,   10,   59,   36,  206,  196,   56,  100,  112,   13,
  213,  115,   76,  115,   77,  115,   76,   62,   77,  119,
   36,   55,  195,   36,  200,   10,  121,  122,   68,  115,
  115,  115,  115,  113,  237,  113,   76,  113,   77,   17,
   60,   17,   88,  122,  122,  122,  187,  122,  163,  122,
  164,  113,  113,  113,  113,   36,  114,    8,  114,   89,
  114,  122,   61,   76,   90,   77,   69,  135,  162,   26,
   89,  161,   26,   14,  114,  114,  114,  114,  138,  139,
   79,   80,   78,    5,   83,  216,   26,    7,   56,  124,
  125,    8,   70,  115,  126,   82,   10,   92,  101,  165,
   89,  110,  103,  102,   13,   90,   82,   90,  115,  113,
  115,  113,  111,  197,  198,  113,  122,  122,  113,  134,
  122,  122,  155,   45,  122,   93,   47,   33,   34,  122,
  122,  122,   91,  122,   83,   33,   34,  122,  114,   87,
  100,  100,   11,   12,  100,  100,   35,  158,  100,   33,
   34,   33,   34,  100,   35,   31,   10,   10,  120,  152,
   10,  100,   31,   39,   10,   10,   10,  169,   35,   10,
   35,   84,   17,   17,   10,   10,   17,   10,  115,   34,
   17,   17,   17,  122,  122,   17,  122,  124,    8,    8,
   17,   17,    8,   17,   86,  140,    8,    8,    8,  107,
   34,    8,   93,   81,   14,   14,    8,    8,   14,    8,
  144,   93,   14,   14,   14,   33,   34,   14,   35,   11,
   12,  145,   14,   14,  166,   14,  115,  115,   11,   12,
  115,  115,   64,   34,  115,   33,   34,   86,  146,  115,
  115,  115,   85,  115,   10,   82,   88,  115,  113,  113,
  147,   35,  113,  113,   35,  114,  113,  114,   79,  148,
   39,  113,  113,  113,  153,  113,  154,  115,   34,  113,
  160,  114,  114,   83,   89,  114,  114,  224,   89,  114,
  129,  133,  174,   90,  114,  114,  114,  178,  114,  179,
   73,   74,  114,   75,   39,   39,   85,  184,   39,  225,
   89,   92,   39,   96,   96,   90,   78,   39,    5,    6,
   84,   90,    7,  180,   98,   39,    8,    9,  104,  191,
  168,   10,  185,  133,  173,   97,   11,   12,  173,   13,
  190,  168,  133,    5,   83,   31,  166,    7,  192,  193,
  194,    8,   81,   87,  126,  241,   10,  220,  155,  204,
   96,  143,  202,  209,   13,  211,  222,    5,   83,  212,
  214,    7,  133,  173,  176,    8,  245,  173,  223,  226,
   10,  228,  229,  173,   14,  230,  231,  233,   13,  234,
  235,  236,  247,  248,  249,  250,  217,  251,  217,  221,
   51,   39,   39,  252,  253,   39,  173,   79,   86,   39,
  256,  181,   96,  183,   39,  254,  263,  257,  261,  262,
  265,  266,   39,  267,  239,  239,  217,  268,  246,  270,
  272,  274,  273,    2,  271,  275,  276,  277,  132,    5,
   83,    3,   76,    7,   84,  208,   77,    8,  239,   65,
  210,  217,   10,   54,  172,   78,   85,   98,   98,  182,
   13,   98,   98,   50,  239,   98,  264,   67,   97,   97,
   98,   66,   97,   97,  114,  232,   97,  175,   98,    5,
   83,   97,  260,    7,    0,    0,    0,    8,  199,   97,
    5,   83,   10,    0,    7,    0,    0,    0,    8,  201,
   13,    0,    0,   10,    0,    0,    0,    0,    0,    5,
   83,   13,    0,    7,  203,    5,    6,    8,    0,    7,
    0,    0,   10,    8,    9,  207,    0,    0,   10,    0,
   13,    5,    6,   11,   12,    7,   13,    0,    0,    8,
    9,  227,    0,  238,   10,    0,    0,    5,    6,   11,
   12,    7,   13,    0,    0,    8,    9,  240,    0,  255,
   10,    0,    0,    5,    6,   11,   12,    7,   13,    0,
    0,    8,    9,  269,    0,    0,   10,    0,    0,    0,
    0,   11,   12,    0,   13,    5,   83,    0,    0,    7,
    0,    0,    0,    8,    0,    0,    0,    0,   10,    0,
    0,    0,    0,    0,    0,    0,   13,    0,    5,   83,
    0,    0,    7,    0,    0,    0,    8,    0,    0,    5,
   83,   10,    0,    7,    0,    0,    0,    8,    0,   13,
    5,   83,   10,    0,    7,    0,    0,    0,    8,    0,
   13,    0,    0,   10,    0,    5,   83,    0,    0,    7,
    0,   13,    0,    8,    0,    0,    5,   83,   10,    0,
    7,    0,    0,    0,    8,    0,   13,    0,    0,   10,
    0,    0,    5,   83,    5,   83,    7,   13,    7,    0,
    8,    0,    8,    0,    0,   10,    0,   10,    5,   83,
    5,   83,    7,   13,    7,   13,    8,    0,    8,    0,
    0,   10,    0,   10,    5,   83,    0,    0,    7,   13,
    0,   13,    8,    0,    5,    6,    0,   10,    7,    0,
    0,    0,    8,    9,  151,   13,    0,   10,    0,    0,
    0,    0,   11,   12,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    0,   41,   42,   43,   40,   45,   41,   47,   40,   40,
   15,   41,   68,  220,   41,   45,    0,   88,   40,   59,
   60,   61,   62,   45,   62,   41,   58,   31,  123,  192,
   40,    7,    0,   41,   45,   41,  257,   45,  245,   45,
  123,   58,   84,   85,   44,   48,  257,   59,    0,   41,
  123,   40,   40,  257,   45,   42,   43,  220,   45,   59,
   47,   37,   41,  274,  275,   69,   45,   59,  274,  275,
   59,   59,  110,   60,   61,   62,   41,   81,  257,   40,
  123,   41,  245,  123,   59,  127,   41,   45,  256,  257,
   41,   94,  260,   58,  136,   41,  264,   58,  269,   59,
  105,  269,   59,   45,  175,  161,   44,  123,   59,  277,
   41,   41,   43,   43,   45,   45,   43,   40,   45,   69,
   45,   59,  160,   45,  166,  125,   76,   77,   40,   59,
   60,   61,   62,   41,   41,   43,   43,   45,   45,  123,
   59,  125,   59,   41,   42,   43,  150,   45,   43,   47,
   45,   59,   60,   61,   62,   45,   41,  125,   43,   42,
   45,   59,   59,   43,   47,   45,   40,  262,   41,   41,
   59,   44,   44,  125,   59,   60,   61,   62,   89,   90,
   60,   61,   62,  256,  257,  189,   58,  260,   44,  262,
  263,  264,  258,  123,  267,   41,  269,   59,   59,   41,
   42,   40,  257,   59,  277,   47,  123,   59,   43,   43,
   45,   45,  262,  163,  164,  123,  256,  257,   40,  263,
  260,  261,  257,  259,  264,  257,  257,  257,  258,  269,
  270,  271,  259,  273,  123,  257,  258,  277,  123,   59,
  256,  257,  274,  275,  260,  261,  276,  258,  264,  257,
  258,  257,  258,  269,  276,  272,  256,  257,   41,  264,
  260,  277,  272,  125,  264,  265,  266,  258,  276,  269,
  276,  123,  256,  257,  274,  275,  260,  277,  257,  258,
  264,  265,  266,  270,  271,  269,  273,  262,  256,  257,
  274,  275,  260,  277,   59,   41,  264,  265,  266,  257,
  258,  269,  257,  123,  256,  257,  274,  275,  260,  277,
  257,  257,  264,  265,  266,  257,  258,  269,  276,  274,
  275,   58,  274,  275,  123,  277,  256,  257,  274,  275,
  260,  261,  257,  258,  264,  257,  258,   41,   44,  269,
  270,  271,   59,  273,  269,  262,  263,  277,  256,  257,
   58,  276,  260,  261,  276,   43,  264,   45,  123,   41,
  125,  269,  270,  271,   41,  273,   41,  257,  258,  277,
   40,  256,  257,  262,  263,  260,  261,   41,   42,  264,
   84,   85,  263,   47,  269,  270,  271,  263,  273,   58,
  270,  271,  277,  273,  256,  257,  123,   58,  260,   41,
   42,  263,  264,   47,   48,   47,  123,  269,  256,  257,
  262,  263,  260,   41,  123,  277,  264,  265,  266,   58,
  124,  269,  125,  127,  128,  123,  274,  275,  132,  277,
  123,  135,  136,  256,  257,  272,  123,  260,  123,   41,
  258,  264,  262,  263,  267,  268,  269,  123,  257,   59,
   94,   95,  258,   58,  277,   59,   41,  256,  257,   59,
  125,  260,  166,  167,  263,  264,  123,  171,   41,   59,
  269,   59,   59,  177,  125,  263,  263,  125,  277,   59,
   59,   59,   59,   59,  125,  125,  190,   59,  192,  193,
  125,  256,  257,   59,   59,  260,  200,  262,  263,  264,
  125,  145,  146,  147,  269,   58,  125,  125,   59,   59,
  257,   59,  277,   59,  218,  219,  220,   59,  222,  125,
   59,   59,  125,    0,  125,   59,   59,   59,   59,  256,
  257,    0,   59,  260,  261,  179,   59,  264,  242,  125,
  184,  245,  269,   17,  125,  262,  263,  256,  257,  146,
  277,  260,  261,   13,  258,  264,  251,   32,  256,  257,
  269,   31,  260,  261,   67,  209,  264,  125,  277,  256,
  257,  269,  245,  260,   -1,   -1,   -1,  264,  125,  277,
  256,  257,  269,   -1,  260,   -1,   -1,   -1,  264,  125,
  277,   -1,   -1,  269,   -1,   -1,   -1,   -1,   -1,  256,
  257,  277,   -1,  260,  125,  256,  257,  264,   -1,  260,
   -1,   -1,  269,  264,  265,  125,   -1,   -1,  269,   -1,
  277,  256,  257,  274,  275,  260,  277,   -1,   -1,  264,
  265,  125,   -1,  125,  269,   -1,   -1,  256,  257,  274,
  275,  260,  277,   -1,   -1,  264,  265,  125,   -1,  125,
  269,   -1,   -1,  256,  257,  274,  275,  260,  277,   -1,
   -1,  264,  265,  125,   -1,   -1,  269,   -1,   -1,   -1,
   -1,  274,  275,   -1,  277,  256,  257,   -1,   -1,  260,
   -1,   -1,   -1,  264,   -1,   -1,   -1,   -1,  269,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  277,   -1,  256,  257,
   -1,   -1,  260,   -1,   -1,   -1,  264,   -1,   -1,  256,
  257,  269,   -1,  260,   -1,   -1,   -1,  264,   -1,  277,
  256,  257,  269,   -1,  260,   -1,   -1,   -1,  264,   -1,
  277,   -1,   -1,  269,   -1,  256,  257,   -1,   -1,  260,
   -1,  277,   -1,  264,   -1,   -1,  256,  257,  269,   -1,
  260,   -1,   -1,   -1,  264,   -1,  277,   -1,   -1,  269,
   -1,   -1,  256,  257,  256,  257,  260,  277,  260,   -1,
  264,   -1,  264,   -1,   -1,  269,   -1,  269,  256,  257,
  256,  257,  260,  277,  260,  277,  264,   -1,  264,   -1,
   -1,  269,   -1,  269,  256,  257,   -1,   -1,  260,  277,
   -1,  277,  264,   -1,  256,  257,   -1,  269,  260,   -1,
   -1,   -1,  264,  265,  266,  277,   -1,  269,   -1,   -1,
   -1,   -1,  274,  275,   -1,  277,
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
"comparacion_bool : expresion comparador expresion",
"comparador : '>'",
"comparador : '<'",
"comparador : '='",
"comparador : MAYOR_IGUAL",
"comparador : MENOR_IGUAL",
"comparador : DISTINTO",
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

//#line 601 "gramatica(copia).y"

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
        TablaSimbolos.agregarAtributo(ptr,"tipo",tipo);
        TablaSimbolos.agregarAtributo(ptr,"uso","variable");
        TablaSimbolos.modifySimbolo(ptr,ptr+Parser.ambito.toString());
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
                    Parser.agregarErrorSemantico(AnalizadorLexico.getLineaActual(), "La variable no fue declarada o no esta al alcance");
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
//#line 823 "Parser.java"
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
//#line 25 "gramatica(copia).y"
{addEstructura("programa");}
break;
case 2:
//#line 26 "gramatica(copia).y"
{addEstructura("programa sin ejecucion");}
break;
case 3:
//#line 28 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba un '}' al final del programa");}
break;
case 4:
//#line 29 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba una sentencia de ejecucion");}
break;
case 5:
//#line 32 "gramatica(copia).y"
{cambiarAmbito(val_peek(0).sval);
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					String aux = TablaSimbolos.obtenerSimbolo(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(aux, "tipo", TablaTipos.LONG_TYPE);
					//TablaSimbolos.imprimirTabla();
					}
break;
case 6:
//#line 41 "gramatica(copia).y"
{
					asignarTipos(val_peek(2).sval);
					}
break;
case 7:
//#line 45 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
break;
case 8:
//#line 46 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
break;
case 9:
//#line 49 "gramatica(copia).y"
{ 
				var_aux.add(val_peek(0).sval);
                }
break;
case 10:
//#line 52 "gramatica(copia).y"
{ 
        		var_aux.add(val_peek(0).sval);
               }
break;
case 11:
//#line 57 "gramatica(copia).y"
{addEstructura("declaracion variables");
											 }
break;
case 12:
//#line 59 "gramatica(copia).y"
{addEstructura("declaracion funcion");
        		   }
break;
case 13:
//#line 63 "gramatica(copia).y"
{agregarToken(nombreFuncion());
										   salirAmbito();
										   agregarToken("\\ENDP"); }
break;
case 14:
//#line 66 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una ejecucion_funcion");}
break;
case 15:
//#line 70 "gramatica(copia).y"
{
						String ptr1 = chequeoAmbito(val_peek(5).sval + Parser.ambito.toString());
            			if(ptr1 != null){
            				TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
            				TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
            				cambiarAmbito(val_peek(5).sval);
            				procesarParametros(ptr1);
            			}
					
						agregarToken("!" + nombreFuncion().replace(':', '/'));
						}
break;
case 16:
//#line 82 "gramatica(copia).y"
{
						String ptr1 = chequeoAmbito(val_peek(4).sval + Parser.ambito.toString());
            			if (ptr1 != null){
            				TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
            				TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
            				TablaSimbolos.agregarAtributo(ptr1,"cantidad_parametros","0");
            				cambiarAmbito(val_peek(4).sval);
						}
						agregarToken("!" + nombreFuncion().replace(':', '/'));
						}
break;
case 17:
//#line 95 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 18:
//#line 96 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 19:
//#line 97 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 98 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 21:
//#line 99 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 24:
//#line 107 "gramatica(copia).y"
{    
						String ptr1 = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
            			if (ptr1 != null){
            				TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(1).sval);
            				TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de parametro");
            				par_aux.add(ptr1);
            			}
                   }
break;
case 25:
//#line 116 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 26:
//#line 117 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 27:
//#line 120 "gramatica(copia).y"
//{tipo = TablaTipos.LONG_TYPE;}
break;
case 28:
//#line 121 "gramatica(copia).y"
//{tipo = TablaTipos.FLOAT_TYPE;}
break;
case 29:
//#line 125 "gramatica(copia).y"
{
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
break;
case 30:
//#line 130 "gramatica(copia).y"
{
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
break;
case 31:
//#line 135 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 32:
//#line 136 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 33:
//#line 137 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 34:
//#line 138 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 35:
//#line 139 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 45:
//#line 160 "gramatica(copia).y"
{addEstructura("if");}
break;
case 46:
//#line 161 "gramatica(copia).y"
{addEstructura("impresion");}
break;
case 47:
//#line 162 "gramatica(copia).y"
{addEstructura("while");}
break;
case 48:
//#line 163 "gramatica(copia).y"
{addEstructura("invocacion con discard");}
break;
case 49:
//#line 164 "gramatica(copia).y"
{addEstructura("error");}
break;
case 51:
//#line 171 "gramatica(copia).y"
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
//#line 179 "gramatica(copia).y"
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
//#line 187 "gramatica(copia).y"
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
//#line 195 "gramatica(copia).y"
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
//#line 203 "gramatica(copia).y"
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
//#line 211 "gramatica(copia).y"
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
//#line 219 "gramatica(copia).y"
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
//#line 227 "gramatica(copia).y"
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
//#line 236 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool ");}
break;
case 60:
//#line 237 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 61:
//#line 240 "gramatica(copia).y"
{ 
					/*APILAR PASO INICIAL*/
					apilar(); }
break;
case 62:
//#line 245 "gramatica(copia).y"
{ 
					/*GENERO BF INCOMPLETA Y APILO PASO INCOMPLETO*/
					apilar();
					agregarToken("SI");	
					agregarToken("#BF");			 
					}
break;
case 66:
//#line 258 "gramatica(copia).y"
{
					String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
					}
break;
case 69:
//#line 267 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 70:
//#line 271 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
									}
break;
case 71:
//#line 275 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 72:
//#line 276 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 73:
//#line 277 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 74:
//#line 278 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 75:
//#line 279 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 76:
//#line 280 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 77:
//#line 281 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 78:
//#line 284 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("#BI");
								}
break;
case 79:
//#line 291 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 80:
//#line 298 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 81:
//#line 305 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 82:
//#line 306 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 83:
//#line 307 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 84:
//#line 308 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 87:
//#line 314 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 88:
//#line 315 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 89:
//#line 316 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 90:
//#line 317 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 93:
//#line 323 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 94:
//#line 324 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 95:
//#line 327 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("#BF");			 
								}
break;
case 96:
//#line 333 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 97:
//#line 334 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 98:
//#line 335 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 99:
//#line 336 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 100:
//#line 340 "gramatica(copia).y"
{
								addEstructura("comparacion");
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					 	String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        						/*COMO CONTROLO CONVERSIONES EXPLICITAS*/
        						if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
									agregarToken(val_peek(1).sval);
        						} else {
            						agregarError(errores_semanticos,"Error","Tipos no compatibles en la comparacion.");
        						}
        						}
break;
case 107:
//#line 363 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
						//TablaSimbolos.imprimirTabla();
 						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        				String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        				if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            				agregarToken(ptr1); 
            				agregarToken("=:");
        				} else {
            				agregarError(errores_semanticos,"Error","Tipos no compatibles en la asignacion.");
        				}
                        }
break;
case 108:
//#line 374 "gramatica(copia).y"
{
						addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);
						String ptr1 = chequeoAmbito(val_peek(3).sval + Parser.ambito.toString());
						/*QUE MAS HAGO??*/
						}
break;
case 109:
//#line 380 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 112:
//#line 389 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
break;
case 113:
//#line 392 "gramatica(copia).y"
{
								//TablaSimbolos.imprimirTabla();
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        						String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        						TablaSimbolos.agregarSimbolo(val_peek(2).sval+"+"+val_peek(0).sval, new Lexema(val_peek(2).sval+"+"+val_peek(0).sval));
       							String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"+"+val_peek(0).sval);
        						//System.out.println(ptr3);
       							TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        						if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            						TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
									agregarToken("+");
        						} else {
            						agregarError(errores_semanticos,"Error","Tipos no compatibles en la suma.");
        						}
								}
break;
case 114:
//#line 406 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					TablaSimbolos.agregarSimbolo(val_peek(2).sval+"-"+val_peek(0).sval, new Lexema(val_peek(2).sval+"-"+val_peek(0).sval));
       						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"-"+val_peek(0).sval);
        					TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            					TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
								agregarToken("-");
        					} else {
            					agregarError(errores_semanticos,"Error","Tipos no compatibles en la resta.");
        					}
    						}
break;
case 116:
//#line 422 "gramatica(copia).y"
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
case 117:
//#line 433 "gramatica(copia).y"
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
case 118:
//#line 444 "gramatica(copia).y"
{
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
    						TablaSimbolos.agregarAtributo(ptr1, "tipo", TablaTipos.FLOAT_TYPE);
    						}
break;
case 119:
//#line 450 "gramatica(copia).y"
{
							String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					TablaSimbolos.agregarSimbolo(val_peek(2).sval+"*"+val_peek(0).sval, new Lexema(val_peek(2).sval+"*"+val_peek(0).sval));
       						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"*"+val_peek(0).sval);
        					TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            					TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
								agregarToken("*");
        					} else {
            					agregarError(errores_semanticos,"Error","Tipos no compatibles en la multiplicacion.");
        					}
							}
break;
case 120:
//#line 464 "gramatica(copia).y"
{
    					String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        				String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        				TablaSimbolos.agregarSimbolo(val_peek(2).sval+"/"+val_peek(0).sval, new Lexema(val_peek(2).sval+"/"+val_peek(0).sval));
       					String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"/"+val_peek(0).sval);
        				TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        				if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            				TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
							agregarToken("/");
        				} else {
            				agregarError(errores_semanticos,"Error","Tipos no compatibles en la division.");
        				}
    					}
break;
case 122:
//#line 481 "gramatica(copia).y"
{
			String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString());
			agregarToken(ptr);
            }
break;
case 123:
//#line 486 "gramatica(copia).y"
{
    		String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		//TablaSimbolos.imprimirTabla();
    		if(ptr != null){
    			TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
			}
			agregarToken(ptr);
            }
break;
case 124:
//#line 494 "gramatica(copia).y"
{
 			String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		if (ptr != null){
    			TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
    			String simb = negarConstante(ptr);
    			agregarToken(simb);
			}
    		}
break;
case 125:
//#line 505 "gramatica(copia).y"
{ 
							String ptr1 = chequeoAmbito(val_peek(5).sval + Parser.ambito.toString());
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(3).sval);
        					String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
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
                        }
break;
case 126:
//#line 535 "gramatica(copia).y"
{ 
    						String ptr1 = chequeoAmbito(val_peek(3).sval + Parser.ambito.toString());
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(1).sval);
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
                    					agregarError(errores_semanticos,"Error","No concuerda el tipo del parametro de la invocacion con el de la funcion.");
                					}
            					} else {
                					agregarError(errores_semanticos,"Error","Cantidad de parametros incorrectos en la funcion.");
            					}
        					} else {
            					agregarError(errores_semanticos,"Error","Funcion no encontrada.");
        					}
                            }
break;
case 127:
//#line 558 "gramatica(copia).y"
{ 
    
    						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        					boolean esFuncion = TablaSimbolos.obtenerAtributo(ptr1,"uso").equals("nombre de funcion");
        					if (esFuncion) {
            					boolean cantidadParametrosCorrectos = TablaSimbolos.obtenerAtributo(ptr1,"cantidad_parametros").equals("0");
            					if (cantidadParametrosCorrectos) {
                					agregarToken(ptr1);
									agregarToken("#CALL");
            					} else {
                					agregarError(errores_semanticos,"Error","Cantidad de parametros incorrectos en la funcion.");
            					}
        					} else {
            					agregarError(errores_semanticos,"Error","Funcion no encontrada.");
        					}
                            }
break;
case 130:
//#line 584 "gramatica(copia).y"
{       
                                String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                TablaSimbolos.agregarSimb(nombre);
                                String simb = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(simb, "valor", val_peek(1).sval);
                                TablaSimbolos.agregarAtributo(simb, "tipo", "String");
                                agregarToken(nombre);    /*agregamos a la polaca el simbolo, junto identificador de cadenas*/
                                contador_cadenas++; 
                                }
break;
case 131:
//#line 594 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 132:
//#line 595 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 133:
//#line 596 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1692 "Parser.java"
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
