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
import AL.TablaTipos;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
//#line 25 "Parser.java"




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
    8,   10,   10,   11,   11,   11,    4,    4,    9,    9,
    9,   12,   12,   15,   15,    2,    2,   14,   14,   16,
   16,   16,   16,   16,   16,   16,   21,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   24,   24,   26,   26,   25,   25,
   18,   18,   18,   18,   18,   18,   18,   18,   18,   29,
   29,   29,   29,   29,   29,   29,   28,   28,   28,   28,
   28,   28,   28,   30,   30,   30,   30,   27,   27,   27,
   27,   27,   23,   31,   31,   31,   31,   31,   31,   17,
   17,   17,   32,   32,   13,   13,   13,   13,   13,   13,
   33,   33,   33,   35,   35,   35,   22,   22,   22,   34,
   34,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    5,    1,    3,    3,    1,    3,    2,    1,    3,    1,
    1,    1,    2,    7,    6,    6,    5,    6,    5,    5,
    4,    1,    3,    2,    1,    1,    1,    1,    9,    8,
   10,    2,    1,    2,    1,    2,    1,    1,    1,    2,
    2,    2,    1,    1,    1,    2,    2,   12,   13,   13,
    9,   14,   15,   15,   11,    9,   11,   11,   11,   11,
    9,    7,    9,    8,    2,    1,    1,    3,    2,    3,
    4,    5,    7,    7,    4,    5,    5,    3,    4,    5,
    2,    4,    1,    3,    3,    3,    5,    2,    4,    1,
    3,    3,    3,    5,    2,    4,    4,    3,    2,    2,
    1,    2,    3,    1,    1,    1,    1,    1,    1,    3,
    4,    3,    2,    3,    3,    3,    1,    6,    6,    4,
    3,    3,    1,    1,    1,    2,    6,    4,    3,    1,
    1,    4,    3,    1,    2,
};
final static short yydefred[] = {                         0,
    5,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   27,   28,    0,    4,    0,   11,    0,    0,   39,   12,
    0,   37,   38,    0,    0,    0,   43,   44,   45,   46,
    0,    0,    0,    0,  125,    0,    0,    0,    0,  131,
    0,    0,    0,  123,  130,  135,    0,    0,    0,    0,
    0,    0,   47,    0,   36,   10,    0,    7,    0,    0,
   13,   40,   41,   42,    0,    0,    0,  124,  129,    0,
    0,    0,  126,  102,    0,  107,  108,  109,    0,    0,
  104,  105,  106,    0,   99,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  133,   25,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    6,    9,    0,
    0,   33,    0,  111,    0,  128,    0,    0,    0,   98,
    0,    0,    0,    0,   75,    0,    0,    0,    0,    0,
   35,   71,    0,    0,    0,  121,  122,  132,    0,    0,
   21,   24,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   32,  113,    0,    0,    0,    0,    0,  120,
    0,    0,    0,    0,    0,    0,   34,   77,    0,   76,
    0,   72,    0,    0,   20,   23,   19,    0,    0,    0,
    0,    0,    0,    0,  114,  127,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   15,    0,   18,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  118,  119,
   96,    0,   97,    0,   73,   74,   14,    0,    0,    0,
    0,   62,    0,   66,    0,    0,    0,    0,   94,    0,
    0,    0,    0,    0,    0,   64,    0,   65,    0,    0,
    0,    0,    0,   61,    0,   63,    0,   51,   56,   30,
    0,    0,    0,    0,    0,    0,    0,    0,   29,    0,
    0,   55,   59,   57,   60,   58,    0,    0,    0,    0,
    0,   31,    0,   69,    0,    0,   48,    0,    0,    0,
    0,    0,   70,   68,   49,   50,   52,    0,    0,   53,
   54,
};
final static short yydgoto[] = {                          2,
    3,   15,   16,   17,   18,   19,   20,   21,   61,  100,
  101,  111,   39,  112,  162,   23,   24,   25,   26,   27,
   28,   29,   41,  225,  270,  271,   42,   90,   91,  129,
   84,  114,   43,   44,   45,
};
final static short yysindex[] = {                      -235,
    0,    0,  -95,  378,  -18,  -19,   11,  -35,  -10,    8,
    0,    0, -200,    0,  403,    0, -198,   53,    0,    0,
  -40,    0,    0,   29,   32,   56,    0,    0,    0,    0,
   44,  -37, -175,   77,    0,   79, -130,  -22,  131,    0,
   90,  216,    2,    0,    0,    0,  -32,  -34,  -12,   22,
  121,   77,    0,   81,    0,    0,   74,    0,  -92,  626,
    0,    0,    0,    0,   35,  142,  -94,    0,    0,    9,
  133,   47,    0,    0,  130,    0,    0,    0,  -42,  -42,
    0,    0,    0,   47,    0,  -19,  174,  150,    0,  -85,
 -110,  -42,  -42,  145,    0,    0,   14, -121,  -63,  143,
  162,  151,  266,  235,  273,   62,    0,    0,    0,  286,
  648,    0,  -20,    0,   27,    0,   47,  154,    5,    0,
    2,    2,  142,  -97,    0,  323,  349,    0,   66,  494,
    0,    0,  270,  150,   70,    0,    0,    0,  279,  305,
    0,    0, -121, -189, -121,  292,  322,   50,  102,  331,
   47,  341,    0,    0,  119,  344,  348,  -42,  -42,    0,
  504,  520,    0,    0,  526,    0,    0,    0,   28,    0,
  536,    0, -121,  324,    0,    0,    0, -121,   62,   62,
  -38,  282,  106,   47,    0,    0,  354,  128,  148,  356,
  550,  358,  359,    0,  160,  161,    0, -121,    0,  372,
  379,  -29,  369,  139,  150,  373,  136,  393,    0,    0,
    0,  380,    0,    0,    0,    0,    0,  318,  317,  333,
  401,    0,  385,    0,  561,  327,  390,   62,    0,  150,
  150,  398,  150,  399,  339,    0,  402,    0,  406,  425,
  419,  572,  583,    0,  601,    0,  612,    0,    0,    0,
  407,  447,  367,  411,  412,  416,  420,  463,    0,  423,
  150,    0,    0,    0,    0,    0,   -2,  428,  432,  353,
  371,    0,  479,    0,  234,  241,    0,  441,  442,  443,
  383,  388,    0,    0,    0,    0,    0,  445,  451,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  516,    0,    0,    1,    0,  459,    0,    0,
    0,    0,    0,    0,  519,    0,    0,   17,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -27,    0,    0,    0,    0,    0,    0,
  288,    0,   39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -5,  107,  123,    0,    0,    0,
    0,    0,    0,    0,  299,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -49,  461,
    0,    0,    0,    0,    0,    0,    0,    0,   72,    0,
  100,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  157,    0,
   61,   84,  109,    0,    0,    0,    0,  -52,    0,    0,
    0,    0,    0,    0,  464,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  190,   65,   73,   80,    0,    0,    0,    0,
    0,    0,    0,  408,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  304,  313,    0,
    0,    0,    0,  201,    0,    0,    0,  409,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  252,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  404,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  753,  518,    0,    0,    0,    0,   13,
  392,  298,  337,   -4,  -23,  264,  -45,    0,    0,  509,
    0,  474,   20, -152,  268,  269,    0,    0,    0,  -64,
    0,    0,   -3,  267,   -1,
};
final static int YYTABLESIZE=951;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   10,  202,   37,   69,   47,   97,   88,   37,   95,   90,
   55,  220,  134,  124,  124,  124,    8,  124,   74,  124,
   32,    1,   37,   98,  155,  161,  135,    4,  102,   49,
   70,  124,  124,  124,  124,  124,  124,  124,   33,  124,
   30,  124,  275,   92,   10,  160,   92,   50,   93,  116,
   38,   93,  115,  124,  139,   37,   52,   75,   56,   10,
  150,  103,  104,  127,  130,   51,   37,   96,  119,  105,
   81,   37,   35,   83,   32,  121,  122,  242,  243,  117,
  245,  117,   60,  117,   11,   12,  194,   62,   37,  180,
   63,   37,   33,   71,  258,  124,   59,  117,  117,  117,
  117,  115,  165,  115,  195,  115,  153,  181,  273,  140,
  171,   58,   26,  156,   64,   26,   32,   59,   72,  115,
  115,  115,  115,   92,  116,   10,  116,   73,  116,   26,
   85,   91,  108,  200,  201,  204,  157,  191,   93,  107,
   22,    8,  116,  116,  116,  116,  206,  110,   79,  103,
   80,  133,   11,   12,  188,  189,  221,   22,    5,   86,
  106,  117,    7,  112,  109,  110,    8,  113,  209,   92,
  120,   10,  117,   79,   93,   80,  227,  132,   79,   13,
   80,  112,  241,  115,   79,  138,   80,   85,  210,   92,
   82,   83,   81,  142,   93,   84,  158,   62,  159,  117,
  143,  117,   86,   35,   35,  144,  116,   35,  145,   81,
   88,   35,   83,   90,   34,   35,   35,    5,   86,   68,
   35,    7,   96,   46,   35,    8,   94,  149,  124,  124,
   10,  103,  124,  124,   34,   35,  124,  154,   13,   11,
   12,  124,  124,  124,   96,  124,   48,  153,   95,  124,
  124,  124,   31,   36,  124,  274,   10,   10,  124,   89,
   10,   11,   12,  124,   10,   10,   10,   34,   35,   10,
   96,  124,    8,    8,   10,   10,    8,   10,   34,   35,
    8,    8,    8,   68,   35,    8,   36,   11,   12,  124,
    8,    8,  147,    8,  117,  117,  126,   36,  117,  117,
   65,   35,  117,   34,   35,   89,  146,  117,  117,  117,
   87,  117,   10,  148,   35,  117,  115,  115,  149,   36,
  115,  115,   36,   82,  115,  151,   85,   92,  168,  115,
  115,  115,  172,  115,   84,   91,  173,  115,   88,  116,
  116,   86,   93,  116,  116,  174,  115,  116,  115,  178,
  128,  131,  116,  116,  116,  116,  116,  116,  136,  137,
  116,  179,  110,  110,  103,  103,  110,   66,  103,  103,
  110,  182,  103,   31,   80,  110,  185,  103,  112,  112,
  184,  198,  112,  110,  186,  103,  112,  163,  187,  131,
  167,  112,  161,  167,    5,   86,  163,  131,    7,  112,
   76,   77,    8,   78,  205,    5,   86,   10,  118,    7,
  101,  208,  218,    8,  211,   13,  213,  214,   10,  219,
  123,  100,  215,  216,  131,  167,   13,  222,  167,    5,
   86,  226,  228,    7,  167,  124,  125,    8,  229,  231,
  230,  235,   10,  236,  203,   35,   35,  164,  240,   35,
   13,  239,   95,   35,  167,  233,  244,  246,   35,  253,
  249,  247,   82,   89,  250,  259,   35,  223,  224,  263,
  264,    5,   86,  166,  265,    7,   87,  278,  266,    8,
   40,  272,  232,  234,   10,  276,   53,  183,  238,  261,
  277,  283,   13,  224,  224,  279,  224,  284,  248,  285,
  286,  287,   14,  290,   40,  238,  238,  288,  238,  291,
  224,   40,  289,   80,   87,    2,  262,  134,    3,   78,
  207,  238,   79,   40,  224,    5,   86,   54,   67,    7,
   17,   16,  170,    8,   57,  176,  238,  252,   10,   67,
  281,  282,    0,  101,  101,   40,   13,  101,  101,  251,
    0,  101,   40,   40,  100,  100,  101,   40,  100,  100,
    0,    0,  100,    0,  101,   40,   40,  100,    0,    0,
    0,  260,    5,   86,    0,  100,    7,    0,    5,   86,
    8,    0,    7,    0,    0,   10,    8,  269,    5,   86,
   40,   10,    7,   13,    5,   86,    8,    0,    7,   13,
    0,   10,    8,  280,    5,   86,    0,   10,    7,   13,
    0,    0,    8,    0,    0,   13,    0,   10,  169,    0,
    0,    0,    5,   86,   40,   13,    7,    0,  190,    0,
    8,   40,   40,    5,    6,   10,    0,    7,    0,    0,
    0,    8,    9,   13,  192,    0,   10,    0,    0,    0,
  193,   11,   12,    0,   13,    0,    0,   40,    5,    6,
  196,    0,    7,    0,    0,    0,    8,    9,    0,    0,
    0,   10,    0,    0,  212,    0,   11,   12,    0,   13,
    5,    6,    0,    0,    7,  237,    0,    0,    8,    9,
    0,    0,    0,   10,    0,    0,  254,    0,   11,   12,
    0,   13,    5,    6,    0,    0,    7,  255,    0,    0,
    8,    9,    0,    0,    0,   10,    0,    0,    5,   86,
   11,   12,    7,   13,    0,  256,    8,    0,    0,  267,
  268,   10,    0,    0,    5,   86,  257,    0,    7,   13,
    0,    0,    8,    0,    0,  267,  268,   10,    0,    5,
   86,    0,    0,    7,    0,   13,    0,    8,    0,    5,
   86,    0,   10,    7,    0,    0,    0,    8,    0,    0,
   13,    0,   10,    0,    0,    5,   86,    0,    0,    7,
   13,    5,   86,    8,    0,    7,    0,    0,   10,    8,
    0,    5,   86,    0,   10,    7,   13,    0,    0,    8,
   99,   99,   13,    0,   10,    5,   86,    0,    0,    7,
    0,    0,   13,    8,    0,    0,    5,   86,   10,    0,
    7,    0,    0,    0,    8,    0,   13,    5,   86,   10,
    0,    7,    0,    0,    0,    8,    0,   13,    5,   86,
   10,    0,    7,    0,    0,    0,    8,    0,   13,   99,
  141,   10,    0,    0,    0,    0,    5,   86,    0,   13,
    7,    0,    0,    0,    8,    0,    0,    5,   86,   10,
    0,    7,    0,    0,    0,    8,    0,   13,    0,    0,
   10,    5,    6,    0,    0,    7,    0,    0,   13,    8,
    9,  110,    0,    0,   10,  175,   99,  177,    0,   11,
   12,    0,   13,    5,    6,    0,    0,    7,    0,    0,
    0,    8,    9,  152,    0,    0,   10,    0,    0,    0,
    0,   11,   12,    0,   13,  197,    0,    0,    0,    0,
  199,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  217,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    0,   40,   45,   41,   40,   40,   59,   45,   41,   59,
   15,   41,  123,   41,   42,   43,    0,   45,   41,   47,
   40,  257,   45,   58,   45,  123,   91,  123,   41,   40,
   32,   59,   60,   61,   62,   41,   42,   43,   58,   45,
   59,   47,   45,   42,   44,   41,   42,   40,   47,   41,
   40,   47,   44,   59,   41,   45,  257,   38,  257,   59,
  106,   49,   41,   87,   88,   58,   45,  257,   72,   50,
  123,   45,  125,  123,   40,   79,   80,  230,  231,   41,
  233,   43,  123,   45,  274,  275,   59,   59,   45,   40,
   59,   45,   58,  269,  247,  123,   44,   59,   60,   61,
   62,   41,  126,   43,  169,   45,  111,   58,  261,   97,
  134,   59,   41,  115,   59,   44,   40,   44,   40,   59,
   60,   61,   62,   59,   41,  125,   43,  258,   45,   58,
   41,   59,   59,  179,  180,  181,  117,  161,   59,   59,
   41,  125,   59,   60,   61,   62,   41,   41,   43,   41,
   45,  262,  274,  275,  158,  159,  202,   58,  256,  257,
   40,  123,  260,   41,  257,   59,  264,  262,   41,   42,
   41,  269,   40,   43,   47,   45,   41,  263,   43,  277,
   45,   59,  228,  123,   43,   41,   45,  123,   41,   42,
   60,   61,   62,  257,   47,  123,   43,   59,   45,   43,
   58,   45,  123,  256,  257,   44,  123,  260,   58,  262,
  263,  264,  262,  263,  257,  258,  269,  256,  257,  257,
  258,  260,  257,  259,  277,  264,  259,  257,  256,  257,
  269,  123,  260,  261,  257,  258,  264,  258,  277,  274,
  275,  269,  270,  271,  257,  273,  257,  252,   59,  277,
  256,  257,  272,  276,  260,  258,  256,  257,  264,   59,
  260,  274,  275,  269,  264,  265,  266,  257,  258,  269,
  257,  277,  256,  257,  274,  275,  260,  277,  257,  258,
  264,  265,  266,  257,  258,  269,  276,  274,  275,  262,
  274,  275,   58,  277,  256,  257,  123,  276,  260,  261,
  257,  258,  264,  257,  258,   42,   41,  269,  270,  271,
   59,  273,  269,   41,  125,  277,  256,  257,  257,  276,
  260,  261,  276,  123,  264,   40,  262,  263,  263,  269,
  270,  271,  263,  273,  262,  263,   58,  277,  123,  256,
  257,  262,  263,  260,  261,   41,   43,  264,   45,   58,
   87,   88,  269,  270,  271,   43,  273,   45,   92,   93,
  277,   40,  256,  257,  256,  257,  260,   31,  260,  261,
  264,   41,  264,  272,  123,  269,  258,  269,  256,  257,
   40,   58,  260,  277,   41,  277,  264,  124,   41,  126,
  127,  269,  123,  130,  256,  257,  133,  134,  260,  277,
  270,  271,  264,  273,  123,  256,  257,  269,   72,  260,
  123,   58,   41,  264,   59,  277,   59,   59,  269,   41,
   84,  123,  263,  263,  161,  162,  277,   59,  165,  256,
  257,   59,   40,  260,  171,  262,  263,  264,   59,  123,
  123,   41,  269,   59,  181,  256,  257,  125,   59,  260,
  277,  125,  263,  264,  191,  123,   59,   59,  269,   41,
   59,  123,  262,  263,   59,   59,  277,  204,  205,   59,
   59,  256,  257,  125,   59,  260,  261,  125,   59,  264,
    7,   59,  219,  220,  269,   58,   13,  151,  225,  123,
   59,  258,  277,  230,  231,  125,  233,  257,  235,   59,
   59,   59,  125,   59,   31,  242,  243,  125,  245,   59,
  247,   38,  125,  262,  263,    0,  253,   59,    0,   59,
  184,  258,   59,   50,  261,  256,  257,  125,  125,  260,
  123,  123,  263,  264,   17,  144,  273,  240,  269,   31,
  273,  273,   -1,  256,  257,   72,  277,  260,  261,  125,
   -1,  264,   79,   80,  256,  257,  269,   84,  260,  261,
   -1,   -1,  264,   -1,  277,   92,   93,  269,   -1,   -1,
   -1,  125,  256,  257,   -1,  277,  260,   -1,  256,  257,
  264,   -1,  260,   -1,   -1,  269,  264,  125,  256,  257,
  117,  269,  260,  277,  256,  257,  264,   -1,  260,  277,
   -1,  269,  264,  125,  256,  257,   -1,  269,  260,  277,
   -1,   -1,  264,   -1,   -1,  277,   -1,  269,  125,   -1,
   -1,   -1,  256,  257,  151,  277,  260,   -1,  125,   -1,
  264,  158,  159,  256,  257,  269,   -1,  260,   -1,   -1,
   -1,  264,  265,  277,  125,   -1,  269,   -1,   -1,   -1,
  125,  274,  275,   -1,  277,   -1,   -1,  184,  256,  257,
  125,   -1,  260,   -1,   -1,   -1,  264,  265,   -1,   -1,
   -1,  269,   -1,   -1,  125,   -1,  274,  275,   -1,  277,
  256,  257,   -1,   -1,  260,  125,   -1,   -1,  264,  265,
   -1,   -1,   -1,  269,   -1,   -1,  125,   -1,  274,  275,
   -1,  277,  256,  257,   -1,   -1,  260,  125,   -1,   -1,
  264,  265,   -1,   -1,   -1,  269,   -1,   -1,  256,  257,
  274,  275,  260,  277,   -1,  125,  264,   -1,   -1,  267,
  268,  269,   -1,   -1,  256,  257,  125,   -1,  260,  277,
   -1,   -1,  264,   -1,   -1,  267,  268,  269,   -1,  256,
  257,   -1,   -1,  260,   -1,  277,   -1,  264,   -1,  256,
  257,   -1,  269,  260,   -1,   -1,   -1,  264,   -1,   -1,
  277,   -1,  269,   -1,   -1,  256,  257,   -1,   -1,  260,
  277,  256,  257,  264,   -1,  260,   -1,   -1,  269,  264,
   -1,  256,  257,   -1,  269,  260,  277,   -1,   -1,  264,
   48,   49,  277,   -1,  269,  256,  257,   -1,   -1,  260,
   -1,   -1,  277,  264,   -1,   -1,  256,  257,  269,   -1,
  260,   -1,   -1,   -1,  264,   -1,  277,  256,  257,  269,
   -1,  260,   -1,   -1,   -1,  264,   -1,  277,  256,  257,
  269,   -1,  260,   -1,   -1,   -1,  264,   -1,  277,   97,
   98,  269,   -1,   -1,   -1,   -1,  256,  257,   -1,  277,
  260,   -1,   -1,   -1,  264,   -1,   -1,  256,  257,  269,
   -1,  260,   -1,   -1,   -1,  264,   -1,  277,   -1,   -1,
  269,  256,  257,   -1,   -1,  260,   -1,   -1,  277,  264,
  265,  266,   -1,   -1,  269,  143,  144,  145,   -1,  274,
  275,   -1,  277,  256,  257,   -1,   -1,  260,   -1,   -1,
   -1,  264,  265,  266,   -1,   -1,  269,   -1,   -1,   -1,
   -1,  274,  275,   -1,  277,  173,   -1,   -1,   -1,   -1,
  178,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  198,
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
"sentencia_ejecutable : invocacion",
"sentencia_ejecutable : error ';'",
"invocacion_con_d : DISCARD invocacion",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion break '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion continue '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion break '}' ';'",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' ejecucion_iteracion continue '}' ';'",
"iteracion_while : ID ':' WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' sentencia_ejecutable",
"iteracion_while : WHILE ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' asignacion ')' '{' '}' ';'",
"iteracion_while : WHILE '(' ')' ':' '(' asignacion ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' ')' '{' ejecucion_iteracion '}' ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' '(' asignacion ')' sentencia_ejecutable ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' sentencia_ejecutable ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' '(' ')' sentencia_ejecutable ';'",
"iteracion_while : WHILE '(' comparacion_bool ')' ':' asignacion sentencia_ejecutable ';'",
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
"then_seleccion : THEN '{' '}'",
"then_seleccion : THEN ejecucion_control '}'",
"then_seleccion_sin_else : THEN '{' ejecucion_control '}' ';'",
"then_seleccion_sin_else : THEN sentencia_ejecutable",
"then_seleccion_sin_else : '{' ejecucion_control '}' ';'",
"then_seleccion_sin_else : sentencia_ejecutable",
"then_seleccion_sin_else : THEN '{' ejecucion_control",
"then_seleccion_sin_else : THEN '{' '}'",
"then_seleccion_sin_else : THEN ejecucion_control '}'",
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

//#line 393 "gramatica(copia).y"


public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = "@";
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

/*public static void crearPunteroFuncion(String puntero_funcion, String funcion_llamada) {
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
}*/
//#line 865 "Parser.java"
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
//#line 21 "gramatica(copia).y"
{addEstructura("programa");}
break;
case 2:
//#line 22 "gramatica(copia).y"
{addEstructura("programa sin ejecucion");}
break;
case 3:
//#line 24 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba un '}' al final del programa");}
break;
case 4:
//#line 25 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperaba una sentencia de ejecucion");}
break;
case 5:
//#line 28 "gramatica(copia).y"
{cambiarAmbito(val_peek(0).sval);
					agregarToken (":START");
					TablaSimbolos.agregarSimb(nombreVariableContrato);
					TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(nombreVariableContrato), "tipo", TablaTipos.LONG_TYPE);
					}
break;
case 7:
//#line 39 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un tipo para declaracion_variable");}
break;
case 8:
//#line 40 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ';' al final de la declaracion");}
break;
case 9:
//#line 43 "gramatica(copia).y"
{ 
                String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable"); }
break;
case 10:
//#line 47 "gramatica(copia).y"
{ 
                String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                TablaSimbolos.agregarAtributo(simb, "tipo", tipo);
                TablaSimbolos.agregarAtributo(simb, "uso", "variable");
                }
break;
case 11:
//#line 54 "gramatica(copia).y"
{addEstructura("declaracion variables");}
break;
case 12:
//#line 55 "gramatica(copia).y"
{addEstructura("declaracion funcion");}
break;
case 13:
//#line 58 "gramatica(copia).y"
{agregarToken(nombreFuncion());
										   salirAmbito();
										   agregarToken("\\ENDP"); }
break;
case 14:
//#line 64 "gramatica(copia).y"
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
//#line 78 "gramatica(copia).y"
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
//#line 92 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo de retorno de la funcion");}
break;
case 17:
//#line 93 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera : y el tipo de retorno de la funcion");}
break;
case 18:
//#line 94 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 19:
//#line 95 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre de la funcion");}
break;
case 20:
//#line 96 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que los parametros esten entre parentesis");}
break;
case 21:
//#line 97 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Aunque la funcion no tenga parametros debe tener los parentesis");}
break;
case 24:
//#line 105 "gramatica(copia).y"
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
case 25:
//#line 117 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el tipo del parametro");}
break;
case 26:
//#line 118 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera el nombre del parametro");}
break;
case 27:
//#line 121 "gramatica(copia).y"
{tipo = TablaTipos.LONG_TYPE;}
break;
case 28:
//#line 122 "gramatica(copia).y"
{tipo = TablaTipos.FLOAT_TYPE;}
break;
case 29:
//#line 127 "gramatica(copia).y"
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
case 30:
//#line 138 "gramatica(copia).y"
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
case 31:
//#line 149 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error", "No puede haber mas sentencias despues del RETURN, debe ser lo ultimo");}
break;
case 41:
//#line 170 "gramatica(copia).y"
{addEstructura("if");}
break;
case 42:
//#line 171 "gramatica(copia).y"
{addEstructura("impresion");}
break;
case 43:
//#line 172 "gramatica(copia).y"
{addEstructura("while");}
break;
case 44:
//#line 173 "gramatica(copia).y"
{addEstructura("invocacion con discard");}
break;
case 45:
//#line 174 "gramatica(copia).y"
{agregarError(errores_sintacticos, "Error", "Se espera la palabra DISCARD antes del nombre de la funcion");}
break;
case 46:
//#line 175 "gramatica(copia).y"
{addEstructura("error");}
break;
case 56:
//#line 190 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool antes del ':' ");}
break;
case 57:
//#line 191 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ':' luego de la comparacion_bool");}
break;
case 58:
//#line 192 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una ejecucion luego de la ASIGNACION");}
break;
case 59:
//#line 193 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una comparacion_bool dentro de los '(' ')' ");}
break;
case 60:
//#line 194 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion dentro de los '(' ')'  ");}
break;
case 61:
//#line 195 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ':' luego de la comparacion_bool");}
break;
case 62:
//#line 196 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion luego del ':' ");}
break;
case 63:
//#line 197 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una asignacion entre los parentesis");}
break;
case 64:
//#line 198 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la asignacion se encuentre entre parentesis");}
break;
case 71:
//#line 213 "gramatica(copia).y"
{
									polaca.add((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 72:
//#line 217 "gramatica(copia).y"
{
									polaca.add((int)pila.pop(), Integer.toString(polaca.size()));
									}
break;
case 73:
//#line 221 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se esperan un THEN");}
break;
case 74:
//#line 222 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un ELSE");}
break;
case 75:
//#line 223 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 76:
//#line 224 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del ELSE");}
break;
case 77:
//#line 225 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera bloque de sentencias luego del THEN");}
break;
case 78:
//#line 226 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 79:
//#line 227 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera END_IF al final");}
break;
case 80:
//#line 230 "gramatica(copia).y"
{
								polaca.add((int)pila.pop(), Integer.toString(polaca.size()+2));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 81:
//#line 237 "gramatica(copia).y"
{
								polaca.add((int)pila.pop(), Integer.toString(polaca.size()+2));
								apilar();
								agregarToken("SI");
								agregarToken("BI");
								}
break;
case 82:
//#line 244 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 83:
//#line 245 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 84:
//#line 246 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 85:
//#line 247 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del THEN");}
break;
case 86:
//#line 248 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 89:
//#line 254 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de { ");}
break;
case 90:
//#line 255 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera THEN antes de la sentencia ejecutable");}
break;
case 91:
//#line 256 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '}' luego de las sentencias del THEN");}
break;
case 92:
//#line 257 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del THEN");}
break;
case 93:
//#line 258 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un '{' para comenzar el THEN");}
break;
case 96:
//#line 264 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera sentencias dentro del cuerpo del ELSE");}
break;
case 97:
//#line 265 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '{' luego del ELSE");}
break;
case 98:
//#line 268 "gramatica(copia).y"
{
								apilar();
								agregarToken("SI");	
								agregarToken("BF");			 
								}
break;
case 99:
//#line 274 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera '(' al principio de la comparacion");}
break;
case 100:
//#line 275 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera ')' al final de la comparacion");}
break;
case 101:
//#line 276 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la comparacion se encuentre entre parentesis");}
break;
case 102:
//#line 277 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una condicion de comparacion");}
break;
case 103:
//#line 280 "gramatica(copia).y"
{addEstructura("comparacion");
												  agregarToken(val_peek(1).sval);}
break;
case 110:
//#line 297 "gramatica(copia).y"
{addEstructura(val_peek(2).sval + " asignacion " + val_peek(0).sval);
												String punt1 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                                                String punt3 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());

                                                agregarToken(punt1); 
                                                agregarToken(val_peek(1).sval);
                                                /*crearPunteroFuncion(punt1, punt3);*/
                                                }
break;
case 111:
//#line 306 "gramatica(copia).y"
{addEstructura(val_peek(3).sval + " asignacion " + val_peek(1).sval);}
break;
case 112:
//#line 308 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera un else luego del while");}
break;
case 115:
//#line 315 "gramatica(copia).y"
{agregarToken("+");}
break;
case 116:
//#line 316 "gramatica(copia).y"
{agregarToken("-");}
break;
case 118:
//#line 318 "gramatica(copia).y"
{agregarToken("+");}
break;
case 119:
//#line 319 "gramatica(copia).y"
{agregarToken("-");}
break;
case 121:
//#line 323 "gramatica(copia).y"
{agregarToken("*");}
break;
case 122:
//#line 324 "gramatica(copia).y"
{agregarToken("/");}
break;
case 124:
//#line 328 "gramatica(copia).y"
{ 
			String punt1 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
            agregarToken(punt1);
             
            if (TablaSimbolos.obtenerAtributo(punt1, "tipo").equals(TablaTipos.FUNC_TYPE))
               funcion_a_asignar = punt1;
            }
break;
case 125:
//#line 336 "gramatica(copia).y"
{
			String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            agregarToken(val_peek(0).sval);
            }
break;
case 126:
//#line 342 "gramatica(copia).y"
{
 			String simb = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
            TablaSimbolos.agregarAtributo(simb, "uso", "constante");
            String simbNeg = negarConstante(val_peek(0).sval);
            agregarToken(simbNeg);
    		 }
break;
case 127:
//#line 351 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(4).sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                          String punt6 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
                          accionSemanticaFuncion2(punt4, punt6, punt2); 
                          }
break;
case 128:
//#line 358 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(2).sval + Parser.ambito.toString());
                          String punt4 = TablaSimbolos.obtenerSimboloAmbito(val_peek(0).sval + Parser.ambito.toString());
                          accionSemanticaFuncion1(punt4, punt2); 
                          }
break;
case 129:
//#line 364 "gramatica(copia).y"
{ 
    					  String punt2 = TablaSimbolos.obtenerSimboloAmbito(val_peek(1).sval + Parser.ambito.toString());
                          accionSemanticaFuncion0(punt2); 
                          }
break;
case 132:
//#line 374 "gramatica(copia).y"
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
case 133:
//#line 386 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena dentro del OUT");}
break;
case 134:
//#line 387 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera una cadena entre parentesis luego del OUT");}
break;
case 135:
//#line 388 "gramatica(copia).y"
{agregarError(errores_sintacticos,"Error","Se espera que la cadena entre parentesis");}
break;
//#line 1506 "Parser.java"
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
