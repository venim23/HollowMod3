package HollowMod;

import HollowMod.events.ExploreStagwaysEvent;
import HollowMod.events.MylasSongHappyEvent;
import HollowMod.events.ZoteMeetingEvent;
import HollowMod.monsters.*;
import HollowMod.potions.*;
import HollowMod.relics.*;
import HollowMod.util.Config;
import HollowMod.util.SoundEffects;
import HollowMod.variables.FocusCostVariable;
import basemod.*;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.abstracts.CustomUnlockBundle;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import HollowMod.cards.*;
import HollowMod.characters.TheBugKnight;
import HollowMod.util.IDCheckDontTouchPls;
import HollowMod.util.TextureLoader;
import HollowMod.variables.DefaultCustomVariable;
import HollowMod.variables.DefaultSecondMagicNumber;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 */

@SpireInitializer
public class hollowMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber,
        SetUnlocksSubscriber{

    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Hollow Mod";
    private static final String AUTHOR = "Venim"; // And pretty soon - You!
    private static final String DESCRIPTION = "A mod that attempts to add the Knight from Hollow Knight as a Playable Character, All credit to Hollow Knight goes to Team Cherry. Created using the DefaultMod Template - credit to Gremious";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color BUGKNIGHT_GRAY = CardHelper.getColor(26.0f, 30.0f, 35.0f);
    public static final Color BUGKNIGHT_PALE = CardHelper.getColor(213.0f, 212.0f, 212.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown
    public static final Color VOID_POTION_COLOR = CardHelper.getColor(0f, 0f, 0f); // Super Dark
    public static final Color LIFEBLOOD_POTION_COLOR = CardHelper.getColor(0f, 244.0f, 244.0f); // Super Dark Red/Brown
    public static final Color INFECTION_POTION_COLOR = CardHelper.getColor(255.0f, 135f, 15f);
    public static final Color SOUL_POTION_COLOR = CardHelper.getColor(255.0f, 255f, 255f);



    private static boolean disableEnemies = false;
    private static Properties DEFAULTS_CONFIG = new Properties();


    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_BUGKNIGHT_GRAY = "HollowModResources/images/512/bg_attack_BK_s.png";
    private static final String SKILL_BUGKNIGHT_GRAY = "HollowModResources/images/512/bg_skill_BK_s.png";
    private static final String POWER_BUGKNIGHT_GRAY = "HollowModResources/images/512/bg_power_BK_s.png";

    private static final String ENERGY_ORB_BUGKNIGHT_GRAY = "HollowModResources/images/512/card_orb.png";
    private static final String CARD_ENERGY_ORB = "HollowModResources/images/512/card_energy_orb.png";

    private static final String ATTACK_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/bg_attack_BK.png";
    private static final String SKILL_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/bg_skill_BK.png";
    private static final String POWER_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/bg_power_BK.png";
    private static final String ENERGY_ORB_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/card_orb.png";

    // Character assets
    private static final String THE_BUGKNIGHT_BUTTON = "HollowModResources/images/charSelect/BugKnightButton.png";
    private static final String THE_BUGKNIGHT_PORTRAIT = "HollowModResources/images/charSelect/BugKnightPortrait.png";
    public static final String THE_BUGKNIGHT_SHOULDER_1 = "HollowModResources/images/char/BugKnight/shoulder.png";
    public static final String THE_BUGKNIGHT_SHOULDER_2 = "HollowModResources/images/char/BugKnight/shoulder2.png";
    public static final String THE_BUGKNIGHT_CORPSE = "HollowModResources/images/char/BugKnight/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    private static final String BADGE_IMAGE = "HollowModResources/images/BadgeBug.png";

    // Atlas and JSON files for the Animations
    public static final String THE_BUGKNIGHT_SKELETON_ATLAS = "HollowModResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_BUGKNIGHT_SKELETON_JSON = "HollowModResources/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String makeAudioPath(String resourcePath) {
        return getModID() + "Resources/audio/sounds/" + resourcePath;
    }
    public static String makeMonsterPath(String resourcePath) {
        return getModID() + "Resources/images/monsters/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    //GET CONFIG
    public static boolean getDisableEnemies() {
        return disableEnemies;
    }
    public static void setDisableEnemies(boolean disableEnemies) {
        hollowMod.disableEnemies = disableEnemies;
    }

    ///END CONFIG


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public hollowMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        setModID("HollowMod");
        // Now go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        // Also click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theDefault with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // FINALLY and most importnatly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        logger.info("Done subscribing");

        logger.info("Creating the color " + TheBugKnight.Enums.HOLLOW_COLOR.toString());

        BaseMod.addColor(TheBugKnight.Enums.HOLLOW_COLOR, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY,
                BUGKNIGHT_GRAY, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY,
                ATTACK_BUGKNIGHT_GRAY, SKILL_BUGKNIGHT_GRAY, POWER_BUGKNIGHT_GRAY, ENERGY_ORB_BUGKNIGHT_GRAY,
                ATTACK_BUGKNIGHT_GRAY_PORTRAIT, SKILL_BUGKNIGHT_GRAY_PORTRAIT, POWER_BUGKNIGHT_GRAY_PORTRAIT,
                ENERGY_ORB_BUGKNIGHT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");
    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = hollowMod.class.getResourceAsStream("/IDCheckStrings.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT

        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = hollowMod.class.getResourceAsStream("/IDCheckStrings.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT

        String packageName = hollowMod.class.getPackage().getName(); // STILL NOT EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    // ====== YOU CAN EDIT AGAIN ======


    public static void BugConfig() {
        try {

            DEFAULTS_CONFIG.setProperty(Config.DISABLE_ENEMIES, "false");
            final SpireConfig config = new SpireConfig("Hollow Mod", "Common", DEFAULTS_CONFIG);
            setDisableEnemies(config.getBool(Config.DISABLE_ENEMIES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        BugConfig();
        hollowMod defaultmod = new hollowMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheBugKnight.Enums.THE_BUGKNIGHT.toString());

        BaseMod.addCharacter(new TheBugKnight("The Bug Knight", TheBugKnight.Enums.THE_BUGKNIGHT),
                THE_BUGKNIGHT_BUTTON, THE_BUGKNIGHT_PORTRAIT, TheBugKnight.Enums.THE_BUGKNIGHT);

        receiveEditPotions();
        logger.info("Added " + TheBugKnight.Enums.THE_BUGKNIGHT.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================


    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("The Bug Knight doesn't have any other settings!", 400.0f, 700.0f,
                settingsPanel, (me) -> {
        }));
        final ModLabeledToggleButton toggleMonsters = new ModLabeledToggleButton(Config.TOGGLE_ENEMIES_LABEL, Config.BOSSES_TOGGLE_START_X, Config.BOSSES_TOGGLE_START_Y , Settings.CREAM_COLOR, FontHelper.charDescFont, disableEnemies, settingsPanel, label -> {}, button -> {
            try {
                SpireConfig config = new SpireConfig(MODNAME, "Common", DEFAULTS_CONFIG);
                setDisableEnemies(button.enabled);
                config.setBool(Config.DISABLE_ENEMIES, button.enabled);
                config.save();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        });
        settingsPanel.addUIElement(toggleMonsters);
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== EVENTS =================
        receiveEditMonsters();
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        BaseMod.addEvent(ZoteMeetingEvent.ID, ZoteMeetingEvent.class, TheCity.ID);
        BaseMod.addEvent(MylasSongHappyEvent.ID, MylasSongHappyEvent.class);
        //BaseMod.addEvent(ExploreStagwaysEvent.ID, ExploreStagwaysEvent.class);
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
        // =============== MONSTERS =================
        //BaseMod.addMonster("infinitespire:MassOfShapes", MassOfShapes::new);

        // ================ /ENCOUNTERS/ ===================
    }

    // =============== / POST-INITIALIZE/ =================

    public void receiveEditMonsters(){
        BaseMod.addMonster(eventZote.ID, () -> new eventZote(50.0F));
        if (!hollowMod.getDisableEnemies()) {
            BaseMod.addMonster(monsterHuskWarrior.ID, () -> new MonsterGroup(new AbstractMonster[]{
                    new monsterHuskWarrior(100.0f),
                    new monsterHuskWarrior(-250.0F)
            }));
            BaseMod.addMonster(monsterHuskSentry.ID, () -> new MonsterGroup(new AbstractMonster[]{
                    new monsterHuskSentry(-250.0F),
                    new monsterHuskSentry(50.0F)

            }));
            BaseMod.addMonster(monsterSlobberingHusk.ID, () -> new MonsterGroup(new AbstractMonster[]{
                    new monsterViolentHusk(-250.0F),
                    new monsterSlobberingHusk(50.0F)

            }));
            BaseMod.addMonster(eliteStalkingDevout.ID, () -> new MonsterGroup(new AbstractMonster[]{
                    new monsterLittleWeaver(-500.0F, -10.0F),
                    new monsterLittleWeaver(-250.0F, 10.0F),
                    new eliteStalkingDevout(100.0F),

            }));

            BaseMod.addMonster(eliteMossKnight.ID, () -> new eliteMossKnight(0.0F));
            BaseMod.addMonster(eliteNosk.ID, () -> new eliteNosk(0.0F));
            BaseMod.addMonster(bossRadiance.ID, () -> new bossRadiance());
            BaseMod.addMonster(bossFalseKnight.ID, () -> new bossFalseKnight());
            BaseMod.addMonster(bossNKGrimm.ID, () -> new bossNKGrimm());

            //BaseMod.addBoss("TheBeyond", "infinitespire:MassOfShapes", createPath("ui/map/massBoss.png"), createPath("ui/map/massBoss-outline.png"));
            // =============== /MONSTERS/ =================


            // ================ ADD ENCOUNTERS ===================

            logger.info("Bossses Loaded");
            BaseMod.addBoss(Exordium.ID, bossFalseKnight.ID, "HollowModResources/images/ui/map/FalseKnightIcon.png", "HollowModResources/images/ui/map/bossIcon-outline.png");
            BaseMod.addBoss(TheCity.ID, bossNKGrimm.ID, "HollowModResources/images/ui/map/GrimmIcon.png", "HollowModResources/images/ui/map/bossIcon-outline.png");
            BaseMod.addBoss(TheBeyond.ID, bossRadiance.ID, "HollowModResources/images/ui/map/RadianceIcon.png", "HollowModResources/images/ui/map/bossIcon-outline.png");
            BaseMod.addStrongMonsterEncounter(Exordium.ID, new MonsterInfo(monsterHuskWarrior.ID, 1.0F));
            BaseMod.addStrongMonsterEncounter(TheBeyond.ID, new MonsterInfo(monsterSlobberingHusk.ID, 1.0F));
            BaseMod.addMonsterEncounter(TheCity.ID, new MonsterInfo(monsterHuskSentry.ID, 1.0F));
            BaseMod.addEliteEncounter(TheBeyond.ID, new MonsterInfo(eliteStalkingDevout.ID, 1.1F));
            BaseMod.addEliteEncounter(Exordium.ID, new MonsterInfo(eliteMossKnight.ID, 1.1F));
            BaseMod.addEliteEncounter(TheCity.ID, new MonsterInfo(eliteNosk.ID, 1.1F));
        }
    }
    // ================ ADD AUDIO ===================
    //Thanks Alchyr!
    @Override
    public void receiveAddAudio() {

        //May Remove if Too Copyrighty
        addAudio(SoundEffects.RadBGM);



        addAudio(SoundEffects.Hornet);
        addAudio(SoundEffects.Cyclone);
        addAudio(SoundEffects.Scream);
        addAudio(SoundEffects.Evade);
        addAudio(SoundEffects.Fireball);
        addAudio(SoundEffects.MinerBad);
        addAudio(SoundEffects.MinerGood);
        addAudio(SoundEffects.Parry);
        addAudio(SoundEffects.Quake);
        addAudio(SoundEffects.ShadowDash);
        addAudio(SoundEffects.DreamNail);
        addAudio(SoundEffects.Blocker);
        addAudio(SoundEffects.Cornifer);
        addAudio(SoundEffects.Defender);
        addAudio(SoundEffects.Nail);
        addAudio(SoundEffects.Orange);
        addAudio(SoundEffects.Elder);
        addAudio(SoundEffects.Healing);
        addAudio(SoundEffects.Jiji);
        addAudio(SoundEffects.Smith);
        addAudio(SoundEffects.Fatty);
        addAudio(SoundEffects.Sly);
        addAudio(SoundEffects.SpiderBud);
        addAudio(SoundEffects.SentryBuzz);
        addAudio(SoundEffects.Zote);
        addAudio(SoundEffects.Quirrel);
        addAudio(SoundEffects.MinerLyrics);
        addAudio(SoundEffects.MinerDeath);
        addAudio(SoundEffects.MinerTalk);
        addAudio(SoundEffects.DevoutSlash);
        addAudio(SoundEffects.DevoutOpen);
        addAudio(SoundEffects.WeaverScream);
        addAudio(SoundEffects.SentryBrava);
        addAudio(SoundEffects.GrimmCape);
        addAudio(SoundEffects.GrimmCast);
        addAudio(SoundEffects.GrimmDie);
        addAudio(SoundEffects.GrimmFire);
        addAudio(SoundEffects.GrimmSpikes);
        addAudio(SoundEffects.GrimmCall);
        addAudio(SoundEffects.RadBeam);
        addAudio(SoundEffects.RadCharge);
        addAudio(SoundEffects.RadFlyD);
        addAudio(SoundEffects.RadMisc);
        addAudio(SoundEffects.RadScream);
        addAudio(SoundEffects.RadSong);
        addAudio(SoundEffects.RadSword);
        addAudio(SoundEffects.ZomBlarg);
        addAudio(SoundEffects.ZomSpit1);
        addAudio(SoundEffects.ZomSpit2);
        addAudio(SoundEffects.ZomSplode1);
        addAudio(SoundEffects.Midwife);
        addAudio(SoundEffects.NosRev2);
        addAudio(SoundEffects.NosRev1);
        addAudio(SoundEffects.NosShort);
        addAudio(SoundEffects.NosLong);
        addAudio(SoundEffects.NosAtt);
        addAudio(SoundEffects.Jinn);
        addAudio(SoundEffects.Fool);
        addAudio(SoundEffects.Stag3);
        addAudio(SoundEffects.Stag2);
        addAudio(SoundEffects.Stag1);
        addAudio(SoundEffects.Stag);
    }


    private void addAudio(Pair<String, String> audioData)
    {
        BaseMod.addAudio(audioData.getKey(), audioData.getValue());
    }


    // ================ ADD POTIONS ===================


    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        //BaseMod.addPotion(VoidPotion.class, VOID_POTION_COLOR, VOID_POTION_COLOR, VOID_POTION_COLOR, VoidPotion.POTION_ID, TheBugKnight.Enums.THE_BUGKNIGHT);
       // BaseMod.addPotion(SoulPotion.class, SOUL_POTION_COLOR, SOUL_POTION_COLOR, SOUL_POTION_COLOR, SoulPotion.POTION_ID, TheBugKnight.Enums.THE_BUGKNIGHT);
       // BaseMod.addPotion(LifebloodPotion.class, LIFEBLOOD_POTION_COLOR, LIFEBLOOD_POTION_COLOR, LIFEBLOOD_POTION_COLOR, LifebloodPotion.POTION_ID, TheBugKnight.Enums.THE_BUGKNIGHT);
       // BaseMod.addPotion(InfectionPotion.class, INFECTION_POTION_COLOR, INFECTION_POTION_COLOR, INFECTION_POTION_COLOR, InfectionPotion.POTION_ID, TheBugKnight.Enums.THE_BUGKNIGHT);


        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        //BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        //BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheDefault.Enums.HOLLOW_COLOR);
        //BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheDefault.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new KingsBrandRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new IsmasTearRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new MillibellesNoteRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new VesselMask(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new DelicateFlowerRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new VoidIdolRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new KingsSoulRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new GodTunerRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new MantisClawRelic(), TheBugKnight.Enums.HOLLOW_COLOR);

        // This adds a relic to the Shared pool. Every character can find this relic
        BaseMod.addRelic(new LifeEnderRelic(), RelicType.SHARED);
        BaseMod.addRelic(new StagPassRelic(), RelicType.SHARED);
        BaseMod.addRelic(new JonisBlessingRelic(), RelicType.SHARED);
        BaseMod.addRelic(new QueensCombRelic(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        //UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        BaseMod.addDynamicVariable(new FocusCostVariable());


        logger.info("Adding cards");
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.


        //Starters
        BaseMod.addCard(new skillQuickDash_s());
        BaseMod.addCard(new attackNailStrike_s());
        BaseMod.addCard(new skillFocusHeal_s());
        BaseMod.addCard(new skillCornifersMap_s());


        //attacks
        BaseMod.addCard(new attackCycloneSlash());
        BaseMod.addCard(new attackCoiledNail());
        BaseMod.addCard(new attackDashSlash());
        BaseMod.addCard(new attackBlackTendrils());
        BaseMod.addCard(new attackHeavyBlow());
        BaseMod.addCard(new attackDescendingDark());
        BaseMod.addCard(new attackHornetsHelp());
        BaseMod.addCard(new attackInfectedAttack());
        BaseMod.addCard(new attackQuirrelsAssistance());
        BaseMod.addCard(new attackSoulStrike());
        BaseMod.addCard(new attackLittleFool());


        BaseMod.addCard(new attackWraithStrike());
        BaseMod.addCard(new attackVengefulVoid());
        BaseMod.addCard(new attackZotesMagnificence());
        BaseMod.addCard(new attackRecoilStrike());
        BaseMod.addCard(new attackFastStrike());
        BaseMod.addCard(new attackInfectedCysts());
        BaseMod.addCard(new attackPogoStrike());
        BaseMod.addCard(new attackMidwifesHunger());
        BaseMod.addCard(new attackAbyssShriek());
        BaseMod.addCard(new attackInfectionAssault());
        BaseMod.addCard(new attackSharpenedNail());
        BaseMod.addCard(new attackGreatSlash());
        BaseMod.addCard(new attackFuryoftheFallen());
        BaseMod.addCard(new attackHowlingWraiths());
        BaseMod.addCard(new attackAwokenDreamNail());
        BaseMod.addCard(new attackPureNailStrike());
        BaseMod.addCard(new attackDesolateDive());
        BaseMod.addCard(new attackVengefulSpirit());
        BaseMod.addCard(new attackHerrahsAnger());


        //Skills
        BaseMod.addCard(new skillCloakDash());
        BaseMod.addCard(new skillInfectedGuardian());
        BaseMod.addCard(new skillConfessorsAdvice());
        //BaseMod.addCard(new skillCornifersMap_s());
        BaseMod.addCard(new skillDoubleDash());
        BaseMod.addCard(new skillMantisMark());
        BaseMod.addCard(new skillMawleksShell());
        BaseMod.addCard(new skillSoulSplash());
        BaseMod.addCard(new skillTheNailsmith());
        BaseMod.addCard(new skillLifebloodCocoon());
        BaseMod.addCard(new skillBlackWings());
        //BaseMod.addCard(new skillStalwartShell());
        BaseMod.addCard(new skillDungDefenderAura());
        BaseMod.addCard(new skillGrimmsGift());
        BaseMod.addCard(new skillRadiancesLament());
        BaseMod.addCard(new skillCrystalDash());
        BaseMod.addCard(new skillHuntersJournal());
        BaseMod.addCard(new skillVoidDash());
        BaseMod.addCard(new skillLifebloodCore());
        BaseMod.addCard(new skillHiveblood());
        //BaseMod.addCard(new skillLifebloodHeart());
        BaseMod.addCard(new skillPerfectDash());
        BaseMod.addCard(new skillSiblingsSouls());
        BaseMod.addCard(new skillLuriensSpire());
        BaseMod.addCard(new skillMonarchWings());
        BaseMod.addCard(new skillChannelNail());
        //BaseMod.addCard(new skillSporeShroom());
        BaseMod.addCard(new skillSoulShaman());
        BaseMod.addCard(new skillMonomonsArchive());
        BaseMod.addCard(new skillTaintedHusks());
        BaseMod.addCard(new skillMylasSalvation());
        BaseMod.addCard(new skillDreamGate());
        BaseMod.addCard(new skillSiblingsShadow());
        BaseMod.addCard(new skillPaleKingsBlessing());

        BaseMod.addCard(new skillShadowDash());
        BaseMod.addCard(new skillGoodIntentions());
        BaseMod.addCard(new skillJinnsSoul());
        //BaseMod.addCard(new skillSoulTotem());

        //Powers
        BaseMod.addCard(new powerElderbugsWisdom());
        BaseMod.addCard(new powerSharpenedShadows());
        BaseMod.addCard(new powerSlysStrikes());
        BaseMod.addCard(new powerWeaversong());
        BaseMod.addCard(new powerBrokenVessel());
        BaseMod.addCard(new powerMothsMistake());
        BaseMod.addCard(new powerGrubsong());
        BaseMod.addCard(new powerThornsofAgony());
        BaseMod.addCard(new powerShapeofUnn());
        BaseMod.addCard(new powerSoulMaster());
        BaseMod.addCard(new powerSoulVessel());
        BaseMod.addCard(new powerSoulEater());
        BaseMod.addCard(new powerFlukenest());
        BaseMod.addCard(new powerBaldurShell());
        BaseMod.addCard(new powerPureVessel());
        BaseMod.addCard(new powerLordofShades());
        BaseMod.addCard(new powerWhiteLadysBlessing());
        BaseMod.addCard(new powerGlowingWomb());


        //Deprecated
        //BaseMod.addCard(new OrbSkill());





        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.

        //starter
        UnlockTracker.unlockCard(attackNailStrike_s.ID);
        UnlockTracker.unlockCard(skillQuickDash_s.ID);
        UnlockTracker.unlockCard(skillFocusHeal_s.ID);
        UnlockTracker.unlockCard(skillCornifersMap_s.ID);


        //Attacks
        UnlockTracker.unlockCard(attackCoiledNail.ID);
        UnlockTracker.unlockCard(attackCycloneSlash.ID);
        UnlockTracker.unlockCard(attackDashSlash.ID);
        UnlockTracker.unlockCard(attackBlackTendrils.ID);
        UnlockTracker.unlockCard(attackDescendingDark.ID);
        UnlockTracker.unlockCard(attackHornetsHelp.ID);
        UnlockTracker.unlockCard(attackInfectedAttack.ID);
        UnlockTracker.unlockCard(attackQuirrelsAssistance.ID);
        UnlockTracker.unlockCard(attackSoulStrike.ID);
        UnlockTracker.unlockCard(attackLittleFool.ID);
        UnlockTracker.unlockCard(attackVengefulVoid.ID);
        UnlockTracker.unlockCard(attackMidwifesHunger.ID);
        UnlockTracker.unlockCard(attackWraithStrike.ID);
        UnlockTracker.unlockCard(attackZotesMagnificence.ID);
        UnlockTracker.unlockCard(attackRecoilStrike.ID);
        UnlockTracker.unlockCard(attackFastStrike.ID);
        UnlockTracker.unlockCard(attackInfectedCysts.ID);
        UnlockTracker.unlockCard(attackPogoStrike.ID);
        UnlockTracker.unlockCard(attackAbyssShriek.ID);
        UnlockTracker.unlockCard(attackInfectionAssault.ID);
        UnlockTracker.unlockCard(attackSharpenedNail.ID);
        UnlockTracker.unlockCard(attackGreatSlash.ID);
        UnlockTracker.unlockCard(attackFuryoftheFallen.ID);
        UnlockTracker.unlockCard(attackHowlingWraiths.ID);
        UnlockTracker.unlockCard(attackAwokenDreamNail.ID);

        //Skills
        UnlockTracker.unlockCard(skillCloakDash.ID);
        UnlockTracker.unlockCard(skillConfessorsAdvice.ID);
        UnlockTracker.unlockCard(skillDoubleDash.ID);
        //UnlockTracker.unlockCard(skillCornifersMap_s.ID);
        UnlockTracker.unlockCard(skillMantisMark.ID);
        UnlockTracker.unlockCard(skillMawleksShell.ID);
        UnlockTracker.unlockCard(skillSoulSplash.ID);
        UnlockTracker.unlockCard(skillTheNailsmith.ID);
        UnlockTracker.unlockCard(skillGoodIntentions.ID);
        UnlockTracker.unlockCard(skillLifebloodCocoon.ID);
        UnlockTracker.unlockCard(skillDungDefenderAura.ID);
        UnlockTracker.unlockCard(skillGrimmsGift.ID);
        UnlockTracker.unlockCard(skillShadowDash.ID);
        UnlockTracker.unlockCard(skillJinnsSoul.ID);
        //UnlockTracker.unlockCard(skillRadiancesLament.ID);
        UnlockTracker.unlockCard(skillCrystalDash.ID);
        UnlockTracker.unlockCard(skillHuntersJournal.ID);
        UnlockTracker.unlockCard(skillVoidDash.ID);
        UnlockTracker.unlockCard(skillLifebloodCore.ID);
        UnlockTracker.unlockCard(skillSoulShaman.ID);
        UnlockTracker.unlockCard(skillHiveblood.ID);
        UnlockTracker.unlockCard(skillPerfectDash.ID);
        //UnlockTracker.unlockCard(skillLifebloodHeart.ID);
        UnlockTracker.unlockCard(skillMonarchWings.ID);
        UnlockTracker.unlockCard(skillSiblingsSouls.ID);
        UnlockTracker.unlockCard(skillChannelNail.ID);
        UnlockTracker.unlockCard(skillDreamGate.ID);
        UnlockTracker.unlockCard(skillSiblingsShadow.ID);

        // Powers

        UnlockTracker.unlockCard(powerSharpenedShadows.ID);
        //UnlockTracker.unlockCard(powerLordofShades.ID);
        UnlockTracker.unlockCard(powerElderbugsWisdom.ID);
        UnlockTracker.unlockCard(powerSlysStrikes.ID);
        UnlockTracker.unlockCard(powerWeaversong.ID);
        UnlockTracker.unlockCard(powerMothsMistake.ID);
        UnlockTracker.unlockCard(powerGrubsong.ID);
        UnlockTracker.unlockCard(powerThornsofAgony.ID);
        UnlockTracker.unlockCard(powerShapeofUnn.ID);
        UnlockTracker.unlockCard(powerSoulMaster.ID);
        UnlockTracker.unlockCard(powerSoulVessel.ID);
        UnlockTracker.unlockCard(powerSoulEater.ID);
        UnlockTracker.unlockCard(powerBaldurShell.ID);
        UnlockTracker.unlockCard(powerFlukenest.ID);
        //Deprecated

        //UnlockTracker.unlockCard(OrbSkill.ID);










        logger.info("Done adding cards!");
    }



    public void receiveSetUnlocks() {
        BaseMod.addUnlockBundle(new CustomUnlockBundle(powerLordofShades.ID, powerPureVessel.ID, powerBrokenVessel.ID), TheBugKnight.Enums.THE_BUGKNIGHT, 0);
        UnlockTracker.addCard(powerLordofShades.ID);
        UnlockTracker.addCard(powerPureVessel.ID);
        UnlockTracker.addCard(powerBrokenVessel.ID);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC, VoidIdolRelic.ID, KingsSoulRelic.ID, QueensCombRelic.ID), TheBugKnight.Enums.THE_BUGKNIGHT, 1);
        UnlockTracker.addRelic(VoidIdolRelic.ID);
        UnlockTracker.addRelic(KingsSoulRelic.ID);
        UnlockTracker.addRelic(QueensCombRelic.ID);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(attackHerrahsAnger.ID, skillLuriensSpire.ID, skillMonomonsArchive.ID), TheBugKnight.Enums.THE_BUGKNIGHT, 2);
        UnlockTracker.addCard(attackHerrahsAnger.ID);
        UnlockTracker.addCard(skillLuriensSpire.ID);
        UnlockTracker.addCard(skillMonomonsArchive.ID);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC, IsmasTearRelic.ID, JonisBlessingRelic.ID, DelicateFlowerRelic.ID), TheBugKnight.Enums.THE_BUGKNIGHT, 3);
        UnlockTracker.addRelic(IsmasTearRelic.ID);
        UnlockTracker.addRelic(JonisBlessingRelic.ID);
        UnlockTracker.addRelic(DelicateFlowerRelic.ID);
        BaseMod.addUnlockBundle(new CustomUnlockBundle(skillPaleKingsBlessing.ID, powerWhiteLadysBlessing.ID, skillRadiancesLament.ID), TheBugKnight.Enums.THE_BUGKNIGHT, 4);
        UnlockTracker.addCard(skillPaleKingsBlessing.ID);
        UnlockTracker.addCard(powerWhiteLadysBlessing.ID);
        UnlockTracker.addCard(skillRadiancesLament.ID);


    }
    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings");


        String language = null;
        switch (Settings.language) {
           /* case ZHS: {
                language = "zhs";
                break;
            }
            case ZHT: {
                language = "zht";
                break;
            }
            case FRA: {
                language = "fra";
                break;
            } */
            case JPN: {
                language = "jpn";
                break;
            }
            default: {
                language = "eng";
                break;
            }
        }

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Orb-Strings.json");
        //UI
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-UI-Strings.json");

        //Monster
       BaseMod.loadCustomStringsFile(MonsterStrings.class,
                getModID() + "Resources/localization/" + language + "/HollowMod-Monster-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        String language = null;
        switch (Settings.language) {
           /* case ZHS: {
                language = "zhs";
                break;
            }
            case ZHT: {
                language = "zht";
                break;
            }
            case FRA: {
                language = "fra";
                break;
            } */
            case JPN: {
                language = "jpn";
                break;
            }
            default: {
                language = "eng";
                break;
            }
        }


        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/" + language + "/HollowMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

}
