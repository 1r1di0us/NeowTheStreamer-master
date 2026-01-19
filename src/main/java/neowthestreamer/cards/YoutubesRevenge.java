package neowthestreamer.cards;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import neowthestreamer.util.CardStats;

public class YoutubesRevenge extends BaseCard {
    public static String ID = makeID("YoutubesRevenge");

    public YoutubesRevenge() {
        super(ID, new CardStats(CardColor.CURSE, CardType.CURSE, CardRarity.SPECIAL, CardTarget.NONE,-2));
        SoulboundField.soulbound.set(this, true);
        this.magicNumber = this.baseMagicNumber = 3;
        this.isEthereal = true;
        this.isInnate = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void triggerWhenDrawn() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, magicNumber)));
        }
    }
}
