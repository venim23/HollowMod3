package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.characters.TheBugKnight;
import HollowMod.powers.sharpenedShadowsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class powerSharpenedShadows extends AbstractDefaultCard {

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

    public static final String ID = hollowMod.makeID("SharpenedShadows");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("powerSharpenedShadow.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;

    private static final int DASH_DAMAGE = 3;
    private static final int UPGRADE_PLUS_DASH_DAMAGE = 2;

    // /STAT DECLARATION/

    public powerSharpenedShadows() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        magicNumber = this.baseMagicNumber = DASH_DAMAGE;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new sharpenedShadowsPower(p, magicNumber), magicNumber));

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DASH_DAMAGE);
            this.initializeDescription();
        }
    }
}