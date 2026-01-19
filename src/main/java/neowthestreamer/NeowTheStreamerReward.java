package neowthestreamer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.EchoForm;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.colorless.JAX;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.cards.green.WraithForm;
import com.megacrit.cardcrawl.cards.purple.DevaForm;
import com.megacrit.cardcrawl.cards.red.DemonForm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import neowthestreamer.cards.*;
import neowthestreamer.interfaces.setRewardInterface;
import neowthestreamer.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static neowthestreamer.NeowTheStreamer.makeID;

public class NeowTheStreamerReward extends NeowReward {

    private static final Logger logger = LogManager.getLogger(NeowTheStreamerReward.class.getName());

    private static final String ID = makeID("NeowTheStreamerReward");

    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);

    public static final String[] NAMES = characterStrings.NAMES;

    public static final String[] CHALLENGE_TEXT = characterStrings.TEXT;

    public static final String[] REWARD_TEXT = characterStrings.UNIQUE_REWARDS;

    public NeowTheStreamerRewardType reward;

    public NeowTheStreamerChallengeType challenge;

    private boolean activated;

    private int hp_bonus;

    private int gold_bonus;

    public AbstractRelic relicReward;

    public AbstractCard cardReward;

    public ArrayList<AbstractCard> curses;

    public enum NeowTheStreamerRewardType {
        NONE, PREVIEW_SHOP_RELIC, PREVIEW_COMMON_RELIC, PREVIEW_RARE_CARD, PREVIEW_COLORLESS_RARE, THREE_COMMON_CARDS, THREE_COLORLESS_CARDS, NEOWS_LAMENT, TWO_FIFTY_GOLD, RANDOM_COMMON_RELIC, MAX_HP, GOLD, GOLD_AND_POTION, TRANSFORM_CARD, UPGRADE_RANDOM, REMOVE_CARD, DUPLICATE_CARD, UPGRADE_DECK, UPGRADE_RELIC, MASK_OF_MIDAS, BUSTED_CROWN, FOUR_SPECIAL_CARDS, BODY_OF_CLERIC, YOUTUBES_BLESSING, CARD_VOUCHERS, RELIC_REWARDS, BOSS_RELIC;
    }

    public enum NeowTheStreamerChallengeType {
        NONE, ONE_CURSE, CULTIST_HEADPIECE, DECK_OF_TERRIBLE_THINGS, LOW_HP_CHALLENGE, HITLESS_CHALLENGE, POTION_TRASHING_CHALLENGE, KEY_SMASHING_CHALLENGE, GOLD_HOARDING_CHALLENGE, DECK_BUILDING_CHALLENGE, CURSE_CARRYING_CHALLENGE, MARK_OF_NEOOM, NEOWS_PRANK, PEAR_WHEEL, CHATS_REVENGE, MAX_HP_LOSS, TWO_CURSES, THREE_CURSES, WRATH_OF_IRONCLAD, WRATH_OF_SILENT, WRATH_OF_DEFECT, WRATH_OF_WATCHER
    }

    public static class NeowTheStreamerOptionDef {
        public NeowTheStreamerReward.NeowTheStreamerRewardType rewardType;
        public NeowTheStreamerReward.NeowTheStreamerChallengeType challengeType;
        public String rewardDesc;
        public String challengeDesc;

        public NeowTheStreamerOptionDef(NeowTheStreamerReward.NeowTheStreamerRewardType rewardType, NeowTheStreamerReward.NeowTheStreamerChallengeType challengeType, String rewardDesc, String challengeDesc) {
            this.rewardType = rewardType;
            this.challengeType = challengeType;
            this.rewardDesc = rewardDesc;
            this.challengeDesc = challengeDesc;
        }
    }

    /*public NeowTheStreamerReward(boolean firstMini) {
        super(firstMini);
        NeowTheStreamerOptionDef option;
        this.optionLabel = "";
        this.activated = false;
        this.curses = new ArrayList<>();
        this.hp_bonus = (int)(AbstractDungeon.player.maxHealth * 0.1F);
        if (firstMini) {
            option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.THREE_ENEMY_KILL, NeowTheStreamerChallengeType.NONE, REWARD_TEXT[22], "");
        } else {
            option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.TEN_PERCENT_HP_BONUS, NeowTheStreamerChallengeType.NONE, REWARD_TEXT[21] + this.hp_bonus + " ]", "");
        }
        this.challenge = option.challengeType;
        this.optionLabel += option.challengeDesc; //should be nothing but just to be sure.
        this.reward = option.rewardType;
        this.optionLabel += option.rewardDesc;
    }*/

    public NeowTheStreamerReward(int category) {
        super(category);
        this.optionLabel = "";
        this.challenge = NeowTheStreamerChallengeType.NONE;
        this.reward = NeowTheStreamerRewardType.NONE;
        this.activated = false;
        this.curses = new ArrayList<>();
        this.hp_bonus = (int)(AbstractDungeon.player.maxHealth * 0.1F);
        NeowTheStreamerOptionDef option = getRandomOption(category);
        this.challenge = option.challengeType;
        this.reward = option.rewardType;
        if (option.challengeType != NeowTheStreamerChallengeType.NONE)
            this.optionLabel += option.challengeDesc;
        if (option.rewardType != NeowTheStreamerRewardType.NONE)
            this.optionLabel += option.rewardDesc;
    }

    private NeowTheStreamerOptionDef getRandomOption(int category) {
        NeowTheStreamerOptionDef option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.NONE, NeowTheStreamerChallengeType.NONE, "", "");
        switch (category) {
            case 0:
                int optionIndex = NeowEvent.rng.random(0, 7);
                switch (optionIndex) {
                    case 0:
                        relicReward = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.SHOP);
                        option.rewardType = NeowTheStreamerRewardType.PREVIEW_SHOP_RELIC;
                        option.rewardDesc = REWARD_TEXT[0] + relicReward.name + " ]";
                        break;
                    case 1:
                        relicReward = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                        option.rewardType = NeowTheStreamerRewardType.PREVIEW_COMMON_RELIC;
                        option.rewardDesc = REWARD_TEXT[0] + relicReward.name + " ]";
                        break;
                    case 2:
                        cardReward = getCard(AbstractCard.CardRarity.RARE);
                        option.rewardType = NeowTheStreamerRewardType.PREVIEW_RARE_CARD;
                        option.rewardDesc = REWARD_TEXT[0] + cardReward.name + " ]";
                        break;
                    case 3:
                        cardReward = AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE);
                        option.rewardType = NeowTheStreamerRewardType.PREVIEW_COLORLESS_RARE;
                        option.rewardDesc = REWARD_TEXT[0] + cardReward.name + " ]";
                        break;
                    case 4:
                        cardReward = getCard(AbstractCard.CardRarity.COMMON);
                        option.rewardType = NeowTheStreamerRewardType.THREE_COMMON_CARDS;
                        option.rewardDesc = REWARD_TEXT[1] + cardReward.name + " ]";
                        break;
                    case 5:
                        cardReward = AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON);
                        option.rewardType = NeowTheStreamerRewardType.THREE_COLORLESS_CARDS;
                        option.rewardDesc = REWARD_TEXT[1] + cardReward.name + " ]";
                        break;
                    case 6:
                        relicReward = new CultistMask();
                        option.challengeType = NeowTheStreamerChallengeType.CULTIST_HEADPIECE;
                        option.challengeDesc = CHALLENGE_TEXT[0];
                        option.rewardType = NeowTheStreamerRewardType.NEOWS_LAMENT;
                        option.rewardDesc = REWARD_TEXT[2];
                        break;
                    case 7:
                        curses = getCurseCards(1);
                        gold_bonus = 250;
                        option.challengeType = NeowTheStreamerChallengeType.ONE_CURSE;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name;
                        option.rewardType = NeowTheStreamerRewardType.TWO_FIFTY_GOLD;
                        option.rewardDesc = REWARD_TEXT[3] + gold_bonus + REWARD_TEXT[4];
                        break;
                    default:
                        logger.info("Option in category 0 out of index");
                        break;
                }
                break;
            case 1:
                int challengeIndex = NeowEvent.rng.random(1, 4);
                switch (challengeIndex) {
                    /*case 0:
                        option.challengeType = NeowTheStreamerChallengeType.DECK_OF_TERRIBLE_THINGS;
                        option.challengeDesc = CHALLENGE_TEXT[2];
                        break;*/
                    case 1:
                        relicReward = new LowHPChallenge();
                        option.challengeType = NeowTheStreamerChallengeType.LOW_HP_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[3];
                        break;
                    case 2:
                        relicReward = new HitlessChallenge();
                        option.challengeType = NeowTheStreamerChallengeType.HITLESS_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[4];
                        break;
                    /*case 3:
                        option.challengeType = NeowTheStreamerChallengeType.POTION_TRASHING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[5];
                        break;
                    case 4:
                        option.challengeType = NeowTheStreamerChallengeType.KEY_SMASHING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[6];
                        break;*/
                    case 3:
                        relicReward = new GoldHoardingChallenge();
                        option.challengeType = NeowTheStreamerChallengeType.GOLD_HOARDING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[7];
                        break;
                    case 4:
                        relicReward = new DeckBuildingChallenge();
                        option.challengeType = NeowTheStreamerChallengeType.DECK_BUILDING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[8];
                        break;
                    /*case 7:
                        curses.add(getCurseCards(NeowEvent.rng, 1));
                        option.challengeType = NeowTheStreamerChallengeType.CURSE_CARRYING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name + CHALLENGE_TEXT[9];
                        break;*/
                    default:
                        logger.info("Challenge in category 1 out of index");
                        break;
                }
                int rewardIndex = NeowEvent.rng.random(0, 4);
                switch (rewardIndex) {
                    case 0:
                        option.rewardType = NeowTheStreamerRewardType.RANDOM_COMMON_RELIC;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[5];
                        break;
                    case 1:
                        option.rewardType = NeowTheStreamerRewardType.MAX_HP;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[6] + "7 ]";
                        break;
                    case 2:
                        option.rewardType = NeowTheStreamerRewardType.GOLD;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[3] + 100 + REWARD_TEXT[4];
                        break;
                    case 3:
                        option.rewardType = NeowTheStreamerRewardType.GOLD_AND_POTION;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[3] + 75 + REWARD_TEXT[7];
                        break;
                    /*case 4:
                        option.rewardType = NeowTheStreamerRewardType.TRANSFORM_CARD;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[8];
                        break;*/
                    case 4:
                        option.rewardType = NeowTheStreamerRewardType.UPGRADE_RANDOM;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[9];
                        break;
                    /*case 6:
                        option.rewardType = NeowTheStreamerRewardType.REMOVE_CARD;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[10];
                        break;
                    case 7:
                        option.rewardType = NeowTheStreamerRewardType.DUPLICATE_CARD;
                        if (relicReward instanceof setRewardInterface) {
                            ((setRewardInterface) relicReward).setReward(option.rewardType);
                        }
                        option.rewardDesc = REWARD_TEXT[11];
                        break;*/
                    default:
                        logger.info("Reward in category 1 out of index");
                        break;
                }
                break;
            case 2:
                int gigaOptionIndex;
                if (AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.IRONCLAD && AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.THE_SILENT && AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.DEFECT && AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.WATCHER) {
                    gigaOptionIndex = NeowEvent.rng.random(0, 6);
                    // no upgrading the starter relic on modded characters
                } else {
                    gigaOptionIndex = NeowEvent.rng.random(0,5);
                }
                switch (gigaOptionIndex) {
                    case 0:
                        this.relicReward = new MarkOfTheNeoom();
                        option.challengeType = NeowTheStreamerChallengeType.MARK_OF_NEOOM;
                        option.challengeDesc = CHALLENGE_TEXT[10];
                        option.rewardType = NeowTheStreamerRewardType.UPGRADE_DECK;
                        option.rewardDesc = REWARD_TEXT[12];
                        break;
                    /*case 2:
                        option.challengeType = NeowTheStreamerChallengeType.NONE;
                        option.challengeDesc = "";
                        option.rewardType = NeowTheStreamerRewardType.MASK_OF_MIDAS;
                        option.rewardDesc = REWARD_TEXT[14];
                        break;
                    case 3:
                        option.challengeType = NeowTheStreamerChallengeType.PEAR_WHEEL;
                        option.challengeDesc = CHALLENGE_TEXT[12];
                        option.rewardType = NeowTheStreamerRewardType.BUSTED_CROWN;
                        option.rewardDesc = REWARD_TEXT[15];
                        break;
                    case 4:
                        option.challengeType = NeowTheStreamerChallengeType.CHATS_REVENGE;
                        option.challengeDesc = CHALLENGE_TEXT[13];
                        option.rewardType = NeowTheStreamerRewardType.FOUR_SPECIAL_CARDS;
                        option.rewardDesc = REWARD_TEXT[16];
                        break;
                    case 5:
                        this.hp_bonus = (int)(AbstractDungeon.player.maxHealth * 0.9F);
                        option.challengeType = NeowTheStreamerChallengeType.MAX_HP_LOSS;
                        option.challengeDesc = CHALLENGE_TEXT[14];
                        option.rewardType = NeowTheStreamerRewardType.BODY_OF_CLERIC;
                        option.rewardDesc = REWARD_TEXT[17];
                        break;*/
                    case 1:
                        this.cardReward = new YoutubesBlessing();
                        option.challengeType = NeowTheStreamerChallengeType.NONE; //youtubes revenge
                        option.challengeDesc = "";
                        option.rewardType = NeowTheStreamerRewardType.YOUTUBES_BLESSING;
                        option.rewardDesc = REWARD_TEXT[18];
                        break;
                    /*case 7:
                        curses = getCurseCards(2);
                        option.challengeType = NeowTheStreamerChallengeType.TWO_CURSES;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name + " #rand " + curses.get(1).name;
                        option.rewardType = NeowTheStreamerRewardType.CARD_VOUCHERS;
                        option.rewardDesc = REWARD_TEXT[19];
                        break;*/
                    case 2:
                        curses = getCurseCards(3);
                        option.challengeType = NeowTheStreamerChallengeType.THREE_CURSES;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name + ", " + curses.get(1).name + " #rand " + curses.get(2).name;
                        option.rewardType = NeowTheStreamerRewardType.RELIC_REWARDS;
                        option.rewardDesc = REWARD_TEXT[20];
                        break;
                    case 3:
                        if (AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.IRONCLAD) {
                            this.relicReward = new WrathOfTheIronclad();
                            this.cardReward = new DemonForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_IRONCLAD;
                            option.challengeDesc = CHALLENGE_TEXT[15];
                        } else {
                            this.relicReward = new WrathOfTheSilent();
                            this.cardReward = new WraithForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_SILENT;
                            option.challengeDesc = CHALLENGE_TEXT[16];
                        }
                        break;
                    case 4:
                        if (AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.THE_SILENT &&
                                AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.IRONCLAD) {
                            this.relicReward = new WrathOfTheSilent();
                            this.cardReward = new WraithForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_SILENT;
                            option.challengeDesc = CHALLENGE_TEXT[16];
                        } else {
                            this.relicReward = new WrathOfTheDefect();
                            this.cardReward = new EchoForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_DEFECT;
                            option.challengeDesc = CHALLENGE_TEXT[17];
                        }
                        break;
                    case 5:
                        if (AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.DEFECT &&
                                AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.THE_SILENT &&
                                AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.IRONCLAD) {
                            this.relicReward = new WrathOfTheDefect();
                            this.cardReward = new EchoForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_DEFECT;
                            option.challengeDesc = CHALLENGE_TEXT[17];
                        } else {
                            this.relicReward = new WrathOfTheWatcher();
                            this.cardReward = new DevaForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_WATCHER;
                            option.challengeDesc = CHALLENGE_TEXT[18];
                        }
                        break;
                    case 6:
                        if (AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.WATCHER &&
                                AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.DEFECT &&
                                AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.THE_SILENT &&
                                AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.IRONCLAD) {
                            this.relicReward = new WrathOfTheWatcher();
                            this.cardReward = new DevaForm();
                            option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_WATCHER;
                            option.challengeDesc = CHALLENGE_TEXT[18];
                        } /*else {
                            option.challengeType = NeowTheStreamerChallengeType.NEOWS_PRANK;
                            option.challengeDesc = CHALLENGE_TEXT[11];
                            option.rewardType = NeowTheStreamerRewardType.UPGRADE_RELIC;
                            option.rewardDesc = REWARD_TEXT[13];
                        }*/
                        break;
                    default:
                        logger.info("Option in category 2 out of index");
                        break;
                }
                break;
            case 3:
                option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.BOSS_RELIC, NeowTheStreamerChallengeType.NONE, REWARD_TEXT[23], "");
                break;
        }
        return option;
    }

    @Override
    public void update() {
        if (this.reward == NeowTheStreamerRewardType.RELIC_REWARDS && this.activated) {
            if (!AbstractDungeon.isScreenUp) {
                AbstractDungeon.combatRewardScreen.open();
                AbstractDungeon.combatRewardScreen.rewards.clear();
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                        AbstractDungeon.returnRandomScreenlessRelic(returnRandomRelicTier())));
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                        AbstractDungeon.returnRandomScreenlessRelic(returnRandomRelicTier())));
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                        AbstractDungeon.returnRandomScreenlessRelic(returnRandomRelicTier())));
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                        AbstractDungeon.returnRandomScreenlessRelic(returnRandomRelicTier())));
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(
                        AbstractDungeon.returnRandomScreenlessRelic(returnRandomRelicTier())));
                AbstractDungeon.combatRewardScreen.positionRewards();
                AbstractDungeon.overlayMenu.proceedButton.setLabel(REWARD_TEXT[27]);
                (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.25F;
            }
        }
    }

    private AbstractRelic.RelicTier returnRandomRelicTier() {
        int roll = AbstractDungeon.relicRng.random(0, 99);
        if (roll < 50)
            return AbstractRelic.RelicTier.COMMON;
        if (roll > 82)
            return AbstractRelic.RelicTier.RARE;
        return AbstractRelic.RelicTier.UNCOMMON;
    }

    @Override
    public void activate() {
        this.activated = true;
        switch (this.challenge) {
            case NONE:
                break;
            case ONE_CURSE:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(0).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case TWO_CURSES:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(0).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(1).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case THREE_CURSES:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(0).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(1).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(curses.get(2).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case CULTIST_HEADPIECE:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case DECK_OF_TERRIBLE_THINGS:
                break;
            case LOW_HP_CHALLENGE:
            case HITLESS_CHALLENGE:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case POTION_TRASHING_CHALLENGE:
            case KEY_SMASHING_CHALLENGE:
                break;
            case GOLD_HOARDING_CHALLENGE:
            case DECK_BUILDING_CHALLENGE:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case CURSE_CARRYING_CHALLENGE:
            case NEOWS_PRANK:
                break;
            case MARK_OF_NEOOM:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case PEAR_WHEEL:
            case CHATS_REVENGE:
                break;
            case WRATH_OF_IRONCLAD:
            case WRATH_OF_SILENT:
            case WRATH_OF_DEFECT:
            case WRATH_OF_WATCHER:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case MAX_HP_LOSS:
                AbstractDungeon.player.decreaseMaxHealth(this.hp_bonus);
                break;
            default:
                logger.info("[ERROR] Missing Neow the Streamer Challenge: " + this.challenge.name());
                break;
        }
        switch (this.reward) {
            case NONE:
                break;
            case PREVIEW_SHOP_RELIC:
            case PREVIEW_COMMON_RELIC:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case PREVIEW_RARE_CARD:
            case PREVIEW_COLORLESS_RARE:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case THREE_COMMON_CARDS:
            case THREE_COLORLESS_CARDS:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case NEOWS_LAMENT:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), new NeowsLament());
                break;
            case TWO_FIFTY_GOLD:
                CardCrawlGame.sound.play("GOLD_JINGLE");
                AbstractDungeon.player.gainGold(gold_bonus);
                break;
            case RANDOM_COMMON_RELIC: // all of these occur in the relic reward
            case MAX_HP:
            case GOLD:
            case GOLD_AND_POTION:
            case TRANSFORM_CARD:
            case UPGRADE_RANDOM:
            case REMOVE_CARD:
            case DUPLICATE_CARD:
            case UPGRADE_DECK:
                break; //still gotta include the case so that it doesn't make an error
            case UPGRADE_RELIC:
                AbstractRelic r = new Circlet();
                switch (AbstractDungeon.player.chosenClass) {
                    case IRONCLAD:
                        r = new BlackBlood();
                        break;
                    case THE_SILENT:
                        r = new RingOfTheSerpent();
                        break;
                    case DEFECT:
                        r = new FrozenCore();
                        break;
                    case WATCHER:
                        r = new HolyWater();
                        break;
                } //maybe introduce modded character compatibility if I'm feeling smart
                //just look for relics with canSpawn()
                r.instantObtain(AbstractDungeon.player, 0, true);
                break;
            case MASK_OF_MIDAS:
                break;
            case BUSTED_CROWN:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), new BustedCrown());
                AbstractDungeon.bossRelicPool.remove(BustedCrown.ID);
                break;
            case FOUR_SPECIAL_CARDS:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Bite(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Apparition(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new JAX(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new RitualDagger(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case BODY_OF_CLERIC:
                break;
            case YOUTUBES_BLESSING:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case CARD_VOUCHERS:
            case RELIC_REWARDS:
                break;
            case BOSS_RELIC:
                AbstractDungeon.player.loseRelic((AbstractDungeon.player.relics.get(0)).relicId);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2),
                        AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS));
                break;
            default:
                logger.info("[ERROR] Missing Neow The Streamer Reward: " + this.reward.name());
                break;
        }
        CardCrawlGame.metricData.addNeowData(this.reward.name(), this.challenge.name());
    }

    public AbstractCard getCard(AbstractCard.CardRarity rarity) {
        switch (rarity) {
            case RARE:
                return AbstractDungeon.rareCardPool.getRandomCard(NeowEvent.rng);
            case UNCOMMON:
                return AbstractDungeon.uncommonCardPool.getRandomCard(NeowEvent.rng);
            case COMMON:
                return AbstractDungeon.commonCardPool.getRandomCard(NeowEvent.rng);
        }
        logger.info("Error in getCard in Neow The Streamer Reward");
        return null;
    }

    public static ArrayList<AbstractCard> getCurseCards(int numCurses) {
        if (numCurses > 6) numCurses = 6;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) list.add(i);
        Collections.shuffle(list);
        ArrayList<AbstractCard> retVal = new ArrayList<>();
        for (int i = 0; i < numCurses; i++) {
            switch (list.get(i)) {
                case 0:
                    retVal.add(new DeezNuts());
                    break;
                case 1:
                    retVal.add(new SoloSpam());
                    break;
                case 2:
                    retVal.add(new Om());
                    break;
                case 3:
                    retVal.add(new Scam());
                    break;
                case 4:
                    retVal.add(new First());
                    break;
                case 5:
                    retVal.add(new Like());
                    break;
            }
        }
        return retVal;
    }

    public static void activateChallengeRewards(NeowTheStreamerRewardType reward, int amount) {
        if (amount > 0) {
            switch (reward) {
                case RANDOM_COMMON_RELIC:
                    for (int i = 0; i < amount; i++) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2),
                                AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON));
                    }
                    break;
                case MAX_HP:
                    AbstractDungeon.player.increaseMaxHp(7 * amount, true);
                    break;
                case GOLD:
                    CardCrawlGame.sound.play("GOLD_JINGLE");
                    AbstractDungeon.player.gainGold(100 * amount);
                    break;
                case GOLD_AND_POTION:
                    CardCrawlGame.sound.play("GOLD_JINGLE");
                    AbstractDungeon.player.gainGold(75 * amount);
                    CardCrawlGame.sound.play("POTION_1");
                    for (int i = 0; i < amount; i++)
                        AbstractDungeon.getCurrRoom().addPotionToRewards(PotionHelper.getRandomPotion());
                    AbstractDungeon.combatRewardScreen.open();
                    (AbstractDungeon.getCurrRoom()).rewardPopOutTimer = 0.0F;
                    int remove = -1;
                    for (int j = 0; j < AbstractDungeon.combatRewardScreen.rewards.size(); j++) {
                        if ((AbstractDungeon.combatRewardScreen.rewards.get(j)).type == RewardItem.RewardType.CARD) {
                            remove = j;
                            break;
                        }
                    }
                    if (remove != -1)
                        AbstractDungeon.combatRewardScreen.rewards.remove(remove);
                    break;
                case TRANSFORM_CARD:
                    if (amount == 1) {
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                                .getPurgeableCards(), amount, REWARD_TEXT[25] + amount + " Card" + REWARD_TEXT[26], false, true, false, false);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                                .getPurgeableCards(), amount, REWARD_TEXT[25] + amount + " Cards" + REWARD_TEXT[26], false, true, false, false);
                    }
                    break;
                case UPGRADE_RANDOM:
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                        if (c.canUpgrade())
                            upgradableCards.add(c);
                    }
                    List<String> cardMetrics = new ArrayList<>();
                    Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
                    if (!upgradableCards.isEmpty())
                        if (upgradableCards.size() <= amount) {
                            for (int i = 0; i < upgradableCards.size(); i++) {
                                (upgradableCards.get(i)).upgrade();
                                cardMetrics.add((upgradableCards.get(i)).cardID);
                                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
                                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect((upgradableCards.get(i)).makeStatEquivalentCopy(), Settings.WIDTH / 2.0F + (((1-amount)+(2*i)) * 190.0F) * Settings.scale, Settings.HEIGHT / 2.0F));
                            }
                        } else {
                            for (int i = 0; i < amount; i++) {
                                (upgradableCards.get(i)).upgrade();
                                cardMetrics.add((upgradableCards.get(i)).cardID);
                                AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(i));
                                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect((upgradableCards.get(i)).makeStatEquivalentCopy(), Settings.WIDTH / 2.0F + (((1-amount)+(2*i)) * 190.0F) * Settings.scale, Settings.HEIGHT / 2.0F));
                            }
                        }
                    break;
                case REMOVE_CARD:
                    AbstractDungeon.player.decreaseMaxHealth(2*amount);
                    if (amount == 1) {
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                                .getPurgeableCards(), amount, REWARD_TEXT[25] + amount + " Card" + REWARD_TEXT[27], false, false, false, true);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                                .getPurgeableCards(), amount, REWARD_TEXT[25] + amount + " Cards" + REWARD_TEXT[27], false, false, false, true);
                    }
                    break;
                case DUPLICATE_CARD:
                    AbstractDungeon.player.decreaseMaxHealth(4*amount);
                    if (amount == 1) {
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                                .getPurgeableCards(), amount, REWARD_TEXT[25] + amount + " Card" + REWARD_TEXT[28], false, false, false, false);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
                                .getPurgeableCards(), amount, REWARD_TEXT[25] + amount + " Cards" + REWARD_TEXT[28], false, false, false, false);
                    }
                    break;
                default:
                    logger.info("Incorrect Challenge Reward");
                    break;
            }
        }
    }
}

