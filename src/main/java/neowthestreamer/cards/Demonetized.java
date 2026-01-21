package neowthestreamer.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.powers.DemonetizedPower;
import neowthestreamer.util.CardStats;

public class Demonetized extends BaseCard {
    public static String ID = makeID("Demonetized");

    public Demonetized() {
        super(ID, new CardStats(AbstractCard.CardColor.CURSE, AbstractCard.CardType.CURSE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE,-2));
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void triggerWhenDrawn() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DemonetizedPower(AbstractDungeon.player)));
        /*ArrayList<RewardItem> goldRewards = new ArrayList<>();
        for (RewardItem r : AbstractDungeon.getCurrRoom().rewards) {
            if (r.type == RewardItem.RewardType.GOLD) {
                goldRewards.add(r);
            }
        }
        for (RewardItem r : goldRewards) {
            AbstractDungeon.getCurrRoom().rewards.remove(r);
        }*/
    }
}
