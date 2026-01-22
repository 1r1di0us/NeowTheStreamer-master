package neowthestreamer.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.relics.KeySmashingChallenge;

import java.util.ArrayList;

@SpirePatch(
        clz = ObtainKeyEffect.class,
        method = "update"
)
public class KeySmashingPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {}
    )

    public static SpireReturn<Void> Insert(ObtainKeyEffect __instance) {
        if (AbstractDungeon.player.hasRelic(KeySmashingChallenge.ID) && !AbstractDungeon.player.getRelic(KeySmashingChallenge.ID).usedUp && AbstractDungeon.player.getRelic(KeySmashingChallenge.ID).counter <= 5) {
            ((KeySmashingChallenge) AbstractDungeon.player.getRelic(KeySmashingChallenge.ID)).onKeyDiscard();
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(ObtainKeyEffect.class, "keyColor");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
