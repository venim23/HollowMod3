package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.powers.SoulVialPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class KingsSoulRelic extends CustomRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */
    private  static final int SoulUp = 2;
    // ID, images, text.
    public static final String ID = hollowMod.makeID("KingsSoulRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("kingsoul.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("void_idol.png"));
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public KingsSoulRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.CLINK);
    }

    // Gain 1 Strength on on equip.
    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(VesselMask.ID)) {
            instantObtain(AbstractDungeon.player, 0, false);
        } else if (AbstractDungeon.player.hasRelic(VoidIdolRelic.ID)) {
            instantObtain(AbstractDungeon.player, 0, false);
        } else  {
            super.obtain();
        }
    }

    public void atTurnStart()
    {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, SoulUp),SoulUp));
    }


    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulVialPower(AbstractDungeon.player, AbstractDungeon.player, SoulUp),SoulUp));

    }

    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}