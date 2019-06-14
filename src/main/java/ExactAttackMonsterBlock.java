package exacting;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "damage",
        paramtypez = {DamageInfo.class}
)
public class ExactAttackMonsterBlock {
    private static final Logger logger = LogManager.getLogger(ExactAttackMonsterBlock.class.getName());

    @SpireInsertPatch(
            locator = ExactAttackMonsterBlock.Locator.class,
            localvars = {"damageAmount"}
    )
    public static void InsertPre(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
        // If the player has damaged the monster for exactly its remaining block, buff the monster
        if ((info.owner == null || info.owner instanceof AbstractPlayer) && damageAmount[0] == __instance.currentBlock) {
            new ExactAttack().buffMonster(__instance);
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
