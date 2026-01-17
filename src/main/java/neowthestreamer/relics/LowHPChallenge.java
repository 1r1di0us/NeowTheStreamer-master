package neowthestreamer.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class LowHPChallenge extends BaseRelic implements ActTwoChallengeInterface {
    public static String ID = makeID("LowHPChallenge");

    public int goal;
    public int maximum;
    public int amount;
    public boolean achievedThisCombat;
    public boolean ended;

    public LowHPChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.MAX_HP);
    }

    public LowHPChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.maximum = 10;
        this.amount = 0;
        this.goal = 2;
        this.reward = reward;
        this.achievedThisCombat = false;
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1] + 10 + DESCRIPTIONS[2];
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.maximum + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[3] + this.amount;
        }
    }

    public void atBattleStart() {
        if (AbstractDungeon.player.currentHealth < maximum && !ended && this.counter < 5) {
            flash();
            achievedThisCombat = true;
            amount++;
            if (this.amount - (this.counter * goal) == goal) {
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
            if (this.amount - (this.counter * goal) == goal) {
                this.counter++;
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
