package neowthestreamer.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.NeowTheStreamerReward;

import static neowthestreamer.NeowTheStreamer.makeID;

public class ChatsRevenge extends BaseRelic implements CustomSavable<Integer> {
    public static final String ID = makeID("ChatsRevenge");
    private int amount;

    public ChatsRevenge() {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.amount = 1;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            //if (m.type == AbstractMonster.EnemyType.BOSS && MonsterHelper.getEncounter(AbstractDungeon.bossKey).monsters.get(0).id.equals(AbstractDungeon.getMonsters().monsters.get(0).id) && this.amount == AbstractDungeon.actNum) {
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                amount++;
                AbstractCard newCurse = NeowTheStreamerReward.getCurseCards(1).get(0);
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(newCurse.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                addToTop(new MakeTempCardInDrawPileAction(newCurse.makeCopy(), 1, true, true));
            }
        }
    }

    @Override
    public Integer onSave() {
        return this.amount;
    }

    @Override
    public void onLoad(Integer amount) {
        this.amount = amount;
    }
}
