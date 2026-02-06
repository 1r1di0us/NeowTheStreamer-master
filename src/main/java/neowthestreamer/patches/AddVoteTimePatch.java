package neowthestreamer.patches;


import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import de.robojumper.ststwitch.TwitchVoteOption;
import de.robojumper.ststwitch.TwitchVoter;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.NeowTheStreamer;

import java.util.ArrayList;
import java.util.function.Consumer;

@SpirePatch(
        clz = TwitchVoter.class,
        method = "initiateVote"
)
public class AddVoteTimePatch {
    @SpireInsertPatch(
            locator = AddVoteTimePatch.Locator.class,
            localvars = {}
    )
    private static void Insert(TwitchVoter __instance, TwitchVoteOption[] options, Consumer<Integer> voteCb) {
        if (NeowTheStreamer.addTime.toggle.enabled && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.EVENT && AbstractDungeon.getCurrRoom().event instanceof NeowEvent) {
            float newTimer = ReflectionHacks.getPrivate(__instance, TwitchVoter.class, "timer");
            newTimer += NeowTheStreamer.ADD_TIME_AMT;
            ReflectionHacks.setPrivate(__instance, TwitchVoter.class, "timer", newTimer);
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(TwitchVoter.class, "triggered");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}
