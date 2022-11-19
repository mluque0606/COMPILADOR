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
    6,    6,    7,    8,    8,    8,    8,    8,    8,    8,
    8,    8,   10,   10,   11,   11,   11,    4,    4,    9,
    9,    9,    9,    9,    9,    9,   12,   12,   15,   15,
    2,    2,   14,   14,   16,   16,   16,   16,   16,   16,
   21,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   23,   24,   25,   25,   27,   27,   26,   26,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   31,   31,
   31,   31,   31,   31,   30,   30,   30,   30,   30,   30,
   32,   32,   32,   32,   29,   29,   29,   29,   29,   28,
   33,   33,   33,   33,   33,   33,   17,   17,   17,   34,
   34,   34,   13,   13,   13,   13,   13,   13,   35,   35,
   35,   37,   37,   37,   22,   22,   22,   36,   36,   19,
   19,   19,   19,
};
final static short yylen[] = {                            2,
    5,    1,    3,    3,    1,    3,    2,    1,    3,    1,
    1,    1,    2,    7,    6,    6,    5,    5,    6,    5,
    5,    4,    1,    3,    2,    1,    1,    1,    1,    9,
    8,   10,    6,    5,    8,    7,    2,    1,    2,    1,
    2,    1,    1,    1,    2,    2,    2,    1,    1,    2,
    2,    9,    9,    9,    6,   11,   11,   11,    8,    8,
    8,    1,    4,    2,    1,    1,    3,    2,    3,    4,
    5,    7,    7,    4,    5,    5,    3,    4,    5,    2,
    4,    1,    3,    4,    5,    2,    4,    1,    3,    4,
    5,    2,    4,    4,    3,    2,    2,    1,    2,    3,
    1,    1,    1,    1,    1,    1,    3,    4,    3,    2,
    3,    1,    3,    3,    1,    6,    6,    4,    3,    3,
    1,    1,    1,    2,    6,    4,    3,    1,    1,    4,
    3,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,    0,    0,    0,    0,    0,   62,
   28,   29,    0,    4,    0,   11,    0,    0,   44,   12,
    0,   42,   43,    0,    0,    0,   48,   49,    0,   50,
    0,    0,    0,  123,    0,    0,    0,    0,  129,    0,
    0,    0,  121,  128,  133,    0,    0,    0,    0,   51,
    0,   41,   10,    0,    7,    0,    0,   13,   45,   46,
   47,    0,    0,    0,    0,    0,    0,    0,    0,  124,
   99,    0,  104,  105,  106,    0,    0,  101,  102,  103,
    0,   96,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  131,   26,    0,    0,    0,    0,    0,    0,    0,
    1,    6,    9,    0,    0,   38,    0,    0,    0,    0,
    0,  108,    0,    0,  122,  127,    0,    0,    0,   95,
    0,    0,    0,    0,   74,    0,    0,    0,    0,    0,
   40,   70,    0,    0,    0,  119,  120,  130,    0,    0,
   22,   25,    0,    0,    0,    0,    0,    0,    0,   37,
    0,    0,    0,    0,    0,  110,    0,    0,    0,  126,
    0,    0,  118,    0,    0,    0,    0,    0,   39,   76,
    0,   75,    0,   71,    0,    0,   21,   24,   20,    0,
    0,    0,    0,    0,    0,    0,   63,    0,    0,  111,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   15,    0,   19,   34,    0,    0,    0,    0,
    0,   65,    0,    0,    0,   55,    0,  125,  116,  117,
   93,    0,   94,    0,   72,   73,   14,    0,    0,   33,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
    0,   59,   91,   36,    0,    0,    0,   60,   61,   68,
    0,    0,    0,    0,    0,    0,    0,    0,   31,   35,
    0,    0,   69,   67,   52,   53,   54,    0,    0,    0,
   30,    0,   56,   57,   58,   32,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   58,   97,
   98,  105,   38,  106,  165,   23,   24,   25,   26,   27,
   28,   39,   29,   63,  213,  239,  240,  109,   41,   87,
   88,  129,   81,  112,   42,   43,   44,
};
final static short yysindex[] = {                      -224,
    0,    0,  -70,  325,   38,  -28,  -29,  -25,  -40,    0,
    0,    0, -184,    0,  341,    0, -174,  104,    0,    0,
  -37,    0,    0,   48,   74,   93,    0,    0,   96,    0,
   66,  -96,  139,    0,  140,  -62,  -33,   89,    0,  142,
  170,   67,    0,    0,    0,  -22,   -6,   15,  139,    0,
  149,    0,    0,  134,    0,  -51,  498,    0,    0,    0,
    0,   30,  171,   73,   14,  -50,  176,    6,   70,    0,
    0,  179,    0,    0,    0,   79,   79,    0,    0,    0,
   70,    0,  -28,  -75,  185,    0,  -36, -103,   79,   79,
  215,    0,    0,   27, -158,   40,  253,  271,  261,  285,
    0,    0,    0,   -9,  514,    0,  -34,  288,  292,  -27,
  -18,    0,   70,  299,    0,    0,   -3,  147,  163,    0,
   67,   67,   14,  293,    0,  185, -100,    0,   80,  398,
    0,    0,  249,  185,  107,    0,    0,    0,  289,  328,
    0,    0, -158, -203, -158,  329,  248,   -5,    2,    0,
  263,  334,  121,  273,  356,    0,  143,  157,  108,    0,
   79,   79,    0,  408,  414,    0,  430,  358,    0,    0,
  -41,    0,  436,    0, -158,  364,    0,    0,    0, -158,
  365,  370,  340,  300,   22,  185,    0,  185,  309,    0,
  392,  394,  335,  337,  377,  452,  381,  389,    0,    0,
  174,  188,    0, -158,    0,    0,  330,  397,  399,  400,
  359,    0,  458,  468,  151,    0,  315,    0,    0,    0,
    0,  401,    0,    0,    0,    0,    0,  402,  332,    0,
  338,  405,  410,    0,  411,  -10,  395,  482,  346,  347,
  151,    0,    0,    0,  415,  417,  366,    0,    0,    0,
  219,  216,  419,  420,  421,  484,  360,  362,    0,    0,
  422,  382,    0,    0,    0,    0,    0,  425,  438,  439,
    0,  440,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  500,    0,    0,    1,    0,  442,    0,    0,
    0,    0,    0,    0,  502,    0,    0,   17,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,    0,    0,    0,  268,
    0,   39,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  156,    5,   78,    0,    0,    0,    0,
    0,  287,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -49,  445,    0,    0,    0,
    0,    0,    0,    0,    0,  117,    0,    8,    0,    0,
    0,    0,    0,    0,    0,    0,   34,    0,    0,    0,
  106,    0,    0,    0,    0,    0,    0,    0,  307,    0,
   61,   84,  -15,    0,    0,    0,    0,  226,    0,    0,
    0,    0,    0,    0,  449,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  111,   51,    0,    0,    0,
    0,    0,    0,    0,  387,  388,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  313,  378,    0,    0,    0,    0,  127,  132,
    0,    0,    0,  391,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  136,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  390,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,   -4,  499,    0,    0,    0,    0,  -16,
  375,  274,   24,   -2,    4,  279,  -23,    0,    0,  491,
    0,  517,  495,  464, -123,  295,  297,   21,    0,    0,
    0,  -59,    0,    0,   -7,   77,  -31,
};
final static int YYTABLESIZE=791;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   10,   22,  122,  122,  122,   68,  122,   71,  122,   88,
   37,   36,   52,  154,   46,   36,    8,  200,   92,  134,
  122,  122,  122,  122,  168,  100,  157,   40,  135,   32,
  148,  100,    1,   94,  251,  182,  117,  160,  108,   36,
  159,  185,   96,   96,   10,  107,  116,  126,   23,  147,
   36,   95,    4,   93,   65,   99,   76,   72,   77,   10,
  184,  119,  210,  107,  214,   23,   36,  139,  121,  122,
   11,   12,   49,   82,   36,  122,  122,  140,  122,  115,
  122,  115,   53,  115,  122,   57,  155,  127,  130,   96,
  141,  238,  118,  122,  122,  122,   30,  115,  115,  115,
  115,  113,  150,  113,  123,  113,   59,  100,   89,   89,
   36,  201,   68,   90,   36,   11,   12,  256,  109,  113,
  113,  113,  113,   36,  114,   10,  114,  192,  114,  167,
   32,   76,   60,   77,  191,   62,  109,  173,  177,   96,
  179,    8,  114,  114,  114,  114,  112,   56,   79,   80,
   78,   61,   36,  193,  194,    5,   83,   27,  133,    7,
   27,  115,   55,    8,  112,  136,  137,  196,   10,   92,
  203,  183,   10,   83,   27,  205,   13,   56,   68,   69,
    5,   83,   82,  113,    7,   90,  124,  125,    8,  161,
   87,  162,  102,   10,   85,   70,  122,  122,  122,  227,
  122,   13,  122,  163,   89,  103,  114,  101,  211,   90,
  110,  111,   82,   88,  122,  113,   47,  122,  122,  120,
  124,  122,  122,   33,   34,  122,  132,   33,   34,  153,
  122,  122,  122,   45,  122,   40,   91,   31,  122,  156,
  100,  100,   35,   31,  100,  100,   35,  250,  100,   84,
   93,   33,   34,  100,   81,  138,   10,   10,   79,  150,
   10,  100,  115,   34,   10,   10,   10,   11,   12,   10,
   35,   93,    8,    8,   10,   10,    8,   10,   33,   34,
    8,    8,    8,   93,   86,    8,  107,   34,   11,   12,
    8,    8,   85,    8,  115,  115,  142,   35,  115,  115,
   11,   12,  115,  122,  122,   35,  122,  115,  115,  115,
  143,  115,   83,   89,  144,  115,  113,  113,  145,   86,
  113,  113,   64,   34,  113,  146,   33,   34,  151,  113,
  113,  113,  152,  113,   10,   33,   34,  113,  158,  114,
  114,   35,  170,  114,  114,   35,  175,  114,   80,  115,
   40,  115,  114,  114,  114,  113,  114,  113,   73,   74,
  114,   75,  128,  131,  115,   34,   40,   40,  176,  174,
   40,  164,  181,   92,   40,  219,   89,  220,   89,   40,
  208,   90,   76,   90,   77,  186,  180,   40,   84,   90,
   98,  187,   31,   81,   87,  188,  189,   79,   85,  232,
  190,   76,  166,   77,  131,  169,    5,   83,  169,   97,
    7,  166,  131,  153,    8,  164,  199,  236,  237,   10,
  114,  204,  114,  206,  209,    5,   83,   13,  207,    7,
   84,  215,  217,    8,  218,  221,  225,  241,   10,  223,
    5,   83,  131,  169,    7,  169,   13,  224,    8,   14,
  226,  169,  252,   10,  228,  229,  245,  230,  231,  243,
  244,   13,  246,  247,  212,   51,  212,  216,  248,  249,
  254,  255,  264,  259,  169,  260,  263,  265,  266,  267,
  271,   40,   40,  273,  269,   40,  270,   80,   86,   40,
  261,  234,  234,  212,   40,  242,  274,  275,  276,    2,
  132,    3,   40,   77,    5,   83,  272,   78,    7,   17,
   18,  172,    8,   16,   66,   54,  234,   10,  178,  212,
  262,   66,  171,   98,   98,   13,   67,   98,   98,   50,
  114,   98,  195,    0,  234,  257,   98,  258,  197,    0,
    0,    0,   97,   97,   98,    0,   97,   97,    5,   83,
   97,    0,    7,    0,  198,   97,    8,    0,    0,    0,
  202,   10,    0,   97,    5,   83,    0,    0,    7,   13,
    5,   83,    8,    0,    7,    0,  222,   10,    8,    0,
    5,    6,  233,   10,    7,   13,    0,    0,    8,    9,
    0,   13,  235,   10,    0,    0,    5,    6,   11,   12,
    7,   13,    0,    0,    8,    9,  253,    0,  268,   10,
    0,    0,    0,    0,   11,   12,    0,   13,    0,    0,
    0,    5,    6,    0,    0,    7,    0,    0,    0,    8,
    9,    0,    0,    0,   10,    0,    0,    5,    6,   11,
   12,    7,   13,    0,    0,    8,    9,    0,    0,    0,
   10,    0,    0,    5,   83,   11,   12,    7,   13,    0,
    0,    8,    0,    5,   83,    0,   10,    7,    0,    5,
   83,    8,    0,    7,   13,    0,   10,    8,    0,    0,
    0,    0,   10,    0,   13,    5,   83,    0,    0,    7,
   13,    5,   83,    8,    0,    7,    0,    0,   10,    8,
    0,    0,    0,    0,   10,    0,   13,    5,   83,    0,
    0,    7,   13,    5,   83,    8,    0,    7,    0,    0,
   10,    8,    0,    5,   83,    0,   10,    7,   13,    0,
    0,    8,    0,    0,   13,    0,   10,    5,   83,    5,
   83,    7,    0,    7,   13,    8,    0,    8,    0,    0,
   10,    0,   10,    5,    6,    0,    0,    7,   13,    0,
   13,    8,    9,  104,    0,    0,   10,    0,    0,    5,
    6,   11,   12,    7,   13,    0,    0,    8,    9,  149,
    0,    0,   10,    0,    0,    0,    0,   11,   12,    0,
   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    4,   41,   42,   43,   40,   45,   41,   47,   59,
   40,   45,   15,   41,   40,   45,    0,   59,   41,  123,
   59,   60,   61,   62,  125,   41,   45,    7,   88,   58,
   40,   48,  257,   40,   45,   41,   68,   41,   62,   45,
   44,   40,   47,   48,   44,   41,   41,  123,   41,   59,
   45,   58,  123,  257,   31,   41,   43,   37,   45,   59,
   59,   69,   41,   59,  188,   58,   45,   41,   76,   77,
  274,  275,  257,  123,   45,   42,   43,   94,   45,   41,
   47,   43,  257,   45,  123,  123,  110,   84,   85,   94,
   95,  215,   69,   60,   61,   62,   59,   59,   60,   61,
   62,   41,  105,   43,   81,   45,   59,  123,   42,   59,
   45,  171,   40,   47,   45,  274,  275,  241,   41,   59,
   60,   61,   62,   45,   41,  125,   43,  159,   45,  126,
   58,   43,   59,   45,  158,   40,   59,  134,  143,  144,
  145,  125,   59,   60,   61,   62,   41,   44,   60,   61,
   62,   59,   45,  161,  162,  256,  257,   41,  262,  260,
   44,  123,   59,  264,   59,   89,   90,  164,  269,   59,
  175,  148,  269,  123,   58,  180,  277,   44,   40,   40,
  256,  257,   41,  123,  260,   59,  262,  263,  264,   43,
   59,   45,   59,  269,   59,  258,   41,   42,   43,  204,
   45,  277,   47,   41,   42,  257,  123,   59,  185,   47,
   40,  262,  262,  263,   59,   40,  257,  256,  257,   41,
  262,  260,  261,  257,  258,  264,  263,  257,  258,  257,
  269,  270,  271,  259,  273,  125,  259,  272,  277,  258,
  256,  257,  276,  272,  260,  261,  276,  258,  264,  123,
  257,  257,  258,  269,  123,   41,  256,  257,  123,  262,
  260,  277,  257,  258,  264,  265,  266,  274,  275,  269,
  276,  257,  256,  257,  274,  275,  260,  277,  257,  258,
  264,  265,  266,  257,   59,  269,  257,  258,  274,  275,
  274,  275,  123,  277,  256,  257,  257,  276,  260,  261,
  274,  275,  264,  270,  271,  276,  273,  269,  270,  271,
   58,  273,  262,  263,   44,  277,  256,  257,   58,   41,
  260,  261,  257,  258,  264,   41,  257,  258,   41,  269,
  270,  271,   41,  273,  269,  257,  258,  277,   40,  256,
  257,  276,  263,  260,  261,  276,   58,  264,  123,   43,
  125,   45,  269,  270,  271,   43,  273,   45,  270,  271,
  277,  273,   84,   85,  257,  258,  256,  257,   41,  263,
  260,  123,  125,  263,  264,   41,   42,   41,   42,  269,
   41,   47,   43,   47,   45,  123,   58,  277,  262,  263,
  123,   58,  272,  262,  263,  123,   41,  262,  263,   41,
  258,   43,  124,   45,  126,  127,  256,  257,  130,  123,
  260,  133,  134,  257,  264,  123,   59,  267,  268,  269,
   43,   58,   45,   59,  125,  256,  257,  277,   59,  260,
  261,  123,   41,  264,   41,   59,  263,  123,  269,   59,
  256,  257,  164,  165,  260,  167,  277,   59,  264,  125,
  263,  173,   58,  269,  125,   59,  125,   59,   59,   59,
   59,  277,  125,   59,  186,  125,  188,  189,   59,   59,
  125,  125,  257,   59,  196,   59,  258,   59,   59,   59,
   59,  256,  257,   59,  125,  260,  125,  262,  263,  264,
  125,  213,  214,  215,  269,  217,   59,   59,   59,    0,
   59,    0,  277,   59,  256,  257,  125,   59,  260,  123,
  123,  263,  264,  123,  125,   17,  238,  269,  144,  241,
  247,   31,  125,  256,  257,  277,   32,  260,  261,   13,
   67,  264,  125,   -1,  256,  241,  269,  241,  125,   -1,
   -1,   -1,  256,  257,  277,   -1,  260,  261,  256,  257,
  264,   -1,  260,   -1,  125,  269,  264,   -1,   -1,   -1,
  125,  269,   -1,  277,  256,  257,   -1,   -1,  260,  277,
  256,  257,  264,   -1,  260,   -1,  125,  269,  264,   -1,
  256,  257,  125,  269,  260,  277,   -1,   -1,  264,  265,
   -1,  277,  125,  269,   -1,   -1,  256,  257,  274,  275,
  260,  277,   -1,   -1,  264,  265,  125,   -1,  125,  269,
   -1,   -1,   -1,   -1,  274,  275,   -1,  277,   -1,   -1,
   -1,  256,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,  269,   -1,   -1,  256,  257,  274,
  275,  260,  277,   -1,   -1,  264,  265,   -1,   -1,   -1,
  269,   -1,   -1,  256,  257,  274,  275,  260,  277,   -1,
   -1,  264,   -1,  256,  257,   -1,  269,  260,   -1,  256,
  257,  264,   -1,  260,  277,   -1,  269,  264,   -1,   -1,
   -1,   -1,  269,   -1,  277,  256,  257,   -1,   -1,  260,
  277,  256,  257,  264,   -1,  260,   -1,   -1,  269,  264,
   -1,   -1,   -1,   -1,  269,   -1,  277,  256,  257,   -1,
   -1,  260,  277,  256,  257,  264,   -1,  260,   -1,   -1,
  269,  264,   -1,  256,  257,   -1,  269,  260,  277,   -1,
   -1,  264,   -1,   -1,  277,   -1,  269,  256,  257,  256,
  257,  260,   -1,  260,  277,  264,   -1,  264,   -1,   -1,
  269,   -1,  269,  256,  257,   -1,   -1,  260,  277,   -1,
  277,  264,  265,  266,   -1,   -1,  269,   -1,   -1,  256,
  257,  274,  275,  260,  277,   -1,   -1,  264,  265,  266,
   -1,   -1,  269,   -1,   -1,   -1,   -1,  274,  275,   -1,
  277,
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
"header_funcion : FUN ID '(' lista_parametros ')' ':' tipo",
"header_funcion : FUN ID '(' ')' ':' tipo",
"header_funcion : FUN ID '(' lista_parametros ')' ':'",
"header_funcion : FUN ID '(' ')' ':'",
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

//#line 469 "gramatica(copia).y"

public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ".";
public static final String nombreVariableContrato = "@contrato";

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
	String punt_parametro = TablaSimbolos.obtenerSimboloAmbito(parametro);
	String punt_funcion = TablaSimbolos.obtenerSimboloAmbito(funcion);
	
	String tipoParametro = TablaSimbolos.obtenerAtributo(punt_parametro, "tipo");
	String tipoParametroFuncion = TablaSimbolos.obtenerAtributo(punt_funcion, "tipo_parametro");
	
	return tipoParametro == tipoParametroFuncion;
}

public static void accionSemanticaFuncion0(String funcion){
	agregarToken(funcion_a_asignar);
	agregarToken(funcion);
	agregarToken("#CALL");
}

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
}
//#line 851 "Parser.java"
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
					Parser.declarando = false;
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
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
                String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable"); }
break;
case 10:
//#line 52 "gramatica(copia).y"
{ 
                String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable");
                }
break;
case 11:
//#line 59 "gramatica(copia).y"
{addEstructura("declaracion variables");
											 Parser.declarando = false; }
break;
case 12:
//#line 61 "gramatica(copia).y"
{addEstructura("declaracion funcion");
        		   Parser.declarando = false;}
break;
case 13:
//#line 65 "gramatica(copia).y"
{agregarToken(nombreFuncion());
										   salirAmbito();
										   Parser.declarando = true;
										   agregarToken("\\ENDP"); }
break;
case 14:
//#line 72 "gramatica(copia).y"
{
						String simb = TablaSimbolos.obtenerSimbolo(val_peek(5).sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb, "tipo", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "uso", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "retorno", tipo);

                        TablaSimbolos.agregarSimb("@ret@" + val_peek(5).sval + Parser.ambito.toString());
                        String simb2 = TablaSimbolos.obtenerSimbolo("@ret@" + val_peek(5).sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb2, "tipo", tipo);
                        TablaSimbolos.agregarAtributo(simb2, "uso", "variable");
						
						cambiarAmbito(val_peek(5).sval);
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
break;
case 15:
//#line 86 "gramatica(copia).y"
{
						String simb = TablaSimbolos.obtenerSimbolo(val_peek(4).sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb, "tipo", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "uso", TablaTipos.FUNC_TYPE);
                        TablaSimbolos.agregarAtributo(simb, "retorno", tipo);

                        TablaSimbolos.agregarSimb("@ret@" + val_peek(4).sval + Parser.ambito.toString());
                        String simb2 = TablaSimbolos.obtenerSimbolo("@ret@" + val_peek(3).sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(simb2, "tipo", tipo);
                        TablaSimbolos.agregarAtributo(simb2, "uso", "variable");
						
						cambiarAmbito(val_peek(4).sval);
						Parser.declarando = false;
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
break;
case 16:
//#line 101 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
break;
case 17:
//#line 102 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
break;
case 18:
//#line 103 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 19:
//#line 104 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 105 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 21:
//#line 106 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 22:
//#line 107 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 25:
//#line 115 "gramatica(copia).y"
{    
						String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                        int primerSeparador = Parser.ambito.toString().indexOf(NAME_MANGLING_CHAR);
                        int ultimoSeparador = Parser.ambito.toString().lastIndexOf(NAME_MANGLING_CHAR);
                        String nombre_funcion = Parser.ambito.substring(ultimoSeparador + 1) + Parser.ambito.substring(primerSeparador, ultimoSeparador);
                        String simbFunc = TablaSimbolos.obtenerSimbolo(nombre_funcion);
                        
                        TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                        TablaSimbolos.agregarAtributo(simb, "uso", "parametro");
                        TablaSimbolos.agregarAtributo(simbFunc, "tipo_parametro", tipo);
                   }
break;
case 26:
//#line 127 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 27:
//#line 128 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 28:
//#line 131 "gramatica(copia).y"
{tipo = TablaTipos.LONG_TYPE;}
break;
case 29:
//#line 132 "gramatica(copia).y"
{tipo = TablaTipos.FLOAT_TYPE;}
break;
case 30:
//#line 137 "gramatica(copia).y"
{
                                                                        String simbFunc = TablaSimbolos.obtenerSimbolo(nombreFuncion());

                                                                        if (TablaSimbolos.obtenerAtributo(simbFunc, "retorno") == TablaTipos.FUNC_TYPE) {
                                                                                TablaSimbolos.agregarAtributo(simbFunc, "nombre_retorno", funcion_a_asignar);
                                                                                funcion_a_asignar = "";  
                                                                        }
																		agregarToken("@ret@" + nombreFuncion());
																		agregarToken("\\RET");
																		}
break;
case 31:
//#line 148 "gramatica(copia).y"
{
        											String simbFunc = TablaSimbolos.obtenerSimbolo(nombreFuncion());
        																
                                                    if (TablaSimbolos.obtenerAtributo(simbFunc, "retorno").equals(TablaTipos.FUNC_TYPE)) {
                                                    	TablaSimbolos.agregarAtributo(simbFunc, "nombre_retorno", funcion_a_asignar);
                                                        funcion_a_asignar = "";  
                                                    }
        											agregarToken("@ret@" + nombreFuncion());
													agregarToken("\\RET");
													}
break;
case 32:
//#line 159 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 33:
//#line 160 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 34:
//#line 161 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 35:
//#line 162 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 36:
//#line 163 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 46:
//#line 184 "gramatica(copia).y"
{addEstructura("if");}
break;
case 47:
//#line 185 "gramatica(copia).y"
{addEstructura("impresion");}
break;
case 48:
//#line 186 "gramatica(copia).y"
{addEstructura("while");}
break;
case 49:
//#line 187 "gramatica(copia).y"
{addEstructura("invocacion con discard");}
break;
case 50:
//#line 188 "gramatica(copia).y"
{addEstructura("error");}
break;
case 52:
//#line 194 "gramatica(copia).y"
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
//#line 202 "gramatica(copia).y"
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
case 55:
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
case 56:
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
case 57:
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
case 58:
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
case 59:
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
case 60:
//#line 259 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool ");}
break;
case 61:
//#line 260 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 62:
//#line 263 "gramatica(copia).y"
{ /*APILAR PASO INICIAL*/
					apilar(); }
break;
case 63:
//#line 267 "gramatica(copia).y"
{ /*GENERO BF INCOMPLETA Y APILO PASO INCOMPLETO*/
					apilar();
					agregarToken("SI");	
					agregarToken("#BF");			 
					}
break;
case 70:
//#line 286 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 71:
//#line 290 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
									}
break;
case 72:
//#line 294 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 73:
//#line 295 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 74:
//#line 296 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 75:
//#line 297 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 76:
//#line 298 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 77:
//#line 299 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 78:
//#line 300 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 79:
//#line 303 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("#BI");
								}
break;
case 80:
//#line 310 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 81:
//#line 317 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 82:
//#line 318 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 83:
//#line 319 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 84:
//#line 320 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 87:
//#line 326 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 88:
//#line 327 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 89:
//#line 328 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 90:
//#line 329 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 93:
//#line 335 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 94:
//#line 336 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 95:
//#line 339 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("#BF");			 
								}
break;
case 96:
//#line 345 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 97:
//#line 346 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 98:
//#line 347 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 99:
//#line 348 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 100:
//#line 351 "gramatica(copia).y"
{addEstructura("comparacion");
												  agregarToken(val_peek(1).sval);;}
break;
case 107:
//#line 365 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
												String punt1 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                                                String punt3 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());

                                                agregarToken(punt1); 
                                                agregarToken("=:");
                                                crearPunteroFuncion(punt1, punt3);
                                                }
break;
case 108:
//#line 374 "gramatica(copia).y"
{addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);}
break;
case 109:
//#line 376 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 112:
//#line 385 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
break;
case 113:
//#line 388 "gramatica(copia).y"
{agregarToken("+");}
break;
case 114:
//#line 389 "gramatica(copia).y"
{agregarToken("-");}
break;
case 116:
//#line 391 "gramatica(copia).y"
{agregarToken("+");}
break;
case 117:
//#line 392 "gramatica(copia).y"
{agregarToken("-");}
break;
case 119:
//#line 396 "gramatica(copia).y"
{agregarToken("*");}
break;
case 120:
//#line 397 "gramatica(copia).y"
{agregarToken("/");}
break;
case 122:
//#line 401 "gramatica(copia).y"
{ 
			String punt1 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
            agregarToken(punt1);
             
            if (TablaSimbolos.obtenerAtributo(punt1, "tipo").equals(TablaTipos.FUNC_TYPE))
               funcion_a_asignar = punt1;
            }
break;
case 123:
//#line 409 "gramatica(copia).y"
{
			String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            agregarToken(val_peek(0).sval);
            }
break;
case 124:
//#line 415 "gramatica(copia).y"
{
 			String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            String simbNeg = negarConstante(val_peek(0).sval);
            agregarToken(simbNeg);
    		 }
break;
case 125:
//#line 424 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(4).sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                          String punt6 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
                          accionSemanticaFuncion2(punt4, punt6, punt2); 
                          }
break;
case 126:
//#line 431 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
                          accionSemanticaFuncion1(punt4, punt2); 
                          }
break;
case 127:
//#line 437 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(1).sval + Parser.ambito.toString());
                          accionSemanticaFuncion0(punt2); 
                          }
break;
case 130:
//#line 450 "gramatica(copia).y"
{       
                                String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                                String valor = val_peek(1).sval;
                                String tipo = "string";
                                TablaSimbolos.agregarSimb(nombre);
                                String simb = TablaSimbolos.obtenerSimbolo(nombre);
                                TablaSimbolos.agregarAtributo(simb, "valor", valor);
                                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                                agregarToken(nombre);    /*agregamos a la polaca el simbolo, junto identificador de cadenas, a la polaca */
                                contador_cadenas++; 
                                }
break;
case 131:
//#line 462 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 132:
//#line 463 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 133:
//#line 464 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1582 "Parser.java"
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
