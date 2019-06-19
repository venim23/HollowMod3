package HollowMod.patches;
import HollowMod.cards.AbstractHollowCard;
import HollowMod.cards.CardHeader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;



@SpirePatch(
        clz= SingleCardViewPopup.class,
        method="renderTitle"
)
public class CardHeaderSingleView {

    @SpireInsertPatch(
            rloc = 0,
            localvars={"card"}
    )
    public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card)
    {
        CardHeader cHeader;
        String headerplswork;

        if (card instanceof AbstractHollowCard){
            if ((((AbstractHollowCard) card).HasCardHeader(card))){
                cHeader = ((AbstractHollowCard) card).GetCardHeader();
                headerplswork = cHeader.NAME;
                FontHelper.renderFontCentered(sb, FontHelper.SCP_cardTitleFont_small, headerplswork, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F + 400.0F * Settings.scale, Color.SKY );

            }

        }

    }

}

