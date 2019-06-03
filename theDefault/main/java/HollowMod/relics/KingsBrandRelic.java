package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class KingsBrandRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("KingsBrand");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("kings_brand.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("kings_brand.png"));


    public KingsBrandRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
