package neowthestreamer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import neowthestreamer.NeowTheStreamer;
import neowthestreamer.cards.DeezNuts;

import java.util.ArrayList;

public class DisguiseDeezNutsAction extends AbstractGameAction {

    public ArrayList<AbstractCard> DeezNutsCards = new ArrayList<>();

    public DisguiseDeezNutsAction() {}

    public void update() {
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof DeezNuts) {
                this.DeezNutsCards.add(c);
            }
        }
        if (DeezNutsCards.isEmpty()) {
            NeowTheStreamer.logger.info("no nuts");
        }
        for (AbstractCard dn : DeezNutsCards) {
            if (dn instanceof DeezNuts) {
                ((DeezNuts) dn).atBattleStartPreDraw();
            }
        }
        this.isDone = true;
    }
}
