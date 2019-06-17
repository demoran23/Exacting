@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

class EnergyReward : Reward() {
    val amount: Int
        get() = (AbstractDungeon.player.maxHealth * .15).toInt();

    override fun description(monster: AbstractMonster): String = "+1 Energy"

    override fun chance(monster: AbstractMonster): Int = when (monster.type) {
        AbstractMonster.EnemyType.NORMAL -> 10
        AbstractMonster.EnemyType.ELITE -> 10
        AbstractMonster.EnemyType.BOSS -> 0 // Bosses are solo, for the most part
    }

    override fun sortOrder(monster: AbstractMonster): Int = 60

    override fun effect(monster: AbstractMonster) {
        AbstractDungeon.player.gainEnergy(1)
        displayBonus(monster)
    }

    override fun predicate(monster: AbstractMonster): Boolean {
        return AbstractDungeon.getCurrRoom().monsters.monsters.count() > 1
    }
}
