package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.relics.PotionDumpingChallenge;

/*@SpirePatch(
        clz= PotionPopUp.class,
        method = "updateInput"
)*/
@SpirePatch(
        clz = RewardItem.class,
        method = "claimReward"
        )
public class PotionDumpingPatch {

    @SpireInsertPatch(
            locator= Locator.class
    )
    public static SpireReturn<Boolean> Insert(RewardItem __instance) {
        if (AbstractDungeon.player.hasRelic(PotionDumpingChallenge.ID) && !AbstractDungeon.player.getRelic(PotionDumpingChallenge.ID).usedUp) {
            ((PotionDumpingChallenge) AbstractDungeon.player.getRelic(PotionDumpingChallenge.ID)).onPotionRemove();
            return SpireReturn.Return(true);
        } else {
            return SpireReturn.Continue();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[0]};
        }
    }

    // This patch was for potion trashing that triggered when you discard a potion. The new one just eats your potion when you get it
    /*@SpireInsertPatch(locator = Locator.class)
    public static void Insert(PotionPopUp __instance) {
        AbstractPotion potion = ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "potion");
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof OnPotionDiscardInterface) {
                ((OnPotionDiscardInterface) r).OnPotionDiscard(potion);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(PotionPopUp.class, "close");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[2]};
        }
    }*/
}
