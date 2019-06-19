package HollowMod.monsters;

import HollowMod.actions.SFXVAction;
import HollowMod.hollowMod;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class monsterHuskSentry extends AbstractMonster {

    public static final String ID = hollowMod.makeID("HuskSentry");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    //Not sure what special encounter this happens for
    //public static final String SPECIAL_ENCOUNTER_ID = InfiniteSpire.createID("Three Voidlings");


    // ****** MOVE AND STAT VALUES ********//
    private int attSlashDamage = 10;
    private int attSlashWk = 1;
    private int attBravaDmg = 12;
    private int attBravaFrl = 2;
    private int swingDmg = 4;
    private int swingHits = 3;
    private int salutBuff = 2;
    private int maxHP = 75;
    private int minHP = 65;
    private int HPlastturn = 100;
    // ******* END OF MOVE AND STAT VALUES *********//

    //LIST THE ANIM NAMES HERE IN COMMENTS FOR EASY MEMORY//
    private String animIdle = "idle";
    private String animSpear = "brava";
    private String animFlying = "intangloop";
    private String animFly = "backstep";
    private String animSlashUp = "slash1";
    private String animSlashDown = "slash2";
    private String animBuff = "salute";

    public monsterHuskSentry()
    //Defines the offset of the model loaded i think?
    {
        this(0.0F);
    }
    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public monsterHuskSentry(float xOffset)
    {
        //the stuff that gets sent up the line to AbstractMonster to do what it does
    /*here's what these refer to: (final String name, final String id, final int maxHealth, final float hb_x, final float hb_y, final float hb_w, final float hb_h, final String imgUrl,


	final float offsetX, final float offsetY,
	final boolean ignoreBlights: Not included as false by default?
	*/
        super(monsterHuskSentry.NAME, ID, 75, 0.0F, 0.0F, 200.0F, 400.0F, null, xOffset, 50.0F);


        if (AbstractDungeon.ascensionLevel >= 7)
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 8;
            this.maxHP += 8;

        }
        if (AbstractDungeon.ascensionLevel >= 2)
        {
            //for Ascenction 3 and higher, adds a bit more damage
            this.attSlashDamage += 2;
            this.salutBuff += 1;
            this.swingDmg += 2;
        }

        if (AbstractDungeon.ascensionLevel >= 17)
        {
            //for Ascenction 3 and higher, adds a bit more damage
            this.salutBuff += 1;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.attBravaDmg)); // attack 0 damage
        this.damage.add(new DamageInfo(this, this.attSlashDamage)); //attack 1 damage
        this.damage.add(new DamageInfo(this, this.swingDmg));
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation
                hollowMod.makeMonsterPath("husksentry/HuskSentry.atlas"),
                hollowMod.makeMonsterPath("husksentry/HuskSentry.json"), 0.9F);
        //starts the animation called idle i think, im unsire of the 1st variable, but i think the second is the animation name, and the 3rd is a boolean for islooping?
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    public void takeTurn()
    {

        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {   //0 Swing- att
            case 0: //BRava
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BRAVA"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.SentryBrava.getKey(), 1.4F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,this, new FrailPower(p,attBravaFrl,true),attBravaFrl));
                break;
            // Defend
            case 1: //Intangible/fly
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FLY"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.SentryBuzz.getKey(), 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this,1)));
                break;
            case 2: //slash
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATT"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.SentryBuzz.getKey(), 3.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,this, new WeakPower(p,attSlashWk,true),attSlashWk));
                break;
            case 3: //salute
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SALUTE"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.SentryBuzz.getKey(),1.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new StrengthPower(p, this.salutBuff), this.salutBuff));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.salutBuff), this.salutBuff));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new VulnerablePower(p, this.salutBuff, true), this.salutBuff));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, this.salutBuff+1,true), this.salutBuff+1));
                break;
            case 4: //swings
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SWINGS"));
                AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.SentryBuzz.getKey(), 1.0F));
                for (int i = 0; i < this.swingHits; i++)
                {
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                break;
        }
        //unsure here, I think it basically  uses an action to send this monsters data to the AbstractMonster.rollMove , which activates the DefaultMonster.getMove and sends a rng amount?
        //this seems to basically be the "get the intent for the next turn's move thing"
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    //this is the method that the ChangeStateAction requires within the class of any monster thant calls it.
    public void changeState(String key)
    {
        switch (key)
        {
            //for each key, it has a simple little transition between animations,
            //for this example, sets the animation to attack, and not looping, then adds the looping idle animation as next in line.
            case "BRAVA":
                this.state.setAnimation(0, animSpear, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "FLY":
                this.state.setAnimation(0, animFly, false);
                this.state.addAnimation(0, animFlying, true, 0.0F);
                break;
            case "SWINGS":
                this.state.setAnimation(0, animSlashUp, false);
                this.state.addAnimation(0, animSlashDown, false, 0.0F);
                this.state.addAnimation(0, animSlashUp, false, 0.0F);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "ATT":
                this.state.setAnimation(0, animSlashUp, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case"SALUTE":
                this.state.setAnimation(0, animBuff, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
        }
    }
    //Unsure, but I think this handles the event of Taking damage, not sure if it's needed or not.
    //basically works just like the change state attack, the oof animation plays once. then it sets the looping idle animation to play again afterwards.
    public void damage(DamageInfo info)
    {
        super.damage(info);
        //just checks to make sure the attack came from the plaer basically.
        if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS) && (info.output > 0) && !this.hasPower(IntangiblePower.POWER_ID) )
        {
            this.state.setAnimation(0, "oof", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
        }
    }
    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {   /*
		  final String moveName, I think this is just for Debugger
		  final byte nextMove, thsi is what the case in takeTurn looks for
		  final Intent intent, This displays the intent until the next turn. DONT LIE TO PLAYERS YOU JERK
		  final int baseDamage, Blank goes a bit over the top here this is where he call DAMAGE INFO ARRAYS
		  (otpional)_final int multiplier, For stull like the birdy attacks of 2x5? that's this thing.
		  (Optional) final boolean isMultiDamage if the multiplier was there set this to true, by default it's false IIRC)
		  */
        if (this.lastMove((byte)1)){
            setMove((byte) 0, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(0)).base);
            this.HPlastturn = this.currentHealth;
            return;
        }
        if ((i < 20 || (!(this.HPlastturn >= this.currentHealth) && (i<50)))){
            setMove((byte)1, Intent.BUFF);
            this.HPlastturn = this.currentHealth;
            return;
        }
        this.HPlastturn = this.currentHealth;

        // so for this, it's a modified probability. it's a 30% chance (any roll less than 30) but it's also gauranteed if it's the first turn of the combat
        if ((i < 60) && (AbstractDungeon.getMonsters().monsters.size() == 1 )){
            setMove((byte) 3, Intent.MAGIC);

        } else if (i < 80) {
            setMove((byte)2, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            //so anything over 44 will be this i think?  so 65% generic attacks.
        } else {
            setMove((byte)4, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base, this.swingHits, true);
        }
    }

    public void die() {
        this.state.setTimeScale(0.1f);
        this.useShakeAnimation(5.0f);
        super.die();
    }

    //Assigns byte values to the attack names. I can't find this directly called, maybe it's just put in the output for debugging
    public static class MoveBytes
    {
        public static final byte BRAVA = 0;
        public static final byte FLY = 1;
        public static final byte SLASH = 2;
        public static final byte SALUTE = 3;
        public static final byte SWINGS = 4;
    }





}
