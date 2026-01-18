package neowthestreamer.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.green.WraithForm;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static neowthestreamer.NeowTheStreamer.makeID;

public class WrathOfTheSilent extends BaseRelic {
    public static String ID = makeID("WrathOfTheSilent");

    public static final int amount = 3;

    public WrathOfTheSilent() {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
        this.counter = 6;
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 6 + this.DESCRIPTIONS[1] + 3 + this.DESCRIPTIONS[3];
        } else if (this.counter == 1) {
            return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[2] + amount + this.DESCRIPTIONS[3];
        } else {
            return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[1] + amount + this.DESCRIPTIONS[3];
        }
    }

    public void atBattleStart() {
        if (this.counter != -1) {
            flash();
            for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                addToBot(new RelicAboveCreatureAction(mo, this));
                addToBot(new ApplyPowerAction(mo, AbstractDungeon.player, new IntangiblePlayerPower(mo, amount), amount, true));
            }
            this.counter--;
            if (this.counter == 0) {
                this.counter = -1;
            }
        }
    }

    public void onVictory() {
        if (this.counter == -1 && !this.usedUp) {
            flash();
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new WraithForm(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            usedUp();
        }
    }
}
