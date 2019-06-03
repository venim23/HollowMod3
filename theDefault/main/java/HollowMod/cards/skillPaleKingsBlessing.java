package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.InfectionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillPaleKingsBlessing extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("PaleKingsBlessing");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillPaleKingsBlessing.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int HEALTH_GAIN = 6;

    private static final int HP_INF = 1;



    // /STAT DECLARATION/

    public skillPaleKingsBlessing() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = HEALTH_GAIN;
        this.defaultSecondMagicNumber = defaultBaseSecondMagicNumber = HP_INF;
        this.exhaust = true;
        this.tags.add(CardTags.HEALING);
        tags.add(CardTagEnum.ALLY);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int newval = 0;
        if (p.hasPower(InfectionPower.POWER_ID)){
            newval =  defaultSecondMagicNumber * (p.getPower(InfectionPower.POWER_ID).amount);
        }
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, (magicNumber+ newval)));
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