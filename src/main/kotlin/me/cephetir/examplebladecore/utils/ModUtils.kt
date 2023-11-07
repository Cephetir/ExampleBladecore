package me.cephetir.examplebladecore.utils

import me.cephetir.bladecore.utils.mc
import net.minecraft.util.ChatComponentText

object ModUtils {
    fun sendMsg(text: String) = mc.ingameGUI?.chatGUI?.printChatMessage(ChatComponentText(text.replace("&", "§"))) ?: Unit
    fun sendModMsg(text: String) = sendMsg("§3[ExampleBladecore] $text")
}