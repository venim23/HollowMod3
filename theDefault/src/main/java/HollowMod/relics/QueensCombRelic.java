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

public class QueensCombRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("QueensComb");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("queens_comb.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("queens_comb.png"));

    public static final int STARTING_BLUE = 15;

    public QueensCombRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(
                AbstractDungeon.player,AbstractDungeon.player, STARTING_BLUE));
    }
    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    // Lose 1 energy on unequip.
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public void atTurnStart(){
    }

    // Description
    @Override

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
