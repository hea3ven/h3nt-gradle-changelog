package com.hea3ven.tools.gradle.grlog.handlers

import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import org.gradle.api.Project
import java.io.File

class PlainTextChangelogHandler : ChangelogHandler() {

	private var append = true

	var versionFormat: String = "%s:"

	fun append() {
		append = true
	}

	fun prepend() {
		append = false
	}

	override fun writeChangeset(project: Project, change: ChangeSet) {
		var text = versionFormat.format(project.version) + "\n"
		text += change.processedLines.map { lineFormat.format(it) }.joinToString("\n")
		text += "\n"
		val outputFile = File(file)
		if (append)
			outputFile.appendText("\n" + text)
		else {
			text = text + "\n" + outputFile.readText()
			outputFile.writeText(text)
		}
	}

}

