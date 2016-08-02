package com.hea3ven.tools.gradle.grlog.handlers

import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import java.io.File

class PlainTextChangelogHandler {
	var file: String = ""

	private var append = true

	var versionFormat: String = "%s:"
	var lineFormat: String = "    %s"

	fun append() {
		append = true
	}

	fun prepend() {
		append = false
	}

	fun writeChangeset(version: String, change: ChangeSet) {
		var text = versionFormat.format(version) + "\n"
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