// This is a generated file. Not intended for manual editing.
package com.github.madwareru.intellijronremix.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.github.madwareru.intellijronremix.language.psi.RONTypes.*;
import static com.github.madwareru.intellijronremix.language.psi.RONParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class _RONParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return RON(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // [extensions] value
  static boolean RON(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "RON")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = RON_0(builder_, level_ + 1);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [extensions]
  private static boolean RON_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "RON_0")) return false;
    extensions(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // BOOLEAN
  static boolean bool(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, BOOLEAN);
  }

  /* ********************************************************** */
  // CHAR
  static boolean char_$(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, CHAR);
  }

  /* ********************************************************** */
  // field_name COLON value
  public static boolean early_pinned_named_field(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "early_pinned_named_field")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMED_FIELD, null);
    result_ = field_name(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, COLON));
    result_ = pinned_ && value(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // EXT_PREFIX ext_body BRACKETR
  public static boolean ext(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ext")) return false;
    if (!nextTokenIs(builder_, EXT_PREFIX)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXT, null);
    result_ = consumeToken(builder_, EXT_PREFIX);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, ext_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, BRACKETR) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // ENABLE_KEYWORD PARENTHESISL IDENT PARENTHESISR
  static boolean ext_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ext_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeTokens(builder_, 0, ENABLE_KEYWORD, PARENTHESISL, IDENT, PARENTHESISR);
    exit_section_(builder_, level_, marker_, result_, false, _RONParser::recoverBracket);
    return result_;
  }

  /* ********************************************************** */
  // ext+
  public static boolean extensions(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "extensions")) return false;
    if (!nextTokenIs(builder_, EXT_PREFIX)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = ext(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!ext(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "extensions", pos_)) break;
    }
    exit_section_(builder_, marker_, EXTENSIONS, result_);
    return result_;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean field_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_name")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENT);
    exit_section_(builder_, marker_, FIELD_NAME, result_);
    return result_;
  }

  /* ********************************************************** */
  // FLOAT
  static boolean float_$(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, FLOAT);
  }

  /* ********************************************************** */
  // INTEGER
  static boolean integer(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, INTEGER);
  }

  /* ********************************************************** */
  // BRACKETL list_body BRACKETR
  public static boolean list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list")) return false;
    if (!nextTokenIs(builder_, BRACKETL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LIST, null);
    result_ = consumeToken(builder_, BRACKETL);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, list_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, BRACKETR) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // [value (COMMA value)* [COMMA]]
  static boolean list_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    list_body_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, true, false, _RONParser::recoverBracket);
    return true;
  }

  // value (COMMA value)* [COMMA]
  private static boolean list_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = value(builder_, level_ + 1);
    result_ = result_ && list_body_0_1(builder_, level_ + 1);
    result_ = result_ && list_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA value)*
  private static boolean list_body_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_body_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!list_body_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "list_body_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA value
  private static boolean list_body_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_body_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean list_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_body_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // BRACEL map_inner_body BRACER
  public static boolean map(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map")) return false;
    if (!nextTokenIs(builder_, BRACEL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MAP, null);
    result_ = consumeToken(builder_, BRACEL);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, map_inner_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, BRACER) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // map_key COLON value
  public static boolean map_entry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_entry")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MAP_ENTRY, "<map entry>");
    result_ = map_key(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, _RONParser::recoverBraceOrComma);
    return result_;
  }

  /* ********************************************************** */
  // [map_entry (COMMA map_entry)* [COMMA]]
  static boolean map_inner_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_inner_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    map_inner_body_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, true, false, _RONParser::recoverBrace);
    return true;
  }

  // map_entry (COMMA map_entry)* [COMMA]
  private static boolean map_inner_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_inner_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = map_entry(builder_, level_ + 1);
    result_ = result_ && map_inner_body_0_1(builder_, level_ + 1);
    result_ = result_ && map_inner_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA map_entry)*
  private static boolean map_inner_body_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_inner_body_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!map_inner_body_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "map_inner_body_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA map_entry
  private static boolean map_inner_body_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_inner_body_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && map_entry(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean map_inner_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_inner_body_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // value
  public static boolean map_key(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_key")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MAP_KEY, "<map key>");
    result_ = value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // field_name COLON value
  public static boolean named_field(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_field")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMED_FIELD, null);
    result_ = field_name(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    pinned_ = result_; // pin = 2
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // [object_name] object_body
  public static boolean object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object")) return false;
    if (!nextTokenIs(builder_, "<object>", IDENT, PARENTHESISL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OBJECT, "<object>");
    result_ = object_0(builder_, level_ + 1);
    result_ = result_ && object_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [object_name]
  private static boolean object_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_0")) return false;
    object_name(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // PARENTHESISL object_inner_body PARENTHESISR
  public static boolean object_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body")) return false;
    if (!nextTokenIs(builder_, PARENTHESISL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PARENTHESISL);
    result_ = result_ && object_inner_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PARENTHESISR);
    exit_section_(builder_, marker_, OBJECT_BODY, result_);
    return result_;
  }

  /* ********************************************************** */
  // named_field | early_pinned_named_field
  static boolean object_entry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_entry")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = named_field(builder_, level_ + 1);
    if (!result_) result_ = early_pinned_named_field(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, _RONParser::recoverParenOrComma);
    return result_;
  }

  /* ********************************************************** */
  // named_field (COMMA object_entry)* [COMMA]
  static boolean object_inner_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_inner_body")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = named_field(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, object_inner_body_1(builder_, level_ + 1));
    result_ = pinned_ && object_inner_body_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, _RONParser::recoverParen);
    return result_ || pinned_;
  }

  // (COMMA object_entry)*
  private static boolean object_inner_body_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_inner_body_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!object_inner_body_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "object_inner_body_1", pos_)) break;
    }
    return true;
  }

  // COMMA object_entry
  private static boolean object_inner_body_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_inner_body_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && object_entry(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean object_inner_body_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_inner_body_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean object_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_name")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENT);
    exit_section_(builder_, marker_, OBJECT_NAME, result_);
    return result_;
  }

  /* ********************************************************** */
  // option_some | NONE
  public static boolean option(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "option")) return false;
    if (!nextTokenIs(builder_, "<option>", NONE, SOME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OPTION, "<option>");
    result_ = option_some(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NONE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // value
  static boolean option_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "option_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, _RONParser::recoverParen);
    return result_;
  }

  /* ********************************************************** */
  // SOME PARENTHESISL option_body PARENTHESISR
  static boolean option_some(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "option_some")) return false;
    if (!nextTokenIs(builder_, SOME)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeTokens(builder_, 2, SOME, PARENTHESISL);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, option_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, PARENTHESISR) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // !BRACER
  static boolean recoverBrace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverBrace")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, BRACER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(COMMA | BRACER)
  static boolean recoverBraceOrComma(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverBraceOrComma")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recoverBraceOrComma_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // COMMA | BRACER
  private static boolean recoverBraceOrComma_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverBraceOrComma_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, BRACER);
    return result_;
  }

  /* ********************************************************** */
  // !BRACKETR
  static boolean recoverBracket(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverBracket")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, BRACKETR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !PARENTHESISR
  static boolean recoverParen(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverParen")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, PARENTHESISR);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(COMMA | PARENTHESISR)
  static boolean recoverParenOrComma(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverParenOrComma")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recoverParenOrComma_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // COMMA | PARENTHESISR
  private static boolean recoverParenOrComma_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recoverParenOrComma_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, PARENTHESISR);
    return result_;
  }

  /* ********************************************************** */
  // STRING | RAW_STRING
  static boolean string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string")) return false;
    if (!nextTokenIs(builder_, "", RAW_STRING, STRING)) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING);
    if (!result_) result_ = consumeToken(builder_, RAW_STRING);
    return result_;
  }

  /* ********************************************************** */
  // [object_name] tuple_body
  public static boolean tuple(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple")) return false;
    if (!nextTokenIs(builder_, "<tuple>", IDENT, PARENTHESISL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TUPLE, "<tuple>");
    result_ = tuple_0(builder_, level_ + 1);
    result_ = result_ && tuple_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [object_name]
  private static boolean tuple_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_0")) return false;
    object_name(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // PARENTHESISL tuple_inner_body PARENTHESISR
  public static boolean tuple_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body")) return false;
    if (!nextTokenIs(builder_, PARENTHESISL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TUPLE_BODY, null);
    result_ = consumeToken(builder_, PARENTHESISL);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, tuple_inner_body(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, PARENTHESISR) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // [value (COMMA value)* [COMMA]]
  static boolean tuple_inner_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_inner_body")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    tuple_inner_body_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, true, false, _RONParser::recoverParen);
    return true;
  }

  // value (COMMA value)* [COMMA]
  private static boolean tuple_inner_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_inner_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = value(builder_, level_ + 1);
    result_ = result_ && tuple_inner_body_0_1(builder_, level_ + 1);
    result_ = result_ && tuple_inner_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA value)*
  private static boolean tuple_inner_body_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_inner_body_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!tuple_inner_body_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "tuple_inner_body_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA value
  private static boolean tuple_inner_body_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_inner_body_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean tuple_inner_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_inner_body_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // bool | integer | float | string | char | option | list | map | object | tuple | object_name
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VALUE, "<value>");
    result_ = bool(builder_, level_ + 1);
    if (!result_) result_ = integer(builder_, level_ + 1);
    if (!result_) result_ = float_$(builder_, level_ + 1);
    if (!result_) result_ = string(builder_, level_ + 1);
    if (!result_) result_ = char_$(builder_, level_ + 1);
    if (!result_) result_ = option(builder_, level_ + 1);
    if (!result_) result_ = list(builder_, level_ + 1);
    if (!result_) result_ = map(builder_, level_ + 1);
    if (!result_) result_ = object(builder_, level_ + 1);
    if (!result_) result_ = tuple(builder_, level_ + 1);
    if (!result_) result_ = object_name(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
