package neowthestreamer.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Like extends BaseCard {
    public static String ID = makeID("Like");

    public Like() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,-2));
        this.cardsToPreview = new Subscribe();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void triggerWhenDrawn() {
        addToBot(new MakeTempCardInHandAction(new Subscribe(), 1));
    }
}
