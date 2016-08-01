package com.hea3ven.tools.gradle.grlog

class ChangeSet {
	val lines: List<String>
		get() = allLines

	private var allLines: MutableList<String> = mutableListOf()

	fun addExtraLine(line: String) {
		allLines.add(line)
	}

}