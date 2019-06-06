package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class QueensCombRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("QueensCombRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("queens_comb.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("queens_comb.png"));
    private static int HEALVAL = 4;

    public QueensCombRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    // Flash at the start of Battle.
    @Override
    public void onEquip() {

        AbstractDungeon.player.maxHealth = (AbstractDungeon.player.maxHealth/2);
        if (AbstractDungeon.player.currentHealth>AbstractDungeon.player.maxHealth){
            AbstractDungeon.player.currentHealth = AbstractDungeon.player.maxHealth;
        }
    }

    @Override
    public void atTurnStart(){
        if (AbstractDungeon.player.damagedThisCombat <=0){
            flash();
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player,AbstractDungeon.player,HEALVAL));
        } else {
            this.stopPulse(); // Pulse while the player can click on it.
        }
    }
    @Override
    public void atPreBattle() {
        beginLongPulse();     // Pulse while the player can click on it.
    }

    @Override
    public void onVictory() {
        stopPulse(); // Don't keep pulsing past the victory screen/outside of combat.
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + HEALVAL + DESCRIPTIONS[1];
    }

}
