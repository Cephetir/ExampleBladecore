package me.cephetir.examplebladecore

import me.cephetir.bladecore.core.event.BladeEventBus
import me.cephetir.bladecore.core.event.listener.listener
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
    modid = "examplebladecore",
    name = "ExampleBladecore",
    version = "1.0",
    acceptedMinecraftVersions = "[1.8.9]",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter",
    clientSideOnly = true
)
object ExampleBladecore {
    private val logger: Logger = LogManager.getLogger("ExampleBladecore")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        logger.info("Initializing ExampleBladecore mod...")

        Config.init()
        ExampleBladecoreCommand.register()
        BladeEventBus.subscribe(this)
    }

    init {
        listener<ClientTickEvent> { event ->

        }
    }
}