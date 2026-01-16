package neowthestreamer.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.blue.EchoForm;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static neowthestreamer.NeowTheStreamer.makeID;

public class WrathOfTheDefect extends BaseRelic {
    public static String ID = makeID("WrathOfTheDefect");

    public static final int amount = 1;

    public WrathOfTheDefect() {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
        this.counter = 6;
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 6 + this.DESCRIPTIONS[1] + 1 + this.DESCRIPTIONS[3];
        } else if (this.counter == 1) {
            return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[2] + amount + this.DESCRIPTIONS[3];
        } else {
            return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[1] + amount + this.DESCRIPTIONS[3];
        }
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster-= amount;
    }

    public void atBattleStart() {
        if (this.counter != -1) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.counter--;
            if (this.counter == 0) {
                this.counter = -1;
            }
        }
    }

    public void onVictory() {
        if (this.counter == -1 && !this.usedUp) {
            flash();
            AbstractDungeon.player.energy.energyMaster+= amount;
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new EchoForm(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            usedUp();
        }
    }
}
