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
            localvars = {"damageAmount", "intentDmg"}
    )
    public static void InsertPre(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount, @ByRef int[] intentDmg) {
        // If the player has damaged the monster for exactly its remaining block, buff the monster
        if ((info.owner == null || info.owner instanceof AbstractPlayer) && damageAmount[0] == __instance.currentBlock) {
            new ExactAttack().buffMonster(__instance);
        }

        // If the player's attack matches the monster's intended attack, the monster will parry the player
        if ((info.owner == null || info.owner instanceof AbstractPlayer) && damageAmount[0] == intentDmg[0]) {
            new ExactAttack().monsterParry(__instance);
            damageAmount[0] = 0; // TODO: Possible bug - when triggered via Thunderclap, this appears to zero out damage for everyone in room.  Requires verification.
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
