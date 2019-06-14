@file:JvmName("ExactAttack")

package exacting

import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.core.CardCrawlGame.*
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.IntangiblePower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.shop.ShopScreen.rollRelicTier
import com.megacrit.cardcrawl.vfx.GainPennyEffect
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.*

class ExactAttack {
    companion object {
        @JvmStatic
        val logger: Logger = LogManager.getLogger(ExactAttack::class.java.name);
    }

    fun buffMonster(monster: AbstractMonster) {
        logger.debug("Buffing monster")

        TextCenteredAction(player, "Exacting Mishap").push()

        when {
            chance(3) -> {
                monster.applyPower(IntangiblePower(monster, 1))
                TextAboveCreatureAction(monster, "+1 Intangible").push()
            }
            chance(7) -> {
                val amount = (monster.maxHealth * .2).toInt()
                monster.heal(amount)
                TextAboveCreatureAction(monster, "+$amount Heal").push()
            }
            chance(10) -> {
                monster.currentBlock += 10
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
        val monsterType = monster.type ?: return

        // No rewards for minions
        if (monster.isMinion())
            return

        when (monsterType) {
            EnemyType.NORMAL -> when {
                chance(2) -> awardMaxHp()
                chance(2) -> awardRelic()
//                chance(3) -> awardRemoveCard()
//                chance(3) -> awardTransformCard()
//                chance(3) -> awardUpgradeCard()
                chance(5) -> awardGainCard()
                chance(10) -> awardPotion()
                chance(10) -> awardHeal()
                chance(10) -> awardEnergy()
                else -> awardGold(15)
            }
            EnemyType.ELITE -> when {
                chance(4) -> awardMaxHp()
                chance(5) -> awardRelic()
//                chance(6) -> awardRemoveCard()
//                chance(6) -> awardTransformCard()
//                chance(6) -> awardUpgradeCard()
                chance(10) -> awardGainCard()
                chance(30) -> awardPotion()
                chance(30) -> awardHeal()
                chance(10) -> awardEnergy(2)
                else -> awardGold(20)
            }
            EnemyType.BOSS -> when {
                chance(5) -> awardGainCard() // Card rewards from bosses are always rare, so this is a real treat
                chance(10) -> awardMaxHp()
                chance(10) -> awardRelic()
//                chance(10) -> awardRemoveCard()
//                chance(10) -> awardTransformCard()
//                chance(10) -> awardUpgradeCard()
                chance(30) -> awardPotion()
                chance(30) -> awardHeal()
                else -> awardGold(45)
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

    private fun awardGold(amount: Int) {
        sound.play("GOLD_GAIN")
        player.gainGold(amount)

        for (i in 1..amount) {
            effectList.add(GainPennyEffect(player.hb.cX, player.hb.cY))
        }

        displayBonus("Gain ${amount} Gold")
    }

    private fun awardGainCard() {
        getCurrRoom().rewards.add(RewardItem(player.cardColor))

        displayBonus("+1 Card Reward")
    }

    private fun awardRelic() {
        val relic = returnRandomRelicEnd(rollRelicTier())
        getCurrRoom().rewards.add(RewardItem(relic))

        displayBonus("+1 Relic Reward")
    }

    private fun awardPotion() {
        val potion = returnRandomPotion();
        getCurrRoom().rewards.add(RewardItem(potion))

        displayBonus("+1 Potion Reward")
    }

    private fun awardMaxHp() {
        player.increaseMaxHp(2, true)

        displayBonus("2 Max HP")
    }

    private fun awardHeal() {
        var amount = (player.maxHealth * .15).toInt();
        player.heal(amount, true)
        displayBonus("Heal 15% of Max HP")
    }

    private fun awardEnergy(amount: Int = 1) {
        player.gainEnergy(1)
        displayBonus("+$amount Energy")
    }

    private fun displayBonus(description: String) {
        logger.info("Granting reward: $description")

        TextCenteredAction(player, "Exact Attack").push()
        TextAboveCreatureAction(player, description).push()
    }
}
