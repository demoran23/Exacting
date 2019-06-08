package exacting;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "damage",
        paramtypez = {DamageInfo.class}
)
public class ExactAttack {
    private static final Logger logger = LogManager.getLogger(AbstractCreature.class.getName());

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"damageAmount"}
    )
    public static void InsertPre(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {

        // If the player has damaged the monster for exactly its full health, reward the player
        // Powers like Immolate apply the DamageAllEnemiesAction, which nulls out the owner
        if ((info.owner == null || info.owner instanceof AbstractPlayer) && damageAmount[0] == __instance.currentHealth) {
            new ExactAttackRewardFactory().getReward(__instance);
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "useStaggerAnimation");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}

