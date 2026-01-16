package neowthestreamer.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;

import static neowthestreamer.NeowTheStreamer.makeID;

public class LowHPChallenge extends BaseRelic {
    public static String ID = makeID("LowHPChallenge");

    public int maximum;
    public int amount;
    public boolean achievedThisCombat;
    public boolean ended;

    public LowHPChallenge(int maximum, NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.maximum = maximum;
        amount = 0;
        this.reward = reward;
        this.achievedThisCombat = false;
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 10 + DESCRIPTIONS[1];
        } else {
            return this.DESCRIPTIONS[0] + this.maximum + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[2] + this.amount;
        }
    }

    public void atBattleStart() {
        if (AbstractDungeon.player.currentHealth < maximum && !ended && this.counter < 5) {
            flash();
            achievedThisCombat = true;
            amount++;
            if (this.amount - (this.counter * 2) == 2) {
                this.counter++;
            }
            this.description = getUpdatedDescription();
        } else if (this.counter < 5 && !ended){
            achievedThisCombat = false;
        }
    }

    public void wasHPLost(int damageAmount) {
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.currentHealth - damageAmount < maximum && !achievedThisCombat && !ended && this.counter < 5) {
            flash();
            achievedThisCombat = true;
            amount++;
            if (this.amount - (this.counter * 2) == 2) {
                this.counter++;
            }
            this.description = getUpdatedDescription();
        }
    }

    public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.actNum == 2 && !ended) {
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
