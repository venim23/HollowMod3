package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillSoulShaman extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("SoulShaman");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillSoulTwister.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;
    private static final int FOCUS_COST = 5;
    private static final int ENERGY = 1;
    private static final int UPGRADED_ENERGY= 1;


    // /STAT DECLARATION/


    public skillSoulShaman() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, FOCUS_COST);
        this.SetCardHeader(CardHeaders.Spell);
        this.tags.add(CardTagEnum.SPELL);
        this.magicNumber = (this.baseMagicNumber = ENERGY);
        this.hollowFocusCost = (hollowBaseFocusCost = FOCUS_COST);
        this.tags.add(CardTagEnum.SOULFOCUS);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p, hollowFocusCost));

        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(magicNumber));

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_ENERGY);
            rawDescription = (UPGRADE_DESCRIPTION);
            initializeDescription();
        }
    }
}