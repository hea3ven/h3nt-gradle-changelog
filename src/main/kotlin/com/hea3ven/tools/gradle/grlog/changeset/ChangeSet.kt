package com.hea3ven.tools.gradle.grlog.changeset

class ChangeSet(val config: ChangeSetConfig = ChangeSetConfig()) {
	val processedLines: List<String>
		get() = getLines(ChangeSetGroup.ADD)
				.plus(getLines(ChangeSetGroup.REM))
				.plus(getLines(ChangeSetGroup.FIX))
				.plus(getLines(ChangeSetGroup.EXT))

	private val groupsMap = mutableMapOf(
			ChangeSetGroup.ALL to LineGroup())

	fun addLine(line: String) {
		groupsMap[ChangeSetGroup.ALL]!!.addLine(line)
		val filter = config.filters.entries.firstOrNull { it.value.containsMatchIn(line) }
		val group = filter?.key ?: ChangeSetGroup.EXT
		groupsMap.getOrPut(group, { LineGroup() }).addLine(line)
	}

	fun getLines(group: ChangeSetGroup) = groupsMap[group]?.lines ?: emptyList()

	class LineGroup() {
		val lines: List<String>
			get() = linesList

		private val linesList = mutableListOf<String>()

		fun addLine(line: String) {
			linesList.add(line)
		}
	}
}
