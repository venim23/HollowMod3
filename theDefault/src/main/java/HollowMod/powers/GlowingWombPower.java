package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static HollowMod.hollowMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class GlowingWombPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = hollowMod.makeID("NewGlowingWombPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("glowingwomb_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("glowingwomb_power32.png"));
    //public static int ElderbugDrawInt = 1;
    public int INFECTEDVALUE;

    public GlowingWombPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        this.INFECTEDVALUE = 1;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        this.amount += stackAmount;
        this.INFECTEDVALUE += 1;

    }



    // On use card, apply (amount) of Dexterity. (Go to the actual power card for the amount.)
    @Override
    public void atStartOfTurnPostDraw() {

        int blockval = this.amount;
        if (this.owner.hasPower(DexterityPower.POWER_ID)){
            blockval += this.owner.getPower(DexterityPower.POWER_ID).amount;
        }
        if (this.owner.hasPower(InfectionPower.POWER_ID)){
            if (this.owner.getPower(InfectionPower.POWER_ID).amount >= this.INFECTEDVALUE){
                flash();
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, blockval));

                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(this.owner, this.owner, InfectionPower.POWER_ID, this.INFECTEDVALUE));
            }
        }

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
            this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.INFECTEDVALUE + DESCRIPTIONS[2]);
        }

    @Override
    public AbstractPower makeCopy() {
        return new GlowingWombPower(owner, source, amount);
    }
}
