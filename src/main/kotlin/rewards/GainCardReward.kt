@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.rewards.RewardItem

class GainCardReward : Reward() {
    override val description: String = "+1 Card Reward"

    override val chance: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 5
            AbstractMonster.EnemyType.ELITE -> 10
            AbstractMonster.EnemyType.BOSS -> 5
        }

    override val sortOrder: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 30
            AbstractMonster.EnemyType.ELITE -> 30
            AbstractMonster.EnemyType.BOSS -> 10 // // Card rewards from bosses are always rare, so this is a real treat
        }

    override fun effect() {
        AbstractDungeon.getCurrRoom().rewards.add(RewardItem(AbstractDungeon.player.cardColor))
        displayBonus()
    }
}
