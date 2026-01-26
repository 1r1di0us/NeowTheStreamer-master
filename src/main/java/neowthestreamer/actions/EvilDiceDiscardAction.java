package neowthestreamer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import neowthestreamer.NeowTheStreamer;
import neowthestreamer.relics.EvilDiceChallenge;

import java.util.ArrayList;

public class EvilDiceDiscardAction extends AbstractGameAction {
    private AbstractPlayer p;

    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    private int amount;

    public EvilDiceDiscardAction(int amount) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = AbstractGameAction.ActionType.DISCARD;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION) {
            if (p.hand.size() <= amount) {
                amount = p.hand.size();
                if (p.hasRelic(EvilDiceChallenge.ID)) {
                    ((EvilDiceChallenge) p.getRelic(EvilDiceChallenge.ID)).discardTalk(p.hand.group);
                } else {
                    NeowTheStreamer.logger.info("no evil relic");
                }
            } else if (amount == 1) {
                ArrayList<AbstractCard> cardGroup = new ArrayList<>();
                cardGroup.add(p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
                addToBot(new DiscardSpecificCardAction(cardGroup.get(0), p.hand));
                if (p.hasRelic(EvilDiceChallenge.ID)) {
                    ((EvilDiceChallenge) p.getRelic(EvilDiceChallenge.ID)).discardTalk(cardGroup);
                } else {
                    NeowTheStreamer.logger.info("no evil relic 2");
                }
            } else {
                ArrayList<AbstractCard> cardGroup = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    cardGroup.add(p.hand.getRandomCard(AbstractDungeon.cardRandomRng));
                    addToBot(new DiscardSpecificCardAction(cardGroup.get(i), p.hand));
                }
                if (p.hasRelic(EvilDiceChallenge.ID)) {
                    ((EvilDiceChallenge) p.getRelic(EvilDiceChallenge.ID)).discardTalk(cardGroup);
                } else {
                    NeowTheStreamer.logger.info("no evil relic 3");
                }
            }
            /*if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                int tmp = this.p.hand.size();
                for (int i = 0; i < tmp; i++) {
                    AbstractCard c = this.p.hand.getTopCard();
                    this.p.hand.moveToDiscardPile(c);
                    if (p.hasRelic(EvilDiceChallenge.ID)) {
                        ((EvilDiceChallenge)p.getRelic(EvilDiceChallenge.ID)).discardTalk(c);
                    }
                }
                AbstractDungeon.player.hand.applyPowers();
                tickDuration();
                return;
            }
            for (int i = 0; i < this.amount; i++) {
                AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                this.p.hand.moveToDiscardPile(c);
                if (p.hasRelic(EvilDiceChallenge.ID)) {
                    ((EvilDiceChallenge)p.getRelic(EvilDiceChallenge.ID)).discardTalk(c);
                }
            }*/
        }
        tickDuration();
    }
}