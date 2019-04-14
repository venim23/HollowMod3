package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static HollowMod.hollowMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class SiblingSoulPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = hollowMod.makeID("SiblingSoulPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("siblingsoul_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("siblingsoul_power32.png"));
    //public static int ElderbugDrawInt = 1;

    public SiblingSoulPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
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
    public void onUseCard(final AbstractCard c, final UseCardAction action) {
        if ((c.type == AbstractCard.CardType.ATTACK) || (c.type == AbstractCard.CardType.SKILL))  {
            flash();
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c.makeStatEquivalentCopy()));
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, SiblingSoulPower.POWER_ID));
        }
    }

    @Override
    public void updateDescription() {
            description = (DESCRIPTIONS[0]);
    }
    @Override
    public AbstractPower makeCopy() {
        return new SiblingSoulPower(owner, source, amount);
    }
}
