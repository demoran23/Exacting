@file:JvmName("ExactAttack")

package exacting

import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.gridSelectScreen
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.player
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ExactAttack {
    companion object {
        @JvmStatic
        val logger: Logger = LogManager.getLogger(ExactAttack::class.java.name)
        val monsterKillRewards = mutableListOf(
            MaxHpReward(),
            RelicReward(),
            GainCardReward(),
            PotionReward(),
            HealReward(),
            EnergyReward(),
            GoldReward()
        )
    }

    fun monsterParry(monster: AbstractMonster) {
        logger.info("Monster parry")
        TextAboveCreatureAction(monster, "Parry").push()
    }

    fun buffMonster(monster: AbstractMonster) {
        logger.debug("Buffing monster")

        TextCenteredAction(player, "Exacting Mishap").push()

        when {
// TODO: Intangible lasts too long, customize it to last a single round, like Stun
//            chance(3) -> {
//                monster.applyPower(IntangiblePower(monster, 1))
//                TextAboveCreatureAction(monster, "+1 Intangible").push()
//            }
            chance(7) -> {
                val amount = (monster.maxHealth * .2).toInt()
                monster.heal(amount)
                TextAboveCreatureAction(monster, "+$amount Heal").push()
            }
            chance(10) -> {
                monster.addBlock(10)
                TextAboveCreatureAction(monster, "+10 Block").push()
            }
            chance(20) -> {
                monster.applyPower(StrengthPower(monster, 1))
                TextAboveCreatureAction(monster, "+1 Strength").push()
            }
            else -> {
                monster.applyPower(DexterityPower(monster, 1))
                TextAboveCreatureAction(monster, "+1 Dexterity").push()
            }
        }
    }

    fun getReward(monster: AbstractMonster) {
        // No rewards for minions
        if (monster.isMinion())
            return

        for (reward in monsterKillRewards
            .filter { it.predicate(monster) }
            .sortedBy { it.sortOrder(monster) }
        ) {
            logger.debug("Checking reward: ${reward::class.java.name}")

            // Gold is the default reward, being last checked with a chance of 100%
            if (chance(reward.chance(monster))) {
                reward.effect(monster)
                return
            }
        }
    }

    private fun awardRemoveCard() {
        gridSelectScreen.open(
            CardGroup.getGroupWithoutBottledCards(player.masterDeck.purgeableCards),
            1,
            "Exact Attack reward: Remove a card",
            false,
            false,
            true,
            true
        )
    }

    private fun awardUpgradeCard() {
        gridSelectScreen.open(
            CardGroup.getGroupWithoutBottledCards(player.masterDeck.upgradableCards),
            1,
            "Exact Attack reward: Upgrade a card",
            true,
            false,
            true,
            false
        )

        gridSelectScreen.selectedCards.forEach {
            logger.debug("upgrading ${it.name}")
            it.upgrade()
        }
        gridSelectScreen.selectedCards.clear();

//        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
//            val c = AbstractDungeon.gridSelectScreen.selectedCards[0] as AbstractCard
//            c.upgrade()
//            AbstractDungeon.player.bottledCardUpgradeCheck(c)
//            AbstractDungeon.effectsQueue.add(ShowCardBrieflyEffect(c.makeStatEquivalentCopy()))
//            AbstractDungeon.gridSelectScreen.selectedCards.clear()
//        }
    }

    private fun awardTransformCard() {
        gridSelectScreen.open(
            CardGroup.getGroupWithoutBottledCards(player.masterDeck.purgeableCards),
            1,
            "Exact Attack reward: Transform a card",
            false,
            true,
            true,
            false
        )
    }
}
