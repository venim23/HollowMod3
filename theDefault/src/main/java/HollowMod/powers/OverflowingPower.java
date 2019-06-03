package HollowMod.powers;

import HollowMod.cards.AbstractHollowCard;
import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static HollowMod.hollowMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class OverflowingPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = hollowMod.makeID("OverflowingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("kingbrand_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("kingbrand_power32.png"));

    public OverflowingPower(final AbstractCreature owner, final AbstractCreature source) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = 0;
        this.source = source;

        this.priority = 1;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    // On use card, apply (amount) of Dexterity. (Go to the actual power card for the amount.)
    @Override
    public void atStartOfTurnPostDraw() {
        flashWithoutSound();
        int SoulStock = 0;
        if (owner.hasPower(SoulPower.POWER_ID)){
            SoulStock = this.owner.getPower(SoulPower.POWER_ID).amount;
        }

        if ((SoulStock - this.amount) < 0){
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner,this.owner, OverflowingPower.POWER_ID));

            if (this.owner.hasPower("Strength")){
                    int StrAdded = 0;
                    int tempval = this.amount - 1;

                    for (int i = tempval; i > 0; i--) {
                        StrAdded += tempval;
                    }
                    StrAdded =- 1;
                    if (StrAdded <= owner.getPower("Strength").amount) {
                        AbstractDungeon.actionManager.addToBottom(
                                new ReducePowerAction(this.owner, this.owner, "Strength", StrAdded));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(
                                new RemoveSpecificPowerAction(this.owner, this.owner, "Strength"));
                    }
                }
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(this.owner, this.owner, SoulPower.POWER_ID, this.amount));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
        }
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flashWithoutSound();
        this.amount++;
        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
            this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]);
        }

    @Override
    public AbstractPower makeCopy() {
        return new OverflowingPower(owner, source);
    }
}
