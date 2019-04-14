package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class NailsmithPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = hollowMod.makeID("NailsmithPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int VOID_LEVEL = 0;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Nailsmith_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Nailsmith_power32.png"));

    public NailsmithPower(final AbstractCreature owner, final int amount) {
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


    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if ((card.canUpgrade()) && (card.type.equals(AbstractCard.CardType.ATTACK))) {
            card.upgrade();
            card.superFlash();
            //The goal here is to make any attack cards automatically upgrade in the discard pile if they are used this round.
        }
    }

    // At the end of the turn, remove gained Dexterity.
    @Override
    public void atEndOfTurn(final boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(owner, owner, NailsmithPower.POWER_ID));
                //Hopefully this Gaurantees that the power only lasts one turn.
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new NailsmithPower(owner, amount);
    }
}
