package neowthestreamer.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static neowthestreamer.NeowTheStreamer.makeID;

public class WrathOfTheWatcherPower extends BasePower {
    public static String ID = makeID("WrathOfTheWatcherPower");

    public WrathOfTheWatcherPower(AbstractCreature owner, int amount) {
        super(ID, PowerType.BUFF, true, owner, amount);
    }

    public void updateDescription() {
        if (this.amount == 1) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    public void atStartOfTurn() {
        if (this.amount == 1) {
            flash();
            addToBot(new VFXAction(new LightningEffect(this.owner.hb.cX, this.owner.hb.cY)));
            addToBot(new LoseHPAction(this.owner, this.owner, 99999));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            // 4 on turn 1, 3 on turn 2, 2 on turn 3, 1 on turn 4, and die on turn 5.
            addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
}
