@file:JvmName("ExactingConfiguration")

package exacting

import basemod.interfaces.PostInitializeSubscriber
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import basemod.BaseMod.*
import com.megacrit.cardcrawl.helpers.FontHelper
import basemod.ModLabeledToggleButton
import basemod.ModPanel
import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import kotlin.reflect.KMutableProperty

@SpireInitializer
class ExactingConfiguration : PostInitializeSubscriber {
    companion object {
        @JvmStatic
        fun initialize() {
            instance = ExactingConfiguration()
        }

        private val logger: Logger = LogManager.getLogger(ExactingConfiguration::class.java.name)

        private val booleanDelegate = SpireConfigBooleanDelegate()

        lateinit var instance: ExactingConfiguration
    }

    init {
        subscribe(this)
    }

    @ConfigKey("disableExactBlockMonsterDebuffs", "Disable Exact Block Monster Debuffs")
    var disableExactBlockMonsterDebuffs: Boolean by booleanDelegate

    @ConfigKey("disableExactAttackMonsterKillRewards", "Disable Exact Attack Monster Kill Rewards")
    var disableExactAttackMonsterKillRewards: Boolean by booleanDelegate

    @ConfigKey("disableExactAttackMonsterBlockBuffs", "Disable Exact Attack Monster Block Buffs")
    var disableExactAttackMonsterBlockBuffs: Boolean by booleanDelegate

    @ConfigKey("disableExactAttackMonsterParry", "Disable Exact Attack Monster Parry")
    var disableExactAttackMonsterParry: Boolean by booleanDelegate

    // Set up the configuration UI
    override fun receivePostInitialize() {
        val instance = this
        val badgeTexture = Texture("images/BaseModBadge.png")
        val settingsPanel = ModPanel().apply {
            val xPos = 350f
            var yPos = 760.0f
            fun next(): Float {
                yPos -= 30
                return yPos
            }

            fun addToggleButton(enabledProp: KMutableProperty<Boolean>) {
                addUIElement(ModLabeledToggleButton(
                    enabledProp.getAnnotation<ConfigKey>().Description,
                    xPos,
                    next(),
                    Settings.CREAM_COLOR,
                    FontHelper.charDescFont,
                    enabledProp.getter.call(),
                    this,
                    { logger.debug("label updated: ${it.text}") },
                    { enabledProp.setter.call(instance, it.enabled) }
                ))
            }

            addToggleButton(::disableExactAttackMonsterKillRewards)
            addToggleButton(::disableExactBlockMonsterDebuffs)
            addToggleButton(::disableExactAttackMonsterBlockBuffs)
            addToggleButton(::disableExactAttackMonsterParry)
        }

        registerModBadge(badgeTexture, "Exacting", "Adam Skinner", "Settings", settingsPanel)
    }
}
