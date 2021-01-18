package com.github.madwareru.intellijronremix.services

import com.github.madwareru.intellijronremix.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
