package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import neowthestreamer.relics.PearWheel;

@SpirePatch(
        clz = CardRewardScreen.class,
        method = "acquireCard"
)
public class PearWheelPatch {
    public static void Postfix(CardRewardScreen __instance, AbstractCard hoveredCard) {
        if (AbstractDungeon.player.hasRelic(PearWheel.ID)) {
            AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
        }
    }
}
