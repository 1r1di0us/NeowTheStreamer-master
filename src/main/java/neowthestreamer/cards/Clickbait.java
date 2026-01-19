package neowthestreamer.cards;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Clickbait extends BaseCard implements CustomSavable<Integer> {
    public static String ID = makeID("Clickbait");
    public static int damageOnRemove;

    public Clickbait() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,1));
        this.magicNumber = this.baseMagicNumber = 5;
        damageOnRemove = this.secondMagic = this.baseSecondMagic = 100;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.secondMagic > 0) {
            damageOnRemove = this.secondMagic = this.baseSecondMagic = damageOnRemove - this.magicNumber;
        }
        // this doesn't update the one in your deck
    }

    public void onRemoveFromMasterDeck() {
        if (damageOnRemove > 0) {
            CardCrawlGame.sound.play("BLOOD_SWISH");
            AbstractDungeon.player.damage(new DamageInfo(null, damageOnRemove, DamageInfo.DamageType.HP_LOSS));
        }
    }

    @Override
    public Integer onSave() {
        return damageOnRemove;
    }

    @Override
    public void onLoad(Integer integer) {
        if (integer != null) {
            damageOnRemove = this.baseSecondMagic = this.secondMagic = integer;
        }
    }
}
