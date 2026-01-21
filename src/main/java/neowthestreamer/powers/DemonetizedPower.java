package neowthestreamer.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.util.ArrayList;

import static neowthestreamer.NeowTheStreamer.makeID;

public class DemonetizedPower extends BasePower {
    public static String ID = makeID("DemonetizedPower");

    public DemonetizedPower(AbstractCreature owner) {
        super(ID, AbstractPower.PowerType.BUFF, false, owner, -1);
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    public void onVictory() {
        ArrayList<RewardItem> goldRewards = new ArrayList<>();
        for (RewardItem r : AbstractDungeon.getCurrRoom().rewards) {
            if (r.type == RewardItem.RewardType.GOLD) {
                goldRewards.add(r);
            }
        }
        for (RewardItem r : goldRewards) {
            AbstractDungeon.getCurrRoom().rewards.remove(r);
        }
    }
}
