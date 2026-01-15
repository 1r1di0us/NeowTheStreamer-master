package neowthestreamer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamer;
import neowthestreamer.NeowTheStreamerEvent;

@SpirePatch(
        clz = NeowRoom.class,
        method = SpirePatch.CONSTRUCTOR
)
public class NeowRoomPatch {

    public static SpireReturn<Void> Prefix(NeowRoom __instance, boolean isDone) {
        if (NeowTheStreamer.modEnabled) {
            __instance.phase = AbstractRoom.RoomPhase.EVENT;
            __instance.event = new NeowTheStreamerEvent(isDone);
            __instance.event.onEnterRoom();
            return SpireReturn.Return();
        } else {
            return SpireReturn.Continue();
        }
    }
/*
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {}
    )

    public static void Insert(NeowRoom __instance, boolean isDone) {
        __instance.event = new NeowTheStreamerEvent(isDone);
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(NeowRoom.class, "event");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }*/
}