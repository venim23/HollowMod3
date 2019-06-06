package HollowMod.util;

import HollowMod.hollowMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.UIStrings;


public class Config {
    public static final String ID = hollowMod.makeID("DisableEnemies");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public final static String DISABLE_ENEMIES = "use-boss";

    public final static float BOSSES_TOGGLE_START_X = 350.0f;
    public final static float BOSSES_TOGGLE_START_Y = 750.0F;
    public final static String TOGGLE_ENEMIES_LABEL = uiStrings.TEXT[0];


}
