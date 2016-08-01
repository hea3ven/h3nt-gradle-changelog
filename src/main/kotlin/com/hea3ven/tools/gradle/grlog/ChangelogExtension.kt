package com.hea3ven.tools.gradle.grlog

import com.hea3ven.tools.gradle.grlog.handlers.PlainTextChangelogHandler
import groovy.lang.Closure

open class ChangelogExtension {
	val handlers: MutableList<PlainTextChangelogHandler> = mutableListOf()

	fun plainTextChangelog(builder: Closure<*>) {
		val handler = PlainTextChangelogHandler()
		builder.call(handler)
		addHandler(handler)
	}

	private fun addHandler(handler: PlainTextChangelogHandler) {
		handlers.add(handler)
	}

}