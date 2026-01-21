package neowthestreamer.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.interfaces.OnPotionDiscardInterface;

@SpirePatch(
        clz= PotionPopUp.class,
        method = "updateInput"
)
public class OnDiscardPotionPatch {
    @SpireInsertPatch(locator = Locator.class)
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
    }
}
