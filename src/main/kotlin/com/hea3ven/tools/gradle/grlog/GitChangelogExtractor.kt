package com.hea3ven.tools.gradle.grlog

import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository

class GitChangelogExtractor(val repo: Repository) {
	var tagFilter: (String) -> Boolean = { true }
	fun getChangeSet(): ChangeSet {
		val git = Git(repo)
		val tags = repo.tags.entries.filter { tagFilter.invoke(it.key) }.map { it.value }
		val log = git.log()
				.call()
				.takeWhile { commit -> tags.none { tag -> tag.objectId.equals(commit.toObjectId()) } }
				.reversed()
		val changeSet = ChangeSet()
		for (commit in log) {
			changeSet.addLine(commit.fullMessage)
		}
		return changeSet
	}

}

