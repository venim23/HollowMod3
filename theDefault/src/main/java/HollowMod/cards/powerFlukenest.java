package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.powers.FlukenestPower;
import HollowMod.powers.ShapeofUnnPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class powerFlukenest extends AbstractDefaultCard {



    public static final String ID = hollowMod.makeID("Flukenest");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("powerFlukenest.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;

    private static final int ENEMIES_HIT = 2;
    private static final int UPGRADE_PLUS_ENEMIES_HIT = 2;

    // /STAT DECLARATION/

    public powerFlukenest() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = (baseMagicNumber = ENEMIES_HIT);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new FlukenestPower(p, p, magicNumber ), magicNumber));

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ENEMIES_HIT);
            this.initializeDescription();
        }
    }
}