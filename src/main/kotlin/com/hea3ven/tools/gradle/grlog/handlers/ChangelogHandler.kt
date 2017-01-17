package com.hea3ven.tools.gradle.grlog.handlers

import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import org.gradle.api.Project

abstract class ChangelogHandler {
	var file: String = ""
	var lineFormat: String = "    %s"
	abstract fun writeChangeset(project: Project, change: ChangeSet)

}