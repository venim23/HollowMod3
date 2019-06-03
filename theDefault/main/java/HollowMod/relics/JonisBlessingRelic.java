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

public class JonisBlessingRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("JonisBlessing");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("jonis_blessing.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("jonis_blessing.png"));

    public static final int STARTING_BLUE = 15;

    public JonisBlessingRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(
                AbstractDungeon.player,AbstractDungeon.player, STARTING_BLUE));
    }


    @Override
    public int onPlayerHeal(int healAmount)
    {
        if ((AbstractDungeon.currMapNode != null) && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT))
        {
            flash();
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(
                    AbstractDungeon.player,AbstractDungeon.player, healAmount));
            return 0;
        }
        return healAmount;
    }

    // Description
    @Override

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
