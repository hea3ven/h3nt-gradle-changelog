package com.hea3ven.tools.gradle.grlog

import org.junit.Assert.*
import com.hea3ven.tools.gradle.grlog.handlers.PlainTextChangelogHandler
import org.codehaus.groovy.runtime.MethodClosure
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import java.util.function.Consumer

class GrLogPluginTest {
	@Test
	fun configurePlainTextChangelog() {
		val proj = ProjectBuilder.builder().build()
		proj.pluginManager.apply("com.hea3ven.tools.gradle.grlog")

		val changelog: ChangelogExtension = proj.extensions.getByType(ChangelogExtension::class.java)
		changelog.plainTextChangelog(
				MethodClosure(Consumer<PlainTextChangelogHandler> { t -> t.file = "CHANGELOG" }, "accept"))
		assertEquals(1, changelog.handlers.size)
		assertTrue(changelog.handlers[0] is PlainTextChangelogHandler)
		assertEquals("CHANGELOG", changelog.handlers[0].file)
	}

	@Suppress("UNCHECKED_CAST")
	private fun <T> Project.getProp(name: String) = findProperty(name) as T
}

