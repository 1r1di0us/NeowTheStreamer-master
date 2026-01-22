package neowthestreamer.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import neowthestreamer.NeowTheStreamerReward;

import java.util.ArrayList;

public class NeowEventPatch {

    @SpirePatch(
            clz = NeowEvent.class,
            method = "blessing"
    )
    public static class AddTooltips {
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
            __instance.roomEventText.clear();
            for (int i = 0; i < 4; i++) {
                if (((NeowTheStreamerReward) rewards.get(i)).curses.isEmpty()) {
                    __instance.roomEventText.addDialogOption((rewards.get(i)).optionLabel, ((NeowTheStreamerReward) rewards.get(i)).cardReward, ((NeowTheStreamerReward) rewards.get(i)).relicReward);
                } else {
                    __instance.roomEventText.addDialogOption((rewards.get(i)).optionLabel, ((NeowTheStreamerReward) rewards.get(i)).curses.get(0), ((NeowTheStreamerReward) rewards.get(i)).relicReward);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(NeowEvent.class, "screenNum");
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

    @SpirePatch(
            clz = NeowEvent.class,
            method = "buttonEffect"
    )
    public static class ChooseCards {
        @SpireInsertPatch(
                locator = ChooseCards.Locator.class,
                localvars = {}
        )
        public static void Insert(NeowEvent __instance, int buttonPressed) {
            if (!Loader.isModLoaded("versus")) {
                NeowEventPatch.chooseCards((NeowEvent) AbstractDungeon.getCurrRoom().event);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "clearRemainingOptions");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
            }
        }
    }

    static void chooseCards(NeowEvent self) {
        ReflectionHacks.setPrivate(self, NeowEvent.class, "pickCard", true);

        // Don't forget remove all cards in deck
        AbstractDungeon.player.masterDeck.group.removeIf(c -> !c.cardID.equals(AscendersBane.ID));

        CardGroup sealedGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        // Guaranteed Rares
        for (int rares = 0; rares < 2; rares++) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);
            sealedGroup.addToBottom(card.makeCopy());
        }

        // Guaranteed Rares
        for (int uncommons = 0; uncommons < 5; uncommons++) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON);
            sealedGroup.addToBottom(card.makeCopy());
        }

        // Guaranteed Rares
        for (int commons = 0; commons < 10; commons++) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON);
            sealedGroup.addToBottom(card.makeCopy());
        }

        // Unguaranteed cards
        if (sealedGroup.size() < 30) {
            int size = sealedGroup.size();
            for (int i = 0; i < 30-size; i++)
            {
                AbstractCard card = AbstractDungeon.getCard(AbstractDungeon.rollRarity());
                if (!sealedGroup.contains(card)) {
                    sealedGroup.addToBottom(card.makeCopy());
                } else { i--; }
            }
        }

        // Make sure all the cards are visible
        for (AbstractCard c : sealedGroup.group) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }

        // Open the choice dialog
        AbstractDungeon.gridSelectScreen.open(sealedGroup, 10,
                "Choose 10 cards to start with.",
                false);
    }
}
