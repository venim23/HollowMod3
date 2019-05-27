package HollowMod.monsters;

import HollowMod.actions.SFXVAction;
import HollowMod.actions.empowerLouseAction;
import HollowMod.hollowMod;
import HollowMod.powers.LeafPower;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

public class eliteNosk extends AbstractMonster {

    // Shitty Explanations by Venim, don't blame anyone but me. Getting rebuked for poor coding is my kink.


    /*SO FIRST. you need an id, just like a card, relic, etc.
     The important thing is to make sure your .json file for "myshittymod-Monster-Strings.json" has the same ID,
     With your modid as a prefix. in MY case that would be HollowMod:MossKnight
     Grem's makeID method in the main mod class (theDefault.java Probably) is what I'm calling
     All the next 3 lines are simply pulling up infor from the json for me tu use later if i feel like it.
    */
    public static final String ID = hollowMod.makeID("Nosk");

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    //This is really only useful if you want your class to handle minions, you will have to fill in this variable youself though.


    // ****** MOVE AND STAT VALUES ********//
    //These will make it easy to adjust the balance for the monster

    //There's a LOT of variable you could assign, but you will usually have to deal with them yourself to count things.
    //Make sure you have a maxHP and minHP

    private int maxHP = 40;
    private int minHP = 30;
    private int newHP = 130;


    //I almost always use Dmg and Def at the end of thinks I want as damage or Block, otherwise my naming conventions are kinda a tossup.
    private int rearDef = 15 ;
    private int cryAmt = -1 ;
    private int vesselDmg = 5;
    private int trampleDmg = 10;
    private int trampleAmt = 2;
    private int stompDmg = 14;
    private int sprayAmt = 2;



    private boolean leafedHas = false;
    private boolean leafMode = false;
    private boolean defStance = false;
    private int leafHeal = 10 ;
    private int leafBlock = 35 ;

    //this variable is handy to keep track of too, I recommend using something like it.
    private int numTurns;
    // ******* END OF MOVE AND STAT VALUES *********//


    //LIST THE ANIM NAMES HERE IN COMMENTS FOR EASY MEMORY//
    //If you are using a DragonBones or whatever Json and Atlas, then the animation names and these strings need to match up.
    //I highly recommend using something like this for all your animation names, because It's a pain to reopen your animation program because you can't remember what you called the one animation you just deleted the string for.


    private String animReveal = "transform";
    private String animTrample = "trample";
    private String animSpray = "pop";
    private String animRearUp = "scream";
    private String animStomp = "stomp";
    private String animScream = "scream";
    private String animIdle = "idle";
    private String animVesIdle = "vesselidle";
    private String animHit = "oof";
    private String animVesHit = "vesseloof";



    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public eliteNosk(float xOffset)
    //in the core game, most monsters have 2 passed variables for X and Y position on screen,
    //80% of the time you won't need to set the Y position, but if you do just add ",float yOffset" and then call it in the super just below this.
    //you will need to assign both values whenever you spawn or setup the encounter for the monster. Most likely in your main mod file. thedefault.java
    {
        //NAME is what the player sees so don't assign it the ID like in a card, Use the stuff at the top to assign things in "ModName-Monster-Strings.json"
        //maxHealth is funny because you can't pass it a variable. Just try to make sure it matches your MaxHP, but i don't think it matters.
        //hb_x/y/w/h are all floats that assign where the health bar is, also it affects where the intent is displayed, if you used a img use the dimensions of that, if an animation, you might need to adjust till it looks right.
        //ImgURL is null for an animation, if you want a static image monster, assign that path (or preferably a variable) here.
        //offsetX and offset y are where on the screen the monster is shown, this is usually a passed in variable at least for x.


        super(NAME, ID, 50, -150, 0, 200.0F, 200.0F, null, xOffset, 0.0F);



        //If the enemy is a regular monster it doesn't need to have a type. Only Elites and Bosses need to have their type assigned.
        this.type = EnemyType.ELITE;



        //So, all  the following Ascension level stuff is to make this bad boy harder on different levels. It's usually simple stuff though.
        //there's rules that are important for this, I'm not sure what they all are off the top of my head.
        // use this. https://slay-the-spire.fandom.com/wiki/Ascension
        if (AbstractDungeon.ascensionLevel >= 8) // Elites are tougher at Asc 8
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 5;
            this.maxHP += 5;
            this.newHP+= 10;
        }
        if (AbstractDungeon.ascensionLevel >=3) //Elites are deadlier at 3
        {
            //increases the power of his multihit and debufff for higher ascensions
            this.trampleDmg+= 2;
            this.stompDmg += 2;
            this.vesselDmg += 5;
        }
        if (AbstractDungeon.ascensionLevel >= 18) //18 says elites have harder move sets so do something fancy
        {
            this.sprayAmt +=1;
        }

        //set the min and max hp bounds, they will be rolled between randomly on spawn
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS **** //
        // in order for the intent and stuff to work right, declare the base damage values of attacks (don't calculate for multihits though, that's handled elsewhere)

        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.vesselDmg)); // attack 0 damage
        this.damage.add(new DamageInfo(this, this.stompDmg));
        this.damage.add(new DamageInfo(this, this.trampleDmg));
        // Attack 1 damage
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation assign the resource path from where you put it, (hollowMod.makemonsterPath assigns the precursor for where you find the 2 folders (i always have a lowercase foldername and capped File names for monsters
                //you could probably just dump all the files into one folder if you wanted or if there's just one monster, but you are a bad person if you do this.
            hollowMod.makeMonsterPath("nosk/Nosk.atlas"),
            hollowMod.makeMonsterPath("nosk/Nosk.json"), 1.0F); // scale float is actually inverted because of course it is. 0.8 is bigger, 1.2 is smaller.

    //no idea what track index means., the 2nd variable wants the string name for you anim you want to start out with (usually idle), and the the boolean says if this anim should repeat unless interrupted or whatever?
    AnimationState.TrackEntry e = this.state.setAnimation(0, animVesIdle, true);
    //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
}

    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    //for coding it almost totally depends on what your design is driven by, this can either be the first or last thing to write. for me it's usually last, for most people it's first, I just usually have the animations already done and the designs on sheets

    public void takeTurn()
    {

        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {   //0 Swing- att

            //ACTION TEMPLATES

            //AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05f, true));
            //AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "COMBO"));
            //AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.Blocker.getKey()));
            //AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4f));
            //AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            case 0: //Spew
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosLong.getKey()));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.2f, true));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SPRAY"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new WeakPower(p, this.sprayAmt, true), this.sprayAmt));
                //AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 1: //Vessel Scream
                this.useFastShakeAnimation(1.0f);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05f, true));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosShort.getKey(), 1.8F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new StrengthPower(p, this.cryAmt), this.cryAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new DexterityPower(p, this.cryAmt), this.cryAmt));
                break;
            case 2: //Vessel Attack
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.Nail.getKey(), 1.2F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 3: // Rear up
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "REARUP"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosShort.getKey(), 1.8F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.rearDef));
                break;
            case 4: //Stomp
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "STOMP"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosAtt.getKey()));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 5: // Trample
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "TRAMPLE"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new FrailPower(p, this.trampleAmt, true), this.trampleAmt));
                break;
        }
        //unsure here, I think it basically  uses an action to send this monsters data to the AbstractMonster.rollMove , which activates the DefaultMonster.getMove and sends a rng amount?
        //this seems to basically be the "get the intent for the next turn's move thing"
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.silenceBGM();
        //AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }




    //this is the method that the ChangeStateAction requires within the class of any monster thant calls it.
    public void changeState(String key)
    {
        switch (key)
        {
            //for each key, it has a simple little transition between animations,
            //for this example, sets the animation to attack, and not looping, then adds the looping idle animation as next in line.
            case "REVEAL":
                this.state.setAnimation(0, animReveal, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "SCREECH":
                this.state.setAnimation(0, animScream, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "TRAMPLE":
                this.state.setAnimation(0, animTrample, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "STOMP":
                this.state.setAnimation(0, animStomp, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "SPRAY":
                this.state.setAnimation(0, animSpray, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "REARUP":
                this.state.setAnimation(0, animRearUp, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;

        }
    }
    //Unsure, but I think this handles the event of Taking damage, not sure if it's needed or not.
    //basically works just like the change state attack, the oof animation plays once. then it sets the looping idle animation to play again afterwards.
    public void damage(DamageInfo info) {
        super.damage(info);
        //just checks to make sure the attack came from the plaer basically.
        if ((AbstractDungeon.getCurrRoom().cannotLose) && (info.type != DamageInfo.DamageType.THORNS) && (info.output > 0)) {



            this.state.setAnimation(0, animVesHit, false);
            this.state.addAnimation(0, animVesIdle, true, 0.0F);

          /*  if (){
                transform();
            }
            */
        } else if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS) && (info.output > 0)){
            this.state.setAnimation(0, animHit, false);
            this.state.addAnimation(0, animIdle, true, 0.0F);
        }
    }
    private void transform ()
    {
        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.unsilenceBGM();
        this.increaseMaxHp(newHP, true);
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "REVEAL"));
        AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosRev1.getKey(), 1.2F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosRev2.getKey(), 1.2F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.NosLong.getKey(), 1.8F));
        this.hb_x = 0 * Settings.scale;
        this.hb_h = 350 * Settings.scale;
        this.hb_w = 300 * Settings.scale;
        this.hb_y = -25 * Settings.scale;
        this.healthHb.y = 0 * Settings.scale;
        this.healthHb.height = 72.0F * Settings.scale;
        this.hb.y = 0 * Settings.scale;
        this.hb.height = 350 * Settings.scale;
        this.hb.width = 300 * Settings.scale;
        this.hb.update();
        this.updateHealthBar();
        refreshIntentHbLocation();
        refreshHitboxLocation();
        healthBarUpdatedEvent();
        AbstractDungeon.getCurrRoom().cannotLose = false;
        this.nextMove = 4;
        this.setMove((byte)0, Intent.ATTACK,((DamageInfo)this.damage.get(1)).base );
        this.intent = (Intent.ATTACK);
    }

    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {
        numTurns++;

        if (numTurns == 1){
            setMove((byte)1, Intent.DEBUFF);
            return;
        }

        if (AbstractDungeon.getCurrRoom().cannotLose){
            if (i < 75) {
                setMove((byte)1, Intent.DEBUFF);
                return;
            } else {
                setMove((byte) 2, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base);
                return;
            }
        } else {
            if ((i < 25) && (!lastMove((byte)0))){
                setMove((byte) 0, Intent.STRONG_DEBUFF);
                return;
            } else if (( i < 50) && (!lastMove((byte)4))){
                setMove((byte) 3, Intent.DEFEND);
                return;
            } else if ((i < 85) && (!lastMove((byte)5))){
                setMove((byte)5, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(2)).base);
                return;
            } else {
                setMove((byte) 4, Intent.ATTACK, ((DamageInfo) this.damage.get(1)).base);
                return;
            }
        }

    }

    public static class MoveBytes
    {
        public static final byte SPEW = 0;
        public static final byte VESSELCRY = 1;
        public static final byte VESSELHIT = 2;
        public static final byte REARUP = 3;
        public static final byte STOMP = 4;
        public static final byte TRAMPLE = 6;

    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.useFastShakeAnimation(2.0f);
            CardCrawlGame.screenShake.rumble(2.0f);
        } else {
            transform();
        }
    }







}