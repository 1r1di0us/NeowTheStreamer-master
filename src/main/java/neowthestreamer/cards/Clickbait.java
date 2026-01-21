package neowthestreamer.cards;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.util.CardStats;

public class Clickbait extends BaseCard implements CustomSavable<Integer> {
    public static String ID = makeID("Clickbait");
    public int damageOnRemove;

    public Clickbait() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,1));
        this.magicNumber = this.baseMagicNumber = 5;
        this.damageOnRemove = this.secondMagic = this.baseSecondMagic = 100;
    }

    public Clickbait(int damageOnRemove) {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,1));
        this.magicNumber = this.baseMagicNumber = 5;
        this.damageOnRemove = this.secondMagic = this.baseSecondMagic = damageOnRemove;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.secondMagic > 0) {
            this.damageOnRemove = this.secondMagic = this.baseSecondMagic = this.damageOnRemove - this.magicNumber;
            for (AbstractCard c : p.masterDeck.group) {
                if (c instanceof Clickbait) {
                    ((Clickbait) StSLib.getMasterDeckEquivalent(this)).playMasterDeckEquivalent();
                }
            }
        }
    }

    public void playMasterDeckEquivalent() {
        if (this.secondMagic > 0) {
            this.damageOnRemove = this.secondMagic = this.baseSecondMagic = this.damageOnRemove - this.magicNumber;
        }
    }

    public void onRemoveFromMasterDeck() {
        if (this.damageOnRemove > 0) {
            CardCrawlGame.sound.play("BLOOD_SWISH");
            AbstractDungeon.player.damage(new DamageInfo(null, this.damageOnRemove, DamageInfo.DamageType.HP_LOSS));
        }
    }

    @Override
    public Integer onSave() {
        return this.damageOnRemove;
    }

    @Override
    public void onLoad(Integer dmgAmt) {
        if (dmgAmt != null) {
            this.damageOnRemove = this.baseSecondMagic = this.secondMagic = dmgAmt;
        } else {
            this.damageOnRemove = this.baseSecondMagic = this.secondMagic = 100;
        }
    }

    public AbstractCard makeCopy() {
        return new Clickbait(this.damageOnRemove);
    }
}
