package neowthestreamer.patches;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import neowthestreamer.cards.DeezNuts;
import neowthestreamer.cards.IsDeezNutsModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.List;
import java.util.stream.Collectors;

public class DeezNutsPatches {
    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GhostSubvertPlay {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager __instance) {
            CardQueueItem cqi = __instance.cardQueue.get(0);
            AbstractCard c = cqi.card;
            if (c != null) {
                if (CardModifierManager.hasModifier(c, IsDeezNutsModifier.ID)) {
                    if (__instance.cardQueue.get(0).monster == null) {
                        __instance.cardQueue.get(0).randomTarget = true;
                    }
                    AbstractDungeon.player.hand.removeCard(c);
                    IsDeezNutsModifier mod = (IsDeezNutsModifier) CardModifierManager.getModifiers(c, IsDeezNutsModifier.ID).get(0);
                    AbstractDungeon.player.cardInUse = mod.deezNuts;
                    __instance.cardQueue.get(0).card = mod.deezNuts;
                    mod.deezNuts.current_x = c.current_x;
                    mod.deezNuts.current_y = c.current_y;
                    mod.deezNuts.target_x = Settings.WIDTH / 2F;
                    mod.deezNuts.target_y = Settings.HEIGHT / 2F;

                    mod.deezNuts.dontTriggerOnUseCard = !cqi.autoplayCard && !mod.deezNuts.canUse(AbstractDungeon.player, null);

                    //AbstractDungeon.actionManager.addToTop(new WaitMoreAction(0.25F));
                    AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            isDone = true;
                            CardCrawlGame.sound.play("ATTACK_WHIFF_2");
                            //for (int itr = 0; itr < 50; itr++) {
                            //    AbstractDungeon.effectsQueue.add(new PurpleNonmovingBlur(mod.deezNuts.target_x, mod.deezNuts.target_y));
                            //}
                        }
                    });
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "usingCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class CantAffordDontUse {
        public static boolean SERIOUSLY_NO = false;

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(GameActionManager __instance) {
            AbstractCard c = __instance.cardQueue.get(0).card;
            if (c.cardID.equals(DeezNuts.ID) && c.dontTriggerOnUseCard) {
                SERIOUSLY_NO = true;
                AbstractDungeon.actionManager.addToBottom(new UseCardAction(c));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "useCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class SeriouslyDontUseIt {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (CantAffordDontUse.SERIOUSLY_NO) {
                CantAffordDontUse.SERIOUSLY_NO = false;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    /* Decided not to copy over info, but in case
    @SpirePatch(clz = CardModifierManager.class, method = "addModifier")
    public static class CopyModifiers {
        public static void Prefix(AbstractCard card, AbstractCardModifier mod) {
            if (mod.shouldApply(card) && !(mod instanceof IsDeezNutsModifier) && !mod.isInherent(card)) {
                if (CardModifierManager.hasModifier(card, IsDeezNutsModifier.ID)) {
                    IsDeezNutsModifier deezNutsMod = (IsDeezNutsModifier) CardModifierManager.getModifiers(card, IsDeezNutsModifier.ID).get(0);
                    CardModifierManager.addModifier(deezNutsMod.deezNuts, mod.makeCopy());
                }
            }
        }
    }
     */

    @SpirePatch(
            clz = EmptyDeckShuffleAction.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class ShuffleDeezNuts {
        public static void Postfix(EmptyDeckShuffleAction __instance) {
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    isDone = true;
                    List<AbstractCard> drawPile = AbstractDungeon.player.drawPile.group.stream().collect(Collectors.toList());
                    for (AbstractCard c : drawPile) {
                        if (CardModifierManager.hasModifier(c, IsDeezNutsModifier.ID)) {
                            int index = AbstractDungeon.player.drawPile.group.indexOf(c);
                            AbstractDungeon.player.drawPile.removeCard(c);
                            IsDeezNutsModifier mod = (IsDeezNutsModifier) CardModifierManager.getModifiers(c, IsDeezNutsModifier.ID).get(0);
                            if (index > 0) {
                                AbstractDungeon.player.drawPile.group.add(index, mod.deezNuts);
                            } else {
                                AbstractDungeon.player.drawPile.addToRandomSpot(mod.deezNuts);
                            }
                        }
                    }
                    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                        if (c instanceof DeezNuts) ((DeezNuts) c).disguiseDeezNuts();
                    }
                }
            });
        }
    }
}