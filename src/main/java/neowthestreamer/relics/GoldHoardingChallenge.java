package neowthestreamer.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class GoldHoardingChallenge extends BaseRelic implements ActTwoChallengeInterface {
    public static String ID = makeID("GoldHoardingChallenge");

    public int goal;
    public int initial;
    public boolean ended;

    public GoldHoardingChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.MAX_HP);
    }

    public GoldHoardingChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.goal = 100;
        this.initial = 99;
        this.reward = reward;
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 100 + DESCRIPTIONS[1] + 99 + DESCRIPTIONS[2];
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)];
        }
    }

    public void onGainGold() {
        if ((AbstractDungeon.player.gold - initial - (counter*goal)) >= goal) {
            counter++;
            this.description = getUpdatedDescription();
        }
    }

    public void onSpendGold() {
        if ((AbstractDungeon.player.gold - initial) < counter*goal && !ended) {
            if (AbstractDungeon.player.gold < initial) {
                this.counter = 0;
            } else {
                this.counter = ((AbstractDungeon.player.gold - initial) / goal);
            }
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
