@file:JvmName("ExactAttackRewardFactory")

package exacting

import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.shop.ShopScreen.rollRelicTier
import com.megacrit.cardcrawl.vfx.GainPennyEffect
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class ExactAttackRewardFactory {
    companion object {
        @JvmStatic
        val logger: Logger = LogManager.getLogger(ExactAttackRewardFactory::class.java.name);
    }

    fun getReward(monster: AbstractMonster) {
        val monsterType = monster.type ?: return
        when (monsterType) {
            AbstractMonster.EnemyType.NORMAL -> when {
                chance(2) -> awardMaxHp()
                chance(2) -> awardRelic()
//                chance(3) -> awardRemoveCard()
//                chance(3) -> awardTransformCard()
//                chance(3) -> awardUpgradeCard()
                chance(5) -> awardGainCard()
                chance(10) -> awardPotion()
                chance(10) -> awardHeal()
                else -> awardGold(15)
            }
            AbstractMonster.EnemyType.ELITE -> when {
                chance(4) -> awardMaxHp()
                chance(5) -> awardRelic()
//                chance(6) -> awardRemoveCard()
//                chance(6) -> awardTransformCard()
//                chance(6) -> awardUpgradeCard()
                chance(10) -> awardGainCard()
                chance(30) -> awardPotion()
                chance(30) -> awardHeal()
                else -> awardGold(25)
            }
            AbstractMonster.EnemyType.BOSS -> when {
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
        AbstractDungeon.gridSelectScreen.open(
            CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.purgeableCards),
            1,
            "Exact Attack reward: Remove a card",
            false,
            false,
            true,
            true
        )
    }

    private fun awardUpgradeCard() {
        AbstractDungeon.gridSelectScreen.open(
            CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.upgradableCards),
            1,
            "Exact Attack reward: Upgrade a card",
            true,
            false,
            true,
            false
        )

        AbstractDungeon.gridSelectScreen.selectedCards.forEach {
            logger.debug("upgrading ${it.name}")
            it.upgrade()
        }
        AbstractDungeon.gridSelectScreen.selectedCards.clear();

    }

    private fun awardTransformCard() {
        AbstractDungeon.gridSelectScreen.open(
            CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.purgeableCards),
            1,
            "Exact Attack reward: Transform a card",
            false,
            true,
            true,
            false
        )
    }

    private fun awardGold(amount: Int) {
        CardCrawlGame.sound.play("GOLD_GAIN")
        AbstractDungeon.player.gainGold(amount)

        for (i in 1..amount) {
            AbstractDungeon.effectList.add(GainPennyEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY))
        }

        displayBonus("Gain ${amount} Gold")
    }

    private fun awardGainCard() {
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(AbstractDungeon.player.cardColor))

        displayBonus("+1 Card Reward")
    }

    private fun awardRelic() {
        val relic = AbstractDungeon.returnRandomRelicEnd(rollRelicTier())
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(relic))

        displayBonus("+1 Relic Reward")
    }

    private fun awardPotion() {
        val potion = AbstractDungeon.returnRandomPotion();
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(potion))

        displayBonus("+1 Potion Reward")
    }

    private fun awardMaxHp() {
        AbstractDungeon.player.increaseMaxHp(2, true)

        displayBonus("2 Max HP")
    }

    private fun awardHeal() {
        var amount = (AbstractDungeon.player.maxHealth * .15).toInt();
        AbstractDungeon.player.heal(amount, true)
        displayBonus("Heal 15% of Max HP")
    }

    private fun displayBonus(description: String) {
        logger.info("Granting reward: $description")

        AbstractDungeon.actionManager.addToBottom(
            TextCenteredAction(
                AbstractDungeon.player,
                "Exact Attack"
            )
        )

        AbstractDungeon.actionManager.addToBottom(
            TextAboveCreatureAction(
                AbstractDungeon.player,
                description
            )
        )
    }
}
