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

public class DelicateFlowerRelic extends CustomRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */

    // ID, images, text.
    public static final String ID = hollowMod.makeID("DelicateFlower");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("delicate_flower.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("delicate_flower.png"));
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public DelicateFlowerRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    // Gain 1 Strength on on equip.
    @Override
    public void atTurnStart() {

        if (DelicateFlowerRelic.this.isActive()) {
            DelicateFlowerRelic.this.flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, 999), 999));
        }
}

    private boolean isActive(){
        return AbstractDungeon.player.currentHealth >= AbstractDungeon.player.maxHealth;
    }
    // Description
    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}
