package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static HollowMod.hollowMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class GrubsongPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = hollowMod.makeID("GrubsongPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("grubsong_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("grubsong_power32.png"));
    private int amount2;
    //public static int ElderbugDrawInt = 1;

    public GrubsongPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        amount2 = this.amount;
        this.source = source;


        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }




    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
    }
    @Override
    public int onAttacked(DamageInfo info, int damageAmount){
        if (amount2 > 0){
            if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS)) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SoulPower(this.owner, 1), 1));
            }
            amount2--;
        }
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer){
        amount2 = this.amount;
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = (DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]);
        } else {
            description = (DESCRIPTIONS[0] + amount + DESCRIPTIONS[2]);
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new GrubsongPower(owner, source, amount);
    }
}
