package HollowMod.relics;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import basemod.animations.AbstractAnimation;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class VoidIdolRelic extends CustomRelic implements OnReceivePowerRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */
    private  static final int debuffer = (-1);
    // ID, images, text.
    public static final String ID = hollowMod.makeID("VoidIdolRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("void_idol.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("void_idol.png"));
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());
    public static AbstractAnimation Shadeanim;

    public VoidIdolRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    // Gain 1 Strength on on equip.
    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(VesselMask.ID)) {
            instantObtain(AbstractDungeon.player, 0, false);
        } else if (AbstractDungeon.player.hasRelic(KingsSoulRelic.ID)) {
            instantObtain(AbstractDungeon.player, 0, false);
        } else  {
            super.obtain();
        }

        if (AbstractDungeon.player.hasRelic(VesselMask.ID)){
            AbstractDungeon.player.relics.remove(0);
        }
        if (AbstractDungeon.player.hasRelic(KingsSoulRelic.ID)){
            AbstractDungeon.player.relics.remove(0);
        }
    }

        //AbstractDungeon.player.
        //updateAnimation();

    public void onUseCard(AbstractCard card, UseCardAction action)
    {

        if ((card.hasTag(CardTagEnum.VOID)) && (card.exhaust)) {
            flash();
            action.exhaustCard = false;
        }
    }


    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature source, int stackAmount) {
        if (power.ID.equals(SoulPower.POWER_ID)) {
            return 0;
        } else {
            return stackAmount;
        }
    }

    public boolean onReceivePower(AbstractPower power, AbstractCreature source)
    {
        if (power.ID.equals(SoulPower.POWER_ID)){
            return false;
        } else {
            return true;
        }
    }

    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, debuffer),debuffer));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, debuffer),debuffer));

    }

    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}
