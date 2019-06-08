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
    }

    private data class RewardConfig(
        val enemyType: AbstractMonster.EnemyType = AbstractMonster.EnemyType.NORMAL,
        val gold: IntRange = 0..0,
        val card: IntRange = 0..0,
        val potion: IntRange = 0..0,
        val relic: IntRange = 0..0,
        val maxHp: IntRange = 0..0
    )

    fun getReward(monster: AbstractMonster) {
        logger.debug("Generating reward")
        when (monster.type) {
            AbstractMonster.EnemyType.NORMAL ->
                return generateReward(
                    RewardConfig(
                        enemyType = monster.type,
                        gold = 0..80,
                        potion = 81..90,
                        card = 91..95,
                        relic = 96..97,
                        maxHp = 98..100
                    )
                )
            AbstractMonster.EnemyType.ELITE ->
                return generateReward(
                    RewardConfig(
                        enemyType = monster.type,
                        gold = 0..60,
                        potion = 61..80,
                        card = 81..90,
                        relic = 91..95,
                        maxHp = 96..100
                    )
                )
            AbstractMonster.EnemyType.BOSS ->
                return generateReward(
                    RewardConfig(
                        enemyType = monster.type,
                        gold = 0..30,
                        potion = 31..50,
                        card = 51..60, // Card rewards from bosses are always rare, so this is a real treat
                        relic = 61..80,
                        maxHp = 81..100
                    )
                )
        }
    }

    private fun generateReward(config: RewardConfig) {
        val roll = Random().nextInt(101)
        when {
            config.gold.contains(roll) -> awardGold(config)
            config.card.contains(roll) -> awardCard(config)
            config.relic.contains(roll) -> awardRelic()
            config.maxHp.contains(roll) -> awardMaxHp()
            config.potion.contains(roll) -> awardPotion()
        }
    }

    private fun awardGold(config: RewardConfig) {
        logger.debug("Giving gold")

        var amount = when (config.enemyType) {
            AbstractMonster.EnemyType.NORMAL -> 15
            AbstractMonster.EnemyType.ELITE -> 25
            AbstractMonster.EnemyType.BOSS -> 40
        }

        CardCrawlGame.sound.play("GOLD_GAIN")
        AbstractDungeon.player.gainGold(amount)

        for (i in 1..amount) {
            AbstractDungeon.effectList.add(GainPennyEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY))
        }

        displayBonus("Gain ${amount} Gold")
    }

    private fun awardCard(config: RewardConfig) {
        logger.debug("Giving card")

        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(AbstractDungeon.player.cardColor))

        displayBonus("+1 Card Reward")
    }

    private fun awardRelic() {
        logger.debug("Giving relic")

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
