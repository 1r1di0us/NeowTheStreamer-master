package neowthestreamer.cards;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.NeowTheStreamer;
import neowthestreamer.util.CardStats;

import java.util.List;
import java.util.stream.Collectors;

public class DeezNuts extends BaseCard implements StartupCard {
    public static String ID = makeID("DeezNuts");

    public DeezNuts() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,0));
        this.magicNumber = this.baseMagicNumber = 1;
        this.cardsToPreview = new Gottem();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, this.magicNumber));
        addToBot(new MakeTempCardInHandAction(new Gottem(), BaseMod.MAX_HAND_SIZE - p.hand.group.size()));
    }

    private static boolean canDisguiseAs(AbstractCard target) {
        return target.cost != -2 && !target.cardID.equals(DeezNuts.ID) && !CardModifierManager.hasModifier(target, IsDeezNutsModifier.ID);
    }

    @Override
    public boolean atBattleStartPreDraw() {
        disguiseDeezNuts();
        return false;
    }

    public void disguiseDeezNuts() {
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                List<AbstractCard> possibilities = AbstractDungeon.player.drawPile.group.stream().filter(DeezNuts::canDisguiseAs).collect(Collectors.toList());
                if (!possibilities.isEmpty() && AbstractDungeon.player.drawPile.contains(DeezNuts.this)) {
                    int index = AbstractDungeon.player.drawPile.group.indexOf(DeezNuts.this);
                    AbstractDungeon.player.drawPile.removeCard(DeezNuts.this);
                    AbstractCard disguise = NeowTheStreamer.getRandomItem(possibilities, AbstractDungeon.cardRandomRng).makeStatEquivalentCopy();
                    CardModifierManager.addModifier(disguise, new IsDeezNutsModifier(DeezNuts.this));
                    if (index > 0) {
                        AbstractDungeon.player.drawPile.group.add(index, disguise);
                    } else {
                        AbstractDungeon.player.drawPile.addToRandomSpot(disguise);
                    }
                }
            }
        });
    }
}
