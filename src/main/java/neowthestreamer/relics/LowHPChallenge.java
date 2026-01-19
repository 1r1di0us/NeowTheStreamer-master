package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.setRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class LowHPChallenge extends BaseRelic implements ActTwoChallengeInterface, setRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("LowHPChallenge");

    public final int goal = 2;
    public final int maximum = 10;
    public boolean achievedThisCombat;
    public int amount;

    public LowHPChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public LowHPChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.reward = reward;
        this.achievedThisCombat = false;
        this.description = getUpdatedDescription();
        this.tips.get(0).body = this.description;
    }

    public void onEquip() {
        this.counter = 0;
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
        this.amount = this.counter / this.goal;
        if (this.amount > 5) amount = 5;
        if (this.reward == null) {
            return this.DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1] + 10 + DESCRIPTIONS[2];
        } else if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.maximum + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)];
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.maximum + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[3] + amount;
        }
    }

    public void atBattleStart() {
        if (AbstractDungeon.player.currentHealth <= maximum && !usedUp && this.amount < 5) {
            flash();
            achievedThisCombat = true;
            counter++;
            this.description = getUpdatedDescription();
            this.tips.get(0).body = this.description;
        } else if (this.amount < 5 && !usedUp){
            achievedThisCombat = false;
            this.amount = this.counter / this.goal;
            this.description = getUpdatedDescription();
            this.tips.get(0).body = this.description;
        }
    }

    public void wasHPLost(int damageAmount) {
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.currentHealth - damageAmount <= maximum && !achievedThisCombat && !usedUp && this.amount < 5) {
            flash();
            achievedThisCombat = true;
            counter++;
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
