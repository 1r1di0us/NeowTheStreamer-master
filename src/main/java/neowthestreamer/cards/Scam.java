package neowthestreamer.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.actions.ScamAction;
import neowthestreamer.util.CardStats;

public class Scam extends BaseCard {
    public static String ID = makeID("Scam");

    public Scam() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,-2));
        this.magicNumber = this.baseMagicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void triggerWhenDrawn() {
        addToBot(new ScamAction(3));
    }
}
