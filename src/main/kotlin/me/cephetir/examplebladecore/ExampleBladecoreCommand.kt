package me.cephetir.examplebladecore

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand

object ExampleBladecoreCommand : Command("examplebladecore") {
    override val commandAliases = setOf(Alias("eb"))

    @DefaultHandler
    fun handle() {
        Config.sm.openGui()
    }

    @SubCommand("sub")
    fun sub() {

    }
}