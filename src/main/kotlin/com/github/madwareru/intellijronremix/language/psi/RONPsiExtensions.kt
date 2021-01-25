package com.github.madwareru.intellijronremix.language.psi

val RONObjectEntry?.keyText get() = this?.namedField?.ident?.text
val RONObjectEntry?.isTuple get() = this?.namedField == null