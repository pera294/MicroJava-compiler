package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"break"   { return new_symbol(sym.BREAK, yytext()); }
"class"   { return new_symbol(sym.CLASS, yytext()); }
"const"   { return new_symbol(sym.CONST, yytext()); }
"else"   { return new_symbol(sym.ELSE, yytext()); }
"if"   { return new_symbol(sym.IF, yytext()); }
"while"   { return new_symbol(sym.WHILE, yytext()); }
"new"   { return new_symbol(sym.NEW, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read"   { return new_symbol(sym.READ, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"extends"   { return new_symbol(sym.EXTENDS, yytext()); }
"continue"   { return new_symbol(sym.CONTINUE, yytext()); }
"map"   { return new_symbol(sym.MAP, yytext()); }

"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.MOD, yytext()); }
"++" 		{ return new_symbol(sym.PLUSPLUS, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"--" 		{ return new_symbol(sym.MINUSMINUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"=>" 		{ return new_symbol(sym.LAMBDA, yytext()); }
"==" 		{ return new_symbol(sym.EQUALEQUAL, yytext()); }
"=" 		{ return new_symbol(sym.EQUAL, yytext()); }
"!=" 		{ return new_symbol(sym.NOTEQUAL, yytext()); }
"<=" 		{ return new_symbol(sym.LESSOREQUAL, yytext()); }
">=" 		{ return new_symbol(sym.GREATEROREQUAL, yytext()); }
"<" 		{ return new_symbol(sym.LESS, yytext()); }
">" 		{ return new_symbol(sym.GREATER, yytext()); }
"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }
":" 		{ return new_symbol(sym.COLON, yytext()); }
";" 		{ return new_symbol(sym.SEMI, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"." 		{ return new_symbol(sym.FULLSTOP, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"["			{ return new_symbol(sym.LBRACKET, yytext()); }
"]"			{ return new_symbol(sym.RBRACKET, yytext()); }

<YYINITIAL> "//" 		     { yybegin(COMMENT); } 
<COMMENT> "\r\n" { yybegin(YYINITIAL); }
<COMMENT> .      { yybegin(COMMENT); }

[0-9]+  { return new_symbol(sym.NUMCONST, new Integer (yytext())); }
"true"							{ return new_symbol(sym.BOOLCONST, new Boolean(yytext())); }
"false"							{ return new_symbol(sym.BOOLCONST, new Boolean(yytext())); }
"'"."'"                            { return new_symbol(sym.CHARCONST, new Character (yytext().charAt(1)));     }

([a-zA-Z])[a-zA-Z0-9_]* 	{return new_symbol (sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }



