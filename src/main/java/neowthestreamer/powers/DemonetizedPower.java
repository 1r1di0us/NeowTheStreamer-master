package neowthestreamer.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import neowthestreamer.interfaces.OnVictoryToChangeRewardsInterface;

import java.util.ArrayList;

import static neowthestreamer.NeowTheStreamer.makeID;

public class DemonetizedPower extends BasePower implements OnVictoryToChangeRewardsInterface {
    public static String ID = makeID("DemonetizedPower");

    public DemonetizedPower(AbstractCreature owner) {
        super(ID, AbstractPower.PowerType.BUFF, false, owner, -1);
    }

    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    public void onVictoryToChangeRewards() {
        flash();
        ArrayList<Integer> goldRewards = new ArrayList<>();
        for (int i = 0; i < AbstractDungeon.combatRewardScreen.rewards.size(); i++) {
            if (AbstractDungeon.combatRewardScreen.rewards.get(i).type == RewardItem.RewardType.GOLD || AbstractDungeon.combatRewardScreen.rewards.get(i).type == RewardItem.RewardType.STOLEN_GOLD) {
                goldRewards.add(i);
            }
        }
        if (!goldRewards.isEmpty()) {
            for (int j = goldRewards.size() - 1; j >= 0; j--) {
                AbstractDungeon.combatRewardScreen.rewards.remove((int) goldRewards.get(j));
            }
        }
    }
}
