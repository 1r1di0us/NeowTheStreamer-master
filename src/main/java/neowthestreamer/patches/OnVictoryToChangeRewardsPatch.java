package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.interfaces.OnVictoryToChangeRewardsInterface;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class OnVictoryToChangeRewardsPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {}
    )
    private static void Insert(AbstractRoom __instance) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof OnVictoryToChangeRewardsInterface) {
                ((OnVictoryToChangeRewardsInterface) r).onVictoryToChangeRewards();
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof OnVictoryToChangeRewardsInterface) {
                ((OnVictoryToChangeRewardsInterface) p).onVictoryToChangeRewards();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "loadingSave");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[2]};
        }
    }
}
