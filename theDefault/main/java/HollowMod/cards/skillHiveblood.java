package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillHiveblood extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("Hiveblood");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillHiveblood.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;
    private static final int HEALTH_COST = 10;
    private static final int UPGRADE_HEALTH_COST = 4;

    private static final int REGEN_BASE = 5;
    private static final int UPGRADE_PLUS_REGEN_BASE = 2;



    // /STAT DECLARATION/

    public skillHiveblood() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = HEALTH_COST;
        this.defaultSecondMagicNumber = defaultBaseSecondMagicNumber = REGEN_BASE;
        this.exhaust = true;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, magicNumber));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RegenPower(p,defaultSecondMagicNumber),defaultSecondMagicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(UPGRADE_HEALTH_COST);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_REGEN_BASE);
            initializeDescription();
        }
    }
}