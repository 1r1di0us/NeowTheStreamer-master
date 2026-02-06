package neowthestreamer.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.actions.FastLoseHPAction;

import static neowthestreamer.NeowTheStreamer.makeID;

public class WrathOfTheIroncladPower extends BasePower {
    public static String ID = makeID("WrathOfTheIroncladPower");

    public WrathOfTheIroncladPower(AbstractCreature owner, int amount) {
        super(ID, PowerType.BUFF, false, owner, amount);
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        flash();
        addToBot(new FastLoseHPAction(AbstractDungeon.player, this.owner, this.amount));
        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new FastLoseHPAction(m, this.owner, this.amount));
        }
    }
}
