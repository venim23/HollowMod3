package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.InfectionPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.unique.DoublePoisonAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillInfectedGuardian extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("NewInfectedGuardian");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillInfectedGuardian.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;


    private static final int MIN_INF = 4;




    // /STAT DECLARATION/

    public skillInfectedGuardian() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = MIN_INF;
        this.exhaust = true;
        this.tags.add(CardTagEnum.INFECTION);
        this.SetCardHeader(CardHeaders.Ally);
        tags.add(CardTagEnum.ALLY);
    }


    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        }
        if (p.hasPower(InfectionPower.POWER_ID)){
            if (p.getPower(InfectionPower.POWER_ID).amount < this.magicNumber) {
                this.cantUseMessage = UPGRADE_DESCRIPTION;
                return false;
            }
        }else {
            this.cantUseMessage = UPGRADE_DESCRIPTION;
            return false;
        }
        return canUse;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int newval = 0;


        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            AbstractDungeon.actionManager.addToBottom(new DoublePoisonAction(monster, p));
        }
        if (p.hasPower(InfectionPower.POWER_ID)){
            newval = (p.getPower(InfectionPower.POWER_ID).amount);
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(p, p, new InfectionPower(AbstractDungeon.player, newval ), newval));


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