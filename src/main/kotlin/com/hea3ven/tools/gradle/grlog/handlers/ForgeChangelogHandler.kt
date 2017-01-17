package com.hea3ven.tools.gradle.grlog.handlers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.hea3ven.tools.gradle.grlog.changeset.ChangeSet
import net.minecraftforge.gradle.user.patcherUser.forge.ForgeExtension
import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class ForgeChangelogHandler : ChangelogHandler() {
	override fun writeChangeset(project: Project, change: ChangeSet) {
		val gson = GsonBuilder().setPrettyPrinting().create()
		val output = Paths.get(file)!!
		var homepage: String
		val promotions = mutableMapOf<String, String>()
		val versions = mutableMapOf<String, MutableMap<String, String>>()
		val commits = mutableMapOf<String, String>()
		var data = Files.newBufferedReader(output).use {
			gson.fromJson(it, JsonObject::class.java)
		}
		val mc = project.findProperty("minecraft") as ForgeExtension
		data[mc.version].asJsonObject.addProperty(project.version.toString(),
				change.processedLines.joinToString(""))
		data["promos"].asJsonObject.addProperty(mc.version + "-latest", project.version.toString())
		data["promos"].asJsonObject.addProperty(mc.version + "-recommended", project.version.toString())

		Files.newBufferedWriter(output, StandardOpenOption.TRUNCATE_EXISTING).use {
			gson.toJson(data, it)
		}
	}

}