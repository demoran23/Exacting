@file:Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")

package exacting

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster

class HealReward : Reward() {
    val amount: Int
        get() = (AbstractDungeon.player.maxHealth * .15).toInt();

    override val description: String = "Heal 15% of Max HP"

    override val chance: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.NORMAL -> 10
            AbstractMonster.EnemyType.ELITE -> 30
            AbstractMonster.EnemyType.BOSS -> 30
        }

    override val sortOrder: Int = 50

    override fun effect() {
        AbstractDungeon.player.heal(amount, true)
        displayBonus()
    }
}

