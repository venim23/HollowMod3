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
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.DamageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class VesselMask extends CustomRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */

    // ID, images, text.
    public static final String ID = hollowMod.makeID("VesselMask");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("mask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("mask.png"));
    public static int nailGain = 1;
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public VesselMask() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }


    // Gain 1 Strength on on equip.
    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if ((card.type == AbstractCard.CardType.ATTACK) && (!card.hasTag(CardTagEnum.SPELL)) && (!card.hasTag(CardTagEnum.ALLY)) && (!AbstractDungeon.player.hasPower(VoidPower.POWER_ID))) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, nailGain), nailGain));
        }
        //this should generate 1 soul on every attack that deals damage to an opponent, as long as the player doesn't have the Void Power.
    }
    // Description
    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}
