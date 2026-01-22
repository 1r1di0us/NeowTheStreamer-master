package neowthestreamer.relics;

import static neowthestreamer.NeowTheStreamer.makeID;

public class MaskOfMidas extends BaseRelic {
    public static final String ID = makeID("MaskOfMidas");

    public MaskOfMidas() {
        super(ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
