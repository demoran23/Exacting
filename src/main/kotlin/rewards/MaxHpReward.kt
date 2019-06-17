@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

class MaxHpReward : Reward() {
    override fun description(monster: AbstractMonster): String = "+2 Max HP"


    override fun chance(monster: AbstractMonster): Int = when (monster.type) {
        AbstractMonster.EnemyType.NORMAL -> 2
        AbstractMonster.EnemyType.ELITE -> 4
        AbstractMonster.EnemyType.BOSS -> 5
    }

    override fun sortOrder(monster: AbstractMonster): Int = when (monster.type) {
        AbstractMonster.EnemyType.NORMAL -> 10
        AbstractMonster.EnemyType.ELITE -> 10
        AbstractMonster.EnemyType.BOSS -> 20
    }

    override fun effect(monster: AbstractMonster) {
        AbstractDungeon.player.increaseMaxHp(2, true)
        displayBonus(monster)
    }
}

