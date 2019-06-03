package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.powers.VoidPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class GreatKnightMask extends CustomRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */

    // ID, images, text.
    public static final String ID = hollowMod.makeID("GreatKnightMask");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("trueheromask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("trueheromask.png"));
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public GreatKnightMask() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    // Gain 1 Strength on on equip.
    // Description
    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}
