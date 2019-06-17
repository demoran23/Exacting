@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.Sozu
import com.megacrit.cardcrawl.rewards.RewardItem

class PotionReward : Reward() {
    override fun description(monster: AbstractMonster): String = "+1 Potion Reward"

    override fun chance(monster: AbstractMonster): Int = when (monster.type) {
        AbstractMonster.EnemyType.NORMAL -> 10
        AbstractMonster.EnemyType.ELITE -> 30
        AbstractMonster.EnemyType.BOSS -> 30
    }

    override fun sortOrder(monster: AbstractMonster): Int = 40

    override fun effect(monster: AbstractMonster) {
        val potion = AbstractDungeon.returnRandomPotion();
        AbstractDungeon.getCurrRoom()
            .rewards.add(RewardItem(potion))
        displayBonus(monster)
    }

    override fun predicate(monster: AbstractMonster): Boolean = !AbstractDungeon.player.hasRelic(
        Sozu.ID
    )
}

