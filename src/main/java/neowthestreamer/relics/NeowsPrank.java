package neowthestreamer.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.ArrayList;
import java.util.Collections;

import static neowthestreamer.NeowTheStreamer.makeID;

public class NeowsPrank extends BaseRelic {
    public static final String ID = makeID("NeowsPrank");
    public ArrayList<String> bossList;

    public NeowsPrank() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
        this.counter = 3;
        this.bossList = new ArrayList<String>();
        this.bossList.add("The Guardian");
        this.bossList.add("Hexaghost");
        this.bossList.add("Slime Boss");
        Collections.shuffle(this.bossList);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        if (this.counter > 0) {
            this.counter--;
            if (this.counter == 0) {
                this.counter = -2;
                usedUp();
                this.description = this.DESCRIPTIONS[1];
                this.tips.clear();
                this.tips.add(new PowerTip(this.name, this.description));
                initializeTips();
            }
            flash();
            AbstractDungeon.lastCombatMetricKey = bossList.get(counter-1);
            AbstractDungeon.getCurrRoom().monsters.monsters.clear();
            AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(bossList.get(counter-1));
            AbstractDungeon.getCurrRoom().monsters.init();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }
}