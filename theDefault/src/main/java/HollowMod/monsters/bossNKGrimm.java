package HollowMod.monsters;

import HollowMod.hollowMod;
import HollowMod.powers.FlameCastPower;
import HollowMod.powers.FlameCloakPower;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockImpactLineEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

public class bossNKGrimm extends AbstractMonster {

    public static final String ID = hollowMod.makeID("NKGrimm");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    // ****** MOVE AND STAT VALUES ********//

    private int fbatDmg = 10;
    private int fbats = 2;
    private int lingerDefVal = 70;
    private int turnsTillLoop = 0;
    private int normalBlock = 12;
    private int normalRegen = 10;
    private int maxHP = 320;
    private int minHP = 310;
    private int spikeDefVal = 10;
    private int spikePwrVal = 1;
    private int tpDamage = 6;
    private int lingerBlockTurns = 3;
    private int flameVal = 5;
    private int fpillerDmg = 20;
    private int fpillarPwer = 3;
    private int numTurns;
    private int defVal = 0;
    private int defDmg = 0;

    // ******* END OF MOVE AND STAT VALUES *********//


    //LIST THE ANIM NAMES HERE IN COMMENTS FOR EASY MEMORY//
    private String animIdle = "idle";
    private String animDefLoop = "defenseloop";
    private String animFireBats = "cloakopen";
    private String animFireSumm = "bigsummon";
    private String animSummon = "summonspikes";
    private String animTeleport = "vanishattack";
    private String animFireBlock = "fierydefend";
    private String animReturn = "deftoidle";
    private String animDefCast = "defwhirl";


    // the Main "Constructer" iirc that's what the big method/function that the class relies upon is called.
    public bossNKGrimm()
    {
        //the stuff that gets sent up the line to AbstractMonster to do what it does
    /*here's what these refer to: (final String name, final String id, final int maxHealth, final float hb_x, final float hb_y, final float hb_w, final float hb_h, final String imgUrl,


	final float offsetX, final float offsetY,
	final boolean ignoreBlights: Not included as false by default?
	*/
        super(NAME, ID, 375, 0, 0, 300.0F, 475.0F, null, 0.0F, 0.0F);
        this.type = EnemyType.BOSS;

        if (AbstractDungeon.ascensionLevel >=9)
        {
            //For monsters encountered at higher ascension levels adds a bit more HP
            this.minHP += 10;
            this.maxHP += 10;
            this.turnsTillLoop -= 1;
        }
        if (AbstractDungeon.ascensionLevel >=4)
        {
            //increases the power of his multihit and debufff for higher ascensions
            this.fbatDmg += 3;
            this.fbats += 1;

        }
        if (AbstractDungeon.ascensionLevel >= 19)
        {
            this.lingerDefVal += 10;
            this.spikePwrVal += 1;
        }
        //set the min and max hp bounds,
        setHp(this.minHP, this.maxHP);
        //****** DAMAGE INFO ARRAYS? **** //
        //creates a list 0,1,2 of damageinfos to pull from for later.
        this.damage.add(new DamageInfo(this, this.fbatDmg)); // attack 0 damage
        this.damage.add(new DamageInfo(this, this.fpillerDmg)); // attack 1 damage
        this.damage.add(new DamageInfo(this, this.tpDamage)); //attack 2 damagee
        this.damage.add(new DamageInfo(this, this.defDmg)); //attack 3 damagee
        // **** END ARRAYS **** //
        loadAnimation(
                //loads the animation
                hollowMod.makeMonsterPath("nkgrimm/NKGrimm.atlas"),
                hollowMod.makeMonsterPath("nkgrimm/NKGrimm.json"), 1.0F);
        //starts the animation called idle i think, im unsire of the 1st variable, but i think the second is the animation name, and the 3rd is a boolean for islooping?
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        //no idea
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmDie.getKey()));
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("BOSS_GRIMM", true);
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 5.0f, 5.0f));

    }
    //take turn is actually one of the later things to occur, it happens AFTER the action has been decided and displayed for a turn as intent. deciding the move happens in GetMove
    public void takeTurn()
    {

        //Just a handy little shortener Blank seems to use to make writing the actions easier
        AbstractPlayer p = AbstractDungeon.player;
        //very simple, it checks what you've assinged as .nextMove's value. that happens in getMove
        switch (this.nextMove)
        {
            case 0:  // bats
                //FastAttack
                //sfxopen
                //delay
                //sfx cast
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 2.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BATS"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCape.getKey()));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
                for (int i = 0; i < this.fbats; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmFire.getKey()));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new FireballEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1f));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.NONE,true));
                    final Burn c = new Burn();
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, 1));
                }
                break;
            case 1: //Flames
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCast.getKey()));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FIRESUMMON"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ScreenOnFireEffect(), 0.2f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameCloakPower(this, this, fpillarPwer), fpillarPwer));
                break;
            case 2: //1block
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFEND"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.normalBlock));
                //new regenpower?
                break;
            case 3: //BlockMODE
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FIREBLOCK"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this, new FlameCastPower(this)));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.lingerDefVal));
                break;
            case 4: //TPPunch
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCast.getKey()));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "TPP"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new BlockImpactLineEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.2f));
                AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(p,this));
                //SFX Cast
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 5: //spikes
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmSpikes.getKey()));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SUMMON"));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.spikeDefVal));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, this.spikePwrVal),this.spikePwrVal));
                break;
            case 6: //DEFDAMAGE Make this a 2 turn thing
                defVal = this.currentBlock;

                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "RETURN"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCall.getKey()));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4f));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "FIRESUMMON"));
                AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(this, this, defVal));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.5f));
                if (this.hasPower(FlameCastPower.POWER_ID)){
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this,this,FlameCastPower.POWER_ID));
                }
                if (this.hasPower(FlameCloakPower.POWER_ID)){
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, FlameCloakPower.POWER_ID));
                }
                if (this.hasPower(BarricadePower.POWER_ID)){
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, BarricadePower.POWER_ID));
                }


                break;
            case 7: //DEFCAST
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[2], 2.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "DEFCAST"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCape.getKey()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameCloakPower(this, this,flameVal),flameVal));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.5f));

                break;
            case 8: //SHIELDBLAST
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "BATS"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmCape.getKey()));
                AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmFire.getKey()));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FireballEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 1.0f));
                //AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(this, defVal), AbstractGameAction.AttackEffect.FIRE));
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
            case "BATS":
                this.state.setAnimation(0, animFireBats, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "DEFEND":
                this.state.setAnimation(0, animFireBlock, false);
                this.state.addAnimation(0, animReturn, false,0.0F);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "FIREBLOCK":
                this.state.setAnimation(0, animFireBlock, false);
                this.state.addAnimation(0, animDefLoop, true, 0.0F);
                break;
            case "RETURN":
                this.state.setAnimation(0, animReturn, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "FIRESUMMON":
                this.state.setAnimation(0, animFireSumm, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "SUMMON":
                this.state.setAnimation(0, animSummon, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "TPP":
                this.state.setAnimation(0, animTeleport, false);
                this.state.addAnimation(0, animIdle, true, 0.0F);
                break;
            case "DEFCAST":
                this.state.setAnimation(0, animDefCast, false);
                this.state.addAnimation(0, animDefLoop, true, 0.0F);
                break;
        }
    }
    //Unsure, but I think this handles the event of Taking damage, not sure if it's needed or not.
    //basically works just like the change state attack, the oof animation plays once. then it sets the looping idle animation to play again afterwards.
    public void damage(DamageInfo info)
    {
        super.damage(info);
        //just checks to make sure the attack came from the plaer basically.
        if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS) && (info.output > 0) && (!this.hasPower(BarricadePower.POWER_ID)))
        {
            this.state.setAnimation(0, "oof", false);
            this.state.addAnimation(0, "idle", true, 0.0F);
        }
    }
    //This is where the monster recieves a roll between 0 and 99 (so a full 1/100 chances is easily done) the getMove method uses that number to determine probability of assigning a specific action
    //
    protected void getMove(int i)
    {
        this.numTurns++;
        if ((this.numTurns == 4) || (turnsTillLoop >= 6)){
            setMove(MOVES[2],(byte) 3, Intent.DEFEND_BUFF);
            turnsTillLoop = 0;
            return;
        } else {
            turnsTillLoop++;
        }
        if (this.lastMove((byte) 3)) {
            setMove(MOVES[6],(byte)7, Intent.BUFF);
            return;
        }
        if (this.lastMove((byte) 7)) {
            setMove(MOVES[7],(byte)6, Intent.MAGIC);
            return;
        }
        if (this.lastMove((byte) 6)) {
            setMove(MOVES[8],(byte)8, Intent.ATTACK, this.defVal);
            return;
        }

        if ((i <10) && (!lastMove((byte)2))){
            setMove((byte) 2, Intent.DEFEND_BUFF);
        } else if ((i < 25) || ((this.numTurns > 4) && (i<50))) {
            if (!lastMove((byte)0)){
                setMove((byte)0, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(0)).base, this.fbats, true);
            } else
            setMove(MOVES[4],(byte) 4, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(2)).base);
        } else if (i < 65) {
            if (!lastMove((byte)1)){
                setMove((byte)1, Intent.ATTACK_BUFF, ((DamageInfo)this.damage.get(1)).base);
            } else {
                setMove(MOVES[4],(byte) 4, Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(2)).base);
            }
                //so anything over 44 will be this i think?  so 65% generic attacks.
        } else if ((i < 85) && ((!this.lastMove((byte)5)) && (!this.lastMoveBefore((byte)5)))){
            setMove((byte)5, Intent.DEFEND_BUFF);
        } else {
            setMove(MOVES[4],(byte)4 , Intent.ATTACK_DEBUFF, ((DamageInfo) this.damage.get(2)).base);
        }
    }

    public void die() {
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3], 4.0f, 4.0f));
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.GrimmDie.getKey()));
        this.state.setTimeScale(0.1f);
        this.useShakeAnimation(5.0f);
        super.die();
        this.onBossVictoryLogic();
    }

    //Assigns byte values to the attack names. I can't find this directly called, maybe it's just put in the output for debugging
    public static class MoveBytes
    {
        public static final byte FIREBATS = 0;//
        public static final byte SUMMONFLAMES = 1;
        public static final byte DEFEND = 2;
        public static final byte FIREBLOCK = 3;//
        public static final byte TELEPORT= 4;
        public static final byte SPIKES= 5;
        public static final byte DEFDRAIN= 6;//
        public static final byte DEFCAST= 7;//
        public static final byte SHIELDBALL = 8;//
    }





}
