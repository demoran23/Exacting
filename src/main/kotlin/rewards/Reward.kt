@file:JvmName("Reward")

package exacting

import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.player
import com.megacrit.cardcrawl.monsters.AbstractMonster
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

abstract class Reward {
    companion object {
        @JvmStatic
        val logger: Logger = LogManager.getLogger(Reward::class.java.name)

        /**
         *  Set by [ExactAttack.getReward]
         */
        @JvmStatic
        lateinit var monster: AbstractMonster
    }

    abstract val sortOrder: Int
    abstract val chance: Int
    protected abstract val description: String
    abstract fun effect()

    open fun predicate(): Boolean {
        return true
    }

    protected fun displayBonus() {
        logger.info("Granting reward: $description")

        TextCenteredAction(player, "Exact Attack").push()
        TextAboveCreatureAction(player, description).push()
    }
}

