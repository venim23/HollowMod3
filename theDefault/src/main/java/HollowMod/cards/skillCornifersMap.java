package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.characters.TheBugKnight;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DiscardAction;

import java.util.Iterator;

import static HollowMod.hollowMod.makeCardPath;

public class skillCornifersMap extends AbstractDefaultCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Using the second magic number isn't very confusing, you just declare and use it the absolutely the same way you would your
     * your other ones (attack, block, magic, etc.)
     *
     * For how to create it, check out:
     * https://github.com/daviscook477/BaseMod/wiki/Dynamic-Variables
     * The files in this base that detail this are:
     * variables.DefaultSecondMagicNumber and cards.AbstractDefaultCard
     *
     * Apply 2(5) vulnerable and 4(9) poison to an enemy.
     */

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("CornifersMap");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillCornifersMap.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;


    // /STAT DECLARATION/

    public skillCornifersMap() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.tags.add(CardTagEnum.ALLY);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(new DiscardPileToTopOfDeckAction(p));
        if (p.discardPile.group.size() > 0) {
            AbstractDungeon.gridSelectScreen.open(p.discardPile, 1, TEXT[0], true, false, true, false);
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var3 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var3.hasNext()) {
                    AbstractCard c = (AbstractCard)var3.next();
                    c.upgrade();
                    p.discardPile.removeCard(c);
                    p.hand.moveToDeck(c, true);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
        }
        if (p.hand.group.size() >= 1){
            AbstractDungeon.actionManager.addToTop(new DiscardAction(p, p, 1, false));
            AbstractCard c = p.discardPile.getTopCard();
            c.upgrade();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}