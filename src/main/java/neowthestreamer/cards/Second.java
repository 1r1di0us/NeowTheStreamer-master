package neowthestreamer.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Second extends BaseCard {
    public static String ID = makeID("Second");

    public Second() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,-2));
        this.exhaust = true;
        this.cardsToPreview = new Third();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            addToTop(new MakeTempCardInDrawPileAction(new Third(), 1, false, true, false));
        }
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }
}
