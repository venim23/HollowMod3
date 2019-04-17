package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PublicKey;

import static HollowMod.hollowMod.makePowerPath;

public class InfectionPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public static final String POWER_ID = hollowMod.makeID("InfectionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Infection_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Infection_power32.png"));

    public InfectionPower(final AbstractPlayer owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;
        this.type = PowerType.DEBUFF;

        logger.info("spawn infection power loaded", amount);

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        logger.info("stack power loaded", stackAmount);
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, InfectionPower.POWER_ID));
        }
    }
    @Override
    public void atEndOfTurn(boolean isPlayer)
        { // At the end of your turn
        AbstractDungeon.actionManager.addToBottom(new DamageAction(owner, new DamageInfo(owner, this.amount)));
        //I'm hoping this makes the Broken Vessel power work better to deal damage to everyone whenever you take damage.
        if (owner.hasPower(BrokenVesselPower.POWER_ID)){
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if ((!monster.isDead) && (!monster.isDying)) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(monster, new DamageInfo(owner, this.amount, DamageInfo.DamageType.THORNS)));
                }
            }
        }
       //in theory this should deal damage (not lose hp but deal blockable damage) to the player at the end of his turn, and then decrement the value of infection by 1.
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, InfectionPower.POWER_ID, 2));

        //There might be a need to add some shit to check for specific powers or something at some point

    }

    @Override
    public void updateDescription()
    {
        this.description = (DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new InfectionPower(AbstractDungeon.player, amount);
    }
}
