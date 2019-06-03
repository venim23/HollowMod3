package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static HollowMod.hollowMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class GrimmsGiftPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = hollowMod.makeID("GrimmGiftPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("GrimmGift_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("GrimmGift_power32.png"));
    //public static int ElderbugDrawInt = 1;

    public GrimmsGiftPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;


        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    // On use card, apply (amount) of Dexterity. (Go to the actual power card for the amount.)
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        if ((target != this.owner) && (info.type != DamageInfo.DamageType.THORNS))
        {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, this.owner, new VulnerablePower(target, this.amount, false), this.amount, true));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer){
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, GrimmsGiftPower.POWER_ID));
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
            description = (DESCRIPTIONS[0]);
        }

    @Override
    public AbstractPower makeCopy() {
        return new GrimmsGiftPower(owner, source, amount);
    }
}
