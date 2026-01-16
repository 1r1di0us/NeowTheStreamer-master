package neowthestreamer.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.blue.EchoForm;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.powers.WrathOfTheWatcherPower;

import static neowthestreamer.NeowTheStreamer.makeID;

public class WrathOfTheWatcher extends BaseRelic {
    public static String ID = makeID("WrathOfTheWatcher");

    public static final int turn = 5;

    public WrathOfTheWatcher() {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
        this.counter = 6;
    }

    @Override
    public String getUpdatedDescription() {
        if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + 6 + this.DESCRIPTIONS[1] + 5 + this.DESCRIPTIONS[3];
        } else if (this.counter == 1) {
            return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[2] + turn + this.DESCRIPTIONS[3];
        } else {
            return this.DESCRIPTIONS[0] + this.counter + this.DESCRIPTIONS[1] + turn + this.DESCRIPTIONS[3];
        }
    }

    public void atBattleStart() {
        if (this.counter != -1) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WrathOfTheWatcherPower(AbstractDungeon.player, turn-1)));
            // 4 on turn 1, 3 on turn 2, 2 on turn 3, 1 on turn 4, and die on turn 5.
            this.counter--;
            if (this.counter == 0) {
                this.counter = -1;
            }
        }
    }

    public void onVictory() {
        if (this.counter == -1 && !this.usedUp) {
            flash();
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new EchoForm(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            usedUp();
        }
    }
}
