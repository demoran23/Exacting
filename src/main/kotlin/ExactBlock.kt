@file:JvmName("ExactBlock")

package exacting

import basemod.BaseMod
import basemod.interfaces.OnPlayerDamagedSubscriber
import basemod.interfaces.OnPlayerLoseBlockSubscriber
import basemod.interfaces.PostEnergyRechargeSubscriber
import basemod.interfaces.PreMonsterTurnSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@SpireInitializer
class ExactBlock : OnPlayerLoseBlockSubscriber, OnPlayerDamagedSubscriber, PreMonsterTurnSubscriber,
    PostEnergyRechargeSubscriber {
    private data class DamageContext(
        val monster: AbstractMonster? = null,
        val startingPlayerBlock: Int = 0,
        val startingPlayerHealth: Int = 0,
        val damage: DamageInfo? = null
    )

    private var context: DamageContext = DamageContext()

    override fun receivePostEnergyRecharge() {
        context = DamageContext()
    }

    override fun receivePreMonsterTurn(monster: AbstractMonster?): Boolean {
        context = DamageContext(
            monster = monster,
            startingPlayerBlock = AbstractDungeon.player.currentBlock,
            startingPlayerHealth = AbstractDungeon.player.currentHealth
        )
        return true
    }

    init {
        BaseMod.subscribe(this)
    }

    companion object {
        @JvmStatic
        fun initialize() {
            ExactBlock()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            println("Kotlin main is running here!")
        }

        val logger: Logger = LogManager.getLogger(ExactBlock::class.java.name);
    }

    override fun receiveOnPlayerDamaged(damageAmount: Int, damageInfo: DamageInfo?): Int {
        context = context.copy(damage = damageInfo)
        val damage = if (AbstractDungeon.player.hasPower("IntangiblePlayer")) 1 else damageAmount;

        if (damage == AbstractDungeon.player.currentBlock) {
            logger.debug("Exact block")
            if (damageInfo?.owner is AbstractMonster)
                debuff(damageInfo.owner as AbstractMonster)
        }

        return damageAmount
    }

    override fun receiveOnPlayerLoseBlock(amount: Int): Int {
        // Do not fire if no damage is done
        // If the player is damaged by an opponent for the exact amount of the player's current block,
        // debuff the opponent
        val playerCurrentBlock = AbstractDungeon.player.currentBlock;
        if (
            context.monster != null
            && context.startingPlayerBlock != 0
            && context.damage?.output == context.startingPlayerBlock
            && playerCurrentBlock == 0
        ) {
            val cm = context.monster
            if (cm != null)
                debuff(cm)
        }

        context = DamageContext()

        return amount
    }

    private fun debuff(monster: AbstractMonster) {
        logger.debug("debuffing ${monster.name} [$context]")

        AbstractDungeon.actionManager.addToBottom(
            ApplyPowerAction(
                monster,
                AbstractDungeon.player,
                StunPower(monster),
                1
            )
        )

        AbstractDungeon.actionManager.addToBottom(
            ApplyPowerAction(
                monster,
                AbstractDungeon.player,
                WeakPower(monster, 1, true),
                1
            )
        )

        AbstractDungeon.actionManager.addToBottom(
            ApplyPowerAction(
                monster,
                AbstractDungeon.player,
                VulnerablePower(monster, 1, true),
                1
            )
        )

        AbstractDungeon.actionManager.addToBottom(
            TextAboveCreatureAction(
                monster,
                "Exact Vengeance"
            )
        )
    }
}
