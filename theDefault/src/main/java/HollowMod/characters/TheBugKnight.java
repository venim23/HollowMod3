package HollowMod.characters;

import HollowMod.hollowMod;
import HollowMod.relics.DelicateFlowerRelic;
import HollowMod.relics.JonisBlessingRelic;
import HollowMod.relics.VesselMask;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import HollowMod.cards.*;

import java.util.ArrayList;

import static HollowMod.hollowMod.*;
import static HollowMod.characters.TheBugKnight.Enums.HOLLOW_COLOR;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in DefaultMod-character-Strings.json in the resources

public class TheBugKnight extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_BUGKNIGHT;
        @SpireEnum(name = "BUGKNIGHT_GRAY_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor HOLLOW_COLOR;
        @SpireEnum(name = "BUGKNIGHT_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 6;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("BugKnightCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "HollowModResources/images/char/BugKnight/orb/layer1.png",
            "HollowModResources/images/char/BugKnight/orb/layer2.png",
            "HollowModResources/images/char/BugKnight/orb/layer3.png",
            "HollowModResources/images/char/BugKnight/orb/layer4.png",
            "HollowModResources/images/char/BugKnight/orb/layer5.png",
            "HollowModResources/images/char/BugKnight/orb/layer6.png",
            "HollowModResources/images/char/BugKnight/orb/layer1d.png",
            "HollowModResources/images/char/BugKnight/orb/layer2d.png",
            "HollowModResources/images/char/BugKnight/orb/layer3d.png",
            "HollowModResources/images/char/BugKnight/orb/layer4d.png",
            "HollowModResources/images/char/BugKnight/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public TheBugKnight(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "HollowModResources/images/char/BugKnight/orb/vfx.png", null,
                new SpriterAnimation(
                        "HollowModResources/images/char/BugKnight/Spriter/KnightAnim.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_BUGKNIGHT_SHOULDER_1, // campfire pose
                THE_BUGKNIGHT_SHOULDER_2, // another campfire pose
                THE_BUGKNIGHT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        //loadAnimation(
        //        THE_BUGKNIGHT_SKELETON_ATLAS,
        //        THE_BUGKNIGHT_SKELETON_JSON,
        //       1.0f);
        //AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
       // e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");


        retVal.add(attackNailStrike_s.ID);
        retVal.add(attackNailStrike_s.ID);
        retVal.add(attackNailStrike_s.ID);
        retVal.add(attackNailStrike_s.ID);
        logger.info("loading dashes");
        retVal.add(skillQuickDash_s.ID);
        retVal.add(skillQuickDash_s.ID);
        retVal.add(skillQuickDash_s.ID);
        retVal.add(skillQuickDash_s.ID);
        //Attacks

        //retVal.add(attackInfectedAttack.ID);
        //retVal.add(attackSoulStrike.ID);
        //retVal.add(attackDesolateDive.ID);
        //retVal.add(attackVengefulVoid.ID);
        //retVal.add(attackVengefulVoid.ID);
        //retVal.add(attackVengefulVoid.ID);
        //retVal.add(attackCoiledNail.ID);
        //retVal.add(attackDescendingDark.ID);
        //retVal.add(attackZotesMagnificence.ID);
        //retVal.add(attackHornetsHelp.ID);
        //retVal.add(attackQuirrelsAssistance.ID);
        logger.info("loading focuses");
        //Skills
        retVal.add(skillFocusHeal_s.ID);
        retVal.add(skillFocusHeal_s.ID);
        //retVal.add(skillSoulTotem.ID);
        //retVal.add(skillSoulSplash.ID);
        //retVal.add(skillTheNailsmith.ID);
        //retVal.add(skillConfessorsAdvice.ID);
        //retVal.add(skillCloakDash.ID);
        //retVal.add(skillMantisMark.ID);
        //retVal.add(skillQuickDash_s.ID);
        //Powers
        //retVal.add(powerSharpenedShadows.ID);
        //retVal.add(powerLordofShades.ID);
        //retVal.add(powerElderbugsWisdom.ID);
        //retVal.add(skillFocusHeal_s.ID);

        logger.info("loading testcards");
        //retVal.add(attackRecoilStrike.ID);
        //retVal.add(skillDoubleDash.ID);
        //retVal.add(powerSlysStrikes.ID);
        //retVal.add(attackDashSlash.ID);
        //retVal.add(skillLifebloodCocoon.ID);
        //retVal.add(skillStalwartShell.ID);
        //retVal.add(attackFastStrike.ID);
        //retVal.add(attackInfectedCysts.ID);
        //retVal.add(skillDungDefenderAura.ID);
        //retVal.add(skillGrimmsGift.ID);
        //retVal.add(attackPogoStrike.ID);
        //retVal.add(attackVengefulSpirit.ID);
        //retVal.add(attackAbyssShriek.ID);
        //retVal.add(powerWeaversong.ID);
        //retVal.add(attackInfectedAttack.ID);
        //retVal.add(skillRadiancesLament.ID);
        //retVal.add(powerBrokenVessel.ID);
        //retVal.add(powerMothsMistake.ID);
        //retVal.add(attackInfectionAssault.ID);
        //retVal.add(skillMawleksShell.ID);
        //retVal.add(powerGrubsong.ID);
        //retVal.add(powerGrubsong.ID);
        //retVal.add(powerThornsofAgony.ID);
        //retVal.add(powerShapeofUnn.ID);
        //retVal.add(skillCrystalDash.ID);
        //retVal.add(skillHuntersJournal.ID);
        //retVal.add(skillVoidDash.ID);
        //retVal.add(attackSharpenedNail.ID);
        //retVal.add(skillCornifersMap.ID);
        //retVal.add(skillLifebloodCore.ID);
        //retVal.add(skillLifebloodHeart.ID);
        //retVal.add(skillHiveblood.ID);
        //retVal.add(skillPerfectDash.ID);
        //retVal.add(attackGreatSlash.ID);
        //retVal.add(attackFuryoftheFallen.ID);
        //retVal.add(skillSiblingsSouls.ID);
        //retVal.add(skillMonarchWings.ID);
        //retVal.add(skillChannelNail.ID);
        //retVal.add(attackHowlingWraiths.ID);
        //retVal.add(attackAwokenDreamNail.ID);
        //retVal.add(powerSoulMaster.ID);
        //retVal.add(powerSoulMaster.ID);
        //retVal.add(powerSoulEater.ID);
        //retVal.add(powerBaldurShell.ID);
        //retVal.add(skillSporeShroom.ID);
        //retVal.add(powerSoulVessel.ID);
        //retVal.add(attackPureNailStrike.ID);
        //retVal.add(skillSoulShaman.ID);
        //retVal.add(skillOvercharmed.ID);

        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        //retVal.add(PlaceholderRelic.ID);
        //retVal.add(PlaceholderRelic2.ID);
        //retVal.add(DefaultClickableRelic.ID);
        retVal.add(VesselMask.ID);
        //UnlockTracker.markRelicAsSeen(PlaceholderRelic.ID);
        //UnlockTracker.markRelicAsSeen(PlaceholderRelic2.ID);
        //UnlockTracker.markRelicAsSeen(DefaultClickableRelic.ID);
        UnlockTracker.markRelicAsSeen(VesselMask.ID);
        UnlockTracker.markRelicAsSeen(JonisBlessingRelic.ID);
        UnlockTracker.markRelicAsSeen(DelicateFlowerRelic.ID);



        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_MAGIC_SLOW_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_MAGIC_SLOW_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return HOLLOW_COLOR;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return hollowMod.BUGKNIGHT_PALE;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new attackFuryoftheFallen();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new TheBugKnight(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return hollowMod.BUGKNIGHT_GRAY;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return hollowMod.BUGKNIGHT_PALE;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

}
