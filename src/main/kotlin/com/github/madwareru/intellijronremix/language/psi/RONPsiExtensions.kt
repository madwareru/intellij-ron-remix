package com.github.madwareru.intellijronremix.language.psi

val RONObjectEntry?.keyText get() = this?.namedField?.ident?.text
fun RONObjectEntry?.keyTextMatches(other: CharSequence?) =
    other != null && this?.namedField?.ident?.textMatches(other) ?: false
val RONMapEntry?.keyAsText get() = this?.mapKey?.text
fun RONMapEntry?.keyAsTextMatches(other: CharSequence?) =
    other != null && this?.mapKey?.textMatches(other) ?: false
val RONObjectEntry?.isTuple get() = this?.namedField == null