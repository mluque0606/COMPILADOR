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
   31,   31,   31,   30,   30,   30,   30,   30,   30,   32,
   32,   32,   32,   29,   29,   29,   29,   29,   28,   33,
   33,   33,   33,   33,   33,   17,   17,   17,   34,   34,
   34,   13,   13,   13,   13,   13,   13,   35,   35,   35,
   37,   37,   37,   22,   22,   22,   36,   36,   19,   19,
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
    7,    7,    4,    5,    5,    3,    4,    5,    2,    4,
    1,    3,    4,    5,    2,    4,    1,    3,    4,    5,
    2,    4,    4,    3,    2,    2,    1,    2,    3,    1,
    1,    1,    1,    1,    1,    3,    4,    3,    2,    3,
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
   98,    0,  103,  104,  105,    0,    0,  100,  101,  102,
    0,   95,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  130,   25,    0,    0,    0,    0,    0,    0,    0,
    1,    6,    9,    0,    0,   37,    0,    0,    0,    0,
    0,  107,    0,    0,  121,  126,    0,    0,    0,   94,
    0,    0,    0,    0,   73,    0,    0,    0,    0,    0,
   39,   69,    0,    0,    0,  118,  119,  129,    0,    0,
   21,   24,    0,    0,    0,    0,    0,    0,    0,   36,
    0,    0,    0,    0,    0,  109,    0,    0,    0,  125,
    0,    0,  117,    0,    0,    0,    0,    0,   38,   75,
    0,   74,    0,   70,    0,    0,   20,   23,   19,    0,
    0,    0,    0,    0,    0,    0,   62,    0,    0,  110,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   16,    0,   18,   33,    0,    0,    0,    0,
    0,   64,    0,    0,    0,   54,    0,  124,  115,  116,
   92,    0,   93,    0,   71,   72,   15,    0,    0,   32,
    0,    0,    0,   63,    0,    0,    0,    0,    0,    0,
    0,   58,   90,   35,    0,    0,    0,   59,   60,   67,
    0,    0,    0,    0,    0,    0,    0,    0,   30,   34,
    0,    0,   68,   66,   51,   52,   53,    0,    0,    0,
   29,    0,   55,   56,   57,   31,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   58,   97,
   98,  105,   38,  106,  165,   23,   24,   25,   26,   27,
   28,   39,   29,   63,  213,  239,  240,  109,   41,   87,
   88,  129,   81,  112,   42,   43,   44,
};
final static short yysindex[] = {                      -210,
    0,    0,  -52,  342,   37,  -44,  -20,  -34,  -40,    0,
    0,    0, -165,    0,  358,    0, -139,   69,    0,    0,
    6,    0,    0,   78,   82,   88,    0,    0,  111,    0,
  123, -112,  120,    0,  156,  -91,   -5,  127,    0,  159,
  173,   80,    0,    0,    0,  -29,  -30,    5,  120,    0,
  144,    0,    0,   76,    0,  -49,  511,    0,    0,    0,
    0,   54,  170,    4,   50,  -57,  209,   59,   62,    0,
    0,  218,    0,    0,    0,   94,   94,    0,    0,    0,
   62,    0,  -44,  -65, -190,    0,   38,  -93,   94,   94,
  288,    0,    0,   47,  -54,   86,  279,  303,  281,  317,
    0,    0,    0,   -9,  527,    0,  -32,  319,  324,  -23,
  -10,    0,   62,  327,    0,    0,    7,  101,  134,    0,
   80,   80,   50,  297,    0, -190,  251,    0,  109,  413,
    0,    0,  202, -190,  110,    0,    0,    0,  325,  336,
    0,    0,  -54, -220,  -54,  328,  265,   11,   -6,    0,
  278,  344,  133,  283,  367,    0,  151,  155,   98,    0,
   94,   94,    0,  423,  437,    0,  451,  354,    0,    0,
    2,    0,  453,    0,  -54,  359,    0,    0,    0,  -54,
  360,  362,  141,  298,   27, -190,    0, -190,  313,    0,
  384,  385,  160,  402,  363,  469,  376,  379,    0,    0,
  178,  182,    0,  -54,    0,    0,  322,  389,  392,  393,
  168,    0,  479,  485,  543,    0,  323,    0,    0,    0,
    0,  394,    0,    0,    0,    0,    0,  396,  343,    0,
  345,  410,  414,    0,  415,   -7,  403,  495,  347,  350,
  543,    0,    0,    0,  417,  418,  381,    0,    0,    0,
  220,  227,  426,  427,  428,  501,  364,  365,    0,    0,
  429,  397,    0,    0,    0,    0,    0,  432,  442,  443,
    0,  445,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  509,    0,    0,    1,    0,  455,    0,    0,
    0,    0,    0,    0,  510,    0,    0,   33,    0,    0,
   49,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,    0,    0,    0,  273,
    0,   71,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   39,   35,   56,    0,    0,    0,    0,
    0,  295,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -48,  462,    0,    0,    0,
    0,    0,    0,    0,    0,  104,    0,   32,    0,    0,
    0,    0,    0,    0,    0,    0,   63,    0,    0,    0,
   60,    0,    0,    0,    0,    0,    0,    0,  121,    0,
   93,  118,  -14,    0,    0,    0,    0,  236,    0,    0,
    0,    0,    0,    0,  464,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  147,  106,    0,    0,    0,
    0,    0,    0,    0,    0,   17,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  128,  140,    0,    0,    0,    0,  131,  165,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  177,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  399,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  337,  508,    0,    0,    0,    0,  -16,
  383,  284,    8,   -2,  -43,  330,  -47,    0,    0,  504,
    0,  519,  507,  473, -172,  305,  308,   22,    0,    0,
    0,  -69,    0,    0,  -12,  256,  -42,
};
final static int YYTABLESIZE=820;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   10,   22,  121,  121,  121,   46,  121,   68,  121,   94,
   87,   92,   52,   32,  108,  214,   17,  154,  135,   37,
  121,  121,  121,  121,   36,  117,   99,   95,   40,  134,
  148,  100,    8,  185,  157,   71,   93,  251,   65,   36,
  127,  130,  238,   68,   10,   99,    1,  160,   14,  147,
  159,  182,  184,   11,   12,   36,  119,  126,   72,   10,
  200,   32,  155,  121,  122,    5,   83,  210,  256,    7,
    4,   36,   22,    8,   81,  106,  118,  140,   10,  121,
  121,  121,  167,  121,  121,  121,   13,  139,  123,   22,
  173,   49,   76,  106,   77,   30,  108,  121,   36,  116,
  111,  201,  150,   36,  121,  121,   36,  121,   99,  121,
  191,  114,   56,  114,  108,  114,  192,   53,  111,   56,
  196,   89,  121,  121,  121,   10,   90,   55,   57,  114,
  114,  114,  114,  112,  102,  112,   59,  112,   36,   17,
   60,   17,   36,  161,   26,  162,   61,   26,  193,  194,
   62,  112,  112,  112,  112,  183,   10,    8,  113,   68,
  113,   26,  113,  114,   88,  114,   70,   36,  133,   76,
  112,   77,  112,   14,  163,   89,  113,  113,  113,  113,
   90,  208,  113,   76,  113,   77,   79,   80,   78,   89,
    5,   83,  211,  114,    7,   69,  124,  125,    8,   82,
  219,   89,  101,   10,  111,   91,   90,  103,  232,  110,
   76,   13,   77,   81,   87,  112,   47,  121,  121,   11,
   12,  121,  121,   86,   45,  121,   93,   31,   82,   91,
  121,  121,  121,  153,  121,   84,   33,   34,  121,   31,
  113,   99,   99,   11,   12,   99,   99,  156,  113,   99,
  250,   33,   34,   83,   99,   35,   10,   10,  120,  150,
   10,   93,   99,  124,   10,   10,   10,   33,   34,   10,
   35,   39,   17,   17,   10,   10,   17,   10,   11,   12,
   17,   17,   17,   33,   34,   17,   35,   80,    8,    8,
   17,   17,    8,   17,   85,   85,    8,    8,    8,   78,
  132,    8,   35,   93,   14,   14,    8,    8,   14,    8,
  107,   34,   14,   14,   14,  115,   34,   14,   33,   34,
   11,   12,   14,   14,  164,   14,  114,  114,  138,   35,
  114,  114,  121,  121,  114,  121,  143,   35,  145,  114,
  114,  114,  142,  114,  136,  137,  144,  114,  112,  112,
   33,   34,  112,  112,  115,   34,  112,  146,   79,  151,
   39,  112,  112,  112,  152,  112,  158,   82,   88,  112,
   86,  170,  174,  113,  113,  168,  176,  113,  113,   64,
   34,  113,  175,   96,   96,  180,  113,  113,  113,  181,
  113,   10,   83,   89,  113,   97,   73,   74,   35,   75,
  186,  187,   39,   39,   31,  188,   39,  189,  190,   91,
   39,  153,  199,  128,  131,   39,  204,   96,  206,  164,
  207,  221,  209,   39,  217,  218,   80,   86,    5,   83,
   96,  141,    7,   84,  223,  215,    8,  224,   78,   84,
  225,   10,  220,   89,  226,  241,  228,  229,   90,   13,
  230,  231,  243,  166,  244,  131,  169,    5,   83,  169,
  252,    7,  166,  131,  172,    8,   14,  245,  247,  246,
   10,  254,  248,  249,  255,  259,  260,  263,   13,  177,
   96,  179,   51,  264,  265,  266,  267,  271,  269,  270,
  273,   39,   39,  131,  169,   39,  169,   79,   85,   39,
  274,  275,  169,  276,   39,  261,    5,   83,    2,    3,
    7,  203,   39,  131,    8,  212,  205,  212,  216,   10,
   76,  272,   77,   65,   54,  169,  178,   13,   97,   97,
  262,   50,   97,   97,   66,    0,   97,  171,   67,  114,
  227,   97,  234,  234,  212,  257,  242,  195,  258,   97,
   96,   96,    5,   83,   96,   96,    7,    0,   96,    0,
    8,  197,    0,   96,    0,   10,    0,  234,    5,   83,
  212,   96,    7,   13,    0,  198,    8,  202,    5,   83,
    0,   10,    7,    0,    0,  234,    8,    0,    0,   13,
    0,   10,    0,  222,    0,    0,    0,    5,    6,   13,
    0,    7,    0,  233,    0,    8,    9,    0,    0,  235,
   10,    0,    0,    5,    6,   11,   12,    7,   13,  253,
    0,    8,    9,    0,    0,  268,   10,    0,    0,    0,
    0,   11,   12,    0,   13,    0,    5,    6,    0,    0,
    7,    0,    0,    0,    8,    9,    0,    0,    0,   10,
    0,    0,    5,    6,   11,   12,    7,   13,    0,    0,
    8,    9,    0,    0,    0,   10,    0,    0,    5,   83,
   11,   12,    7,   13,    0,    0,    8,    0,    5,   83,
    0,   10,    7,    0,    0,    0,    8,    0,    0,   13,
    0,   10,    5,   83,    0,    0,    7,    0,    0,   13,
    8,    0,    0,    0,    0,   10,    5,   83,    5,   83,
    7,    0,    7,   13,    8,    0,    8,    0,    0,   10,
    0,   10,    0,    0,    5,   83,    0,   13,    7,   13,
    0,    0,    8,    0,    5,   83,    0,   10,    7,    0,
    5,   83,    8,    0,    7,   13,    0,   10,    8,    0,
    5,   83,    0,   10,    7,   13,    5,   83,    8,    0,
    7,   13,    0,   10,    8,    0,    5,    6,    0,   10,
    7,   13,    0,    0,    8,    9,  104,   13,    0,   10,
    0,    0,    5,    6,   11,   12,    7,   13,    0,    0,
    8,    9,  149,    0,    0,   10,    0,    0,    5,   83,
   11,   12,    7,   13,    0,    0,    8,    0,    0,  236,
  237,   10,    0,    0,    0,    0,    0,    0,    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    4,   41,   42,   43,   40,   45,   40,   47,   40,
   59,   41,   15,   58,   62,  188,    0,   41,   88,   40,
   59,   60,   61,   62,   45,   68,   41,   58,    7,  123,
   40,   48,    0,   40,   45,   41,  257,   45,   31,   45,
   84,   85,  215,   40,   44,   41,  257,   41,    0,   59,
   44,   41,   59,  274,  275,   45,   69,  123,   37,   59,
   59,   58,  110,   76,   77,  256,  257,   41,  241,  260,
  123,   45,   41,  264,  123,   41,   69,   94,  269,   41,
   42,   43,  126,   45,  123,   47,  277,   41,   81,   58,
  134,  257,   43,   59,   45,   59,   41,   59,   45,   41,
   41,  171,  105,   45,   42,   43,   45,   45,  123,   47,
  158,   41,   44,   43,   59,   45,  159,  257,   59,   44,
  164,   42,   60,   61,   62,  125,   47,   59,  123,   59,
   60,   61,   62,   41,   59,   43,   59,   45,   45,  123,
   59,  125,   45,   43,   41,   45,   59,   44,  161,  162,
   40,   59,   60,   61,   62,  148,  269,  125,   41,   40,
   43,   58,   45,   43,   59,   45,  258,   45,  262,   43,
   43,   45,   45,  125,   41,   42,   59,   60,   61,   62,
   47,   41,   43,   43,   45,   45,   60,   61,   62,   59,
  256,  257,  185,  123,  260,   40,  262,  263,  264,   41,
   41,   42,   59,  269,  262,   59,   47,  257,   41,   40,
   43,  277,   45,  262,  263,  123,  257,  256,  257,  274,
  275,  260,  261,   59,  259,  264,  257,  272,  123,  259,
  269,  270,  271,  257,  273,   59,  257,  258,  277,  272,
  123,  256,  257,  274,  275,  260,  261,  258,   40,  264,
  258,  257,  258,  123,  269,  276,  256,  257,   41,  262,
  260,  257,  277,  262,  264,  265,  266,  257,  258,  269,
  276,  125,  256,  257,  274,  275,  260,  277,  274,  275,
  264,  265,  266,  257,  258,  269,  276,  123,  256,  257,
  274,  275,  260,  277,   59,  123,  264,  265,  266,  123,
  263,  269,  276,  257,  256,  257,  274,  275,  260,  277,
  257,  258,  264,  265,  266,  257,  258,  269,  257,  258,
  274,  275,  274,  275,  123,  277,  256,  257,   41,  276,
  260,  261,  270,  271,  264,  273,   58,  276,   58,  269,
  270,  271,  257,  273,   89,   90,   44,  277,  256,  257,
  257,  258,  260,  261,  257,  258,  264,   41,  123,   41,
  125,  269,  270,  271,   41,  273,   40,  262,  263,  277,
   41,  263,  263,  256,  257,  125,   41,  260,  261,  257,
  258,  264,   58,   47,   48,   58,  269,  270,  271,  125,
  273,  269,  262,  263,  277,  123,  270,  271,  276,  273,
  123,   58,  256,  257,  272,  123,  260,   41,  258,  263,
  264,  257,   59,   84,   85,  269,   58,  123,   59,  123,
   59,   59,  125,  277,   41,   41,  262,  263,  256,  257,
   94,   95,  260,  261,   59,  123,  264,   59,  262,  263,
  263,  269,   41,   42,  263,  123,  125,   59,   47,  277,
   59,   59,   59,  124,   59,  126,  127,  256,  257,  130,
   58,  260,  133,  134,  263,  264,  125,  125,   59,  125,
  269,  125,   59,   59,  125,   59,   59,  258,  277,  143,
  144,  145,  125,  257,   59,   59,   59,   59,  125,  125,
   59,  256,  257,  164,  165,  260,  167,  262,  263,  264,
   59,   59,  173,   59,  269,  125,  256,  257,    0,    0,
  260,  175,  277,   59,  264,  186,  180,  188,  189,  269,
   59,  125,   59,  125,   17,  196,  144,  277,  256,  257,
  247,   13,  260,  261,   31,   -1,  264,  125,   32,   67,
  204,  269,  213,  214,  215,  241,  217,  125,  241,  277,
  256,  257,  256,  257,  260,  261,  260,   -1,  264,   -1,
  264,  125,   -1,  269,   -1,  269,   -1,  238,  256,  257,
  241,  277,  260,  277,   -1,  125,  264,  125,  256,  257,
   -1,  269,  260,   -1,   -1,  256,  264,   -1,   -1,  277,
   -1,  269,   -1,  125,   -1,   -1,   -1,  256,  257,  277,
   -1,  260,   -1,  125,   -1,  264,  265,   -1,   -1,  125,
  269,   -1,   -1,  256,  257,  274,  275,  260,  277,  125,
   -1,  264,  265,   -1,   -1,  125,  269,   -1,   -1,   -1,
   -1,  274,  275,   -1,  277,   -1,  256,  257,   -1,   -1,
  260,   -1,   -1,   -1,  264,  265,   -1,   -1,   -1,  269,
   -1,   -1,  256,  257,  274,  275,  260,  277,   -1,   -1,
  264,  265,   -1,   -1,   -1,  269,   -1,   -1,  256,  257,
  274,  275,  260,  277,   -1,   -1,  264,   -1,  256,  257,
   -1,  269,  260,   -1,   -1,   -1,  264,   -1,   -1,  277,
   -1,  269,  256,  257,   -1,   -1,  260,   -1,   -1,  277,
  264,   -1,   -1,   -1,   -1,  269,  256,  257,  256,  257,
  260,   -1,  260,  277,  264,   -1,  264,   -1,   -1,  269,
   -1,  269,   -1,   -1,  256,  257,   -1,  277,  260,  277,
   -1,   -1,  264,   -1,  256,  257,   -1,  269,  260,   -1,
  256,  257,  264,   -1,  260,  277,   -1,  269,  264,   -1,
  256,  257,   -1,  269,  260,  277,  256,  257,  264,   -1,
  260,  277,   -1,  269,  264,   -1,  256,  257,   -1,  269,
  260,  277,   -1,   -1,  264,  265,  266,  277,   -1,  269,
   -1,   -1,  256,  257,  274,  275,  260,  277,   -1,   -1,
  264,  265,  266,   -1,   -1,  269,   -1,   -1,  256,  257,
  274,  275,  260,  277,   -1,   -1,  264,   -1,   -1,  267,
  268,  269,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  277,
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

//#line 640 "gramatica(copia).y"

//public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = "@";
public static final String nombreVariableContrato = "@contrato";

//public static String funcion_a_asignar = "";
public static StringBuilder ambito = new StringBuilder();

public static final List<Integer> posicionesPolaca = new ArrayList<>();
public static final List<String> polaca = new ArrayList<>();
public static final Stack pila = new Stack();
//private static String tipo;

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
                Lexema lexema = new Lexema(d);
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
                Lexema lexema = new Lexema(i);
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

/*
//Funcion que chequea si el tipo de parametro es valido para la funcion
public static boolean chequearParametro(String parametro, String funcion){
	String punt_parametro = TablaSimbolos.obtenerSimboloAmbito(parametro);
	String punt_funcion = TablaSimbolos.obtenerSimboloAmbito(funcion);
	
	String tipoParametro = TablaSimbolos.obtenerAtributo(punt_parametro, "tipo");
	String tipoParametroFuncion = TablaSimbolos.obtenerAtributo(punt_funcion, "tipo_parametro");
	
	return tipoParametro == tipoParametroFuncion;
}

*/

/*
public static void accionSemanticaFuncion0(String funcion){
	agregarToken(funcion_a_asignar);
	agregarToken(funcion);
	agregarToken("#CALL");
}
*/

/*
//Verifica el parametro en una llamada a funcion, agrega error en caso de ser incompatibles
public static void accionSemanticaFuncion1(String parametro, String funcion){
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
*/

/*
//Verifica los parametros en una llamada a funcion, agrega error en caso de que sean incompatibles
public static void accionSemanticaFuncion2(String parametro1, String parametro2, String funcion){
	if((chequearParametro(parametro1, funcion)) && (chequearParametro(parametro2, funcion))){
		agregarToken(funcion_a_asignar);
		agregarToken(funcion);
		agregarToken(parametro1);
		agregarToken(parametro2);
		agregarToken("#CALL");
	}
	else{
		agregarError(errores_semanticos, ERROR, "El tipo del parametro es distinto al provisto");
	}
}
*/

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


/*
public static void crearPunteroFuncion(String puntero_funcion, String funcion_llamada) {
        //tomo el tipo de dato de funcion_asignada y funcion de la tabla de simbolos
        String puntero_funcion_asignada = TablaSimbolos.obtenerSimbolo(puntero_funcion);
        String puntero_funcion_llamada = TablaSimbolos.obtenerSimbolo(funcion_llamada);

        String tipo_puntero = TablaSimbolos.obtenerAtributo(puntero_funcion_asignada, "tipo");
        String retorno_funcion_llamada = TablaSimbolos.obtenerAtributo(puntero_funcion_llamada, "retorno");
        
        boolean retorna_funciones = funcion_a_asignar.equals("") && retorno_funcion_llamada == TablaTipos.FUNC_TYPE;
        boolean es_funcion = !funcion_llamada.equals("");
        
        //pregunto si ninguno de ellos es distinto del tipo string
        if (tipo_puntero.equals(TablaTipos.FUNC_TYPE) && (es_funcion || retorna_funciones)) {
                //verifico que el atributo 'uso' del simbolo puntero sea: PUNTERO_FUNCION
        	String puntero_funcion_a_copiar;

                if (retorna_funciones) {
                        String lexema_a_copiar = TablaSimbolos.obtenerAtributo(puntero_funcion_llamada, "nombre_retorno");
                        puntero_funcion_a_copiar = TablaSimbolos.obtenerSimbolo(lexema_a_copiar);
                } else {
                        puntero_funcion_a_copiar = TablaSimbolos.obtenerSimbolo(funcion_a_asignar);
                }

                String uso_puntero = TablaSimbolos.obtenerAtributo(puntero_funcion_asignada, "uso");
                
                if (uso_puntero.equals("variable")) {
                        //agrego a los atributos de puntero_funcion todos los atributos de funcion en la tabla de simbolos, con excepcion del atributo 'uso' y 'lexema'
                        Map<String,String> atributos = TablaSimbolos.getAtr(puntero_funcion_a_copiar).getMas();
                        assert atributos != null;

                        TablaSimbolos.agregarAtributo(puntero_funcion_asignada, "funcion_asignada", atributos.get("lexema"));

                        for (String atributo : atributos.keySet()) {
                                if (atributo.equals("uso") || atributo.equals("lexema")) continue;  //no agrego el atributo uso
                                
                                TablaSimbolos.agregarAtributo(puntero_funcion_asignada, atributo, atributos.get(atributo));
                        }
                }

                funcion_a_asignar = "";   // reiniciamos la funcion a asignar           
        }
*/
//#line 922 "Parser.java"
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
					/*Parser.declarando = false;*/
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
					}
break;
case 6:
//#line 42 "gramatica(copia).y"
{
					asignarTipos(val_peek(2).sval);
					}
break;
case 7:
//#line 46 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
break;
case 8:
//#line 47 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
break;
case 9:
//#line 50 "gramatica(copia).y"
{ 
				var_aux.add(val_peek(0).sval);
                }
break;
case 10:
//#line 53 "gramatica(copia).y"
{ 
        		var_aux.add(val_peek(0).sval);
               }
break;
case 11:
//#line 58 "gramatica(copia).y"
{addEstructura("declaracion variables");
											 }
break;
case 12:
//#line 60 "gramatica(copia).y"
{addEstructura("declaracion funcion");
        		   }
break;
case 13:
//#line 64 "gramatica(copia).y"
{agregarToken(nombreFuncion());
										   salirAmbito();
										   /*Parser.declarando = true;*/
										   agregarToken("\\ENDP"); }
break;
case 14:
//#line 68 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una ejecucion_funcion");}
break;
case 15:
//#line 72 "gramatica(copia).y"
{
						String ptr1 = chequeoAmbito(val_peek(5).sval + Parser.ambito.toString()); /*ACOMODAR AMBITO*/
            			TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
            			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
            			cambiarAmbito(val_peek(5).sval);
            			procesarParametros(ptr1);
						/*String simb = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());*/
                        /*TablaSimbolos.agregarAtributo(simb, "tipo", TablaTipos.FUNC_TYPE);*/
                        /*TablaSimbolos.agregarAtributo(simb, "uso", TablaTipos.FUNC_TYPE);*/
                        /*TablaSimbolos.agregarAtributo(simb, "retorno", tipo);*/

                        /*TablaSimbolos.agregarSimb("@ret@" + $2.sval + Parser.ambito.toString());*/
                        /*String simb2 = TablaSimbolos.obtenerSimbolo("@ret@" + $2.sval + Parser.ambito.toString());*/
                        /*TablaSimbolos.agregarAtributo(simb2, "tipo", tipo);*/
                        /*TablaSimbolos.agregarAtributo(simb2, "uso", "variable");*/
					
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
break;
case 16:
//#line 90 "gramatica(copia).y"
{
						String ptr1 = chequeoAmbito(val_peek(4).sval + Parser.ambito.toString());  /*ACOMODAR AMBITO*/
            			TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(0).sval);
            			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de funcion");
            			TablaSimbolos.agregarAtributo(ptr1,"cantidad_parametros","0");
            			cambiarAmbito(val_peek(4).sval);
            			
						/*String simb = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());*/
                        /*TablaSimbolos.agregarAtributo(simb, "tipo", TablaTipos.FUNC_TYPE);*/
                        /*TablaSimbolos.agregarAtributo(simb, "uso", TablaTipos.FUNC_TYPE);*/
                        /*TablaSimbolos.agregarAtributo(simb, "retorno", tipo);*/

                        /*TablaSimbolos.agregarSimb("@ret@" + $2.sval + Parser.ambito.toString());*/
                        /*String simb2 = TablaSimbolos.obtenerSimbolo("@ret@" + $3.sval + Parser.ambito.toString());*/
                        /*TablaSimbolos.agregarAtributo(simb2, "tipo", tipo);*/
                        /*TablaSimbolos.agregarAtributo(simb2, "uso", "variable");*/
						
						/*Parser.declarando = false;*/
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
break;
case 17:
//#line 112 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 18:
//#line 113 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 19:
//#line 114 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 115 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 21:
//#line 116 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 24:
//#line 124 "gramatica(copia).y"
{    
						String ptr1 = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString()); /*ACOMODAR AMBITO*/
            			TablaSimbolos.agregarAtributo(ptr1,"tipo",val_peek(1).sval);
            			TablaSimbolos.agregarAtributo(ptr1,"uso","nombre de parametro");
            			par_aux.add(ptr1);
            			
						/*String simb = TablaSimbolos.obtenerSimbolo($2.sval + Parser.ambito.toString());*/
                        /*int primerSeparador = Parser.ambito.toString().indexOf(NAME_MANGLING_CHAR);*/
                        /*int ultimoSeparador = Parser.ambito.toString().lastIndexOf(NAME_MANGLING_CHAR);*/
                        /*String nombre_funcion = Parser.ambito.substring(ultimoSeparador + 1) + Parser.ambito.substring(primerSeparador, ultimoSeparador);*/
                        /*String simbFunc = TablaSimbolos.obtenerSimbolo(nombre_funcion);*/
                        
                        /*TablaSimbolos.agregarAtributo(simb, "tipo", tipo);*/
                        /*TablaSimbolos.agregarAtributo(simb, "uso", "parametro");*/
                        /*TablaSimbolos.agregarAtributo(simbFunc, "tipo_parametro", tipo);*/
                   }
break;
case 25:
//#line 141 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 26:
//#line 142 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 29:
//#line 152 "gramatica(copia).y"
{
                                                                        /*String simbFunc = TablaSimbolos.obtenerSimbolo(nombreFuncion());

                                                                        if (TablaSimbolos.obtenerAtributo(simbFunc, "retorno") == TablaTipos.FUNC_TYPE) {
                                                                                TablaSimbolos.agregarAtributo(simbFunc, "nombre_retorno", funcion_a_asignar);
                                                                                funcion_a_asignar = "";  
                                                                        }*/
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
break;
case 30:
//#line 164 "gramatica(copia).y"
{
        											/*String simbFunc = TablaSimbolos.obtenerSimbolo(nombreFuncion());
        																
                                                    if (TablaSimbolos.obtenerAtributo(simbFunc, "retorno").equals(TablaTipos.FUNC_TYPE)) {
                                                    	TablaSimbolos.agregarAtributo(simbFunc, "nombre_retorno", funcion_a_asignar);
                                                        funcion_a_asignar = "";  
                                                    }*/
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
break;
case 31:
//#line 175 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 32:
//#line 176 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 33:
//#line 177 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 34:
//#line 178 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 35:
//#line 179 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 45:
//#line 200 "gramatica(copia).y"
{addEstructura("if");}
break;
case 46:
//#line 201 "gramatica(copia).y"
{addEstructura("impresion");}
break;
case 47:
//#line 202 "gramatica(copia).y"
{addEstructura("while");}
break;
case 48:
//#line 203 "gramatica(copia).y"
{addEstructura("invocacion con discard");}
break;
case 49:
//#line 204 "gramatica(copia).y"
{addEstructura("error");}
break;
case 51:
//#line 210 "gramatica(copia).y"
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
//#line 218 "gramatica(copia).y"
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
//#line 226 "gramatica(copia).y"
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
//#line 234 "gramatica(copia).y"
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
//#line 242 "gramatica(copia).y"
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
//#line 250 "gramatica(copia).y"
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
//#line 258 "gramatica(copia).y"
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
//#line 266 "gramatica(copia).y"
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
//#line 275 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool ");}
break;
case 60:
//#line 276 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 61:
//#line 279 "gramatica(copia).y"
{ /*APILAR PASO INICIAL*/
					apilar(); }
break;
case 62:
//#line 283 "gramatica(copia).y"
{ /*GENERO BF INCOMPLETA Y APILO PASO INCOMPLETO*/
					apilar();
					agregarToken("SI");	
					agregarToken("#BF");			 
					}
break;
case 66:
//#line 295 "gramatica(copia).y"
{
					String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString()); /*ACOMODAR AMBITO*/
					}
break;
case 69:
//#line 304 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 70:
//#line 308 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
									}
break;
case 71:
//#line 312 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 72:
//#line 313 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 73:
//#line 314 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 74:
//#line 315 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 75:
//#line 316 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 76:
//#line 317 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 77:
//#line 318 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 78:
//#line 321 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("#BI");
								}
break;
case 79:
//#line 328 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 80:
//#line 335 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 81:
//#line 336 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 82:
//#line 337 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 83:
//#line 338 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 86:
//#line 344 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 87:
//#line 345 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 88:
//#line 346 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 89:
//#line 347 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 92:
//#line 353 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 93:
//#line 354 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 94:
//#line 357 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("#BF");			 
								}
break;
case 95:
//#line 363 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 96:
//#line 364 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 97:
//#line 365 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 98:
//#line 366 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 99:
//#line 370 "gramatica(copia).y"
{
								addEstructura("comparacion");
								/*agregarToken($2.sval);*/
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
case 106:
//#line 394 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
 						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());
        				String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
        				if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            				agregarToken(ptr1); 
            				agregarToken("=:");
        				} else {
            				agregarError(errores_semanticos,"Error","Tipos no compatibles en la asignacion.");
        				}

						/*String punt1 = TablaSimbolos.obtenerSimboloAmbito($1.sval + Parser.ambito.toString());*/
                        /*String punt3 = TablaSimbolos.obtenerSimboloAmbito($3.sval + Parser.ambito.toString());*/
                        /*agregarToken(punt1); */
                        /*agregarToken("=:");*/
                        /*crearPunteroFuncion(punt1, punt3);*/
                        }
break;
case 107:
//#line 411 "gramatica(copia).y"
{addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);}
break;
case 108:
//#line 413 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 111:
//#line 422 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
break;
case 112:
//#line 425 "gramatica(copia).y"
{
								/*agregarToken("+");*/
								String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        						String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        						TablaSimbolos.agregarSimbolo(val_peek(2).sval+"+"+val_peek(0).sval, new Lexema(val_peek(2).sval+"+"+val_peek(0).sval));
       							String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"+"+val_peek(0).sval);
        						TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        						if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            						TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
									agregarToken("+");
        						} else {
            						agregarError(errores_semanticos,"Error","Tipos no compatibles en la suma.");
        						}
								}
break;
case 113:
//#line 440 "gramatica(copia).y"
{
    						/*agregarToken("-");*/
    						String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					TablaSimbolos.agregarSimbolo(val_peek(2).sval+"+"+val_peek(0).sval, new Lexema(val_peek(2).sval+"+"+val_peek(0).sval));
       						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"+"+val_peek(0).sval);
        					TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            					TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
								agregarToken("+");
        					} else {
            					agregarError(errores_semanticos,"Error","Tipos no compatibles en la resta.");
        					}
    						}
break;
case 115:
//#line 457 "gramatica(copia).y"
{agregarToken("+");}
break;
case 116:
//#line 458 "gramatica(copia).y"
{agregarToken("-");}
break;
case 118:
//#line 462 "gramatica(copia).y"
{
							/*agregarToken("*");*/
							String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        					String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        					TablaSimbolos.agregarSimbolo(val_peek(2).sval+"+"+val_peek(0).sval, new Lexema(val_peek(2).sval+"+"+val_peek(0).sval));
       						String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"+"+val_peek(0).sval);
        					TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        					if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            					TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
								agregarToken("*");
        					} else {
            					agregarError(errores_semanticos,"Error","Tipos no compatibles en la multiplicacion.");
        					}
							}
break;
case 119:
//#line 477 "gramatica(copia).y"
{
    					/*agregarToken("/");*/
    					String ptr1 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
        				String ptr2 = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

        				TablaSimbolos.agregarSimbolo(val_peek(2).sval+"+"+val_peek(0).sval, new Lexema(val_peek(2).sval+"+"+val_peek(0).sval));
       					String ptr3 = TablaSimbolos.obtenerSimbolo(val_peek(2).sval+"+"+val_peek(0).sval);
        				TablaSimbolos.agregarAtributo(ptr3,"uso","auxiliar");
        				if (TablaSimbolos.obtenerAtributo(ptr1,"tipo").equals(TablaSimbolos.obtenerAtributo(ptr2,"tipo"))) {
            				TablaSimbolos.agregarAtributo(ptr3,"tipo",TablaSimbolos.obtenerAtributo(ptr1,"tipo")); /* le agrego el tipo a la variable auxiliar*/
							agregarToken("/");
        				} else {
            				agregarError(errores_semanticos,"Error","Tipos no compatibles en la division.");
        				}
    					}
break;
case 121:
//#line 495 "gramatica(copia).y"
{
			String ptr = chequeoAmbito(val_peek(0).sval + Parser.ambito.toString()); /*ACOMODAR AMBITO */
			agregarToken(ptr);
			
			/*String punt1 = TablaSimbolos.obtenerSimboloAmbito($1.sval + Parser.ambito.toString());*/
            /*agregarToken(punt1);*/
             
            /*if (TablaSimbolos.obtenerAtributo(punt1, "tipo").equals(TablaTipos.FUNC_TYPE))*/
            /*   funcion_a_asignar = punt1;*/
            }
break;
case 122:
//#line 506 "gramatica(copia).y"
{
    		String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
			agregarToken(ptr);
			
			/*String simb = TablaSimbolos.obtenerSimbolo($1.sval);*/
            /*TablaSimbolos.agregarAtributo(simb, "uso", "constante");*/
            /*agregarToken($1.sval);*/
            }
break;
case 123:
//#line 516 "gramatica(copia).y"
{
 			String ptr = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
    		TablaSimbolos.agregarAtributo(ptr, "uso", "constante");
    		String simb = negarConstante(ptr);
			agregarToken(simb);
 			
 			/*String simb = TablaSimbolos.obtenerSimbolo($2.sval);*/
            /*TablaSimbolos.agregarAtributo(simb, "uso", "constante");*/
            /*String simbNeg = negarConstante($2.sval);*/
            /*agregarToken(simbNeg);*/
    		 }
break;
case 124:
//#line 530 "gramatica(copia).y"
{ 
							String ptr1 = chequeoAmbito(val_peek(5).sval + Parser.ambito.toString()); /*ACOMODAR AMBITO*/
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
							
    					  /*String punt2 = TablaSimbolos.obtenerSimboloAmbito($2.sval + Parser.ambito.toString());*/
                          /*String punt4 = TablaSimbolos.obtenerSimboloAmbito($4.sval + Parser.ambito.toString());*/
                          /*String punt6 = TablaSimbolos.obtenerSimboloAmbito($6.sval + Parser.ambito.toString());*/
                          /*accionSemanticaFuncion2(punt4, punt6, punt2); */
                        }
break;
case 125:
//#line 565 "gramatica(copia).y"
{ 
    						String ptr1 = chequeoAmbito(val_peek(3).sval + Parser.ambito.toString());   /*ACOMODAR AMBITO*/
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
    
    					  /*String punt2 = TablaSimbolos.obtenerSimboloAmbito($2.sval + Parser.ambito.toString());*/
                          /*String punt4 = TablaSimbolos.obtenerSimboloAmbito($4.sval + Parser.ambito.toString());*/
                          /*accionSemanticaFuncion1(punt4, punt2); */
                            }
break;
case 126:
//#line 592 "gramatica(copia).y"
{ 
    
    						String ptr1 = chequeoAmbito(val_peek(2).sval + Parser.ambito.toString());      /*ACOMODAR AMBITO*/
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
        					
    					  /*String punt2 = TablaSimbolos.obtenerSimboloAmbito($2.sval + Parser.ambito.toString());*/
                          /*accionSemanticaFuncion0(punt2); */
                            }
break;
case 129:
//#line 621 "gramatica(copia).y"
{       
                                String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                String valor = val_peek(1).sval;
                                //String tipo = "string";
                                TablaSimbolos.agregarSimb(nombre);
                                String simb = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(simb, "valor", valor);
                                TablaSimbolos.agregarAtributo(simb, "tipo", "string");
                                agregarToken(nombre);    /*agregamos a la polaca el simbolo, junto identificador de cadenas, a la polaca */
                                contador_cadenas++; 
                                }
break;
case 130:
//#line 633 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 131:
//#line 634 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 132:
//#line 635 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1814 "Parser.java"
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
