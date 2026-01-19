package neowthestreamer.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ScamAction extends AbstractGameAction {
    private AbstractPlayer p = AbstractDungeon.player;
    private int cost;

    public ScamAction(int cost) {
        super();
        this.cost = cost;
    }

    public void update() {
        boolean betterPossible = false;
        boolean possible = false;
        for (AbstractCard c : this.p.hand.group) {
            if (c.costForTurn < cost) {
                betterPossible = true;
                continue;
            }
            if (c.cost < cost)
                possible = true;
        }
        if (betterPossible || possible)
            findAndModifyCard(betterPossible);
        tickDuration();
    }

    private void findAndModifyCard(boolean better) {
        AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
        if (better) {
            if (c.costForTurn < cost) {
                c.cost = cost;
                c.costForTurn = cost;
                c.isCostModified = true;
                c.superFlash(Color.GOLD.cpy());
            } else {
                findAndModifyCard(better);
            }
        } else if (c.cost < cost) {
            c.cost = cost;
            c.costForTurn = cost;
            c.isCostModified = true;
            c.superFlash(Color.GOLD.cpy());
        } else {
            findAndModifyCard(better);
        }
    }
}