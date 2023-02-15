// This is a generated file. Not intended for manual editing.
package com.github.madwareru.intellijronremix.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface RONValue extends PsiElement {

  @Nullable
  RONList getList();

  @Nullable
  RONMap getMap();

  @Nullable
  RONObject getObject();

  @Nullable
  RONObjectName getObjectName();

  @Nullable
  RONOption getOption();

  @Nullable
  RONTuple getTuple();

  @Nullable
  PsiElement getBoolean();

  @Nullable
  PsiElement getFloat();

  @Nullable
  PsiElement getInteger();

}
