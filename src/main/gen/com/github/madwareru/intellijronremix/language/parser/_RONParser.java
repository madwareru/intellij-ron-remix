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
  public static boolean bool(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bool")) return false;
    if (!nextTokenIs(builder_, BOOLEAN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BOOLEAN);
    exit_section_(builder_, marker_, BOOL, result_);
    return result_;
  }

  /* ********************************************************** */
  // CHAR
  static boolean char_$(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, CHAR);
  }

  /* ********************************************************** */
  // IDENT
  public static boolean enum_$(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_$")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, IDENT);
    exit_section_(builder_, marker_, ENUM, result_);
    return result_;
  }

  /* ********************************************************** */
  // EXT_PREFIX ENABLE_KEYWORD PARENTHESISL IDENT PARENTHESISR BRACKETR
  public static boolean ext(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ext")) return false;
    if (!nextTokenIs(builder_, EXT_PREFIX)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, EXT_PREFIX, ENABLE_KEYWORD, PARENTHESISL, IDENT, PARENTHESISR, BRACKETR);
    exit_section_(builder_, marker_, EXT, result_);
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
  // BRACKETL [value (COMMA value)* [COMMA]] BRACKETR
  public static boolean list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list")) return false;
    if (!nextTokenIs(builder_, BRACKETL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BRACKETL);
    result_ = result_ && list_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, BRACKETR);
    exit_section_(builder_, marker_, LIST, result_);
    return result_;
  }

  // [value (COMMA value)* [COMMA]]
  private static boolean list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_1")) return false;
    list_1_0(builder_, level_ + 1);
    return true;
  }

  // value (COMMA value)* [COMMA]
  private static boolean list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = value(builder_, level_ + 1);
    result_ = result_ && list_1_0_1(builder_, level_ + 1);
    result_ = result_ && list_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA value)*
  private static boolean list_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!list_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "list_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA value
  private static boolean list_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean list_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "list_1_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // BRACEL [map_entry (COMMA map_entry)* [COMMA]] BRACER
  public static boolean map(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map")) return false;
    if (!nextTokenIs(builder_, BRACEL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BRACEL);
    result_ = result_ && map_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, BRACER);
    exit_section_(builder_, marker_, MAP, result_);
    return result_;
  }

  // [map_entry (COMMA map_entry)* [COMMA]]
  private static boolean map_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_1")) return false;
    map_1_0(builder_, level_ + 1);
    return true;
  }

  // map_entry (COMMA map_entry)* [COMMA]
  private static boolean map_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = map_entry(builder_, level_ + 1);
    result_ = result_ && map_1_0_1(builder_, level_ + 1);
    result_ = result_ && map_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA map_entry)*
  private static boolean map_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!map_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "map_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA map_entry
  private static boolean map_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && map_entry(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean map_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_1_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
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
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // bool | integer | float | string | char | enum
  public static boolean map_key(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_key")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MAP_KEY, "<map key>");
    result_ = bool(builder_, level_ + 1);
    if (!result_) result_ = integer(builder_, level_ + 1);
    if (!result_) result_ = float_$(builder_, level_ + 1);
    if (!result_) result_ = string(builder_, level_ + 1);
    if (!result_) result_ = char_$(builder_, level_ + 1);
    if (!result_) result_ = enum_$(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENT COLON value
  public static boolean named_field(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_field")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, IDENT, COLON);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, NAMED_FIELD, result_);
    return result_;
  }

  /* ********************************************************** */
  // [object_name] (object_body | tuple_body)
  public static boolean object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object")) return false;
    if (!nextTokenIs(builder_, "<object>", IDENT, PARENTHESISL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OBJECT, "<object>");
    result_ = object_0(builder_, level_ + 1);
    result_ = result_ && object_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [object_name]
  private static boolean object_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_0")) return false;
    object_name(builder_, level_ + 1);
    return true;
  }

  // object_body | tuple_body
  private static boolean object_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_1")) return false;
    boolean result_;
    result_ = object_body(builder_, level_ + 1);
    if (!result_) result_ = tuple_body(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // PARENTHESISL [object_entry (COMMA object_entry)* [COMMA]] PARENTHESISR
  public static boolean object_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body")) return false;
    if (!nextTokenIs(builder_, PARENTHESISL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PARENTHESISL);
    result_ = result_ && object_body_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PARENTHESISR);
    exit_section_(builder_, marker_, OBJECT_BODY, result_);
    return result_;
  }

  // [object_entry (COMMA object_entry)* [COMMA]]
  private static boolean object_body_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body_1")) return false;
    object_body_1_0(builder_, level_ + 1);
    return true;
  }

  // object_entry (COMMA object_entry)* [COMMA]
  private static boolean object_body_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = object_entry(builder_, level_ + 1);
    result_ = result_ && object_body_1_0_1(builder_, level_ + 1);
    result_ = result_ && object_body_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA object_entry)*
  private static boolean object_body_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!object_body_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "object_body_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA object_entry
  private static boolean object_body_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && object_entry(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean object_body_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_body_1_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // named_field
  public static boolean object_entry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_entry")) return false;
    if (!nextTokenIs(builder_, IDENT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = named_field(builder_, level_ + 1);
    exit_section_(builder_, marker_, OBJECT_ENTRY, result_);
    return result_;
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
  // SOME PARENTHESISL value PARENTHESISR | NONE
  public static boolean option(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "option")) return false;
    if (!nextTokenIs(builder_, "<option>", NONE, SOME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OPTION, "<option>");
    result_ = option_0(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, NONE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SOME PARENTHESISL value PARENTHESISR
  private static boolean option_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "option_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SOME, PARENTHESISL);
    result_ = result_ && value(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PARENTHESISR);
    exit_section_(builder_, marker_, null, result_);
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
  // PARENTHESISL [value (COMMA value)* [COMMA]] PARENTHESISR
  public static boolean tuple_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body")) return false;
    if (!nextTokenIs(builder_, PARENTHESISL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PARENTHESISL);
    result_ = result_ && tuple_body_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PARENTHESISR);
    exit_section_(builder_, marker_, TUPLE_BODY, result_);
    return result_;
  }

  // [value (COMMA value)* [COMMA]]
  private static boolean tuple_body_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body_1")) return false;
    tuple_body_1_0(builder_, level_ + 1);
    return true;
  }

  // value (COMMA value)* [COMMA]
  private static boolean tuple_body_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = value(builder_, level_ + 1);
    result_ = result_ && tuple_body_1_0_1(builder_, level_ + 1);
    result_ = result_ && tuple_body_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COMMA value)*
  private static boolean tuple_body_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body_1_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!tuple_body_1_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "tuple_body_1_0_1", pos_)) break;
    }
    return true;
  }

  // COMMA value
  private static boolean tuple_body_1_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body_1_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [COMMA]
  private static boolean tuple_body_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tuple_body_1_0_2")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // bool | integer | float | string | char | option | list | map | object | enum
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
    if (!result_) result_ = enum_$(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
