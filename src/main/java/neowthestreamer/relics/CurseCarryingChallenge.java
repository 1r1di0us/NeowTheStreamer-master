package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.SetRewardInterface;

import static neowthestreamer.NeowTheStreamer.makeID;

public class CurseCarryingChallenge extends BaseRelic implements ActTwoChallengeInterface, SetRewardInterface, CustomSavable<Integer>, OnRemoveCardFromMasterDeckRelic {
    public static String ID = makeID("CurseCarryingChallenge");

    public final int goal = 2;
    public AbstractCard curse;
    public boolean failed;

    public CurseCarryingChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public CurseCarryingChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.reward = reward;
        this.failed = false;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void onEquip() {
        this.counter = 0;
        this.curse = NeowTheStreamerReward.getCurseCards(1).get(0);
        AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(this.curse.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        failed = false;
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

    public boolean findCurse() {
        boolean foundCurse = false;
        if (this.curse != null) {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.cardID.equals(this.curse.cardID)) {
                    foundCurse = true;
                    break;
                }
            }
        } else {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                for (AbstractCard ntsc : NeowTheStreamerReward.getCurseCards(10)) {
                    if (c.cardID.equals(ntsc.cardID)) {
                        foundCurse = true;
                        this.curse = c;
                        break;
                    }
                }
            }
        }
        if (!foundCurse) {
            failed = true;
        }
        return foundCurse;
    }

    @Override
    public String getUpdatedDescription() {
        this.amount = this.counter / this.goal;
        if (this.amount > 5) amount = 5;
        if (this.reward == null || getRewardIndex(this.reward) == 0 || this.curse == null) {
            return DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1];
        } else if (this.counter == -1) {
            return DESCRIPTIONS[2] + this.curse.name + DESCRIPTIONS[3] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)];
        } else if (failed) {
            return DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[4] + amount + DESCRIPTIONS[5];
        } else {
            return DESCRIPTIONS[2] + this.curse.name + DESCRIPTIONS[3] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[4] + amount;
        }
    }

    public void atBattleStart() {
        if (!usedUp && this.amount < 5 && !failed) {
            flash();
            if (this.curse == null) findCurse();
            this.counter++;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
    }

    @Override
    public void onRemoveCardFromMasterDeck(AbstractCard card) {
        boolean removedCard = false;
        if (this.curse == null) findCurse();
        if (card.cardID.equals(curse.cardID)) removedCard = true;
        if (removedCard) {
            flash();
            failed = true;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
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
        findCurse();
        this.reward = loadRewardFromIndex(rewardIndex);
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }
}
