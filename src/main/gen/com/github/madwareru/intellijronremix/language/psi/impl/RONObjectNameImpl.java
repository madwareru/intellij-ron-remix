// This is a generated file. Not intended for manual editing.
package com.github.madwareru.intellijronremix.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.madwareru.intellijronremix.language.psi.RONTypes.*;
import com.github.madwareru.intellijronremix.language.psi.RONObjectNameMixin;
import com.github.madwareru.intellijronremix.language.psi.*;

public class RONObjectNameImpl extends RONObjectNameMixin implements RONObjectName {

  public RONObjectNameImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull RONVisitor visitor) {
    visitor.visitObjectName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof RONVisitor) accept((RONVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdent() {
    return findNotNullChildByType(IDENT);
  }

}
