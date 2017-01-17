package com.hea3ven.tools.gradle.grlog.handlers

import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class ForgeChangelogHandlerTest {
	@Rule
	@JvmField
	val tempFolder = TemporaryFolder()

	private lateinit var changelogFile: File

	@Before
	fun setUp() {
		changelogFile = tempFolder.newFile("CHANGELOG.txt") ?: throw RuntimeException("could not setup test")
		changelogFile.writeText(testData)
	}

	@Test
	fun addExistingMcVersionChangeset() {
		val handler = ForgeChangelogHandler()
		handler.file = changelogFile.absolutePath
		val change = ChangeSet()
		change.addLine("version 1.2.1")

		handler.writeChangeset(createProject("1.9","12.16.1.1894", "1.2.1", "snapshot_20160419"), change)

		assertEquals(result1Data, changelogFile.readText())
	}

	private fun createProject(mcVersion: String, forgeVersion: String, version: String,
			mappingsVersion: String): Project {
		val project: Project = ProjectBuilder.builder().build()
		project.pluginManager.apply("net.minecraftforge.gradle.forge")
		project.version = mcVersion + "-" + version
		val mc = (project.findProperty("minecraft") as ForgeExtension)
		mc.version = mcVersion + "-" + forgeVersion
		return project
	}

	companion object {
		val testData = loadResource("/forge_changelog/test.json")
		val result1Data = loadResource("/forge_changelog/result1.json")

		private fun loadResource(resource: String)
				= javaClass.getResourceAsStream(resource).reader().readText()
	}
}