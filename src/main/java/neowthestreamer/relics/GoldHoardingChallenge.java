package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.setRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class GoldHoardingChallenge extends BaseRelic implements ActTwoChallengeInterface, setRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("GoldHoardingChallenge");

    public int goal;
    public int initial;

    public GoldHoardingChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public GoldHoardingChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.goal = 100;
        this.initial = 99;
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.get(0).body = this.description;
    }

    public void setReward(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.get(0).body = this.description;
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
            this.tips.get(0).body = this.description;
        }
    }

    public void onSpendGold() {
        if ((AbstractDungeon.player.gold - initial) < counter*goal && !usedUp) {
            if (AbstractDungeon.player.gold < initial) {
                this.counter = 0;
            } else {
                this.counter = ((AbstractDungeon.player.gold - initial) / goal);
            }
            this.description = getUpdatedDescription();
            this.tips.get(0).body = this.description;
        }
    }

    /*public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.actNum == 2 && !this.usedUp) {
            this.amount = this.counter / this.goal;
            if (this.amount > 5) amount = 5;
            if (this.amount > 0) {
                this.activated = true;
            }
            NeowTheStreamerReward.activateChallengeRewards(this.reward, this.amount);
            this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
            this.counter = -1;
            this.amount = -1;
            this.usedUp();
        }
    }*/

    public void onEnterActTwo() {
        if (!usedUp) {
            if (this.counter > 0) {
                this.activated = true;
            }
            NeowTheStreamerReward.activateChallengeRewards(this.reward, counter);
            this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
            this.counter = -1;
            this.usedUp();
        }
    }

    @Override
    public Integer onSave() {
        return getRewardIndex(this.reward);
    }

    @Override
    public void onLoad(Integer rewardIndex) {
        if (rewardIndex == null) {
            return;
        }
        this.reward = loadRewardFromIndex(rewardIndex);
        this.description = getUpdatedDescription();
        this.tips.get(0).body = this.description;
    }
}
