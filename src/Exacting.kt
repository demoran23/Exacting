@file:JvmName("Exacting")

import basemod.BaseMod
import basemod.interfaces.OnPlayerDamagedSubscriber
import basemod.interfaces.OnPlayerLoseBlockSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

@SpireInitializer
class Exacting : OnPlayerLoseBlockSubscriber, OnPlayerDamagedSubscriber {
    private var currentMonster: AbstractMonster? = null

    init {
        BaseMod.subscribe(this)
        currentMonster = null
    }

    companion object {
        @JvmStatic
        fun initialize() {
            Exacting()
        }
    }

    override fun receiveOnPlayerDamaged(damageAmount: Int, damageInfo: DamageInfo?): Int {
        // Store the current monster for when block is removed
        if (damageInfo != null && damageInfo.owner is AbstractMonster) {
            currentMonster = damageInfo.owner as AbstractMonster
        }

        return damageAmount
    }

    override fun receiveOnPlayerLoseBlock(amount: Int): Int {
        // Do not fire if no damage is done
        // If the player is damaged by an opponent for the exact amount of the player's current block,
        // stun the opponent
        val cm = currentMonster
        if (amount != 0 && cm != null && AbstractDungeon.player.currentBlock == amount) {
            // Override the current move
            cm.setMove("Stunned", cm.nextMove, AbstractMonster.Intent.STUN, 0)

            // Let the player know that the creature was stunned
            AbstractDungeon.actionManager.addToBottom(
                TextAboveCreatureAction(
                    cm,
                    TextAboveCreatureAction.TextType.STUNNED
                )
            )
        }

        currentMonster = null

        return amount
    }
}
