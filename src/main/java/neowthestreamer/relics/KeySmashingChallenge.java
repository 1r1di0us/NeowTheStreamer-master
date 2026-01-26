package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.helpers.PowerTip;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.SetRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class KeySmashingChallenge extends BaseRelic implements ActTwoChallengeInterface, SetRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("KeySmashingChallenge");

    public KeySmashingChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public KeySmashingChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void onEquip() {
        this.counter = 0;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void setReward(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        this.amount = this.counter;
        if (this.amount > 5) {
            amount = 5;
            counter = 5;
        }
        if (this.reward == null || getRewardIndex(this.reward) == 0) {
            return this.DESCRIPTIONS[0];
        } else if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + MSG[getRewardIndex(this.reward)];
        } else {
            return this.DESCRIPTIONS[0] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[1] + amount;
        }
    }

    public void onKeyDiscard() {
        if (!usedUp && this.amount < 5) {
            flash();
            this.counter++;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
    }

    public void onEnterActTwo() {
        if (!usedUp) {
            this.amount = this.counter;
            if (this.amount > 5) {
                amount = 5;
                counter = 5;
            }
            this.activated = true;
            if (this.amount > 0) {
                NeowTheStreamerReward.activateChallengeRewards(this.reward, this.amount);
            } else {
                usedUp();
            }
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
        if (counter == -1) {
            usedUp();
        }
        this.reward = loadRewardFromIndex(rewardIndex);
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }
}
