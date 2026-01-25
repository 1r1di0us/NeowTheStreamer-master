package neowthestreamer.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;

import static neowthestreamer.NeowTheStreamer.makeID;

public class NeowingBell extends BaseRelic {
    public static final String ID = makeID("NeowingBell");

    private int relicsReceived = 2;

    public NeowingBell() {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.relicsReceived = 0;
        CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.6F, -0.9F));
    }

    public void update() {
        super.update();
        if (this.relicsReceived == 1 && !AbstractDungeon.isScreenUp) {
            AbstractDungeon.combatRewardScreen.open();
            AbstractDungeon.combatRewardScreen.rewards.clear();
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                    AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON)));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                    AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON)));
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                    AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.RARE)));
            AbstractDungeon.combatRewardScreen.positionRewards();
            AbstractDungeon.overlayMenu.proceedButton.setLabel(this.DESCRIPTIONS[2]);
            this.relicsReceived = 2;
            (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
        }
        if (this.hb.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.10F, -0.15F));
            flash();
        }
        if (this.relicsReceived == 0 && !AbstractDungeon.isScreenUp) {
            this.relicsReceived = 1;
        }
    }
}
