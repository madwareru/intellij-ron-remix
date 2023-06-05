package com.github.madwareru.intellijronremix.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.github.madwareru.intellijronremix.language.psi.RONTypes.*;

%%

%{
  public __RONLexer() {
    this((java.io.Reader)null);
  }
%}

%{
  private int zzShaStride = -1;

  private int zzPostponedMarkedPos = -1;
%}

%{
  IElementType imbueRawLiteral() {
    yybegin(YYINITIAL);

    zzStartRead = zzPostponedMarkedPos;
    zzShaStride = -1;
    zzPostponedMarkedPos = -1;

    return RAW_STRING;
  }

  IElementType imbueBlockComment() {
    yybegin(YYINITIAL);

    zzStartRead = zzPostponedMarkedPos;
    zzPostponedMarkedPos = -1;

    return BLOCK_COMMENT;
  }
%}

%public
%class __RONLexer
%implements FlexLexer
%function advance
%type IElementType

%s IN_RAW_STRING
%s IN_RAW_STRING_SUFFIX

%s IN_BLOCK_COMMENT

%unicode

EOL=\R
WHITE_SPACE=\s+

COMMENT="//".*
BOOLEAN=true|false
RAW_IDENT="r#"[A-Za-z_0-9\.+-]+ // See IDENT_RAW_CHAR in https://github.com/ron-rs/ron/blob/8fa9995ea9d9288cb68bf90a9ddc6821e71d0158/src/parse.rs
IDENT={RAW_IDENT}|[A-Za-z_][A-Za-z0-9_]*
INTEGER=[+-]?((0x[0-9A-Fa-f][0-9A-Fa-f_]*)|(0b[0-1][0-1_]*)|(0o[0-7][0-7_]*)|([0-9][0-9_]*))

// translation of https://github.com/ron-rs/ron/blob/master/docs/grammar.md#numbers
FLOAT_STD=[0-9]+\.[0-9]*
FLOAT_FRAC=\.[0-9]+
EXP=[Ee][+-]?[0-9]+
FLOAT_NUM=({FLOAT_STD}|{FLOAT_FRAC})({EXP})?
FLOAT=[+-]?({FLOAT_NUM}|NaN|inf)

CHAR='([^\r\n\"] | (\\[\S]))'
STRING=\"([^\r\n\"]|(\\[\S]))*\"

%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  "BLOCK COMMENT"      { return BLOCK_COMMENT; }
  "RAW STRING"         { return RAW_STRING; }
  "("                  { return PARENTHESISL; }
  ")"                  { return PARENTHESISR; }
  "["                  { return BRACKETL; }
  "]"                  { return BRACKETR; }
  "{"                  { return BRACEL; }
  "}"                  { return BRACER; }
  ":"                  { return COLON; }
  ","                  { return COMMA; }
  "Some"               { return SOME; }
  "None"               { return NONE; }
  "#!["                { return EXT_PREFIX; }
  "enable"             { return ENABLE_KEYWORD; }

  "r" #* \"            {
                          yybegin(IN_RAW_STRING);
                          zzPostponedMarkedPos = zzStartRead;
                          zzShaStride = yylength() - 2;
                       }

  {COMMENT}            { return COMMENT; }
  {BOOLEAN}            { return BOOLEAN; }
  {IDENT}              { return IDENT; }
  {INTEGER}            { return INTEGER; }
  {FLOAT}              { return FLOAT; }
  {CHAR}               { return CHAR; }
  {STRING}             { return STRING; }

  "/*"                 {
                         yybegin(IN_BLOCK_COMMENT);
                         yypushback(2);
                       }
}

<IN_RAW_STRING> {
  \" #*                {
                          int shaExcess = yylength() - 1 - zzShaStride;
                          if (shaExcess >= 0) {
                              yybegin(IN_RAW_STRING_SUFFIX);
                              yypushback(shaExcess);
                          }
                       }
  [^]                  { }
  <<EOF>>              { return imbueRawLiteral(); }
}

<IN_RAW_STRING_SUFFIX> {
  [^]                  {
                          yypushback(1);
                          return imbueRawLiteral();
                       }
  <<EOF>>              { return imbueRawLiteral(); }
}

<IN_BLOCK_COMMENT> {
  "*/"                 { return imbueBlockComment(); }
  <<EOF>>              { return imbueBlockComment(); }
  [^]                  { }
}

[^] { return BAD_CHARACTER; }
