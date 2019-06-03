package HollowMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import java.util.ArrayList;

public class LittleFoolAction
        extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RecycleAction");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractPlayer p;
    private DamageInfo info;
    private int dmgPer;
    public LittleFoolAction(AbstractCreature target, DamageInfo info, int damagePer)
    {
        this.info = info;
        this.dmgPer = damagePer;
        setValues(target, info);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.attackEffect = AbstractGameAction.AttackEffect.FIRE;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (this.p.hand.isEmpty())
            {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() == 1)
            {

                this.p.hand.moveToExhaustPile(this.p.hand.getBottomCard());
                this.info.base = this.dmgPer;
                AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.FIRE));
                tickDuration();
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                this.info.base +=dmgPer;
                this.p.hand.moveToExhaustPile(c);
            }
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        tickDuration();
    }
}