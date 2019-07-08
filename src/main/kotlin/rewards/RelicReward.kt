@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.shop.ShopScreen

class RelicReward : Reward() {
    override val description: String = "+1 Relic Reward"

    override val chance: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 2
            AbstractMonster.EnemyType.ELITE -> 5
            AbstractMonster.EnemyType.BOSS -> 10
        }

    override val sortOrder: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 20
            AbstractMonster.EnemyType.ELITE -> 20
            AbstractMonster.EnemyType.BOSS -> 30
        }

    override fun effect() {
        val relic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier())
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(relic))

        displayBonus()
    }
}

