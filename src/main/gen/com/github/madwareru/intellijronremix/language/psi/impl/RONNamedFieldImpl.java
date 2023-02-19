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

public class RONNamedFieldImpl extends ASTWrapperPsiElement implements RONNamedField {

  public RONNamedFieldImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull RONVisitor visitor) {
    visitor.visitNamedField(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof RONVisitor) accept((RONVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public RONFieldName getFieldName() {
    return findNotNullChildByClass(RONFieldName.class);
  }

  @Override
  @Nullable
  public RONValue getValue() {
    return findChildByClass(RONValue.class);
  }

}
