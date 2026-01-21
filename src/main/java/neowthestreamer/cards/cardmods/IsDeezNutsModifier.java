package neowthestreamer.cards.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

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
