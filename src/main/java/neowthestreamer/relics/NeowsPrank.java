package neowthestreamer.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import neowthestreamer.interfaces.OnVictoryToChangeRewardsInterface;

import java.util.ArrayList;
import java.util.Collections;

import static neowthestreamer.NeowTheStreamer.makeID;

public class NeowsPrank extends BaseRelic implements OnVictoryToChangeRewardsInterface {
    public static final String ID = makeID("NeowsPrank");
    public ArrayList<String> bossList;
    public final int healAmt = 25;

    public NeowsPrank() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
        this.counter = 3;
        this.bossList = new ArrayList<String>();
        this.bossList.add("The Guardian");
        this.bossList.add("Hexaghost");
        this.bossList.add("Slime Boss");
        Collections.shuffle(this.bossList);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        if (this.counter > 0 && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
            flash();
            addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, healAmt));
            this.counter--;
            AbstractDungeon.lastCombatMetricKey = bossList.get(counter);
            AbstractDungeon.getCurrRoom().monsters.monsters.clear();
            AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(bossList.get(counter));
            AbstractDungeon.getCurrRoom().monsters.init();
            AbstractDungeon.getCurrRoom().monsters.usePreBattleAction();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public void onVictoryToChangeRewards() {
        if (this.counter >= 0 && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
            //ArrayList<Integer> remove = new ArrayList<>();
            RewardItem potionReward = new RewardItem();
            for (int j = 0; j < AbstractDungeon.combatRewardScreen.rewards.size(); j++) {
                if ((AbstractDungeon.combatRewardScreen.rewards.get(j)).type == RewardItem.RewardType.POTION) {
                //    remove.add(j);
                //} else {
                    potionReward = new RewardItem(AbstractDungeon.combatRewardScreen.rewards.get(j).potion);
                }
            }
            //if (!remove.isEmpty()) {
            //    for (int j = remove.size() - 1; j >= 0; j--) {
            //        AbstractDungeon.combatRewardScreen.rewards.remove((int)remove.get(j));
            //    }
            //}
            AbstractDungeon.combatRewardScreen.clear();

            int tmp = 100 + AbstractDungeon.miscRng.random(-5, 5);
            if (AbstractDungeon.ascensionLevel >= 13) {
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(MathUtils.round(tmp * 0.75F)));
                //AbstractDungeon.getCurrRoom().addGoldToRewards(MathUtils.round(tmp * 0.75F));
            } else {
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(tmp));
                //AbstractDungeon.getCurrRoom().addGoldToRewards(tmp);
            }

            if (potionReward.type == RewardItem.RewardType.POTION) {
                AbstractDungeon.combatRewardScreen.rewards.add(potionReward);
            }

            RewardItem cardReward = StSLib.generateCardReward(getRareRewardCards(), true);
            AbstractDungeon.combatRewardScreen.rewards.add(cardReward);

            AbstractDungeon.combatRewardScreen.positionRewards();
            if (this.counter == 0) {
                this.counter = -2;
                this.usedUp = true;
                this.description = MSG[9];
                this.tips.clear();
                this.tips.add(new PowerTip(this.name, this.description));
                initializeTips();
            }
        }
    }

    private ArrayList<AbstractCard> getRareRewardCards() {
        AbstractPlayer player = AbstractDungeon.player;
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        int numCards = 3;
        for (AbstractRelic r : player.relics)
            numCards = r.changeNumberOfCardsInReward(numCards);
        if (ModHelper.isModEnabled("Binary"))
            numCards--;
        for (int i = 0; i < numCards; i++) {
            AbstractCard card = null;
            AbstractDungeon.cardBlizzRandomizer -= AbstractDungeon.cardBlizzGrowth;
            if (AbstractDungeon.cardBlizzRandomizer <= AbstractDungeon.cardBlizzMaxOffset)
                AbstractDungeon.cardBlizzRandomizer = AbstractDungeon.cardBlizzMaxOffset;
            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;
                if (player.hasRelic("PrismaticShard")) {
                    card = CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE);
                } else {
                    card = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE);
                }
                for (AbstractCard c : retVal) {
                    if (c.cardID.equals(card.cardID))
                        containsDupe = true;
                }
            }
            if (card != null)
                retVal.add(card);
        }
        ArrayList<AbstractCard> retVal2 = new ArrayList<>();
        for (AbstractCard c : retVal)
            retVal2.add(c.makeCopy());
        for (AbstractCard c : retVal2) {
            for (AbstractRelic r : player.relics)
                r.onPreviewObtainCard(c);
        }
        return retVal2;
    }
}