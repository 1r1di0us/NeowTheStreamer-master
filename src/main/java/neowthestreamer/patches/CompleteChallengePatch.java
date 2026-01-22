package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import neowthestreamer.interfaces.ActTwoChallengeInterface;

import java.util.ArrayList;

@SpirePatch(
        clz = TreasureRoomBoss.class,
        method = "onPlayerEntry"
)
public class CompleteChallengePatch {
    public static void Postfix(TreasureRoomBoss __instance) {
        ArrayList<AbstractRelic> ChallengeRelics = new ArrayList<>();
        if (AbstractDungeon.actNum == 1 && __instance.relics.isEmpty()) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof ActTwoChallengeInterface) {
                    ChallengeRelics.add(r);
                }
            }
        }
        for (AbstractRelic cr : ChallengeRelics) {
            ((ActTwoChallengeInterface) cr).onEnterActTwo();
        }
    }
}
