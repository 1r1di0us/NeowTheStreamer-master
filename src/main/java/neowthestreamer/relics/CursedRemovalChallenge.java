package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.SetRewardInterface;

import java.util.ArrayList;

import static neowthestreamer.NeowTheStreamer.makeID;

public class CursedRemovalChallenge extends BaseRelic implements ActTwoChallengeInterface, SetRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("CursedRemovalChallenge");

    public final int goal = 1;
    public int deckSize = 0;

    public CursedRemovalChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public CursedRemovalChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.HEAVY);
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void onEquip() {
        this.deckSize = AbstractDungeon.player.masterDeck.size();
        this.counter = 0;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void setReward(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        this.amount = this.counter / this.goal;
        if (this.amount > 5) amount = 5;
        if (this.reward == null || getRewardIndex(this.reward) == 0) {
            return this.DESCRIPTIONS[0];
        } else if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + MSG[getRewardIndex(this.reward)];
        } else {
            return this.DESCRIPTIONS[0] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[3] + amount;
        }
    }

    public void onMasterDeckChange() {
        int amt = 0;
        if (AbstractDungeon.player.masterDeck.size() < this.deckSize && !this.usedUp) {
            amt = this.deckSize - AbstractDungeon.player.masterDeck.size();
            ArrayList<AbstractCard> curses = NeowTheStreamerReward.getCurseCards(amt);
            for (int i = 0; i < amt; i++) {
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(i), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }
        }
        this.deckSize = AbstractDungeon.player.masterDeck.size();
    }

    public void onObtainCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE && !this.usedUp) {
            this.counter++;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
        this.deckSize = AbstractDungeon.player.masterDeck.size() + 1;
    }

    public void onEnterActTwo() {
        if (!usedUp) {
            this.amount = this.counter / this.goal;
            if (this.amount > 5) amount = 5;
            this.activated = true;
            if (this.amount > 0) {
                NeowTheStreamerReward.activateChallengeRewards(this.reward, this.amount);
            } else {
                usedUp();
            }
        }
    }

    @Override
    public Integer onSave() {
        return getRewardIndex(this.reward);
    }

    @Override
    public void onLoad(Integer rewardIndex) {
        if (rewardIndex == null) {
            return;
        }
        if (counter == -1) {
            usedUp();
        }
        this.reward = loadRewardFromIndex(rewardIndex);
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }
}
