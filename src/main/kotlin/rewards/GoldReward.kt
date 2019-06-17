package exacting

import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.GainPennyEffect

class GoldReward : Reward() {
    private fun amount(monster: AbstractMonster): Int = when (monster.type) {
        AbstractMonster.EnemyType.ELITE -> 20
        AbstractMonster.EnemyType.BOSS -> 45
        else -> 15
    }

    override fun description(monster: AbstractMonster): String {
        return "Gain ${amount(monster)} Gold"
    }

    override fun chance(monster: AbstractMonster): Int = 100

    override fun sortOrder(monster: AbstractMonster): Int = 1000

    override fun effect(monster: AbstractMonster) {
        val amount = amount(monster)

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

        displayBonus(monster)
    }
}
