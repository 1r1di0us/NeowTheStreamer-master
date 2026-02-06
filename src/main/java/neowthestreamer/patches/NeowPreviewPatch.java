package neowthestreamer.patches;


import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

public class NeowPreviewPatch {

    @SpirePatch(
            clz = LargeDialogOptionButton.class,
            method = "renderRelicPreview"
    )
    public static class RelicPreviewPatch {
        public static SpireReturn<Void> Prefix(LargeDialogOptionButton __instance, SpriteBatch sb) {
            AbstractRelic relicToPreview = ReflectionHacks.getPrivate(__instance, LargeDialogOptionButton.class, "relicToPreview");
            if (!Settings.isControllerMode && relicToPreview != null && __instance.hb.hovered
                    && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.EVENT && AbstractDungeon.getCurrRoom().event instanceof NeowEvent) {
                float y = ReflectionHacks.getPrivate(__instance, LargeDialogOptionButton.class, "y");
                TipHelper.queuePowerTips(1070.0F * Settings.scale, (float) y + TipHelper.calculateToAvoidOffscreen(relicToPreview.tips, (float) y), relicToPreview.tips);
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = LargeDialogOptionButton.class,
            method = "renderCardPreview"
    )
    public static class CardPreviewPatch {
        public static SpireReturn<Void> Prefix( LargeDialogOptionButton __instance, SpriteBatch sb) {
            AbstractRelic relicToPreview = ReflectionHacks.getPrivate(__instance, LargeDialogOptionButton.class, "relicToPreview");
            AbstractCard cardToPreview = ReflectionHacks.getPrivate(__instance, LargeDialogOptionButton.class, "cardToPreview");
            float x = ReflectionHacks.getPrivate(__instance, LargeDialogOptionButton.class, "x");
            float y = ReflectionHacks.getPrivate(__instance, LargeDialogOptionButton.class, "y");
            if (cardToPreview != null && __instance.hb.hovered
                    && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.EVENT && AbstractDungeon.getCurrRoom().event instanceof NeowEvent) {
                if (relicToPreview != null) cardToPreview.current_x = x + 400.0F + __instance.hb.width / 1.75F;
                else cardToPreview.current_x = x + 50.0F + __instance.hb.width / 1.75F;
                if (y < cardToPreview.hb.height / 2.0F + 5.0F) {
                    y = cardToPreview.hb.height / 2.0F + 5.0F;
                }
                cardToPreview.current_y = y;
                cardToPreview.render(sb);
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}
