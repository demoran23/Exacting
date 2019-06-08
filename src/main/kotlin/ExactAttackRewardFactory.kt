@file:JvmName("ExactAttackRewardFactory")

package exacting

import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction
import com.megacrit.cardcrawl.actions.utility.TextCenteredAction
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.shop.ShopScreen.rollRelicTier
import com.megacrit.cardcrawl.vfx.GainPennyEffect
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*

class ExactAttackRewardFactory {
    companion object {
        val logger: Logger = LogManager.getLogger(ExactAttackRewardFactory::class.java.name);
        val chance: (Int) -> Boolean = { chance -> Random().nextInt(100) < chance }
    }

    fun getReward(monster: AbstractMonster) {
        val monsterType = monster.type ?: return

        when (monsterType) {
            AbstractMonster.EnemyType.NORMAL -> when {
                chance(2) -> awardMaxHp()
                chance(2) -> awardRelic()
                chance(5) -> awardCard()
                chance(10) -> awardPotion()
                else -> awardGold(15)
            }
            AbstractMonster.EnemyType.ELITE -> when {
                chance(4) -> awardMaxHp()
                chance(5) -> awardRelic()
                chance(10) -> awardCard()
                chance(30) -> awardPotion()
                else -> awardGold(25)
            }
            AbstractMonster.EnemyType.BOSS -> when {
                chance(5) -> awardCard() // Card rewards from bosses are always rare, so this is a real treat
                chance(10) -> awardMaxHp()
                chance(10) -> awardRelic()
                chance(30) -> awardPotion()
                else -> awardGold(45)
            }
        }
    }

    private fun awardGold(amount: Int) {
        CardCrawlGame.sound.play("GOLD_GAIN")
        AbstractDungeon.player.gainGold(amount)

        for (i in 1..amount) {
            AbstractDungeon.effectList.add(GainPennyEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY))
        }

        displayBonus("Gain ${amount} Gold")
    }

    private fun awardCard() {
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(AbstractDungeon.player.cardColor))

        displayBonus("+1 Card Reward")
    }

    private fun awardRelic() {
        val relic = AbstractDungeon.returnRandomRelicEnd(rollRelicTier())
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(relic))

        displayBonus("+1 Relic Reward")
    }

    private fun awardPotion() {
        logger.debug("Giving potion")

        val potion = AbstractDungeon.returnRandomPotion();
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(potion))

        displayBonus("+1 Potion Reward")
    }

    private fun awardMaxHp() {
        logger.debug("Giving max hp")
        AbstractDungeon.player.increaseMaxHp(2, true)
        displayBonus("2 Max HP")
    }

    private fun displayBonus(description: String) {
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
