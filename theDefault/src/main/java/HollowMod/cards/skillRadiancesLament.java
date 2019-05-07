package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillRadiancesLament extends AbstractHollowCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Using the second magic number isn't very confusing, you just declare and use it the absolutely the same way you would your
     * your other ones (attack, block, magic, etc.)
     *
     * For how to create it, check out:
     * https://github.com/daviscook477/BaseMod/wiki/Dynamic-Variables
     * The files in this base that detail this are:
     * variables.DefaultSecondMagicNumber and cards.AbstractHollowCard
     *
     * Apply 2(5) vulnerable and 4(9) poison to an enemy.
     */

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("RadiancesLament");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillRadianceLament.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;



    // /STAT DECLARATION/

    public skillRadiancesLament() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.exhaust = true;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int discardInfections =0;
        int exhaustedInfections = 0;
        int drawInfections = 0;
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.hasTag(CardTagEnum.INFECTION)) {
                discardInfections++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c.hasTag(CardTagEnum.INFECTION)) {
                exhaustedInfections++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.hasTag(CardTagEnum.INFECTION)) {
                drawInfections++;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new FetchAction(AbstractDungeon.player.discardPile, (card -> card.hasTag(CardTagEnum.INFECTION)), discardInfections));
        AbstractDungeon.actionManager.addToBottom(new FetchAction(AbstractDungeon.player.exhaustPile, (card -> card.hasTag(CardTagEnum.INFECTION)), exhaustedInfections));
        if (this.upgraded){
            AbstractDungeon.actionManager.addToBottom(new FetchAction(AbstractDungeon.player.drawPile, (card -> card.hasTag(CardTagEnum.INFECTION)), drawInfections));
        }
    }

    //Add stuff about Them costing 1 less this turn and exhausting?

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.retain = true;
            rawDescription = (UPGRADE_DESCRIPTION);
            initializeDescription();
        }
    }
}