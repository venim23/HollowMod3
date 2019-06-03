package HollowMod.monsters;

import HollowMod.hollowMod;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.*;

public class monsterLittleWeaver extends AbstractMonster {

    public static final String ID = hollowMod.makeID("LittleWeaver");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    //Not sure what special encounter this happens for
    //public static final String SPECIAL_ENCOUNTER_ID = InfiniteSpire.createID("Three Voidlings");


    // ****** MOVE AND STAT VALUES ********//
    private int clawDmg = 3;
    private int clawHits = 3;
    private int biteDmg = 9;
    private int bitevulnAmt = 2;
    private int webweakAmt = 1;
    private int shivDazedAmt = 2;
    private int maxHP = 28;
    private int minHP = 22;
    // ******* END OF MOVE AND STAT VALUES *********//
    // ******* END OF MOVE AND STAT VALUES *********//

    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public monsterLittleWeaver(final float x, final float y)
    {
        //the stuff that gets sent up the line to AbstractMonster to do what it does
    /*here's what these refer to: (final String name, final String id, final int maxHealth, final float hb_x, final float hb_y, final float hb_w, final float hb_h, final String imgUrl,


	final float offsetX, final float offsetY,
	final boolean ignoreBlights: Not included as false by default?
	*/
        super(monsterLittleWeaver.NAME, ID, AbstractDungeon.monsterHpRng.random(22, 28), 0.0F, 0.0F, 150.0F, 175.0F, null, x, y);


        if (AbstractDungeon.ascensionLevel > 2)
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 2;
            this.maxHP += 2;

        }
        if (AbstractDungeon.ascensionLevel > 7)
        {
            //for Ascenction 3 and higher, adds a bit more damage
            this.clawDmg += 2;
            this.biteDmg+= 2;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.clawDmg)); // attack 0 damage
        this.damage.add(new DamageInfo(this, this.biteDmg)); //attack 1 damage
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation
                hollowMod.makeMonsterPath("littleweaver/LittleWeaver.atlas"),
                hollowMod.makeMonsterPath("littleweaver/LittleWeaver.json"), 1.0F);
        //starts the animation called idle i think, im unsire of the 1st variable, but i think the second is the animation name, and the 3rd is a boolean for islooping?
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
    }

   /* public static class MoveBytes
    {
        public static final byte FRENZY = 0;
        public static final byte BITE = 1;
        public static final byte WEBBING = 2;
        public static final byte VIBRATE = 3;
    }
    */
    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    public void takeTurn()
    {

        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {   //0 Swing- att
            case 0: //BLockstrike
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CLAWS"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.WeaverScream.getKey()));
                for (int i = 0; i < this.clawHits; i++)
                {
                   //new SFX? action
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ScrapeEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale), 0.2f));



                }
                break;
            // Defend
            case 1: //calls the assigned block value
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BITE"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale), 0.5f));

                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player,1, true),1));
                break;
            case 2: //Webbing
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEBUFF"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WebEffect(AbstractDungeon.player, this.hb.cX - 70.0f * Settings.scale, this.hb.cY + 10.0f * Settings.scale)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, webweakAmt, true), webweakAmt));

                break;
            case 3: //Shiver
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEBUFF"));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.shivDazedAmt));
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
            case "CLAWS":
                this.state.setAnimation(0, "strike", false);
                this.state.addAnimation(0, "strike", false, 0.5F);
                this.state.addAnimation(0, "strike", false, 0.5F);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case "BITE":
                this.state.setAnimation(0, "bite", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case"DEBUFF":
                this.state.setAnimation(0, "shiver", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
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
         if (i < 33) {
            if (lastMove((byte)0)){
                setMove((byte)3, Intent.MAGIC);
            } else {
                setMove((byte)0, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, this.clawHits, true);
            }
            //so anything over 44 will be this i think?  so 65% generic attacks.
        } else if (i < 66){
             if (!lastMove((byte)2)){
                 setMove((byte)2, Intent.DEBUFF);
             } else {
                 setMove((byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
             }
        } else if (i >= 66) {
             if (!lastMove((byte) 1)) {
                 setMove((byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
             } else {
                 setMove((byte) 2, Intent.DEBUFF);
             }
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
        public static final byte FRENZY = 0;
        public static final byte BITE = 1;
        public static final byte WEBBING = 2;
        public static final byte VIBRATE = 3;
    }





}
