package neowthestreamer.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class DeckBuildingChallenge extends BaseRelic implements ActTwoChallengeInterface {
    public static String ID = makeID("DeckBuildingChallenge");

    public int goal;
    public int initial;
    public boolean ended;

    public DeckBuildingChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.MAX_HP);
    }

    public DeckBuildingChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.goal = 4;
        this.initial = 11;
        this.reward = reward;
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 4 + DESCRIPTIONS[1] + 11 + DESCRIPTIONS[2];
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)];
        }
    }

    public void onMasterDeckChange() {
        if ((AbstractDungeon.player.masterDeck.size() - initial) / goal != counter && !ended) {
            counter = (AbstractDungeon.player.masterDeck.size() - initial) / goal;
            this.description = getUpdatedDescription();
        }
    }

    public void onEnterActTwo() {
        if (!ended) {
            if (this.counter > 0) {
                this.activated = true;
            }
            NeowTheStreamerReward.activateChallengeRewards(this.reward, counter);
            this.counter = -1;
            this.ended = true;
            this.usedUp();
        }
    }
}
