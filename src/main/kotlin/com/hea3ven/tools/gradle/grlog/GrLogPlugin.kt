package com.hea3ven.tools.gradle.grlog

import org.gradle.api.Plugin
import org.gradle.api.Project

class GrLogPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		project.extensions.create("changelog", ChangelogExtension::class.java)
	}
}