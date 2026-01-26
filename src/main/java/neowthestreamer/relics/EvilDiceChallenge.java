package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.actions.EvilDiceDiscardAction;
import neowthestreamer.actions.EvilDiceTalkAction;
import neowthestreamer.actions.ShowIntentAction;
import neowthestreamer.interfaces.ActTwoChallengeInterface;
import neowthestreamer.interfaces.SetRewardInterface;
import neowthestreamer.monsters.DiceDagger;

import java.util.ArrayList;

import static neowthestreamer.NeowTheStreamer.makeID;

public class EvilDiceChallenge extends BaseRelic implements ActTwoChallengeInterface, SetRewardInterface, CustomSavable<Integer> {
    public static String ID = makeID("EvilDiceChallenge");

    public final int goal = 2;
    public final int DAGGER_HEALTH = 100;
    public final int DAGGER_DMG = 10;
    public final int BLOCK_AMT = 15;
    public final int DISCARD_AMT = 2;
    public final int ENERGY_AMT = 1;
    public final int POWER_AMT = 1;
    public final int HEAL_AMT = 5;

    public static final float[] POSX = new float[] { 325.0F };
    public static final float[] POSY = new float[] { 215.0F };
    private AbstractMonster dagger;

    private int roll = 0;
    private boolean firstTurn = false;

    public EvilDiceChallenge() {
        this(NeowTheStreamerReward.NeowTheStreamerRewardType.NONE);
    }

    public EvilDiceChallenge(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
        this.reward = reward;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void onEquip() {
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
        this.amount = (counter / goal);
        if (this.amount > 5) amount = 5;
        if (this.reward == null || getRewardIndex(this.reward) == 0) {
            return this.DESCRIPTIONS[0] + 2 + DESCRIPTIONS[1];
        } else if (this.counter == -1) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)];
        } else if (roll == 0) {
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[2] + amount;
        } else {
            String rollDesc = DESCRIPTIONS[10+roll];
            return this.DESCRIPTIONS[0] + this.goal + DESCRIPTIONS[1] + MSG[getRewardIndex(this.reward)] + DESCRIPTIONS[2] + amount + DESCRIPTIONS[3] + roll + rollDesc;
        }
    }

    public void atBattleStart() {
        if (!usedUp && this.amount < 5) {
            firstTurn = true;
            flash();
            roll = AbstractDungeon.miscRng.random(1, 6);
            this.counter++;
            this.description = getUpdatedDescription();
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
    }

    public void onEnergyRecharge() {
        if (!usedUp) {
            if (firstTurn) { // its too slow on the first turn
                firstTurn = false;
            } else {
                roll = AbstractDungeon.miscRng.random(1, 6);
                this.description = getUpdatedDescription();
                this.tips.clear();
                this.tips.add(new PowerTip(this.name, this.description));
                initializeTips();
            }
            if (roll == 1) {
                if (this.dagger == null || this.dagger.isDeadOrEscaped()) {
                    DiceDagger daggerToSpawn = new DiceDagger(DAGGER_HEALTH, DAGGER_DMG, POSX[0], POSY[0]);
                    this.dagger = daggerToSpawn;
                    addToBot(new SpawnMonsterAction(daggerToSpawn, true));
                    addToBot(new ShowIntentAction(dagger));
                }
            }
        }
    }

    public void atTurnStartPostDraw() {
        if (!usedUp) {
            AbstractPlayer p = AbstractDungeon.player;
            switch (roll) {
                case 1:
                    AbstractDungeon.getMonsters().showIntent();
                    addToBot(new EvilDiceTalkAction(DESCRIPTIONS[4], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
                    break;
                case 2:
                    addToBot(new EvilDiceTalkAction(DESCRIPTIONS[5], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
                    for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        addToBot(new GainBlockAction(m, BLOCK_AMT));
                    }
                    break;
                case 3:
                    //addToBot(new EvilDiceDiscardAction(DISCARD_AMT));
                    addToBot(new EvilDiceTalkAction(DESCRIPTIONS[6], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
                    addToBot(new DiscardAction(p, p, DISCARD_AMT, false));
                    break;
                case 4:
                    addToBot(new EvilDiceTalkAction(DESCRIPTIONS[8], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
                    addToBot(new LoseEnergyAction(ENERGY_AMT));
                    break;
                case 5:
                    addToBot(new EvilDiceTalkAction(DESCRIPTIONS[9], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
                    //addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, POWER_AMT, false)));
                    addToBot(new ApplyPowerAction(p, p, new WeakPower(p, POWER_AMT, false)));
                    addToBot(new ApplyPowerAction(p, p, new FrailPower(p, POWER_AMT, false)));
                    break;
                case 6:
                    addToBot(new EvilDiceTalkAction(DESCRIPTIONS[10], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
                    addToBot(new HealAction(p, p, HEAL_AMT));
                    break;
            }
        }
    }

    public void discardTalk(ArrayList<AbstractCard> group) {
        if (group == null) {
            addToBot(new EvilDiceTalkAction(DESCRIPTIONS[6] + "nothing?" + DESCRIPTIONS[7], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
        } else if (group.size() == 1) {
            addToBot(new EvilDiceTalkAction(DESCRIPTIONS[6] + group.get(0).name + DESCRIPTIONS[7], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
        } else if (group.size() == 2) {
            addToBot(new EvilDiceTalkAction(DESCRIPTIONS[6] + group.get(0).name + " or " + group.get(1).name + DESCRIPTIONS[7], 2.0F, 2.0F, this.hb.cX, this.hb.cY));
        }
    }

    public void onEnterActTwo() {
        if (!usedUp) {
            this.amount = (counter / goal);
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
