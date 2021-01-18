package com.github.madwareru.intellijronremix.ide

import com.github.madwareru.intellijronremix.language.psi.RONTypes
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler

class RONQuoteHandler : SimpleTokenSetQuoteHandler(RONTypes.STRING, RONTypes.RAW_STRING)
