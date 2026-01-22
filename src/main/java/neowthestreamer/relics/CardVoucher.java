package neowthestreamer.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import static neowthestreamer.NeowTheStreamer.makeID;

public class CardVoucher extends BaseRelic implements ClickableRelic {
    public static final String ID = makeID("CardVoucher");
    public final int numVouchers = 3;

    public CardVoucher() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
        this.counter = numVouchers;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    @Override
    public void onRightClick() {
        if (!this.usedUp && !(AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)) {
            this.counter--;
            if (this.counter == 0) {
                this.counter = -2;
                this.description = MSG[9];
                this.tips.clear();
                this.tips.add(new PowerTip(this.name, this.description));
                initializeTips();
            }
        }
    }
}
