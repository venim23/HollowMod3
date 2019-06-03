package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class MantisClawRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("MantisClawRelic");



    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("mantis_claw.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("mantis_claw.png"));

    //private static final int STR_AMT = 1;
    private static final int NUM_CARDS = 3;
    private boolean onceper = false;

    public MantisClawRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    // Flash at the start of Battle.

    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (card.hasTag(CardTagEnum.DASH)){
            if (this.counter < 3){
                ++this.counter;
            }
            if ((this.counter >= 3) && (!onceper)) {
                this.flash();
                onceper = true;
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }
    }



    @Override
    public void atTurnStart() {
    this.counter = 0;
    this.onceper = false;
    }
    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
