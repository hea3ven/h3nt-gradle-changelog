package com.hea3ven.tools.gradle.grlog.changeset

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ChangeSetTest {
	@Test
	fun defaultFiltersAddsNonMatchingLineToExtra() {
		val change = ChangeSet()

		change.addLine("some test")

		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.ADD))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.REM))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.FIX))
		assertEquals(Arrays.asList("some test"), change.getLines(ChangeSetGroup.EXT))
		assertEquals(Arrays.asList("some test"), change.getLines(ChangeSetGroup.ALL))
	}

	@Test
	fun defaultFiltersAddsAddMatchingLinesToAddGroup() {
		val change = ChangeSet()

		change.addLine("Add something")
		change.addLine("add something")
		change.addLine("add: something")
		change.addLine("Added something")
		change.addLine(" Add something")
		change.addLine("[NEW] something")
		change.addLine("[nEw] something")
		change.addLine("something [NEW] in")
		change.addLine("New something")

		assertEquals(
				Arrays.asList<String>("Add something", "add something", "add: something", "Added something",
						"[NEW] something", "[nEw] something"), change.getLines(ChangeSetGroup.ADD))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.REM))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.FIX))
		assertEquals(Arrays.asList(" Add something", "something [NEW] in", "New something"),
				change.getLines(ChangeSetGroup.EXT))
		assertEquals(Arrays.asList("Add something", "add something", "add: something", "Added something",
				" Add something", "[NEW] something", "[nEw] something", "something [NEW] in",
				"New something"), change.getLines(ChangeSetGroup.ALL))

	}

	@Test
	fun defaultFiltersAddsRemMatchingLinesToRemGroup() {
		val change = ChangeSet()

		change.addLine("Rem something")
		change.addLine("rem something")
		change.addLine("rem: something")
		change.addLine("Remove something")
		change.addLine("Removed something")
		change.addLine(" Rem something")
		change.addLine("Del something")
		change.addLine("Delete something")
		change.addLine("Deleted something")
		change.addLine("[REM] something")
		change.addLine("[rEm] something")
		change.addLine("something [REM] in")
		change.addLine("[DEL] something")
		change.addLine("[deL] something")

		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.ADD))
		assertEquals(
				Arrays.asList<String>("Rem something", "rem something", "rem: something", "Remove something",
						"Removed something", "Del something", "Delete something", "Deleted something",
						"[REM] something", "[rEm] something", "[DEL] something", "[deL] something"),
				change.getLines(ChangeSetGroup.REM))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.FIX))
		assertEquals(Arrays.asList(" Rem something", "something [REM] in"),
				change.getLines(ChangeSetGroup.EXT))
		assertEquals(Arrays.asList("Rem something", "rem something", "rem: something", "Remove something",
				"Removed something", " Rem something", "Del something", "Delete something",
				"Deleted something", "[REM] something", "[rEm] something", "something [REM] in",
				"[DEL] something", "[deL] something"), change.getLines(
				ChangeSetGroup.ALL))

	}

	@Test
	fun defaultFiltersAddsFixMatchingLinesToFixGroup() {
		val change = ChangeSet()

		change.addLine("Fix something")
		change.addLine("Fixed something")
		change.addLine("fix: something")
		change.addLine(" Fix something")
		change.addLine("bug: something")
		change.addLine("[FIX] something")
		change.addLine("[fIx] something")
		change.addLine("[BUG] something")
		change.addLine("something [fix] in")
		change.addLine("something [bug] in")
		change.addLine("bug something")

		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.ADD))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.REM))
		assertEquals(
				Arrays.asList<String>("Fix something", "Fixed something", "fix: something", "bug: something",
						"[FIX] something", "[fIx] something", "[BUG] something"),
				change.getLines(ChangeSetGroup.FIX))
		assertEquals(
				Arrays.asList(" Fix something", "something [fix] in", "something [bug] in", "bug something"),
				change.getLines(ChangeSetGroup.EXT))
		assertEquals(Arrays.asList("Fix something", "Fixed something", "fix: something", " Fix something",
				"bug: something", "[FIX] something", "[fIx] something", "[BUG] something",
				"something [fix] in", "something [bug] in", "bug something"),
				change.getLines(ChangeSetGroup.ALL))

	}

	@Test
	fun replacedFilterAddsMatchingLinesToGroup() {
		val changeConfig = ChangeSetConfig()
		changeConfig.setFilter(ChangeSetGroup.ADD, "^b")
		val change = ChangeSet(changeConfig)

		change.addLine("Add something")
		change.addLine("b something")

		assertEquals(Arrays.asList("b something"), change.getLines(ChangeSetGroup.ADD))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.REM))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.FIX))
		assertEquals(Arrays.asList("Add something"), change.getLines(ChangeSetGroup.EXT))
		assertEquals(Arrays.asList("Add something", "b something"), change.getLines(ChangeSetGroup.ALL))

	}

	@Test
	fun newGroupFilterAddsMatchingLinesToGroup() {
		val changeConfig = ChangeSetConfig()
		val testGroup = ChangeSetGroup("TEST")
		changeConfig.setFilter(testGroup, "^b")
		val change = ChangeSet(changeConfig)

		change.addLine("Add something")
		change.addLine("b something")

		assertEquals(Arrays.asList("Add something"), change.getLines(ChangeSetGroup.ADD))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.REM))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.FIX))
		assertEquals(Arrays.asList<String>(), change.getLines(ChangeSetGroup.EXT))
		assertEquals(Arrays.asList("b something"), change.getLines(testGroup))
		assertEquals(Arrays.asList("Add something", "b something"), change.getLines(ChangeSetGroup.ALL))
	}
}