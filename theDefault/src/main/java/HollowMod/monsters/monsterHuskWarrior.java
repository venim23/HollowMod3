package HollowMod.monsters;

import HollowMod.hollowMod;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class monsterHuskWarrior extends AbstractMonster {

    public static final String ID = hollowMod.makeID("HuskWarrior");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    //Not sure what special encounter this happens for
    //public static final String SPECIAL_ENCOUNTER_ID = InfiniteSpire.createID("Three Voidlings");


    // ****** MOVE AND STAT VALUES ********//
    private int attdefAtt = 6;
    private int attdefDef = 6;
    private int flurryDamage = 4;
    private int flurryHits = 2;
    private int chargeBuff = 3;
    private int shieldVal= 8;
    private int maxHP = 45;
    private int minHP = 35;
    // ******* END OF MOVE AND STAT VALUES *********//


    public monsterHuskWarrior()
    //Defines the offset of the model loaded i think?
    {
        this(0.0F);
    }
    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public monsterHuskWarrior(float xOffset)
    {
        //the stuff that gets sent up the line to AbstractMonster to do what it does
    /*here's what these refer to: (final String name, final String id, final int maxHealth, final float hb_x, final float hb_y, final float hb_w, final float hb_h, final String imgUrl,


	final float offsetX, final float offsetY,
	final boolean ignoreBlights: Not included as false by default?
	*/
        super(monsterHuskWarrior.NAME, ID, 32, 0.0F, 0.0F, 200.0F, 300.0F, null, xOffset, 0.0F);


        if (AbstractDungeon.ascensionLevel > 7)
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 2;
            this.maxHP += 2;
            this.shieldVal += 2;

        }
        if (AbstractDungeon.ascensionLevel > 2)
        {
            //for Ascenction 3 and higher, adds a bit more damage
            this.attdefAtt += 1;
            this.attdefDef += 1;
            this.flurryDamage += 1;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.attdefAtt)); // attack 0 damage
        this.damage.add(new DamageInfo(this, this.flurryDamage)); //attack 1 damage
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation
                hollowMod.makeMonsterPath("huskwarrior/HuskWarrior.atlas"),
                hollowMod.makeMonsterPath("huskwarrior/HuskWarrior.json"), 0.6F);
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
            case 0: //BLockstrike
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATT_DEF"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.attdefDef));
                break;
            // Defend
            case 1: //calls the assigned block value
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFEND"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.shieldVal));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this,2),2));
                break;
            case 2: //Flurry
                for (int i = 0; i < this.flurryHits; i++)
                {
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                break;
            case 3: //charge

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.chargeBuff), this.chargeBuff));
                this.flurryDamage++;

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
            case "ATTACK":
                this.state.setAnimation(0, "attack", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case "DEFEND":
                this.state.setAnimation(0, "defend", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case"ATT_DEF":
                this.state.setAnimation(0, "attack", false);
                this.state.addAnimation(0, "defend", false, 0.4f);
                this.state.addAnimation(0, "idle", true, 0.6F);
        }
    }
    //Unsure, but I think this handles the event of Taking damage, not sure if it's needed or not.
    //basically works just like the change state attack, the oof animation plays once. then it sets the looping idle animation to play again afterwards.
    public void damage(DamageInfo info)
    {
        super.damage(info);
        //just checks to make sure the attack came from the plaer basically.
        if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS) && (info.output > 0))
        {
            this.state.setAnimation(0, "oof", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
        }
    }
    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {
        // so for this, it's a modified probability. it's a 30% chance (any roll less than 30) but it's also gauranteed if it's the first turn of the combat
        if (i < 30) {
            setMove((byte) 0, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo) this.damage.get(0)).base);
                        //So here's what setmove looks for:
		  /*
		  final String moveName, I think this is just for Debugger
		  final byte nextMove, thsi is what the case in takeTurn looks for
		  final Intent intent, This displays the intent until the next turn. DONT LIE TO PLAYERS YOU JERK
		  final int baseDamage, Blank goes a bit over the top here this is where he call DAMAGE INFO ARRAYS
		  (otpional)_final int multiplier, For stull like the birdy attacks of 2x5? that's this thing.
		  (Optional) final boolean isMultiDamage if the multiplier was there set this to true, by default it's false IIRC)
		  */
        } else if (i < 60) {
            setMove((byte)1, Intent.DEFEND_BUFF);
            //so anything over 44 will be this i think?  so 65% generic attacks.
        } else if (i < 85){
            setMove((byte)2, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, this.flurryHits, true);
        } else if ((i >= 85) && (!this.lastMove((byte)3))){
            setMove((byte)3, AbstractMonster.Intent.BUFF);
        } else {
            setMove((byte)2, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, this.flurryHits, true);
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
        public static final byte BLOCKSTRIKE = 0;
        public static final byte DEFEND = 1;
        public static final byte FLURRY = 2;
        public static final byte CHARGE = 3;
    }





}
