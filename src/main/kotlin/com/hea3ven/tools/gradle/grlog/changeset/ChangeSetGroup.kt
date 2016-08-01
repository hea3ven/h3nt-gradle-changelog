package com.hea3ven.tools.gradle.grlog.changeset

class ChangeSetGroup(val name: String) {
	companion object {
		val ALL = ChangeSetGroup("ALL")
		val ADD = ChangeSetGroup("ADD")
		val REM = ChangeSetGroup("REM")
		val FIX = ChangeSetGroup("FIX")
		val EXT = ChangeSetGroup("EXT")
		val ALL_GROUPED = ChangeSetGroup("ALL_GROUPED")
	}
}