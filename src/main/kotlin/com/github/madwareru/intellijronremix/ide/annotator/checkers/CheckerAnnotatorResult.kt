package com.github.madwareru.intellijronremix.ide.annotator.checkers

import com.intellij.openapi.util.TextRange

sealed class CheckerAnnotatorResult {
    object Ok : CheckerAnnotatorResult()
    data class Error(val errorText: String, val subRange: TextRange) : CheckerAnnotatorResult()
}