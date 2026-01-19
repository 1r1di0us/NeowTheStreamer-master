package neowthestreamer.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class SoloSpam extends BaseCard {
    public static String ID = makeID("SoloSpam");

    public SoloSpam() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,3));
        this.magicNumber = this.baseMagicNumber = 1;
    }

    public SoloSpam(int cost) {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,cost));
        this.magicNumber = this.baseMagicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            addToTop(new ReduceCostAction(this));
            addToTop(new MakeTempCardInDrawPileAction(new SoloSpam(this.cost - 1), 1, true, true, true));
        } else {
            this.exhaustOnUseOnce = true;
        }
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }
}
