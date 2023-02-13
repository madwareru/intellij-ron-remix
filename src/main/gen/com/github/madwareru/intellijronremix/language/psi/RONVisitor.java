// This is a generated file. Not intended for manual editing.
package com.github.madwareru.intellijronremix.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.model.psi.PsiExternalReferenceHost;

public class RONVisitor extends PsiElementVisitor {

  public void visitEnumVariantOrUnitStruct(@NotNull RONEnumVariantOrUnitStruct o) {
    visitPsiElement(o);
  }

  public void visitExt(@NotNull RONExt o) {
    visitPsiElement(o);
  }

  public void visitExtensions(@NotNull RONExtensions o) {
    visitPsiElement(o);
  }

  public void visitFieldName(@NotNull RONFieldName o) {
    visitPsiExternalReferenceHost(o);
  }

  public void visitList(@NotNull RONList o) {
    visitPsiElement(o);
  }

  public void visitMap(@NotNull RONMap o) {
    visitPsiElement(o);
  }

  public void visitMapEntry(@NotNull RONMapEntry o) {
    visitPsiElement(o);
  }

  public void visitMapKey(@NotNull RONMapKey o) {
    visitPsiElement(o);
  }

  public void visitNamedField(@NotNull RONNamedField o) {
    visitPsiElement(o);
  }

  public void visitObject(@NotNull RONObject o) {
    visitPsiElement(o);
  }

  public void visitObjectBody(@NotNull RONObjectBody o) {
    visitPsiElement(o);
  }

  public void visitObjectName(@NotNull RONObjectName o) {
    visitPsiExternalReferenceHost(o);
  }

  public void visitOption(@NotNull RONOption o) {
    visitPsiElement(o);
  }

  public void visitTuple(@NotNull RONTuple o) {
    visitPsiElement(o);
  }

  public void visitTupleBody(@NotNull RONTupleBody o) {
    visitPsiElement(o);
  }

  public void visitValue(@NotNull RONValue o) {
    visitPsiElement(o);
  }

  public void visitPsiExternalReferenceHost(@NotNull PsiExternalReferenceHost o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
