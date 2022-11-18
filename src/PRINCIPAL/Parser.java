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
import java.util.Map;
import java.util.Stack;
import AL.TablaTipos;

//#line 27 "Parser.java"




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
   20,   24,   24,   26,   26,   25,   25,   18,   18,   18,
   18,   18,   18,   18,   18,   18,   29,   29,   29,   29,
   29,   29,   28,   28,   28,   28,   28,   28,   30,   30,
   30,   30,   27,   27,   27,   27,   27,   23,   31,   31,
   31,   31,   31,   31,   17,   17,   17,   32,   32,   32,
   13,   13,   13,   13,   13,   13,   33,   33,   33,   35,
   35,   35,   22,   22,   22,   34,   34,   19,   19,   19,
   19,
};
final static short yylen[] = {                            2,
    5,    1,    3,    3,    1,    3,    2,    1,    3,    1,
    1,    1,    2,    7,    6,    6,    5,    5,    6,    5,
    5,    4,    1,    3,    2,    1,    1,    1,    1,    9,
    8,   10,    6,    5,    8,    7,    2,    1,    2,    1,
    2,    1,    1,    1,    2,    2,    2,    1,    1,    2,
    2,   12,   12,   12,    9,   14,   14,   14,   11,   11,
   11,    2,    1,    1,    3,    2,    3,    4,    5,    7,
    7,    4,    5,    5,    3,    4,    5,    2,    4,    1,
    3,    4,    5,    2,    4,    1,    3,    4,    5,    2,
    4,    4,    3,    2,    2,    1,    2,    3,    1,    1,
    1,    1,    1,    1,    3,    4,    3,    2,    3,    1,
    3,    3,    1,    6,    6,    4,    3,    3,    1,    1,
    1,    2,    6,    4,    3,    1,    1,    4,    3,    1,
    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   28,   29,    0,    4,    0,   11,    0,    0,   44,   12,
    0,   42,   43,    0,    0,    0,   48,   49,   50,    0,
    0,    0,  121,    0,    0,    0,    0,  127,    0,    0,
    0,  119,  126,  131,    0,    0,    0,    0,    0,   51,
    0,   41,   10,    0,    7,    0,    0,   13,   45,   46,
   47,    0,    0,    0,    0,    0,    0,  122,   97,    0,
  102,  103,  104,    0,    0,   99,  100,  101,    0,   94,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  129,
   26,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    6,    9,    0,    0,   38,    0,  106,    0,  120,
  125,    0,    0,    0,   93,    0,    0,    0,    0,   72,
    0,    0,    0,    0,    0,   40,   68,    0,    0,    0,
  117,  118,  128,    0,    0,   22,   25,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   37,  108,    0,    0,
    0,  124,    0,    0,  116,    0,    0,    0,    0,    0,
   39,   74,    0,   73,    0,   69,    0,    0,   21,   24,
   20,    0,    0,    0,    0,    0,    0,    0,    0,  109,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   15,    0,   19,    0,    0,    0,   34,    0,
    0,    0,    0,    0,    0,  123,  114,  115,   91,    0,
   92,    0,   70,   71,   14,    0,    0,    0,    0,    0,
   33,    0,    0,    0,   89,    0,    0,    0,   36,    0,
    0,    0,    0,   63,    0,    0,    0,   55,   31,   35,
    0,    0,    0,    0,   62,    0,    0,    0,    0,    0,
    0,   30,    0,    0,   59,   60,   61,   66,    0,    0,
    0,    0,    0,   32,    0,    0,    0,   67,   65,   52,
   53,   54,    0,    0,    0,   56,   57,   58,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   58,   95,
   96,  105,   37,  106,  157,   23,   24,   25,   26,   27,
   28,   38,   39,  235,  250,  251,   40,   85,   86,  124,
   79,  108,   41,   42,   43,
};
final static short yysindex[] = {                      -227,
    0,    0,  -58,  180,   19,  -44,   -7,  -30,  -21,   50,
    0,    0, -183,    0,  218,    0, -157,  -24,    0,    0,
  -11,    0,    0,   57,   80,   84,    0,    0,    0,   73,
 -124,  123,    0,  124,  -88,  -33,   95,    0,  134,  -84,
   -1,    0,    0,    0,  -39,  -27,   15,   -5,  123,    0,
  125,    0,    0,   88,    0,  -61,  456,    0,    0,    0,
    0,    4,   34,  -65,  171,    2,   51,    0,    0,  162,
    0,    0,    0,    6,    6,    0,    0,    0,   51,    0,
  -44,  -95,  120,    0,  -47, -108,    6,    6,  186,    0,
    0,   27, -115,  -36,  227,  200,  257,  285,  275,  293,
    0,    0,    0,   13,  472,    0,  -18,    0,   51,    0,
    0,   25,   46,  145,    0,   -1,   -1,   34,  263,    0,
  120,  338,    0,   77,  344,    0,    0,  -62,  120,   81,
    0,    0,    0,  290,  314,    0,    0, -115, -176, -115,
  306,  329,  312,  254,   22,   33,    0,    0,  115,  342,
   38,    0,    6,    6,    0,  360,  366,    0,  376,  326,
    0,    0,  -25,    0,  390,    0, -115,  334,    0,    0,
    0, -115,  133,  353,  339,  345,  103,  276,   30,    0,
  347,  362,  158,  311,  348,  401,  354,  361,    0,    0,
  143,  159,    0, -115,    0,  151,  383,  -23,    0,  300,
  367,  368,  369,  163,  389,    0,    0,    0,    0,  373,
    0,    0,    0,    0,    0,  319,  323,  406,  391,  336,
    0,  337,  405,  133,    0,  120,  120,  273,    0,  407,
  408,  289,  427,    0,  416,  426,  488,    0,    0,    0,
  411,  316,  279,  412,    0,  413,   10,  418,  432,  359,
  372,    0,  435,  488,    0,    0,    0,    0,  190,  229,
  439,  440,  441,    0,  442,  378,  381,    0,    0,    0,
    0,    0,  451,  454,  455,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  516,    0,    0,    1,    0,  462,    0,    0,
    0,    0,    0,    0,  522,    0,    0,   17,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -38,    0,    0,    0,    0,    0,    0,  174,    0,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  374,   35,   56,    0,    0,    0,    0,    0,  248,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -53,  465,    0,    0,    0,    0,    0,
    0,    0,    0,   76,    0,  100,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,    0,    0,   78,    0,   68,   90,  -15,    0,    0,
    0,    0,  196,    0,    0,    0,    0,    0,    0,  469,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  131,   48,    0,
    0,    0,    0,    0,    0,    0,  415,  421,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  136,  169,    0,    0,    0,    0,   55,   60,
    0,    0,    0,  424,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   94,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  409,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  622,  514,    0,    0,    0,    0,  -10,
  420,  320,  332,   -4,  -34,  253, -144,    0,    0,  525,
    0,  547,   16, -195,  307,  308,    0,    0,    0,  -70,
    0,    0,  -17,  101,  -41,
};
final static int YYTABLESIZE=816;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   10,   90,  120,  120,  120,   86,  120,   69,  120,   45,
   52,   35,   92,   31,  129,  130,    8,  217,   47,   56,
  120,  120,  120,  120,  112,   98,  149,  121,  197,    1,
   93,  236,   36,  190,   55,   99,   98,   35,   83,   35,
   87,  249,  111,   66,   10,   88,   35,  122,  125,  114,
   35,   70,  145,  218,  259,   97,  116,  117,  265,   10,
  156,   31,  176,  100,    4,  152,   35,  134,  151,   80,
  203,  144,  179,   49,   35,  105,   74,   29,   75,  233,
   91,  135,   35,  113,  120,  113,  159,  113,  153,   48,
  154,  178,  191,  105,  165,   35,  107,   11,   12,   53,
  147,  113,  113,  113,  113,  110,   87,   98,  111,  182,
  111,   57,  111,   88,  107,   59,   27,   35,   85,   27,
  113,  186,  113,  110,  150,   10,  111,  111,  111,  111,
  112,   56,  112,   27,  112,  183,  184,   74,   60,   75,
   23,    8,   61,  201,   65,   74,  102,   75,  112,  112,
  112,  112,   83,  128,   77,   78,   76,   23,   11,   12,
    5,   81,   66,   67,    7,  113,  119,  120,    8,   68,
   81,    5,   81,   10,   80,    7,   82,   82,  111,    8,
  111,   13,   79,  101,   10,  155,   87,  131,  132,   90,
  111,   88,   13,    5,   81,  103,  107,    7,  207,   87,
  164,    8,  115,  223,   88,   74,   10,   75,   80,   86,
  109,  112,  112,  112,   13,  127,   77,  120,  120,   89,
  137,  120,  120,   32,   33,  120,  133,   30,   44,   91,
  120,  120,  120,  196,  120,   46,  119,  147,  120,  148,
   98,   98,   34,  139,   98,   98,   11,   12,   98,   32,
   33,   32,   33,   98,   84,   40,   10,   10,  110,   33,
   10,   98,   32,   33,   10,   10,   10,  258,   34,   10,
   34,   91,    8,    8,   10,   10,    8,   10,   32,   33,
    8,    8,    8,   91,  138,    8,   32,   33,   11,   12,
    8,    8,   84,    8,  110,   33,   96,   34,  113,  113,
   11,   12,  113,  113,   14,   34,  113,   32,   33,   81,
   87,  113,  113,  113,  140,  113,   82,   88,   78,  113,
   40,   79,   85,  111,  111,  141,   34,  111,  111,   62,
   33,  111,  142,  143,  123,  126,  111,  111,  111,  162,
  111,   10,   51,  166,  111,  112,  112,  167,   34,  112,
  112,  208,   87,  112,  168,   77,   83,   88,  112,  112,
  112,   63,  112,  172,   71,   72,  112,   73,  173,  174,
   95,  158,  180,  126,  161,    5,   81,  161,  175,    7,
  158,  126,  181,    8,  189,  156,   40,   40,   10,  196,
   40,  194,  198,   90,   40,  237,   13,  199,  113,   40,
  202,  254,  206,  200,  205,  213,  209,   40,  126,  161,
  118,  161,  211,  241,  120,  120,  120,  161,  120,  212,
  120,  214,   30,  216,  219,  220,  221,  222,  224,   96,
   96,  225,  120,   96,   96,    5,    6,   96,  161,    7,
  253,  226,   96,    8,    9,  227,  228,  268,   10,  229,
   96,   40,   40,   11,   12,   40,   13,   78,   84,   40,
  230,  231,  160,  232,   40,  239,  240,  243,  163,  252,
  256,  257,   40,    5,    6,  260,  177,    7,  234,  234,
  238,    8,    9,  262,  185,  269,   10,  245,  245,  234,
  187,   11,   12,  264,   13,  255,  263,  270,  271,  272,
  188,  245,  274,   95,   95,  275,  234,   95,   95,  276,
  204,   95,  277,  278,  192,    2,   95,  245,    5,   81,
  130,    3,    7,   75,   95,  210,    8,   76,    5,   81,
   54,   10,    7,   64,    5,   81,    8,   17,    7,   13,
  244,   10,    8,   18,    5,    6,   16,   10,    7,   13,
  246,  242,    8,    9,   64,   13,  261,   10,  170,   50,
  266,  267,   11,   12,    0,   13,  273,    0,    0,    0,
    0,    5,    6,    0,    0,    7,    0,    0,    0,    8,
    9,    0,    0,    0,   10,    0,    0,    0,    0,   11,
   12,    0,   13,    5,   81,    0,    0,    7,    0,    5,
   81,    8,    0,    7,    0,    0,   10,    8,    0,    0,
    0,    0,   10,    0,   13,    5,   81,    0,    0,    7,
   13,    5,   81,    8,    0,    7,    0,    0,   10,    8,
    0,    5,   81,    0,   10,    7,   13,    0,    0,    8,
    0,    0,   13,    0,   10,    5,   81,    0,    0,    7,
    0,    0,   13,    8,    0,    0,    5,   81,   10,    0,
    7,    0,    0,    0,    8,    0,   13,   94,   94,   10,
    0,    5,   81,    0,    0,    7,    0,   13,    0,    8,
    0,    5,   81,    0,   10,    7,    0,    5,   81,    8,
    0,    7,   13,    0,   10,    8,    0,    5,   81,    0,
   10,    7,   13,    0,    0,    8,    0,    0,   13,    0,
   10,    5,    6,   94,  136,    7,    0,    0,   13,    8,
    9,  104,    0,    0,   10,    0,    0,    5,    6,   11,
   12,    7,   13,    0,    0,    8,    9,  146,    0,    0,
   10,    0,    0,    5,   81,   11,   12,    7,   13,    0,
    0,    8,    0,    0,  247,  248,   10,    0,    0,  169,
   94,  171,    0,    0,   13,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  193,    0,
    0,    0,    0,  195,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  215,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    0,   41,   41,   42,   43,   59,   45,   41,   47,   40,
   15,   45,   40,   58,  123,   86,    0,   41,   40,   44,
   59,   60,   61,   62,   66,   41,   45,  123,  173,  257,
   58,  227,   40,   59,   59,   41,   47,   45,  123,   45,
   42,  237,   41,   40,   44,   47,   45,   82,   83,   67,
   45,   36,   40,  198,   45,   41,   74,   75,  254,   59,
  123,   58,   41,   48,  123,   41,   45,   41,   44,  123,
   41,   59,   40,  257,   45,   41,   43,   59,   45,  224,
  257,   92,   45,   41,  123,   43,  121,   45,   43,   40,
   45,   59,  163,   59,  129,   45,   41,  274,  275,  257,
  105,   59,   60,   61,   62,   41,   59,  123,   41,  151,
   43,  123,   45,   59,   59,   59,   41,   45,   59,   44,
   43,  156,   45,   59,  109,  125,   59,   60,   61,   62,
   41,   44,   43,   58,   45,  153,  154,   43,   59,   45,
   41,  125,   59,   41,  269,   43,   59,   45,   59,   60,
   61,   62,   59,  262,   60,   61,   62,   58,  274,  275,
  256,  257,   40,   40,  260,  123,  262,  263,  264,  258,
  123,  256,  257,  269,   41,  260,  261,  123,   43,  264,
   45,  277,  123,   59,  269,   41,   42,   87,   88,   59,
  123,   47,  277,  256,  257,  257,  262,  260,   41,   42,
  263,  264,   41,   41,   47,   43,  269,   45,  262,  263,
   40,   43,  123,   45,  277,  263,  123,  256,  257,  259,
  257,  260,  261,  257,  258,  264,   41,  272,  259,  257,
  269,  270,  271,  257,  273,  257,  262,  242,  277,  258,
  256,  257,  276,   44,  260,  261,  274,  275,  264,  257,
  258,  257,  258,  269,   59,  125,  256,  257,  257,  258,
  260,  277,  257,  258,  264,  265,  266,  258,  276,  269,
  276,  257,  256,  257,  274,  275,  260,  277,  257,  258,
  264,  265,  266,  257,   58,  269,  257,  258,  274,  275,
  274,  275,   40,  277,  257,  258,  123,  276,  256,  257,
  274,  275,  260,  261,  125,  276,  264,  257,  258,  262,
  263,  269,  270,  271,   58,  273,  262,  263,  123,  277,
  125,  262,  263,  256,  257,   41,  276,  260,  261,  257,
  258,  264,   58,   41,   82,   83,  269,  270,  271,  263,
  273,  269,  125,  263,  277,  256,  257,   58,  276,  260,
  261,   41,   42,  264,   41,  262,  263,   47,  269,  270,
  271,   30,  273,   58,  270,  271,  277,  273,   40,   58,
  123,  119,  258,  121,  122,  256,  257,  125,  125,  260,
  128,  129,   41,  264,   59,  123,  256,  257,  269,  257,
  260,   58,   40,  263,  264,  123,  277,   59,   67,  269,
  125,  123,   41,   59,   58,  263,   59,  277,  156,  157,
   79,  159,   59,  125,   41,   42,   43,  165,   45,   59,
   47,  263,  272,   41,  125,   59,   59,   59,   40,  256,
  257,   59,   59,  260,  261,  256,  257,  264,  186,  260,
  125,  123,  269,  264,  265,  123,   41,  258,  269,   59,
  277,  256,  257,  274,  275,  260,  277,  262,  263,  264,
  125,  125,  125,   59,  269,   59,   59,   41,  125,   59,
   59,   59,  277,  256,  257,   58,  145,  260,  226,  227,
  228,  264,  265,  125,  125,  257,  269,  235,  236,  237,
  125,  274,  275,   59,  277,  243,  125,   59,   59,   59,
  125,  249,  125,  256,  257,  125,  254,  260,  261,   59,
  179,  264,   59,   59,  125,    0,  269,  265,  256,  257,
   59,    0,  260,   59,  277,  125,  264,   59,  256,  257,
   17,  269,  260,  125,  256,  257,  264,  123,  260,  277,
  125,  269,  264,  123,  256,  257,  123,  269,  260,  277,
  125,  232,  264,  265,   30,  277,  125,  269,  139,   13,
  254,  254,  274,  275,   -1,  277,  125,   -1,   -1,   -1,
   -1,  256,  257,   -1,   -1,  260,   -1,   -1,   -1,  264,
  265,   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,  274,
  275,   -1,  277,  256,  257,   -1,   -1,  260,   -1,  256,
  257,  264,   -1,  260,   -1,   -1,  269,  264,   -1,   -1,
   -1,   -1,  269,   -1,  277,  256,  257,   -1,   -1,  260,
  277,  256,  257,  264,   -1,  260,   -1,   -1,  269,  264,
   -1,  256,  257,   -1,  269,  260,  277,   -1,   -1,  264,
   -1,   -1,  277,   -1,  269,  256,  257,   -1,   -1,  260,
   -1,   -1,  277,  264,   -1,   -1,  256,  257,  269,   -1,
  260,   -1,   -1,   -1,  264,   -1,  277,   46,   47,  269,
   -1,  256,  257,   -1,   -1,  260,   -1,  277,   -1,  264,
   -1,  256,  257,   -1,  269,  260,   -1,  256,  257,  264,
   -1,  260,  277,   -1,  269,  264,   -1,  256,  257,   -1,
  269,  260,  277,   -1,   -1,  264,   -1,   -1,  277,   -1,
  269,  256,  257,   92,   93,  260,   -1,   -1,  277,  264,
  265,  266,   -1,   -1,  269,   -1,   -1,  256,  257,  274,
  275,  260,  277,   -1,   -1,  264,  265,  266,   -1,   -1,
  269,   -1,   -1,  256,  257,  274,  275,  260,  277,   -1,
   -1,  264,   -1,   -1,  267,  268,  269,   -1,   -1,  138,
  139,  140,   -1,   -1,  277,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  167,   -1,
   -1,   -1,   -1,  172,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  194,
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
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' break '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' continue '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' break '}' ';'",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' continue '}' ';'",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : WHILE '(' ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' ')' '{' ejecucion_iteracion '}' ';'",
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

//#line 395 "gramatica(copia).y"


public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ".";
public static final String nombreVariableContrato = "%";

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
	String tipoParametro = TablaSimbolos.obtenerAtributo(parametro, "tipo");
	String tipoParametroFuncion = TablaSimbolos.obtenerAtributo(funcion, "tipo_parametro");
	
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
//#line 840 "Parser.java"
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
//#line 23 "gramatica(copia).y"
{addEstructura("programa");}
break;
case 2:
//#line 24 "gramatica(copia).y"
{addEstructura("programa sin ejecucion");}
break;
case 3:
//#line 26 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba un '}' al final del programa");}
break;
case 4:
//#line 27 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba una sentencia de ejecucion");}
break;
case 5:
//#line 30 "gramatica(copia).y"
{cambiarAmbito(val_peek(0).sval);
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
					}
break;
case 7:
//#line 41 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
break;
case 8:
//#line 42 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
break;
case 9:
//#line 45 "gramatica(copia).y"
{ 
                String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable"); }
break;
case 10:
//#line 49 "gramatica(copia).y"
{ 
                String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable");
                }
break;
case 11:
//#line 56 "gramatica(copia).y"
{addEstructura("declaracion variables");}
break;
case 12:
//#line 57 "gramatica(copia).y"
{addEstructura("declaracion funcion");}
break;
case 13:
//#line 60 "gramatica(copia).y"
{agregarToken(nombreFuncion());
										   salirAmbito();
										   agregarToken("\\ENDP"); }
break;
case 14:
//#line 66 "gramatica(copia).y"
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
//#line 80 "gramatica(copia).y"
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
						agregarToken("!" + nombreFuncion().replace(':', '/'));}
break;
case 16:
//#line 94 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
break;
case 17:
//#line 95 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
break;
case 18:
//#line 96 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 19:
//#line 97 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 98 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 21:
//#line 99 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 22:
//#line 100 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 25:
//#line 108 "gramatica(copia).y"
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
//#line 120 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 27:
//#line 121 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 28:
//#line 124 "gramatica(copia).y"
{tipo = TablaTipos.LONG_TYPE;}
break;
case 29:
//#line 125 "gramatica(copia).y"
{tipo = TablaTipos.FLOAT_TYPE;}
break;
case 30:
//#line 130 "gramatica(copia).y"
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
//#line 141 "gramatica(copia).y"
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
//#line 152 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 33:
//#line 153 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 34:
//#line 154 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que la funcion retorne algun valor");}
break;
case 35:
//#line 155 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 36:
//#line 156 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "Se espera que tenga una expresion el return");}
break;
case 46:
//#line 177 "gramatica(copia).y"
{addEstructura("if");}
break;
case 47:
//#line 178 "gramatica(copia).y"
{addEstructura("impresion");}
break;
case 48:
//#line 179 "gramatica(copia).y"
{addEstructura("while");}
break;
case 49:
//#line 180 "gramatica(copia).y"
{addEstructura("invocacion con discard");}
break;
case 50:
//#line 181 "gramatica(copia).y"
{addEstructura("error");}
break;
case 60:
//#line 196 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool dentro de los '(' ')' ");}
break;
case 61:
//#line 197 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 68:
//#line 212 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 69:
//#line 216 "gramatica(copia).y"
{
									polaca.set((int)pila.pop(), Integer.toString(polaca.size()+1));
									}
break;
case 70:
//#line 220 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 71:
//#line 221 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 72:
//#line 222 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 73:
//#line 223 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 74:
//#line 224 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 75:
//#line 225 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 76:
//#line 226 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 77:
//#line 229 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 78:
//#line 236 "gramatica(copia).y"
{
								polaca.set((int)pila.pop(), Integer.toString(polaca.size()+3));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 79:
//#line 243 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 80:
//#line 244 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 81:
//#line 245 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 82:
//#line 246 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 85:
//#line 252 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 86:
//#line 253 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 87:
//#line 254 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 88:
//#line 255 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 91:
//#line 261 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 92:
//#line 262 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 93:
//#line 265 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("BF");			 
								}
break;
case 94:
//#line 271 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 95:
//#line 272 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 96:
//#line 273 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 97:
//#line 274 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 98:
//#line 277 "gramatica(copia).y"
{addEstructura("comparacion");
												 agregarToken(val_peek(1).sval);}
break;
case 99:
//#line 282 "gramatica(copia).y"
{ agregarToken(">");}
break;
case 105:
//#line 291 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
												String punt1 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                                                String punt3 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());

                                                agregarToken(punt1); 
                                                agregarToken("=:");
                                                crearPunteroFuncion(punt1, punt3);
                                                }
break;
case 106:
//#line 300 "gramatica(copia).y"
{addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);}
break;
case 107:
//#line 302 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 110:
//#line 311 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un valor luego de la sentencia ELSE");}
break;
case 111:
//#line 314 "gramatica(copia).y"
{agregarToken("+");}
break;
case 112:
//#line 315 "gramatica(copia).y"
{agregarToken("-");}
break;
case 114:
//#line 317 "gramatica(copia).y"
{agregarToken("+");}
break;
case 115:
//#line 318 "gramatica(copia).y"
{agregarToken("-");}
break;
case 117:
//#line 322 "gramatica(copia).y"
{agregarToken("*");}
break;
case 118:
//#line 323 "gramatica(copia).y"
{agregarToken("/");}
break;
case 120:
//#line 327 "gramatica(copia).y"
{ 
			String punt1 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
            agregarToken(punt1);
             
            if (TablaSimbolos.obtenerAtributo(punt1, "tipo").equals(TablaTipos.FUNC_TYPE))
               funcion_a_asignar = punt1;
            }
break;
case 121:
//#line 335 "gramatica(copia).y"
{
			String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            agregarToken(val_peek(0).sval);
            }
break;
case 122:
//#line 341 "gramatica(copia).y"
{
 			String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            String simbNeg = negarConstante(val_peek(0).sval);
            agregarToken(simbNeg);
    		 }
break;
case 123:
//#line 350 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(4).sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                          String punt6 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
                          accionSemanticaFuncion2(punt4, punt6, punt2); 
                          }
break;
case 124:
//#line 357 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
                          accionSemanticaFuncion1(punt4, punt2); 
                          }
break;
case 125:
//#line 363 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(1).sval + Parser.ambito.toString());
                          accionSemanticaFuncion0(punt2); 
                          }
break;
case 128:
//#line 376 "gramatica(copia).y"
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
case 129:
//#line 388 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 130:
//#line 389 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 131:
//#line 390 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1469 "Parser.java"
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
