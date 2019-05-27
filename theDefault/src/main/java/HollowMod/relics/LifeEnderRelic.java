package HollowMod.relics;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static HollowMod.hollowMod.makeRelicOutlinePath;
import static HollowMod.hollowMod.makeRelicPath;

public class LifeEnderRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = hollowMod.makeID("LifeEnderRelic");



    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("life_ender.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("life_ender.png"));

    //private static final int STR_AMT = 1;
    private static final int NUM_CARDS = 1;

    public LifeEnderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    // Flash at the start of Battle.

    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK){
            ++this.counter;
            if (this.counter % 5 == 0) {
                this.counter = 0;
                this.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, NUM_CARDS));
            }
        }
    }


    @Override
    public void atBattleStart() {
        this.counter = 0;
    }
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
