package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.relics.MaskOfMidas;

import java.util.ArrayList;


public class MaskOfMidasPatch {
    @SpirePatch(
            clz = ShopScreen.class,
            method = "updatePurge"
    )
    public static class updateMaskOfMidas {
        public static SpireReturn<Void> Prefix(ShopScreen __instance) {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.player.hasRelic(MaskOfMidas.ID)) {
                ShopScreen.purgeCard();
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(card.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.shopScreen.purgeAvailable = false;
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = ShopScreen.class,
            method = "purchasePurge"
    )
    public static class StartMaskPulse {
        @SpireInsertPatch(
                locator = MaskOfMidasPatch.StartMaskPulse.Locator.class,
                localvars = {}
        )
        public static void Insert(ShopScreen __instance) {
            if (AbstractDungeon.player.hasRelic(MaskOfMidas.ID)) {
                AbstractDungeon.player.getRelic(MaskOfMidas.ID).beginLongPulse();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "previousScreen");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = ShopScreen.class,
            method = "update"
    )
    public static class StopMaskPulse {
        public static void Postfix(ShopScreen __instance) {
            if (AbstractDungeon.player.hasRelic(MaskOfMidas.ID) && (!__instance.purgeAvailable || AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID)) {
                AbstractDungeon.player.getRelic(MaskOfMidas.ID).stopPulse();
            }
        }
    }
}
