package neowthestreamer.cards;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class YoutubesBlessing extends BaseCard {
    public static String ID = makeID("YoutubesBlessing");

    public YoutubesBlessing() {
        super(ID, new CardStats(CardColor.CURSE, CardType.CURSE, CardRarity.SPECIAL, CardTarget.SELF,0));
        SoulboundField.soulbound.set(this, true);
        this.magicNumber = this.baseMagicNumber = 3;
        this.cardsToPreview = new YoutubesRevenge();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber));
    }
}
