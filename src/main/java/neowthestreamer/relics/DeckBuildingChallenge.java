package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.setRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class DeckBuildingChallenge extends BaseRelic implements ActTwoChallengeInterface, setRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("DeckBuildingChallenge");

    public int goal;
    public int initial;

    public DeckBuildingChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public DeckBuildingChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.goal = 4;
        this.initial = 11;
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
            return this.DESCRIPTIONS[0] + 4 + DESCRIPTIONS[1] + 11 + DESCRIPTIONS[2];
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)];
        }
    }

    public void onMasterDeckChange() {
        if ((AbstractDungeon.player.masterDeck.size() - initial) / goal != counter && !usedUp) {
            counter = (AbstractDungeon.player.masterDeck.size() - initial) / goal;
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
