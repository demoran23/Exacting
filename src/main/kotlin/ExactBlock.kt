@file:JvmName("ExactBlock")

package exacting

import basemod.BaseMod
import basemod.interfaces.OnPlayerDamagedSubscriber
import basemod.interfaces.PostEnergyRechargeSubscriber
import basemod.interfaces.PreMonsterTurnSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.*
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@SpireInitializer
class ExactBlock : OnPlayerDamagedSubscriber, PreMonsterTurnSubscriber, PostEnergyRechargeSubscriber {
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
            startingPlayerBlock = player.currentBlock,
            startingPlayerHealth = player.currentHealth
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
            //ExactingConfiguration()
        }

        @JvmStatic
        val logger: Logger = LogManager.getLogger(ExactBlock::class.java.name);
    }

    override fun receiveOnPlayerDamaged(damageAmount: Int, damageInfo: DamageInfo?): Int {
        context = context.copy(damage = damageInfo)
        val damage = if (player.hasPower("IntangiblePlayer")) 1 else damageAmount;

        if (damage != 0 && damage == player.currentBlock) {
            if (damageInfo?.owner is AbstractMonster)
                debuff(damageInfo.owner as AbstractMonster)
        }

        return damageAmount
    }

    private fun debuff(monster: AbstractMonster) {
        if (ExactingConfiguration.instance.disableExactBlockMonsterDebuffs)
            return

        logger.info("Debuffing monster")

        when {
            chance(20) -> monster.applyPower(StunPower(monster))
            chance(40) -> monster.applyPower(WeakPower(monster, 1, true))
            chance(40) -> monster.applyPower(VulnerablePower(monster, 1, true))
            else -> {
                monster.applyPower(VulnerablePower(monster, 1, true))
                monster.applyPower(WeakPower(monster, 1, true))
            }
        }

        TextCenteredAction(player, "Exact Vengeance").push()
    }
}
