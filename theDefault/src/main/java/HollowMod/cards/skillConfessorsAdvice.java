package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.characters.TheBugKnight;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillConfessorsAdvice extends AbstractDefaultCard {

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

    public static final String ID = hollowMod.makeID("ConfessorsAdvice");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillConfessorsAdvice.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;

    private static final int CARDS_COPIED  = 1;
    private static final int UPGRADED_CARDS_COPIED  = 1;

    // /STAT DECLARATION/

    public skillConfessorsAdvice() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        magicNumber = this.baseMagicNumber = CARDS_COPIED;

        this.tags.add(CardTagEnum.ALLY);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard CardtoCopy = AbstractDungeon.player.drawPile.getTopCard();
        AbstractDungeon.actionManager.addToBottom( new PlayTopCardAction(
                AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));

        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(CardtoCopy, magicNumber));


    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(UPGRADED_CARDS_COPIED);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}