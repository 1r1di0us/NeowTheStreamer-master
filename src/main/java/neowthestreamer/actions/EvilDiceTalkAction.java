package neowthestreamer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class EvilDiceTalkAction extends AbstractGameAction {
    private String msg;

    private boolean used = false;

    private float bubbleDuration;
    private float relicX;
    private float relicY;
    private float dialogX;
    private float dialogY;

    public EvilDiceTalkAction(String text, float duration, float bubbleDuration, float x, float y) {
        setValues(source, AbstractDungeon.player);
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_MED;
        } else {
            this.duration = duration;
        }
        this.msg = text;
        this.actionType = AbstractGameAction.ActionType.TEXT;
        this.bubbleDuration = bubbleDuration;
        this.relicX = x;
        this.relicY = y;
        this.dialogX = relicX - 30.0F * Settings.scale;
        this.dialogY = relicY + 100F * Settings.scale;
    }

    public EvilDiceTalkAction(String text) {
        this( text, 2.0F, 2.0F, AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY);
    }

    public void update() {
        if (!this.used) {
            AbstractDungeon.effectList.add(new SpeechBubble(this.relicX + this.dialogX, this.relicY + this.dialogY, this.bubbleDuration, this.msg, this.source.isPlayer));
            this.used = true;
        }
        tickDuration();
    }
}
