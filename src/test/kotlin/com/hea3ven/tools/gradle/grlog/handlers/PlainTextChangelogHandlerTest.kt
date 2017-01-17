package com.hea3ven.tools.gradle.grlog.handlers

import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class PlainTextChangelogHandlerTest {
	@Rule
	@JvmField
	val tempFolder = TemporaryFolder()

	private lateinit var changelogFile: File

	@Before
	fun setUp() {
		changelogFile = tempFolder.newFile("CHANGELOG.txt") ?: throw RuntimeException("could not setup test")
		changelogFile.writeText("some previous text\n")
	}

	@Test
	fun appendToChangelog() {
		val handler = PlainTextChangelogHandler()
		handler.file = changelogFile.absolutePath
		val change = ChangeSet()
		change.addLine("Fix something")
		change.addLine("Add something")

		handler.append()
		handler.writeChangeset(createProject("1.2.3"), change)

		assertEquals("some previous text\n\n1.2.3:\n    Add something\n    Fix something\n",
				changelogFile.readText())
	}

	@Test
	fun prependToChangelog() {
		val handler = PlainTextChangelogHandler()
		handler.file = changelogFile.absolutePath
		val change = ChangeSet()
		change.addLine("Fix something")
		change.addLine("Add something")

		handler.prepend()
		handler.writeChangeset(createProject("1.2.3"), change)

		assertEquals("1.2.3:\n    Add something\n    Fix something\n\nsome previous text\n",
				changelogFile.readText())
	}

	@Test
	fun versionFormat() {
		val handler = PlainTextChangelogHandler()
		handler.file = changelogFile.absolutePath
		val change = ChangeSet()
		change.addLine("Fix something")
		change.addLine("Add something")

		handler.versionFormat = "Version %s:"
		handler.writeChangeset(createProject("1.2.3"), change)

		assertEquals("some previous text\n\nVersion 1.2.3:\n    Add something\n    Fix something\n",
				changelogFile.readText())
	}

	@Test
	fun lineFormat() {
		val handler = PlainTextChangelogHandler()
		handler.file = changelogFile.absolutePath
		val change = ChangeSet()
		change.addLine("Fix something")
		change.addLine("Add something")

		handler.lineFormat = "    * %s."
		handler.writeChangeset(createProject("1.2.3"), change)

		assertEquals("some previous text\n\n1.2.3:\n    * Add something.\n    * Fix something.\n",
				changelogFile.readText())
	}

	private fun createProject(version: String): Project {
		val project: Project = ProjectBuilder.builder().build()
		project.version = version
		return project
	}

}