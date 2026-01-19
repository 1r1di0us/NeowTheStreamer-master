package neowthestreamer.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Third extends BaseCard {
    public static String ID = makeID("Third");

    public Third() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,-2));
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}
}
