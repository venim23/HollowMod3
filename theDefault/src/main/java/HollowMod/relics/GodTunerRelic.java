package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.powers.SoulPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class GodTunerRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("GodTunerRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("godtuner.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("godtuner.png"));

    public GodTunerRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }


    @Override
    public void onEquip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        ++energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        final EnergyManager energy = AbstractDungeon.player.energy;
        --energy.energyMaster;
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
