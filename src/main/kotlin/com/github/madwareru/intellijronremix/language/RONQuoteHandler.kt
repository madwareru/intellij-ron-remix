package com.github.madwareru.intellijronremix.language

import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

class RONQuoteHandler : SimpleTokenSetQuoteHandler(RONTypes.STRING, RONTypes.RAW_STRING)