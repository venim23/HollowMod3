package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.TextureLoader;
import basemod.helpers.CardTags;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.ReApplyPowersAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.*;


import java.util.UUID;

import static HollowMod.hollowMod.makePowerPath;

//Add X to the level of Void Power, Void will add vulnerability and

public class VoidPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = hollowMod.makeID("VoidPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("VoidGod_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("VoidGod_power32.png"));

    public VoidPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        if (owner.hasPower(SoulMasterPower.POWER_ID)) {
            this.amount = (amount + owner.getPower(SoulMasterPower.POWER_ID).amount);
        } else {
            this.amount = amount;
        }
        this.priority = 1;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, SoulPower.POWER_ID, owner.getPower(SoulPower.POWER_ID).amount));

        updateDescription();


    }

    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        if (owner.hasPower(SoulMasterPower.POWER_ID)) {
            this.amount += (stackAmount + owner.getPower(SoulMasterPower.POWER_ID).amount);
        } else {
            this.amount += stackAmount;
        }
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, VoidPower.POWER_ID));
        }
    }


    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if ((info.type != DamageInfo.DamageType.THORNS) && (info.owner != this.owner)) {
            flashWithoutSound();
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.owner, (new DamageInfo(info.owner, this.amount, DamageInfo.DamageType.THORNS)),true));
            //damageAmount += this.amount;
        }
        return damageAmount;
    }

    // Note: If you want to apply an effect when a power is being applied you have 3 options:
    //onInitialApplication is "When THIS power is first applied for the very first time only."
    //onApplyPower is "When the owner applies a power to something else (only used by Sadistic Nature)."
    //onReceivePowerPower from StSlib is "When any (including this) power is applied to the owner."


    // At the end of the turn, remove gained Dexterity.
    @Override
    public void atStartOfTurn() {

        if ( this.amount > 0) {
            flash(); // Makes the power icon flash.
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(owner, owner, VoidPower.POWER_ID, 1));
                updateDescription();
                // this should reduce the void power by 1 at the e3nd of each turn.
        }

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + (this.amount -1)  + DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new VoidPower(owner, amount);
    }
}
