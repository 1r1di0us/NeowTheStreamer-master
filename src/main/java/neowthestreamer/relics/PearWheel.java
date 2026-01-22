package neowthestreamer.relics;

import static neowthestreamer.NeowTheStreamer.makeID;

public class PearWheel extends BaseRelic {
    public static final String ID = makeID("PearWheel");

    public PearWheel() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
