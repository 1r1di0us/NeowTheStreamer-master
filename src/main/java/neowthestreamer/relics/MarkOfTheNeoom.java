package neowthestreamer.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

import static neowthestreamer.NeowTheStreamer.makeID;

public class MarkOfTheNeoom extends BaseRelic {
    public static final String ID = makeID("MarkOfTheNeoom");

    public MarkOfTheNeoom() {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onEquip() {

    }

    public void upgradeAll() {
        ArrayList<String> upgradedCards;
        int effectCount = 0;
        upgradedCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                effectCount++;
                if (effectCount <= 20) {
                    float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c
                            .makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                }
                upgradedCards.add(c.cardID);
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
            }
        }
    }

    public int onPlayerHeal(int healAmount) {
        if (!this.usedUp) {
            flash();
            return 0;
        } else {
            return healAmount;
        }
    }

    public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.floorNum == 1) {
            upgradeAll();
        }
        if (AbstractDungeon.actNum == 2 && !this.usedUp) {
            this.usedUp();
        }
    }
}
