package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.interfaces.ActTwoChallengeInterface;

import static neowthestreamer.NeowTheStreamer.logger;

@SpirePatch(
        clz = BossRelicSelectScreen.class,
        method = "update"
)
public class CompleteChallengePatch {
    public static void Postfix(BossRelicSelectScreen __instance) {
        if (AbstractDungeon.actNum == 1 && __instance.relics.isEmpty()) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof ActTwoChallengeInterface) {
                    ((ActTwoChallengeInterface) r).onEnterActTwo();

                }
            }
        }
    }
}
