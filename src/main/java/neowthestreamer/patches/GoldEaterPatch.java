package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import neowthestreamer.relics.GoldEatingChallenge;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "gainGold"
)
public class GoldEaterPatch {
    public static SpireReturn<Void> Prefix(AbstractPlayer __instance, int amount) {
        if (__instance.hasRelic(GoldEatingChallenge.ID) && !__instance.getRelic(GoldEatingChallenge.ID).usedUp) {
            ((GoldEatingChallenge) __instance.getRelic(GoldEatingChallenge.ID)).onEatGold(amount);
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }
    }
}
