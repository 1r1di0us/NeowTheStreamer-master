package neowthestreamer.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import neowthestreamer.NeowTheStreamer;

import static neowthestreamer.NeowTheStreamer.makeID;

public class DiceDagger extends AbstractMonster {
    public static String ID = makeID("DiceDagger");

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Dagger");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final int HP_MIN = 20;

    private static final int HP_MAX = 25;

    private static final int SACRIFICE_DMG = 8;

    private static final byte EXPLODE = 1;

    public DiceDagger(int hp, int dmg, float x, float y) {
        super(NAME, ID, hp, 0.0F, -50.0F, 140.0F, 130.0F, null, x, y);
        initializeAnimation();
        this.damage.add(new DamageInfo((AbstractCreature)this, dmg));
    }

    public void initializeAnimation() {
        loadAnimation("images/monsters/theForest/mage_dagger/skeleton.atlas", "images/monsters/theForest/mage_dagger/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ChangeStateAction(this, "SUICIDE"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new LoseHPAction((AbstractCreature)this, (AbstractCreature)this, this.currentHealth));
                break;
            default:
                NeowTheStreamer.logger.info("Dice Dagger move problem");
                break;
        }
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RollMoveAction(this));
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hurt", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            this.stateData.setMix("Hurt", "Idle", 0.1F);
            this.stateData.setMix("Idle", "Hurt", 0.1F);
        }
    }

    protected void getMove(int num) {
        setMove((byte)1, AbstractMonster.Intent.ATTACK_DEBUFF, SACRIFICE_DMG);
    }

    public void changeState(String key) {
        switch (key) {
            case "SUICIDE":
                this.state.setAnimation(0, "Attack2", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
            default:
                NeowTheStreamer.logger.info("too many moves on evil dice dagger");
                break;
        }
    }
}
