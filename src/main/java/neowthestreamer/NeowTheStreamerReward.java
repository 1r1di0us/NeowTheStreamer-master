package neowthestreamer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.colorless.JAX;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import java.util.ArrayList;
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

    AbstractRelic relicReward;

    AbstractCard cardReward;

    ArrayList<AbstractCard> curses;

    public enum NeowTheStreamerRewardType {
        NONE, RANDOM_SHOP_RELIC, RANDOM_COMMON_RELIC, RANDOM_RARE_CARD, RANDOM_COLORLESS_RARE, THREE_COMMON_CARDS, THREE_COLORLESS_CARDS, NEOWS_LAMENT, TWO_FIFTY_GOLD, RANDOM_VANILLA_RELIC, MAX_HP, GOLD_AND_POTION, GOLD_AND_RARE, GOLD_AND_TRANSFORM, REMOVE_CARD, UPGRADE_CARD, UPGRADE_TWO_RANDOM, UPGRADE_DECK, UPGRADE_RELIC, MASK_OF_MIDAS, BUSTED_CROWN, FOUR_SPECIAL_CARDS, BODY_OF_CLERIC, YOUTUBES_BLESSING, CARD_VOUCHERS, RELIC_REWARDS, BOSS_RELIC;
    }

    public enum NeowTheStreamerChallengeType {
        NONE, ONE_CURSE, CULTIST_HEADPIECE, DECK_OF_TERRIBLE_THINGS, LOW_HP_CHALLENGE, HITLESS_CHALLENGE, POTION_TRASHING_CHALLENGE, KEY_TRASHING_CHALLENGE, GOLD_HOARDING_CHALLENGE, DECK_BUILDING_CHALLENGE, CURSE_CARRYING_CHALLENGE, MARK_OF_NEOOM, NEOWS_PRANK, PEAR_WHEEL, CHATS_REVENGE, MAX_HP_LOSS, TWO_CURSES, THREE_CURSES, WRATH_OF_IRONCLAD, WRATH_OF_SILENT, WRATH_OF_DEFECT, WRATH_OF_WATCHER
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
            option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.THREE_ENEMY_KILL, NeowTheStreamerChallengeType.NONE, REWARD_TEXT[25], "");
        } else {
            option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.TEN_PERCENT_HP_BONUS, NeowTheStreamerChallengeType.NONE, REWARD_TEXT[24] + this.hp_bonus + " ]", "");
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
                        option.rewardType = NeowTheStreamerRewardType.RANDOM_SHOP_RELIC;
                        option.rewardDesc = REWARD_TEXT[0] + relicReward.name + " ]";
                        break;
                    case 1:
                        relicReward = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
                        option.rewardType = NeowTheStreamerRewardType.RANDOM_COMMON_RELIC;
                        option.rewardDesc = REWARD_TEXT[0] + relicReward.name + " ]";
                        break;
                    case 2:
                        cardReward = getCard(AbstractCard.CardRarity.RARE);
                        option.rewardType = NeowTheStreamerRewardType.RANDOM_RARE_CARD;
                        option.rewardDesc = REWARD_TEXT[0] + cardReward.name + " ]";
                        break;
                    case 3:
                        cardReward = AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE);
                        option.rewardType = NeowTheStreamerRewardType.RANDOM_COLORLESS_RARE;
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
                        option.rewardDesc = REWARD_TEXT[0] + cardReward.name + " ]";
                        break;
                    case 6:
                        option.challengeType = NeowTheStreamerChallengeType.CULTIST_HEADPIECE;
                        option.challengeDesc = CHALLENGE_TEXT[0];
                        option.rewardType = NeowTheStreamerRewardType.NEOWS_LAMENT;
                        option.rewardDesc = REWARD_TEXT[2];
                        break;
                    case 7:
                        //curses.add(NeowTheStreamer.getCurseCard(NeowTheStreamerEvent.rng));
                        curses.add(new Parasite()); //placeholder
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
                int challengeIndex = NeowEvent.rng.random(0, 7);
                switch (challengeIndex) {
                    case 0:
                        option.challengeType = NeowTheStreamerChallengeType.DECK_OF_TERRIBLE_THINGS;
                        option.challengeDesc = CHALLENGE_TEXT[2];
                        break;
                    case 1:
                        option.challengeType = NeowTheStreamerChallengeType.LOW_HP_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[3];
                        break;
                    case 2:
                        option.challengeType = NeowTheStreamerChallengeType.HITLESS_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[4];
                        break;
                    case 3:
                        option.challengeType = NeowTheStreamerChallengeType.POTION_TRASHING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[5];
                        break;
                    case 4:
                        option.challengeType = NeowTheStreamerChallengeType.KEY_TRASHING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[6];
                        break;
                    case 5:
                        option.challengeType = NeowTheStreamerChallengeType.GOLD_HOARDING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[7];
                        break;
                    case 6:
                        option.challengeType = NeowTheStreamerChallengeType.DECK_BUILDING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[8];
                        break;
                    case 7:
                        //curses.add(NeowTheStreamer.getCurseCard(NeowTheStreamerEvent.rng));
                        curses.add(cardReward = new Parasite()); //placeholder
                        option.challengeType = NeowTheStreamerChallengeType.CURSE_CARRYING_CHALLENGE;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name + CHALLENGE_TEXT[9];
                        break;
                    default:
                        logger.info("Challenge in category 1 out of index");
                        break;
                }
                int rewardIndex = NeowEvent.rng.random(0, 7);
                switch (rewardIndex) {
                    case 0:
                        option.rewardType = NeowTheStreamerRewardType.RANDOM_VANILLA_RELIC;
                        option.rewardDesc = REWARD_TEXT[5];
                        break;
                    case 1:
                        hp_bonus = 10;
                        option.rewardType = NeowTheStreamerRewardType.MAX_HP;
                        option.rewardDesc = REWARD_TEXT[6] + hp_bonus + " ]";
                        break;
                    case 2:
                        gold_bonus = 150;
                        option.rewardType = NeowTheStreamerRewardType.GOLD_AND_POTION;
                        option.rewardDesc = REWARD_TEXT[7] + gold_bonus + REWARD_TEXT[8] + REWARD_TEXT[9];
                        break;
                    case 3:
                        gold_bonus = 100;
                        option.rewardType = NeowTheStreamerRewardType.GOLD_AND_RARE;
                        option.rewardDesc = REWARD_TEXT[7] + gold_bonus + REWARD_TEXT[8] + REWARD_TEXT[10];
                        break;
                    case 4:
                        gold_bonus = 50;
                        option.rewardType = NeowTheStreamerRewardType.GOLD_AND_TRANSFORM;
                        option.rewardDesc = REWARD_TEXT[7] + gold_bonus + REWARD_TEXT[8] + REWARD_TEXT[11];
                        break;
                    case 5:
                        option.rewardType = NeowTheStreamerRewardType.REMOVE_CARD;
                        option.rewardDesc = REWARD_TEXT[12];
                        break;
                    case 6:
                        option.rewardType = NeowTheStreamerRewardType.UPGRADE_CARD;
                        option.rewardDesc = REWARD_TEXT[13];
                        break;
                    case 7:
                        option.rewardType = NeowTheStreamerRewardType.UPGRADE_TWO_RANDOM;
                        option.rewardDesc = REWARD_TEXT[14];
                        break;
                    default:
                        logger.info("Reward in category 1 out of index");
                        break;
                }
                if (challengeIndex == 5) { // gold could go crazy
                    option.rewardDesc += " (max 5)";
                }
                break;
            case 2:
                int gigaOptionIndex = NeowEvent.rng.random(0, 9);
                if (AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.IRONCLAD && AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.THE_SILENT && AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.DEFECT && AbstractDungeon.player.chosenClass != AbstractPlayer.PlayerClass.WATCHER) {
                    gigaOptionIndex = NeowEvent.rng.random(1, 9);
                } // no upgrading the starter relic on modded characters
                switch (gigaOptionIndex) {
                    case 0:
                        option.challengeType = NeowTheStreamerChallengeType.NEOWS_PRANK;
                        option.challengeDesc = CHALLENGE_TEXT[11];
                        option.rewardType = NeowTheStreamerRewardType.UPGRADE_RELIC;
                        option.rewardDesc = REWARD_TEXT[16];
                        break;
                    case 1:
                        option.challengeType = NeowTheStreamerChallengeType.MARK_OF_NEOOM;
                        option.challengeDesc = CHALLENGE_TEXT[10];
                        option.rewardType = NeowTheStreamerRewardType.UPGRADE_DECK;
                        option.rewardDesc = REWARD_TEXT[15];
                        break;
                    case 2:
                        option.challengeType = NeowTheStreamerChallengeType.NONE;
                        option.challengeDesc = "";
                        option.rewardType = NeowTheStreamerRewardType.MASK_OF_MIDAS;
                        option.rewardDesc = REWARD_TEXT[17];
                        break;
                    case 3:
                        option.challengeType = NeowTheStreamerChallengeType.PEAR_WHEEL;
                        option.challengeDesc = CHALLENGE_TEXT[12];
                        option.rewardType = NeowTheStreamerRewardType.BUSTED_CROWN;
                        option.rewardDesc = REWARD_TEXT[18];
                        break;
                    case 4:
                        option.challengeType = NeowTheStreamerChallengeType.CHATS_REVENGE;
                        option.challengeDesc = CHALLENGE_TEXT[13];
                        option.rewardType = NeowTheStreamerRewardType.FOUR_SPECIAL_CARDS;
                        option.rewardDesc = REWARD_TEXT[19];
                        break;
                    case 5:
                        this.hp_bonus = (int)(AbstractDungeon.player.maxHealth * 0.9F);
                        option.challengeType = NeowTheStreamerChallengeType.MAX_HP_LOSS;
                        option.challengeDesc = CHALLENGE_TEXT[14];
                        option.rewardType = NeowTheStreamerRewardType.BODY_OF_CLERIC;
                        option.rewardDesc = REWARD_TEXT[20];
                        break;
                    case 6:
                        option.challengeType = NeowTheStreamerChallengeType.NONE; //youtubes revenge
                        option.challengeDesc = "";
                        option.rewardType = NeowTheStreamerRewardType.YOUTUBES_BLESSING;
                        option.rewardDesc = REWARD_TEXT[21];
                        break;
                    case 7:
                        curses.add(new Parasite());
                        curses.add(new Pain());
                        option.challengeType = NeowTheStreamerChallengeType.TWO_CURSES;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name + " #rand " + curses.get(1).name;
                        option.rewardType = NeowTheStreamerRewardType.CARD_VOUCHERS;
                        option.rewardDesc = REWARD_TEXT[22];
                        break;
                    case 8:
                        curses.add(new Parasite());
                        curses.add(new Pain());
                        curses.add(new Normality());
                        option.challengeType = NeowTheStreamerChallengeType.THREE_CURSES;
                        option.challengeDesc = CHALLENGE_TEXT[1] + curses.get(0).name + ", " + curses.get(1).name + " #rand " + curses.get(2).name;
                        option.rewardType = NeowTheStreamerRewardType.RELIC_REWARDS;
                        option.rewardDesc = REWARD_TEXT[23];
                        break;
                    case 9:
                        int wrathIndex;
                        switch (AbstractDungeon.player.chosenClass) {
                            case IRONCLAD:
                                wrathIndex = NeowEvent.rng.random(0, 2);
                                if (wrathIndex == 0) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_SILENT;
                                    option.challengeDesc = CHALLENGE_TEXT[16];
                                } else if (wrathIndex == 1) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_DEFECT;
                                    option.challengeDesc = CHALLENGE_TEXT[17];
                                } else if (wrathIndex == 2) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_WATCHER;
                                    option.challengeDesc = CHALLENGE_TEXT[18];
                                }
                                break;
                            case THE_SILENT:
                                wrathIndex = NeowEvent.rng.random(0, 2);
                                if (wrathIndex == 0) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_IRONCLAD;
                                    option.challengeDesc = CHALLENGE_TEXT[15];
                                } else if (wrathIndex == 1) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_DEFECT;
                                    option.challengeDesc = CHALLENGE_TEXT[17];
                                } else if (wrathIndex == 2) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_WATCHER;
                                    option.challengeDesc = CHALLENGE_TEXT[18];
                                }
                                break;
                            case DEFECT:
                                wrathIndex = NeowEvent.rng.random(0, 2);
                                if (wrathIndex == 0) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_IRONCLAD;
                                    option.challengeDesc = CHALLENGE_TEXT[15];
                                } else if (wrathIndex == 1) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_SILENT;
                                    option.challengeDesc = CHALLENGE_TEXT[16];
                                } else if (wrathIndex == 2) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_WATCHER;
                                    option.challengeDesc = CHALLENGE_TEXT[18];
                                }
                                break;
                            case WATCHER:
                                wrathIndex = NeowEvent.rng.random(0, 2);
                                if (wrathIndex == 0) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_IRONCLAD;
                                    option.challengeDesc = CHALLENGE_TEXT[15];
                                } else if (wrathIndex == 1) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_SILENT;
                                    option.challengeDesc = CHALLENGE_TEXT[16];
                                } else if (wrathIndex == 2) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_DEFECT;
                                    option.challengeDesc = CHALLENGE_TEXT[17];
                                }
                                break;
                            default:
                                wrathIndex = NeowEvent.rng.random(0, 3);
                                if (wrathIndex == 0) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_IRONCLAD;
                                    option.challengeDesc = CHALLENGE_TEXT[15];
                                } else if (wrathIndex == 1) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_SILENT;
                                    option.challengeDesc = CHALLENGE_TEXT[16];
                                } else if (wrathIndex == 2) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_DEFECT;
                                    option.challengeDesc = CHALLENGE_TEXT[17];
                                } else if (wrathIndex == 3) {
                                    option.challengeType = NeowTheStreamerChallengeType.WRATH_OF_WATCHER;
                                    option.challengeDesc = CHALLENGE_TEXT[18];
                                }
                                break;
                        }
                        break;
                    default:
                        logger.info("Option in category 2 out of index");
                        break;
                }
                break;
            case 3:
                option = new NeowTheStreamerOptionDef(NeowTheStreamerRewardType.BOSS_RELIC, NeowTheStreamerChallengeType.NONE, REWARD_TEXT[26], "");
                break;
        }
        return option;
    }

    public void activate() {
        this.activated = true;
        switch (this.challenge) {
            case NONE:
                break;
            case CULTIST_HEADPIECE:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), (AbstractRelic)new CultistMask());
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
            case DECK_OF_TERRIBLE_THINGS:
            case LOW_HP_CHALLENGE:
            case HITLESS_CHALLENGE:
            case POTION_TRASHING_CHALLENGE:
            case KEY_TRASHING_CHALLENGE:
            case GOLD_HOARDING_CHALLENGE:
            case DECK_BUILDING_CHALLENGE:
            case CURSE_CARRYING_CHALLENGE:
            case MARK_OF_NEOOM:
            case NEOWS_PRANK:
            case PEAR_WHEEL:
            case CHATS_REVENGE:
                break;
            case MAX_HP_LOSS:
                AbstractDungeon.player.decreaseMaxHealth(this.hp_bonus);
                break;
            case WRATH_OF_IRONCLAD:
            case WRATH_OF_SILENT:
            case WRATH_OF_DEFECT:
            case WRATH_OF_WATCHER:
                break;
            default:
                logger.info("[ERROR] Missing Neow the Streamer Challenge: " + this.challenge.name());
                break;
        }
        switch (this.reward) {
            case NONE:
                break;
            case RANDOM_SHOP_RELIC:
            case RANDOM_COMMON_RELIC:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), this.relicReward);
                break;
            case RANDOM_RARE_CARD:
            case RANDOM_COLORLESS_RARE:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            case THREE_COMMON_CARDS:
            case THREE_COLORLESS_CARDS:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(cardReward.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            case NEOWS_LAMENT:
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), (AbstractRelic)new NeowsLament());
                break;
            case TWO_FIFTY_GOLD:
                CardCrawlGame.sound.play("GOLD_JINGLE");
                AbstractDungeon.player.gainGold(gold_bonus);
                break;
            case RANDOM_VANILLA_RELIC: // all of these occur in the challenge thing
            case MAX_HP:
            case GOLD_AND_POTION:
            case GOLD_AND_RARE:
            case GOLD_AND_TRANSFORM:
            case REMOVE_CARD:
            case UPGRADE_CARD:
            case UPGRADE_TWO_RANDOM:
                break;
            case UPGRADE_DECK:
                break;
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
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((Settings.WIDTH / 2), (Settings.HEIGHT / 2), (AbstractRelic)new BustedCrown());
                AbstractDungeon.bossRelicPool.remove(BustedCrown.ID);
                break;
            case FOUR_SPECIAL_CARDS:
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Bite(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new Apparition(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new JAX(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new RitualDagger(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                break;
            case BODY_OF_CLERIC:
            case YOUTUBES_BLESSING:
            case CARD_VOUCHERS:
            case RELIC_REWARDS:
                break;
            case BOSS_RELIC:
                AbstractDungeon.player.loseRelic(((AbstractRelic)AbstractDungeon.player.relics.get(0)).relicId);
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
}

