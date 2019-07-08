package exacting

import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.GainPennyEffect

class GoldReward : Reward() {
    private val amount: Int
        get() = when (monster.type) {
            AbstractMonster.EnemyType.ELITE -> 20
            AbstractMonster.EnemyType.BOSS -> 45
            else -> 15
        }

    override val description: String
        get() {
            return "Gain $amount Gold"
        }

    override val chance: Int = 100

    override val sortOrder: Int = 1000

    override fun effect() {
        CardCrawlGame.sound.play("GOLD_GAIN")
        AbstractDungeon.player.gainGold(amount)

        for (i in 1..amount) {
            AbstractDungeon.effectList.add(
                GainPennyEffect(
                    AbstractDungeon.player.hb.cX,
                    AbstractDungeon.player.hb.cY
                )
            )
        }

        displayBonus()
    }
}
