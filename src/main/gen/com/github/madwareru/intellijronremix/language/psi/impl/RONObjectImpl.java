// This is a generated file. Not intended for manual editing.
package com.github.madwareru.intellijronremix.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.madwareru.intellijronremix.language.psi.RONTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.madwareru.intellijronremix.language.psi.*;

public class RONObjectImpl extends ASTWrapperPsiElement implements RONObject {

  public RONObjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull RONVisitor visitor) {
    visitor.visitObject(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof RONVisitor) accept((RONVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public RONObjectBody getObjectBody() {
    return findNotNullChildByClass(RONObjectBody.class);
  }

  @Override
  @Nullable
  public RONObjectName getObjectName() {
    return findChildByClass(RONObjectName.class);
  }

}
