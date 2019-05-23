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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import com.megacrit.cardcrawl.vfx.combat.WebEffect;

public class eliteStalkingDevout extends AbstractMonster {

    public static final String ID = hollowMod.makeID("StalkingDevout");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;


    public AbstractMonster[] weavers;


    // ****** MOVE AND STAT VALUES ********//
    private int recoilBlock = 25 ;
    private int bigHeal = 10 ;
    private int bigBlock = 30 ;
    private int shiverBlock = 15;
    private int slashDamage = 11;
    private int slashHits = 3;
    private int slashTimer = 0;
    private int slashMax = 4;
    private int outrageStr= 2;
    private int maxHP = 180;
    private int minHP = 160;
    private int numTurns;
    // ******* END OF MOVE AND STAT VALUES *********//


    //LIST THE ANIM NAMES HERE IN COMMENTS FOR EASY MEMORY//
    private String animIdle = "idle";
    private String animShake = "shiver";
    private String animDef = "recoil";
    private String animSpec2 = "spawn";
    private String animSpec = "slashes";

    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public eliteStalkingDevout(float xOffset)
    {
        //the stuff that gets sent up the line to AbstractMonster to do what it does
    /*here's what these refer to: (final String name, final String id, final int maxHealth, final float hb_x, final float hb_y, final float hb_w, final float hb_h, final String imgUrl,


	final float offsetX, final float offsetY,
	final boolean ignoreBlights: Not included as false by default?
	*/
        super(NAME, ID, 180, 50, 0, 400.0F, 350.0F, null, xOffset, 0.0F);
        this.type = EnemyType.ELITE;
        this.weavers = new AbstractMonster[2];

        if (AbstractDungeon.ascensionLevel >= 8)
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 10;
            this.maxHP += 10;

        }
        if (AbstractDungeon.ascensionLevel >=3)
        {
            //increases the power of his multihit and debufff for higher ascensions
            this.slashDamage+= 2;
            this.recoilBlock += 2;
        }
        if (AbstractDungeon.ascensionLevel >= 18)
        {
            this.slashMax -=1;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.slashDamage)); // attack 0 damage
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation
                hollowMod.makeMonsterPath("stalkingdevout/StalkingDevout.atlas"),
                hollowMod.makeMonsterPath("stalkingdevout/StalkingDevout.json"), 0.8F);
        //starts the animation called idle i think, im unsire of the 1st variable, but i think the second is the animation name, and the 3rd is a boolean for islooping?
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    //Assigns byte values to the attack names. I can't find this directly called, maybe it's just put in the output for debugging

    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    public void takeTurn()
    {

        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {   //0 Swing- att
            case 0: //spawn
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SPAWN"));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction((new monsterLittleWeaver(-500.0F, -10.0F)),true));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction((new monsterLittleWeaver(-250.0F, 10.0F)),true));
                if (this.hasPower(StrengthPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, StrengthPower.POWER_ID, this.getPower(StrengthPower.POWER_ID).amount));
                }
                break;
            // Defend
            case 1: //RECOIL
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFEND"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.recoilBlock));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BlurPower(this,2),2));
                break;
            case 2: //slashes
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SLASH"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.DevoutOpen.getKey()));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5f));
                for (int i = 0; i < this.slashHits; i++)
                {
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
                    AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.DevoutSlash.getKey()));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));

                }
                break;
            case 3: //outrage
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BUFF"));
                for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m.isDying) {
                        continue;
                    }
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.outrageStr), this.outrageStr));
                }
                break;
            case 4: // Recover
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFEND"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.bigBlock));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BUFF"));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.bigHeal));
                break;
            case 5: // Harden
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFEND"));
                for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m.isDying) {
                        continue;
                    }
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m,this,this.shiverBlock));
                }
                break;
        }
        //unsure here, I think it basically  uses an action to send this monsters data to the AbstractMonster.rollMove , which activates the DefaultMonster.getMove and sends a rng amount?
        //this seems to basically be the "get the intent for the next turn's move thing"
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void usePreBattleAction() {
        this.weavers[0] = AbstractDungeon.getMonsters().monsters.get(0);
        this.weavers[1] = AbstractDungeon.getMonsters().monsters.get(1);
        for (final AbstractMonster m : this.weavers) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MinionPower(this)));
        }
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
            case "SLASH":
                this.state.setAnimation(0, "slashes", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case "DEFEND":
                this.state.setAnimation(0, "skitter", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case "SPAWN":
                this.state.setAnimation(0, "spawn", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
                break;
            case "BUFF":
                this.state.setAnimation(0, "shiver", false);
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

    private int numAliveWeavers() {
        int count = 0;
        for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && m != this && !m.isDying) {
                ++count;
            }
        }
        return count;
    }
    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {
        if (this.slashTimer >= this.slashMax) {
            setMove((byte) 2, Intent.ATTACK, ((DamageInfo) this.damage.get(0)).base, this.slashHits, true);
            slashTimer = 0;
            return;
        } else { slashTimer++; }
        if (this.numAliveWeavers() < 1){
            setMove((byte) 0, Intent.UNKNOWN);
            return;
        }
        if (i < 25) {
            if (!lastMove((byte) 1)) {
                setMove((byte) 1, Intent.DEFEND_BUFF);
            } else {
                setMove((byte) 5, Intent.DEFEND_BUFF);
            }
        } else if (i < 50) {
            if (!lastMove((byte) 5)) {
                setMove((byte) 5, Intent.DEFEND_BUFF);
            } else {
                setMove((byte) 1, Intent.DEFEND_BUFF);
            }
        } else if (i < 80){
            setMove((byte)3, Intent.BUFF);
        } else if (i >= 80) {
            setMove((byte) 4, Intent.MAGIC);
        }
    }

    public static class MoveBytes
    {
        public static final byte LONELYCALL = 0;//
        public static final byte RECOIL = 1;//
        public static final byte SLASHES = 2;//
        public static final byte SADOUTRAGE = 3;
        public static final byte RECOVER = 4;
        public static final byte HARDEN = 5;//
    }

    public void die() {
        this.state.setTimeScale(0.1f);
        this.useShakeAnimation(5.0f);
        super.die();
        for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }
    }







}
