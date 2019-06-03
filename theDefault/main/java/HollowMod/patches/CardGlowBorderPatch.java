package HollowMod.patches;

import HollowMod.cards.AbstractHollowCard;
import HollowMod.patches.CardTagEnum;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;

import java.sql.Ref;

@SpirePatch(
        clz = CardGlowBorder.class,
        method = SpirePatch.CONSTRUCTOR
)
public class CardGlowBorderPatch {
    @SpirePostfixPatch
    public static void PostFix(CardGlowBorder __instance, AbstractCard c)
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (c.hasTag(CardTagEnum.VOID)) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", new Color(0.1f, 0f, 0.2f, 0.99f));
            }
            if (c.hasTag(CardTagEnum.SPELL) && (!c.hasTag(CardTagEnum.VOID))) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", new Color(0.937f, 0.98f, .99f, 0.75f));
            }
            if (c.hasTag(CardTagEnum.INFECTION)) {
                ReflectionHacks.setPrivate(__instance, AbstractGameEffect.class, "color", new Color(1.0f, 0.635f, 0f, 0.75f));
            }
        }
    }
}
