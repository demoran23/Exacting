@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.Sozu
import com.megacrit.cardcrawl.rewards.RewardItem

class PotionReward : Reward() {
    override val description: String = "+1 Potion Reward"

    override val chance: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 10
            AbstractMonster.EnemyType.ELITE -> 30
            AbstractMonster.EnemyType.BOSS -> 30
        }

    override val sortOrder: Int = 40

    override fun effect() {
        val potion = AbstractDungeon.returnRandomPotion();
        AbstractDungeon.getCurrRoom()
            .rewards.add(RewardItem(potion))
        displayBonus()
    }

    override fun predicate(): Boolean = !AbstractDungeon.player.hasRelic(
        Sozu.ID
    )
}

