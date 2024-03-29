package exacting

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class StunPower(abstractMonster: AbstractMonster) : AbstractPower() {

    private var originalIntent: AbstractMonster.Intent;
    private var originalNextMove: Byte;
    private var monster: AbstractMonster
    private var roundsApplied = 0;

    companion object {
        @JvmStatic
        val logger: Logger = LogManager.getLogger(StunPower::class.java.name);
        @JvmStatic
        val UniqueIdentifier = "exacting:Stun"
    }

    init {
        this.type = PowerType.BUFF; // Bypass Artifact
        this.isTurnBased = true
        this.owner = abstractMonster
        this.monster = abstractMonster
        this.name = "Stun"
        this.ID = UniqueIdentifier
        this.description = "This creature is stunned and will not act."
        this.updateDescription()
        img = ImageMaster.loadImage("images/stun.png");
        this.originalIntent = abstractMonster.intent
        this.originalNextMove = abstractMonster.nextMove
    }

    override fun onInitialApplication() {
        super.onInitialApplication()
        monster.setMove("Stunned", -1, AbstractMonster.Intent.STUN)
        monster.createIntent()
    }

    override fun atEndOfRound() {
        if (roundsApplied++ == 1) {
            AbstractDungeon.actionManager.addToBottom(
                RemoveSpecificPowerAction(
                    this.owner,
                    this.owner,
                    this.ID
                )
            )
        }
    }

    override fun onRemove() {
        super.onRemove()
        logger.debug("Removing power")
        monster.nextMove = originalNextMove
        monster.setMove(this.originalNextMove, originalIntent)
        monster.createIntent()
    }
}
