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
    }
    abstract fun sortOrder(monster: AbstractMonster): Int
    abstract fun chance(monster: AbstractMonster): Int
    protected abstract fun description(monster: AbstractMonster): String
    abstract fun effect(monster: AbstractMonster)

    open fun predicate(monster: AbstractMonster): Boolean {
        return true
    }

    protected fun displayBonus(monster: AbstractMonster) {
        val description = description(monster)
        logger.info("Granting reward: $description")

        TextCenteredAction(player, "Exact Attack").push()
        TextAboveCreatureAction(player, description).push()
    }
}

