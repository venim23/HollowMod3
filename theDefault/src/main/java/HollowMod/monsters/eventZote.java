package HollowMod.monsters;

import HollowMod.actions.SFXVAction;
import HollowMod.actions.empowerLouseAction;
import HollowMod.events.ZoteMeetingEvent;
import HollowMod.hollowMod;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class eventZote extends AbstractMonster {

    // Shitty Explanations by Venim, don't blame anyone but me. Getting rebuked for poor coding is my kink.


    /*SO FIRST. you need an id, just like a card, relic, etc.
     The important thing is to make sure your .json file for "myshittymod-Monster-Strings.json" has the same ID,
     With your modid as a prefix. in MY case that would be HollowMod:MossKnight
     Grem's makeID method in the main mod class (theDefault.java Probably) is what I'm calling

     All the next 3 lines are simply pulling up infor from the json for me tu use later if i feel like it.
    */
    public static final String ID = hollowMod.makeID("Zote");

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    //This is really only useful if you want your class to handle minions, you will have to fill in this variable youself though.


    // ****** MOVE AND STAT VALUES ********//
    //These will make it easy to adjust the balance for the monster

    //There's a LOT of variable you could assign, but you will usually have to deal with them yourself to count things.
    //Make sure you have a maxHP and minHP

    private int maxHP = 1000;
    private int minHP = 1000;

    //I almost always use Dmg and Def at the end of thinks I want as damage or Block, otherwise my naming conventions are kinda a tossup.
    private int swingDmg = 250 ;
    private int swingtimes = 5 ;
    private int falbuff = 999;

    //this variable is handy to keep track of too, I recommend using something like it.
    private int numTurns;
    // ******* END OF MOVE AND STAT VALUES *********//


    //LIST THE ANIM NAMES HERE IN COMMENTS FOR EASY MEMORY//
    //If you are using a DragonBones or whatever Json and Atlas, then the animation names and these strings need to match up.
    //I highly recommend using something like this for all your animation names, because It's a pain to reopen your animation program because you can't remember what you called the one animation you just deleted the string for.

    private String animIdle = "idle";
    private String animTimer = "toolate";
    private String animOuch = "fallidle";
    private String animScare = "bigattack";



    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public eventZote(float xOffset)
    //in the core game, most monsters have 2 passed variables for X and Y position on screen,
    //80% of the time you won't need to set the Y position, but if you do just add ",float yOffset" and then call it in the super just below this.
    //you will need to assign both values whenever you spawn or setup the encounter for the monster. Most likely in your main mod file. thedefault.java
    {
	    //NAME is what the player sees so don't assign it the ID like in a card, Use the stuff at the top to assign things in "ModName-Monster-Strings.json"
        //maxHealth is funny because you can't pass it a variable. Just try to make sure it matches your MaxHP, but i don't think it matters.
        //hb_x/y/w/h are all floats that assign where the health bar is, also it affects where the intent is displayed, if you used a img use the dimensions of that, if an animation, you might need to adjust till it looks right.
        //ImgURL is null for an animation, if you want a static image monster, assign that path (or preferably a variable) here.
        //offsetX and offset y are where on the screen the monster is shown, this is usually a passed in variable at least for x.


        super(NAME, ID, 1000, 50, 0, 200.0F, 200.0F, null, xOffset, 0.0F);



        //If the enemy is a regular monster it doesn't need to have a type. Only Elites and Bosses need to have their type assigned.
        this.type = EnemyType.ELITE;


        //So, all  the following Ascension level stuff is to make this bad boy harder on different levels. It's usually simple stuff though.
        //there's rules that are important for this, I'm not sure what they all are off the top of my head.
        // use this. https://slay-the-spire.fandom.com/wiki/Ascension
        //set the min and max hp bounds, they will be rolled between randomly on spawn
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS **** //
        // in order for the intent and stuff to work right, declare the base damage values of attacks (don't calculate for multihits though, that's handled elsewhere)

        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.swingDmg)); // attack 0 damage
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation assign the resource path from where you put it, (hollowMod.makemonsterPath assigns the precursor for where you find the 2 folders (i always have a lowercase foldername and capped File names for monsters
                //you could probably just dump all the files into one folder if you wanted or if there's just one monster, but you are a bad person if you do this.
                hollowMod.makeMonsterPath("zote/Zote.atlas"),
                hollowMod.makeMonsterPath("zote/Zote.json"), 1.0F); // scale float is actually inverted because of course it is. 0.8 is bigger, 1.2 is smaller.

        //no idea what track index means., the 2nd variable wants the string name for you anim you want to start out with (usually idle), and the the boolean says if this anim should repeat unless interrupted or whatever?
        AnimationState.TrackEntry e = this.state.setAnimation(0, animIdle, true);
        //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    //for coding it almost totally depends on what your design is driven by, this can either be the first or last thing to write. for me it's usually last, for most people it's first, I just usually have the animations already done and the designs on sheets

    public void takeTurn()
    {
        /*
        public static final byte COMBO = 0;
        public static final byte SPIT = 1;
        public static final byte DEFSTANCE = 2;
        public static final byte DEFBREAK = 3;
        public static final byte LEAFFORM = 4;
        public static final byte RECOVER = 5;
        public static final byte NOTHING = 6;
        */
        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {   //0 Swing- att
            case 0: //Combo

                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BIGHIT"));
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
                CardCrawlGame.music.silenceTempBgmInstantly();

                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new StrengthPower(p, this.falbuff)));

                break;
            case 1: //Spit
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "TOOLATE"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(this,AbstractDungeon.player.currentHealth-1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

                break;
            case 2: //Nothing
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.Zote.getKey(), 1.8F));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[AbstractDungeon.monsterHpRng.random(2,5)], 2.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));
                break;
        }
        //unsure here, I think it basically  uses an action to send this monsters data to the AbstractMonster.rollMove , which activates the DefaultMonster.getMove and sends a rng amount?
        //this seems to basically be the "get the intent for the next turn's move thing"
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("BOSS_CITY", true);
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 3.0f, 3.0f));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 999));



    }


    /*private boolean canSpawn() {
        int aliveCount = 0;
        for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != this && !m.isDying) {
                ++aliveCount;
            }
        }
        return aliveCount <= 3;
    }
    */
    //this is the method that the ChangeStateAction requires within the class of any monster thant calls it.
    public void changeState(String key)
    {
        switch (key)
        {
            //for each key, it has a simple little transition between animations,
            //for this example, sets the animation to attack, and not looping, then adds the looping idle animation as next in line.
            case "BIGHIT":
                this.state.setAnimation(0, animScare, false);
                this.state.addAnimation(0, animOuch, true, 0.0F);
                break;
            case "TOOLATE":
                this.state.setAnimation(0, animTimer, false);
                this.state.addAnimation(0, animOuch, true, 0.0F);
                break;
        }
    }
    //Unsure, but I think this handles the event of Taking damage, not sure if it's needed or not.
    //basically works just like the change state attack, the oof animation plays once. then it sets the looping idle animation to play again afterwards.
    public void damage(DamageInfo info)
    {
        super.damage(info);
    }

    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {
        this.numTurns++;
        if (this.numTurns == 1){
            setMove(MOVES[0], (byte) 0, Intent.ATTACK,((DamageInfo)this.damage.get(0)).base, this.swingtimes, true);
            return;
        }
        if (this.numTurns > 5){
            setMove((byte)1, Intent.MAGIC);
        } else {
            setMove((byte)2, Intent.STUN);
        }
    }

    public static class MoveBytes
    {
        public static final byte SWING = 0;
        public static final byte TOOLATE = 1;
        public static final byte NOTHING = 2;


    }

    public void die() {
        this.state.setTimeScale(0.1f);
        this.useShakeAnimation(5.0f);

        super.die();
    }







}
