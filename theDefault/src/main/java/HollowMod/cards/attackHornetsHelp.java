package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.actions.HornetsHelpAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.patches.CardTagEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static HollowMod.hollowMod.makeCardPath;

public class attackHornetsHelp extends CustomCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("HornetsHelp");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("attackHornetsHelp.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = -1;
    private static final int MAGIC = 1;

    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /STAT DECLARATION/


    public attackHornetsHelp() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        tags.add(CardTagEnum.ALLY);
        magicNumber = baseMagicNumber = MAGIC;

    }


    // Actions the card should do.
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }
        AbstractDungeon.actionManager.addToBottom(new HornetsHelpAction(p, m, magicNumber,
                upgraded, damageTypeForTurn, freeToPlayOnce, energyOnUse));
    }
    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}