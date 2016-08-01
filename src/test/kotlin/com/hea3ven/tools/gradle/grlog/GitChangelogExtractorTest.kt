package com.hea3ven.tools.gradle.grlog

import org.eclipse.jgit.internal.storage.dfs.InMemoryRepository
import org.eclipse.jgit.junit.TestRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class GitChangelogExtractorTest {
	@Test
	fun noConfigAndNoTagsGetsFromBegining() {
		val repo = TestRepository(InMemoryRepository.Builder().build())
		var parent = repo.branch("master").commit().message("A").create()
		parent = repo.commit().message("B").parent(parent).create()
		parent = repo.commit().message("C").parent(parent).create()
		repo.update("HEAD", parent)
		val ex = GitChangelogExtractor(repo.repository)

		val result = ex.getChangeSet()

		assertEquals(Arrays.asList("A", "B", "C"), result.lines)
	}

	@Test
	fun noConfigAndTagsGetsFromLastTag() {
		val repo = TestRepository(InMemoryRepository.Builder().build())
		var parent = repo.branch("master").commit().message("A").create()
		repo.update("refs/tags/1", parent)
		parent = repo.branch("master").commit().message("B").parent(parent).create()
		repo.update("refs/tags/2", parent)
		repo.branch("master").commit().message("C").parent(parent).create()
		repo.reset("master")
		val ex = GitChangelogExtractor(repo.repository)

		val result = ex.getChangeSet()

		assertEquals(Arrays.asList("C"), result.lines)
	}

	@Test
	fun tagFilterConfiguredAndTagsGetsFromLastMatchingTag() {
		val repo = TestRepository(InMemoryRepository.Builder().build())
		var parent = repo.branch("master").commit().message("A").create()
		repo.update("refs/tags/v1", parent)
		parent = repo.branch("master").commit().message("B").parent(parent).create()
		repo.update("refs/tags/v2", parent)
		parent = repo.branch("master").commit().message("C").parent(parent).create()
		repo.update("refs/tags/3", parent)
		repo.branch("master").commit().message("D").parent(parent).create()
		repo.reset("master")
		val ex = GitChangelogExtractor(repo.repository)

		ex.tagFilter = { it.startsWith("v") }
		val result = ex.getChangeSet()

		assertEquals(Arrays.asList("C", "D"), result.lines)
	}

	@Test
	fun configFilterWithBranchesGetsFromLastMatchingTagAndMergedBranches() {
		// Test history:
		// A --- B (v1-1) --- D ------- F (v1-2)
		//        \                      \
		//        `--- C (v2-1) --- E ---+--- G *
		// Expected: D E F G
		val repo = TestRepository(InMemoryRepository.Builder().build())
		var parent = repo.branch("master").commit().message("A").create()
		repo.tick(10)
		parent = repo.branch("master").commit().message("B").parent(parent).create()
		repo.update("refs/tags/v1-1", parent)
		repo.tick(10)
		var branchParent = repo.branch("feature").update(parent)
		branchParent = repo.branch("feature").commit().message("C").parent(branchParent).create()
		repo.update("refs/tags/v2-1", branchParent)
		repo.tick(10)
		parent = repo.branch("master").commit().message("D").parent(parent).create()
		repo.tick(10)
		branchParent = repo.branch("feature").commit().message("E").parent(branchParent).create()
		repo.tick(10)
		parent = repo.branch("master").commit().message("F").parent(parent).create()
		repo.update("refs/tags/v1-2", parent)
		repo.tick(10)
		repo.branch("feature").commit().message("G").parent(branchParent).parent(parent).create()
		repo.tick(10)
		repo.reset("feature")
		val ex = GitChangelogExtractor(repo.repository)

		ex.tagFilter = { it.startsWith("v2-") }
		val result = ex.getChangeSet()

		assertEquals(Arrays.asList("D", "E", "F", "G"), result.lines)
	}
}