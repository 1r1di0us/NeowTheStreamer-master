package neowthestreamer.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import neowthestreamer.NeowTheStreamerReward;
import neowthestreamer.util.GeneralUtils;
import neowthestreamer.util.TextureLoader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;

import static neowthestreamer.NeowTheStreamer.*;

public abstract class BaseRelic extends CustomRelic {
    public AbstractCard.CardColor pool = null;
    public RelicType relicType = RelicType.SHARED;
    protected String imageName;

    private static final String ID = makeID("BaseRelic");

    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);

    public static final String[] MSG = relicStrings.DESCRIPTIONS;

    public boolean activated = false;
    public NeowTheStreamerReward.NeowTheStreamerRewardType reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
    public int amount;

    //for character specific relics
    public BaseRelic(String id, String imageName, AbstractCard.CardColor pool, RelicTier tier, LandingSound sfx) {
        this(id, imageName, tier, sfx);

        setPool(pool);
    }

    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        this(id, GeneralUtils.removePrefix(id), tier, sfx);
    }

    //To use a basegame relic image, just pass in the imagename used by a basegame relic instead of the ID.
    //eg. "calendar.png"
    public BaseRelic(String id, String imageName, RelicTier tier, LandingSound sfx) {
        super(testStrings(id), notPng(imageName) ? "" : imageName, tier, sfx);

        this.imageName = imageName;
        if (notPng(imageName)) {
            loadTexture();
        }
    }

    protected void loadTexture() {
        this.img = TextureLoader.getTextureNull(relicPath(imageName + ".png"), true);
        if (img != null) {
            outlineImg = TextureLoader.getTextureNull(relicPath(imageName + "Outline.png"), true);
            if (outlineImg == null)
                outlineImg = img;
        }
        else {
            ImageMaster.loadRelicImg("Derp Rock", "derpRock.png");
            this.img = ImageMaster.getRelicImg("Derp Rock");
            this.outlineImg = ImageMaster.getRelicOutlineImg("Derp Rock");
        }
    }

    @Override
    public void loadLargeImg() {
        if (notPng(imageName)) {
            if (largeImg == null) {
                this.largeImg = ImageMaster.loadImage(relicPath("large/" + imageName + ".png"));
            }
        }
        else {
            super.loadLargeImg();
        }
    }

    private void setPool(AbstractCard.CardColor pool) {
        switch (pool) { //Basegame pools are handled differently
            case RED:
                relicType = RelicType.RED;
                break;
            case GREEN:
                relicType = RelicType.GREEN;
                break;
            case BLUE:
                relicType = RelicType.BLUE;
                break;
            case PURPLE:
                relicType = RelicType.PURPLE;
                break;
            default:
                this.pool = pool;
                break;
        }
    }

    /**
     * Checks whether relic has localization set up correctly and gives a more accurate error message if it does not
     * @param ID the relic's ID
     * @return the relic's ID, to allow use in super constructor invocation
     */
    private static String testStrings(String ID) {
        RelicStrings text = CardCrawlGame.languagePack.getRelicStrings(ID);
        if (text == null) {
            throw new RuntimeException("The \"" + ID + "\" relic does not have associated text. Make sure " +
                    "there's no issue with the RelicStrings.json file, and that the ID in the json file matches the " +
                    "relic's ID. It should look like \"${modID}:" + GeneralUtils.removePrefix(ID) + "\".");
        }
        return ID;
    }

    private static boolean notPng(String name) {
        return !name.endsWith(".png");
    }

    @Override
    public void usedUp() {
        this.grayscale = true;
        this.usedUp = true;
        this.description = MSG[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public int getRewardIndex(NeowTheStreamerReward.NeowTheStreamerRewardType reward) {
        switch (reward) {
            case RANDOM_COMMON_RELIC:
                return 1;
            case MAX_HP:
                return 2;
            case GOLD:
                return 3;
            case GOLD_AND_POTION:
                return 4;
            case TRANSFORM_CARD:
                return 5;
            case UPGRADE_RANDOM:
                return 6;
            case REMOVE_CARD:
                return 7;
            case DUPLICATE_CARD:
                return 8;
            default:
                logger.info("Incorrect Challenge Reward");
                return 0;
        }
    }

    public NeowTheStreamerReward.NeowTheStreamerRewardType loadRewardFromIndex(int rewardIndex) {
        switch (rewardIndex) {
            case 0:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
            case 1:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.RANDOM_COMMON_RELIC;
            case 2:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.MAX_HP;
            case 3:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.GOLD;
            case 4:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.GOLD_AND_POTION;
            case 5:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.TRANSFORM_CARD;
            case 6:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.UPGRADE_RANDOM;
            case 7:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.REMOVE_CARD;
            case 8:
                return NeowTheStreamerReward.NeowTheStreamerRewardType.DUPLICATE_CARD;
            default:
                logger.info("Incorrect Challenge Reward");
                return NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
        }
    }

    public void update() {
        super.update();
        if (this.activated) {
            if (this.amount == 0) {
                this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
                this.counter = -1;
                this.amount = -1;
                this.usedUp();
                this.activated = false;
            } else {
                if (reward == NeowTheStreamerReward.NeowTheStreamerRewardType.TRANSFORM_CARD || reward == NeowTheStreamerReward.NeowTheStreamerRewardType.REMOVE_CARD || reward == NeowTheStreamerReward.NeowTheStreamerRewardType.DUPLICATE_CARD) {
                    if (AbstractDungeon.gridSelectScreen.selectedCards.size() == this.amount) {
                        AbstractDungeon.overlayMenu.proceedButton.hide();
                        switch (this.reward) {
                            case TRANSFORM_CARD:
                                for (int i = 0; i < amount; i++) {
                                    AbstractDungeon.gridSelectScreen.selectedCards.get(i).untip();
                                    AbstractDungeon.gridSelectScreen.selectedCards.get(i).unhover();
                                    AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
                                    AbstractDungeon.transformCard(AbstractDungeon.gridSelectScreen.selectedCards.get(i), true, AbstractDungeon.miscRng);
                                    if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && AbstractDungeon.transformedCard != null) {
                                        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(
                                                AbstractDungeon.getTransformedCard(), Settings.WIDTH / 2.0F + (((1 - amount) + (2 * i)) * 160.0F) * Settings.scale, Settings.HEIGHT / 2.0F, false));
                                    }
                                }
                                (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
                                break;
                            case REMOVE_CARD:
                                for (int i = 0; i < amount; i++) {
                                    CardCrawlGame.sound.play("CARD_EXHAUST");
                                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(i), Settings.WIDTH / 2.0F + (((1 - amount) + (2 * i)) * 160.0F) * Settings.scale, (Settings.HEIGHT / 2)));
                                    AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
                                }
                                (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
                                break;
                            case DUPLICATE_CARD:
                                for (int i = 0; i < amount; i++) {
                                    AbstractCard c = (AbstractDungeon.gridSelectScreen.selectedCards.get(i)).makeStatEquivalentCopy();
                                    AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(c, Settings.WIDTH / 2.0F + (((1 - amount) + (2 * i)) * 160.0F) * Settings.scale, Settings.HEIGHT / 2.0F));
                                }
                                (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
                                break;
                        }
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
                        this.counter = -1;
                        this.amount = -1;
                        this.usedUp();
                        this.activated = false;
                    } else {
                        AbstractDungeon.overlayMenu.proceedButton.hide();
                    }
                } else {
                    this.reward = NeowTheStreamerReward.NeowTheStreamerRewardType.NONE;
                    this.counter = -1;
                    this.amount = -1;
                    this.usedUp();
                    this.activated = false;
                }
            }
        }
    }
}