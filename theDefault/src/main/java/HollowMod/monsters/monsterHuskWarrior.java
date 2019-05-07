package HollowMod.monsters;

import HollowMod.hollowMod;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class monsterHuskWarrior extends AbstractMonster {

    private static final String NAME = "HuskWarrior";
    public static final String ID = hollowMod.makeID(NAME);

    //Not sure what special encounter this happens for
    //public static final String SPECIAL_ENCOUNTER_ID = InfiniteSpire.createID("Three Voidlings");


    // ****** MOVE AND STAT VALUES ********//
    private int bigattackDamage = 15;
    private int attackdebuffDamage = 5;
    private int attackdebuffValue = 1;
    private int bigdebuffValue = 5;
    private int blockAmt = 5;
    private int maxHP = 55;
    private int minHP = 45;
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

        }
        if (AbstractDungeon.ascensionLevel > 2)
        {
            //for Ascenction 3 and higher, adds a bit more damage
            this.bigattackDamage += 1;
            this.bigdebuffValue += 1;
            this.attackdebuffDamage += 1;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.bigattackDamage)); // attack 0 damage
        this.damage.add(new DamageInfo(this, 1)); //attack 1 damage
        this.damage.add(new DamageInfo(this, this.attackdebuffDamage)); // attack 2 damage
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
        {
            case 0:  // calls attack 0 damage stored damage info, alternatively you could created the damage info inside the case too
                //this is all pretty generit AbstractDungeion.actionManager stuff for adding new actions to the bottom for the monster to perform
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 1: //calls the assigned block value
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFEND"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt));
                break;
            case 2: //calls attack 2 damage info
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new StrengthPower( this, this.bigdebuffValue), this.bigdebuffValue));
                break;
            case 3: //calls attack 1 damage info
                for (int i = 0; i < 2; i++)
                {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new StrengthPower(this, this.attackdebuffValue), this.attackdebuffValue));
                }
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
        if ((i < 30) || (GameActionManager.turn == 0))
        {
            //this is within that 30%chance, it makes sure it doesn't repeat that move again.
            if (!lastMove((byte)2)) {
                setMove((byte)2, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
                //This lets the big debuff follow the attack/debuff if the ascension level is 17 or higher
            } else if (AbstractDungeon.ascensionLevel >= 17) {
                //So here's what setmove looks for:
		  /*
		  final String moveName, I think this is just for Debugger
		  final byte nextMove, thsi is what the case in takeTurn looks for
		  final Intent intent, This displays the intent until the next turn. DONT LIE TO PLAYERS YOU JERK
		  final int baseDamage, Blank goes a bit over the top here this is where he call DAMAGE INFO ARRAYS
		  (otpional)_final int multiplier, For stull like the birdy attacks of 2x5? that's this thing.
		  (Optional) final boolean isMultiDamage if the multiplier was there set this to true, by default it's false IIRC)
		  */
                setMove((byte)3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(2)).base, 2, true);
            } else {
                setMove((byte)0, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
        }
        //I think this is just a catch for between 30 and 45 so 15% chance? since it's an else if that overlaps? it's a weird way to do that blank.
        else if (i < 45) {
            setMove((byte)1, AbstractMonster.Intent.DEFEND);
            //so anything over 44 will be this i think?  so 65% generic attacks.
        } else {
            setMove((byte)0, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
    }
    //Assigns byte values to the attack names. I can't find this directly called, maybe it's just put in the output for debugging
    public static class MoveBytes
    {
        public static final byte BLOCKSTRIKE = 0;
        public static final byte DEFEND = 1;
        public static final byte FLURRY = 2;
        public static final byte CHARGEUP = 3;
        public static final byte SWING = 4;
    }





}
