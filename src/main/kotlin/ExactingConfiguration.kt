@file:JvmName("ExactingConfiguration")

package exacting

import basemod.BaseMod.registerModBadge
import basemod.BaseMod.subscribe
import basemod.ModLabeledToggleButton
import basemod.ModPanel
import basemod.interfaces.PostInitializeSubscriber
import com.badlogic.gdx.graphics.Texture
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
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
    var disableExactBlockMonsterDebuffs by booleanDelegate

    @ConfigKey("disableExactAttackMonsterKillRewards", "Disable Exact Attack Monster Kill Rewards")
    var disableExactAttackMonsterKillRewards by booleanDelegate

    @ConfigKey("disableExactAttackMonsterBlockBuffs", "Disable Exact Attack Monster Block Buffs")
    var disableExactAttackMonsterBlockBuffs by booleanDelegate

    @ConfigKey("disableExactAttackMonsterParry", "Disable Exact Attack Monster Parry")
    var disableExactAttackMonsterParry by booleanDelegate

    // Set up the configuration UI
    override fun receivePostInitialize() {
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
                    { },
                    { enabledProp.setter.call(it.enabled) }
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
