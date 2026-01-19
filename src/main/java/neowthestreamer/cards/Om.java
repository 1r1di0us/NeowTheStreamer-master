package neowthestreamer.cards;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Om extends BaseCard {
    public static String ID = makeID("Om");
    public static int hpLossThisCombat;

    public Om() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,1));
        this.selfRetain = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void tookDamage() {
        if (AbstractDungeon.player.hand.group.contains(this) && hpLossThisCombat == GameActionManager.hpLossThisCombat) {
            addToBot(new MakeTempCardInDrawPileAction(new Om(), 1, true, true));
        } else if (hpLossThisCombat != GameActionManager.hpLossThisCombat) { // crude way of checking if the damage was hp loss or not.
            hpLossThisCombat = GameActionManager.hpLossThisCombat;
        }
    }
}
