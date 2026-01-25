package neowthestreamer.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static neowthestreamer.NeowTheStreamer.makeID;

public class BodyOfCleric extends BaseRelic {
    public static final String ID = makeID("BodyOfCleric");
    public int healAmt;
    public final int actGain = 3;

    public BodyOfCleric() {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        healAmt = actGain * AbstractDungeon.actNum;
    }

    @Override
    public String getUpdatedDescription() {
        if (healAmt == 0) {
            return this.DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1] + actGain + ".";
        }
        return this.DESCRIPTIONS[0] + healAmt + DESCRIPTIONS[1] + actGain + ".";
    }

    public void onVictory() {
        flash();
        AbstractDungeon.player.increaseMaxHp(healAmt, true);
    }

    public void onEnterRoom(AbstractRoom room) {
        if (healAmt != actGain * AbstractDungeon.actNum) {
            healAmt = actGain * AbstractDungeon.actNum;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
    }
}
