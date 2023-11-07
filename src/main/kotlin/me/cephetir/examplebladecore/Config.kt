package me.cephetir.examplebladecore

import me.cephetir.bladecore.config.settings.SettingManager
import me.cephetir.bladecore.utils.mc
import java.io.File

object Config {
    private val configFile = File(mc.mcDataDir, "config/ExampleBladecore/config.toml")
    val sm = SettingManager(configFile, "ExampleBladecore", Sorting())

    fun init() {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
        }

        sm.loadConfig()
    }

    private class Sorting : SettingManager.CategorySorting() {
        private val categories = listOf(
            "General",
        )

        override fun getCategoryComparator(): Comparator<in SettingManager.Category> = Comparator.comparingInt { categories.indexOf(it.name) }
    }

    @JvmField
    val toggle = sm.booleanSetting("Toggle") {
        description = "Some toggle"
        category = "General"
    }
}