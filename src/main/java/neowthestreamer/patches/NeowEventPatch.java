package neowthestreamer.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.NeowTheStreamerReward;

import java.util.ArrayList;

import static neowthestreamer.NeowTheStreamer.makeID;



public class NeowEventPatch {

    @SpirePatch(
            clz = NeowEvent.class,
            method = "blessing"
    )
    public static class RedoRewards {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {}
        )
        public static void Insert(NeowEvent __instance) {
            ArrayList<NeowReward> rewards = new ArrayList<>();
            rewards.add(new NeowTheStreamerReward(0));
            rewards.add(new NeowTheStreamerReward(1));
            rewards.add(new NeowTheStreamerReward(2));
            rewards.add(new NeowTheStreamerReward(3));
            ReflectionHacks.setPrivate(__instance, NeowEvent.class, "rewards", rewards);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "clearRemainingOptions");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    // I am very upset now I cannot do silly text changes
    /*
    private static final String ID = makeID("NeowEventPatch");

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    public static final String[] TEXT = characterStrings.TEXT;

    @SpirePatch(
            clz = NeowEvent.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class ReplaceNeowText {
        public static void Prefix(NeowEvent __instance, boolean isDone) {
            __instance.TEXT = characterStrings.TEXT;
        }
    }*/
}
