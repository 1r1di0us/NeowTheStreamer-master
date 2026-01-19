package neowthestreamer.cards;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static neowthestreamer.NeowTheStreamer.makeID;

@AbstractCardModifier.SaveIgnore
public class IsDeezNutsModifier extends AbstractCardModifier {
    public static final String ID = makeID("IsDeezNutsModifier");

    public AbstractCard deezNuts;

    public IsDeezNutsModifier(AbstractCard source) {
        this.deezNuts = source;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IsDeezNutsModifier(deezNuts.makeStatEquivalentCopy());
    }
}
