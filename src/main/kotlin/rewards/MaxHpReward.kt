@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

class MaxHpReward : Reward() {
    override val description: String = "+2 Max HP"


    override val chance: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 2
            AbstractMonster.EnemyType.ELITE -> 4
            AbstractMonster.EnemyType.BOSS -> 5
        }

    override val sortOrder: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 10
            AbstractMonster.EnemyType.ELITE -> 10
            AbstractMonster.EnemyType.BOSS -> 20
        }

    override fun effect() {
        AbstractDungeon.player.increaseMaxHp(2, true)
        displayBonus()
    }
}

