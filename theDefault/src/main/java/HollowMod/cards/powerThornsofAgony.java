package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SlyStrikePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import static HollowMod.hollowMod.makeCardPath;

public class powerThornsofAgony extends AbstractDefaultCard {



    public static final String ID = hollowMod.makeID("ThornsofAgony");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("powerThornsofAgony.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;

    private static final int THORNS = 2;
    private static final int UPGRADE_PLUS_THORNS = 1;

    // /STAT DECLARATION/

    public powerThornsofAgony() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        magicNumber = this.baseMagicNumber = THORNS;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber), magicNumber));

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.isEthereal = false;
            this.upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_THORNS);
            this.initializeDescription();
        }
    }
}