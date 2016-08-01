package com.hea3ven.tools.gradle.grlog.changeset

class ChangeSetConfig {
	internal val filters = mutableMapOf(
			ChangeSetGroup.ADD to Regex("^[Aa][Dd][Dd]([Ee][Dd])?( |:)|^\\[[Nn][Ee][Ww]\\]"),
			ChangeSetGroup.REM to Regex("^[Rr][Ee][Mm]([Oo][Vv][Ee]([Dd]?))?( |:)|" +
					"^[Dd][Ee][Ll]([Ee][Tt]([Ee][Dd])?)?|^\\[([Rr][Ee][Mm]|[Dd][Ee][Ll])\\]"),
			ChangeSetGroup.FIX to Regex(
					"^[Ff][Ii][Xx]([Ee][Dd])?( |:)|^[Bb][Uu][Gg]:|^\\[[Ff][Ii][Xx]\\]|" +
							"^\\[[Bb][Uu][Gg]\\]")
	)

	fun setFilter(group: ChangeSetGroup, pattern: String) {
		filters[group] = Regex(pattern)
	}
}