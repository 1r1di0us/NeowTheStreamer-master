package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.setRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class HitlessChallenge extends BaseRelic implements ActTwoChallengeInterface, setRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("HitlessChallenge");

    public final int goal = 3;
    public final int initial = 2;
    public int amount;
    public boolean failed;

    public HitlessChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public HitlessChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.amount = -1;
        this.reward = reward;
        this.failed = false;
        this.description = getUpdatedDescription();
        this.tips.get(0).body = this.description;
    }

    public void onEquip() {
        this.counter = AbstractDungeon.floorNum;
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
        this.amount = (this.counter - this.initial) / this.goal;
        if (this.amount > 5) amount = 5;
        if (this.reward == null) {
            return this.DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1] + 3 + DESCRIPTIONS[2];
        } else if (counter == -1) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)];
        } else if (!failed) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[3] + this.amount;
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4] + this.counter;
        }
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0 && !failed && !usedUp) {
            flash();
            this.failed = true;
            this.description = getUpdatedDescription();
            this.tips.get(0).body = this.description;
        }
        return damageAmount;
    }

    public void onEnterRoom(AbstractRoom room) {
        /*if (AbstractDungeon.actNum == 2 && !this.usedUp) {
            this.amount = (this.counter - this.initial) / this.goal;
            if (this.amount > 5) amount = 5;
            if (this.amount > 0) {
                this.activated = true;
            }
            NeowTheStreamerReward.activateChallengeRewards(this.reward, amount);
            this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
            this.amount = -1;
            this.counter = -1;
            this.usedUp();
        } else */
        if (!failed && !usedUp && this.amount < 5 && this.counter < AbstractDungeon.floorNum) {
            flash();
            this.counter = AbstractDungeon.floorNum;
            this.description = getUpdatedDescription();
            this.tips.get(0).body = this.description;
        }
    }

    public void onEnterActTwo() {
        if (!usedUp) {
            this.amount = (this.counter - this.initial) / this.goal;
            if (this.amount > 5) amount = 5;
            if (this.amount > 0) {
                this.activated = true;
            }
            NeowTheStreamerReward.activateChallengeRewards(this.reward, amount);
            this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
            this.amount = -1;
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
