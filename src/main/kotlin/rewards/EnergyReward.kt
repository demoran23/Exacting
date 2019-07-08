@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

class EnergyReward : Reward() {
    val amount: Int
        get() = (AbstractDungeon.player.maxHealth * .15).toInt();

    override val description: String = "+1 Energy"

    override val chance: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 10
            AbstractMonster.EnemyType.ELITE -> 10
            AbstractMonster.EnemyType.BOSS -> 0 // Bosses are solo, for the most part
        }

    override val sortOrder: Int = 60

    override fun effect() {
        AbstractDungeon.player.gainEnergy(1)
        displayBonus()
    }

    override fun predicate(): Boolean {
        return AbstractDungeon.getCurrRoom().monsters.monsters.count() > 1
    }
}
