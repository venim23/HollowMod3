package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.powers.SoulPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class MillibellesNoteRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("MillibellesNoteRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("millibellesnote.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("millibellesnote.png"));

    public MillibellesNoteRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, 2), 2));
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
