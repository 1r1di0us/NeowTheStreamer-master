package neowthestreamer.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class HitlessChallenge extends BaseRelic implements ActTwoChallengeInterface {
    public static String ID = makeID("HitlessChallenge");

    public int goal;
    public int initial;
    public int amount;
    public boolean failed;
    public boolean ended;

    public HitlessChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.MAX_HP);
    }

    public HitlessChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.counter = 0;
        this.amount = 0;
        this.goal = 3;
        this.initial = 3;
        this.reward = reward;
        this.failed = false;
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1] + 3 + DESCRIPTIONS[2];
        } else if (!failed) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[3] + this.amount;
        } else {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + this.initial + DESCRIPTIONS[2] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[4] + this.amount;
        }
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0 && !failed && !ended) {
            flash();
            this.failed = true;
            this.description = getUpdatedDescription();
        }
        return damageAmount;
    }

    public void onEnterRoom(AbstractRoom room) {
        if (!failed && !ended) {
            flash();
            this.amount++;
            if (this.amount - initial - (counter * goal) == goal) {
                counter++;
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
