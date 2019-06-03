package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class IsmasTearRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("IsmasTear");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ismastear.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ismastear.png"));


    public IsmasTearRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
