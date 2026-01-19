package neowthestreamer.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Subscribe extends BaseCard {
    public static String ID = makeID("Subscribe");

    public Subscribe() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,2));
        this.cardsToPreview = new NewVideo();
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
        this.selfRetain = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onRetained() {
        addToBot(new ReduceCostAction(this));
        addToBot(new MakeTempCardInDrawPileAction(new NewVideo(), 1, true, true));
    }
}
