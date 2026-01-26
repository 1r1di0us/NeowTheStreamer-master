package neowthestreamer.cards;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import neowthestreamer.util.CardStats;

public class DayZeroAsking extends BaseCard implements CustomSavable<Integer> {
    public static String ID = makeID("DayZeroAsking");
    public int dayCount;

    public DayZeroAsking() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF,0));
        this.magicNumber = this.baseMagicNumber = 10;
        this.dayCount = this.secondMagic = this.baseSecondMagic = 0;
        this.exhaust = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0] + this.dayCount + cardStrings.EXTENDED_DESCRIPTION[1];
    }

    public DayZeroAsking(int dayCount) {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF,0));
        this.magicNumber = this.baseMagicNumber = 10;
        this.dayCount = this.secondMagic = this.baseSecondMagic = dayCount;
        this.exhaust = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0] + this.dayCount + cardStrings.EXTENDED_DESCRIPTION[1];
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new LightningEffect(p.hb.cX, p.hb.cY)));
        addToBot(new LoseHPAction(p, p, 99999));
        if (StSLib.getMasterDeckEquivalent(this) != null) {
            AbstractCard card = StSLib.getMasterDeckEquivalent(this);
            CardCrawlGame.metricData.addPurgedItem(card.getMetricID());
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            p.masterDeck.removeCard(card);
        }
    }

    public void triggerWhenDrawn() {
        this.dayCount = this.secondMagic = this.baseSecondMagic = this.dayCount + 1;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0] + this.dayCount + cardStrings.EXTENDED_DESCRIPTION[1];
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof DayZeroAsking) {
                ((DayZeroAsking) StSLib.getMasterDeckEquivalent(this)).incrementMasterDeckEquivalent();
            }
        }
        if (this.secondMagic % this.magicNumber == 0 && this.secondMagic != 0) {
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(this.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    public void incrementMasterDeckEquivalent() {
        this.dayCount = this.secondMagic = this.baseSecondMagic = this.dayCount + 1;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0] + this.dayCount + cardStrings.EXTENDED_DESCRIPTION[1];
    }

    @Override
    public Integer onSave() {
        return this.dayCount;
    }

    @Override
    public void onLoad(Integer dayCount) {
        if (dayCount != null) {
            this.dayCount = this.baseSecondMagic = this.secondMagic = dayCount;
        } else {
            this.dayCount = this.baseSecondMagic = this.secondMagic = 1;
        }
        this.name = cardStrings.EXTENDED_DESCRIPTION[0] + this.dayCount + cardStrings.EXTENDED_DESCRIPTION[1];
    }

    public AbstractCard makeCopy() {
        return new DayZeroAsking(this.dayCount);
    }
}
