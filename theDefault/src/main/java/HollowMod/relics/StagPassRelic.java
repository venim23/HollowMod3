package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.powers.SoulPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class StagPassRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("StagPassRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("trampass_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("trampass_relic.png"));

    public StagPassRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }
    boolean canwork = false;
    // Flash at the start of Battle.
    @Override
    public void onEnterRoom(final AbstractRoom room) {
        if (room instanceof ShopRoom) {
            this.canwork = true;
        }
        else {
            this.canwork = false;
        }
    }

    @Override
    public void onObtainCard(final AbstractCard c) {
        if (this.canwork){
            if ((c.canUpgrade()) && (!c.upgraded)){
                c.upgrade();
            }
        }


    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
