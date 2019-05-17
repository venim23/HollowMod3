package HollowMod.monsters;

import HollowMod.hollowMod;
import HollowMod.powers.FlameCloakPower;
import HollowMod.powers.InfectionPower;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.*;

public class bossRadiance extends AbstractMonster {

    public static final String ID = hollowMod.makeID("Radiance");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;


    // ****** MOVE AND STAT VALUES ********//

    private int infectAmt = 5;
    private int bigbeamDmg = 50;
    private int swordsDmg = 8;
    private int swordsHits = 1;
    private int swordsTurns = 3;
    private int swordsCur = 0;
    private int wallDef = 20;
    private int wallPwr = 3;
    private int ballDmg = 9;
    private int ballHits = 2;
    private int ballGain = 0;
    private int wingDmg = 5;
    private int flyDmg = 15;
    private int flyVln = 2;
    private int turnssinceSR = 2;
    private int maxHP = 650;
    private int minHP = 600;
    private int numTurns;


    // ******* END OF MOVE AND STAT VALUES *********//


    //LIST THE ANIM NAMES HERE IN COMMENTS FOR EASY MEMORY//
    private String animIdle = "idle";
    private String animBeam1 = "glowingloop";
    private String animBeam2 = "bigbeam";
    private String animFlyup = "flyup";
    private String animFlydown = "flydown";
    private String animScream = "infectionscream";
    private String animBalls = "lightorbs";
    private String animWing = "wingattack";
    private String animWall = "swordwall";
    private String animRain1 = "swordrain1";
    private String animRain2 = "swordrain2";
    private String animRain3 = "swordrain3";


    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public bossRadiance()
    {
        //the stuff that gets sent up the line to AbstractMonster to do what it does
    /*here's what these refer to: (final String name, final String id, final int maxHealth, final float hb_x, final float hb_y, final float hb_w, final float hb_h, final String imgUrl,


	final float offsetX, final float offsetY,
	final boolean ignoreBlights: Not included as false by default?
	*/
        super(NAME, ID, 650, 0, 0, 300.0F, 570.0F, null, 0.0F, -30.0F);
        this.type = EnemyType.BOSS;

        if (AbstractDungeon.ascensionLevel >=9)
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 10;
            this.maxHP += 10;
            this.swordsTurns++;
        }
        if (AbstractDungeon.ascensionLevel >=4)
        {
            //increases the power of his multihit and debufff for higher ascensions
            this.swordsDmg += 2;
            //this.fbats += 1;

        }
        if (AbstractDungeon.ascensionLevel >= 19)
        {
            this.wingDmg += 10;
            //this.spikePwrVal += 1;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);



        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.flyDmg)); // attack 0 damage fly
        this.damage.add(new DamageInfo(this, this.swordsDmg)); // attack 1 damage swordwall
        this.damage.add(new DamageInfo(this, this.bigbeamDmg)); //attack 2 damagee beam
        this.damage.add(new DamageInfo(this, this.ballDmg)); //attack 3 damagee ball
        this.damage.add(new DamageInfo(this, this.wingDmg)); //attack 3 damagee ball
        // **** END ARRAYS **** // wing



        loadAnimation(
                //loads the animation
                hollowMod.makeMonsterPath("radiance/Radiance.atlas"),
                hollowMod.makeMonsterPath("radiance/Radiance.json"), 1.6F);
        //starts the animation called idle i think, im unsire of the 1st variable, but i think the second is the animation name, and the 3rd is a boolean for islooping?
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadMisc.getKey(),1.6F));
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("MINDBLOOM", true);
        AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 2.0f, 2.0f));

    }
    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    public void takeTurn()
    {

        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {
            case 0:  // flyup
                //FastAttack
                //sfxopen
                //delay
                //sfx cast
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[1], 2.0f, 2.1f));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FLY1"));
                //AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCape.getKey()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 1), 1));
                break;
            case 1: //flydown
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadFlyD.getKey()));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FLY2"));
                //AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new Effec, 0.2f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new VulnerablePower(p, this.flyVln, true), this.flyVln));
                break;
            case 2: //1block malleable power btw Sword Wall
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SWORDWALL"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadMisc.getKey()));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.wallDef));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this, this.wallPwr), this.wallPwr));
                //new regenpower?
                break;
            case 3: //Sword Rain
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SWORDRAIN"));
                for (int i = 0; i < this.swordsHits; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadSword.getKey()));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlyingDaggerEffect(this.hb.cX - 120.0F, this.hb.cY, 0, false), 0.2f));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.NONE,true));
                }
                break;
            case 4: //PRebeam
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHARGEBEAM"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadCharge.getKey()));
                break;
            case 5: //BigBeam
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[2], 2.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FIREBEAM"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadBeam.getKey()));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0f * Settings.scale), 1.0f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                break;
            case 6: //lightballs
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "LIGHTBALL"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 5));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 5));
                break;
            case 7: //infscream
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SCREAM"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadScream.getKey()));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new InfectionPower(p, this.infectAmt),this.infectAmt));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.5f));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this,this, 10));

                break;
            case 8: //wingattack
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "WING"));
                //AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(this, this.wingDmg), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
        }
        //unsure here, I think it basically  uses an action to send this monsters data to the AbstractMonster.rollMove , which activates the monsterFalseKnight.getMove and sends a rng amount?
        //this seems to basically be the "get the intent for the next turn's move thing"
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
            case "CHARGEBEAM":
                this.state.setAnimation(0, animBeam1, true);
                break;
            case "FIREBEAM":
                this.state.setAnimation(0, animBeam2, false);
                this.state.addAnimation(0, animIdle, true, 0.4F);
                break;
            case "FLY1":
                this.state.setAnimation(0, animFlyup, false);
                break;
            case "FLY2":
                this.state.setAnimation(0, animFlydown, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "SWORDRAIN":
                if (this.swordsHits > 3) {
                    this.state.setAnimation(0, animRain3, false);
                    this.state.addAnimation(0, animIdle, true, 0.0F);
                    break;
                } else if (this.swordsHits ==3){
                    this.state.setAnimation(0, animRain2, false);
                    this.state.addAnimation(0, animIdle, true, 0.0F);
                    break;
                } else {
                    this.state.setAnimation(0, animRain1, false);
                    this.state.addAnimation(0, animIdle, true, 0.0F);
                    break;
                }
            case "SWORDWALL":
                this.state.setAnimation(0, animWall, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "WING":
                this.state.setAnimation(0, animWing, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "LIGHTBALL":
                this.state.setAnimation(0, animBalls, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "SCREAM":
                this.state.setAnimation(0, animScream, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
        }
    }
    //Unsure, but I think this handles the event of Taking damage, not sure if it's needed or not.
    //basically works just like the change state attack, the oof animation plays once. then it sets the looping idle animation to play again afterwards.
    public void damage(DamageInfo info)
    {
        super.damage(info);
        //just checks to make sure the attack came from the plaer basically.
        if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS) && (info.output > 0)&& (!this.lastMove((byte)0)))
        {
            if (this.lastMoveBefore((byte)4)){
                    this.state.setAnimation(0, "oof", false);
                    this.state.addAnimation(0, animBeam1, true, 0.0F);
            } else {
                this.state.setAnimation(0, "oof", false);
                this.state.addAnimation(0, "idle", true, 0.0F);
            }
        }
    }
    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {
        this.numTurns++;
        this.turnssinceSR++;
        //First we handle the CONTINUE moves, that rely on the previous move before it. 1,5,3
        if (lastMove((byte)0)){
            setMove(MOVES[1],(byte)1, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base);
            return;
        } else if (lastMove((byte)4)){
            setMove(MOVES[5],(byte)5, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base);
            return;
        }
        if ((lastMove((byte)2)) || ((lastMove((byte)3)) && (this.swordsCur < swordsTurns))){
            this.swordsHits++;
            setMove((byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, this.swordsHits, true);
            this.swordsCur++;
            this.turnssinceSR = 0;
            return;
        } else if (((lastMove((byte)2)) || (lastMove((byte)3)) && this.swordsCur >= swordsTurns)){
            this.swordsCur = 0;
            this.swordsHits = 1;
            this.turnssinceSR = 0;
            if (this.hasPower(MalleablePower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, MalleablePower.POWER_ID));
            }
        }
        //Now we do the actual calcs for the weight of the remaingin 5 moves.
        if ((this.numTurns > 3) && ((this.currentHealth < (this.maxHealth / 5)) || ((i < 10) && (!lastMove((byte)7))))){
            setMove(MOVES[6],(byte) 7, Intent.STRONG_DEBUFF);
        } else if ((i < 35) && (turnssinceSR > 3)){
            setMove(MOVES[2],(byte)2, Intent.DEFEND_BUFF);
        } else if ((i < 50) && (!lastTwoMoves((byte)6))){
            setMove((byte)6, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(3)).base, this.ballHits, true);
        } else if (( i < 75 ) && (!this.lastMove((byte)1))){
            setMove((byte)0, Intent.MAGIC);
        } else if ((i < 95) && (!lastMove((byte)5)) && (!lastMoveBefore((byte)5))){
            setMove(MOVES[4],(byte)4, Intent.UNKNOWN);
        } else {
            setMove((byte)8, Intent.ATTACK, ((DamageInfo)this.damage.get(4)).base);
        }

    }

    public void die() {

        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.RadSong.getKey()));
        AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[3], 3.0f, 3.0f));
        this.state.setTimeScale(0.1f);
        this.useShakeAnimation(5.0f);
        super.die();
    }

    //Assigns byte values to the attack names. I can't find this directly called, maybe it's just put in the output for debugging
    public static class MoveBytes
    {
        public static final byte FLYUP = 0;//
        public static final byte FLYDOWN = 1;//
        public static final byte SWORDWALL = 2;//
        public static final byte SWORDRAIN = 3;//
        public static final byte PREBEAM= 4;//
        public static final byte BIGBEAM= 5;//
        public static final byte LIGHTBALLS= 6;//
        public static final byte SCREAM= 7;//
        public static final byte WING = 8;
    }





}
