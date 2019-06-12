package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.hollowMod;
import HollowMod.characters.TheBugKnight;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DiscardAction;

import java.util.Iterator;

import static HollowMod.hollowMod.makeCardPath;

public class skillCornifersMap_s extends AbstractHollowCard {

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

    public static final String ID = hollowMod.makeID("CornifersMapStart");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillCornifersMap.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;
    private static final int FOCUSCOST = 4;
    private static final int UPGRADE_FOCUSCOST = 3;
    private static final int CARDDRAW = 3;



    // /STAT DECLARATION/

    public skillCornifersMap_s() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.hollowFocusCost = (hollowBaseFocusCost = FOCUSCOST);
        this.magicNumber = this.baseMagicNumber = CARDDRAW;
        this.SetCardHeader(CardHeaders.AllySpell);
        this.tags.add(CardTagEnum.ALLY);
        this.tags.add(CardTagEnum.SPELL);
        this.tags.add(CardTagEnum.SOULFOCUS);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*if (new FocusSoulAction(p, hollowFocusCost).canFocusSoulAction()) {
            AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p, hollowFocusCost));
        } else {
            this.exhaust = true;
        }
        */
        AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p, hollowFocusCost));
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Cornifer.getKey()));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeFocusCost(UPGRADE_FOCUSCOST);
            initializeDescription();
        }
    }
}