package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.SetRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class HitlessChallenge extends BaseRelic implements ActTwoChallengeInterface, SetRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("HitlessChallenge");

    public final int goal = 2;
    public boolean failedCombat = false;

    public HitlessChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public HitlessChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
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
        this.amount = this.counter / this.goal;
        if (this.amount > 5) amount = 5;
        if (this.reward == null || getRewardIndex(this.reward) == 0) {
            return this.DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1];
        } else if (counter == -1) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)];
        } else if (failedCombat) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[2] + this.amount;
        }
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0 && !usedUp && !failedCombat) {
            flash();
            stopPulse();
            failedCombat = true;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
        return damageAmount;
    }

    public void onEnterRoom(AbstractRoom room) {
        if (room.phase == AbstractRoom.RoomPhase.COMBAT && !usedUp) {
            this.failedCombat = false;
            this.pulse = true;
            beginPulse();
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        } else {
            stopPulse();
        }
    }

    public void onVictory() {
        if (!usedUp && !failedCombat && this.amount < 5) {
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
            this.amount = this.counter / this.goal;
            if (this.amount > 5) amount = 5;
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
