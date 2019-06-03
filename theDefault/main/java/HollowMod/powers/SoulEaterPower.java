package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static HollowMod.hollowMod.makePowerPath;

//Add X to the level of Void Power, Void will add vulnerability and

public class SoulEaterPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = hollowMod.makeID("SoulEaterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("souleater_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("souleater_power32.png"));

    public SoulEaterPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        this.amount += stackAmount;
    }


    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if ((card.type == AbstractCard.CardType.ATTACK) && (!card.hasTag(CardTagEnum.SPELL)) && (!card.hasTag(CardTagEnum.ALLY)) && (!AbstractDungeon.player.hasPower(VoidPower.POWER_ID))) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, this.amount), this.amount));
        }
        //this should generate 1 soul on every attack that deals damage to an opponent, as long as the player doesn't have the Void Power.
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoulEaterPower(owner, amount);
    }
}
