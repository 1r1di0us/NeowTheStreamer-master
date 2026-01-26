package neowthestreamer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShowIntentAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;

    private AbstractMonster dagger;

    public ShowIntentAction(AbstractMonster mon) {
        this.dagger = mon;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION) {
            dagger.createIntent();
        }
        tickDuration();
    }
}
