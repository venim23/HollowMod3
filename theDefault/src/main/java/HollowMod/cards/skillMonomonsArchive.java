package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillMonomonsArchive extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("MonomonsArchive");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillMonomonsArchive.png");
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
    private static final int BLOCK_AMOUNT = 15;
    private static final int UPGRADED_BLOCK_AMOUNT= 5;


    // /STAT DECLARATION/


    public skillMonomonsArchive() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, FOCUS_COST);
        this.SetCardHeader(CardHeaders.AllySpell);
        this.tags.add(CardTagEnum.ALLY);
        this.tags.add(CardTagEnum.SPELL);
        this.tags.add(CardTagEnum.SOULFOCUS);
        baseBlock = BLOCK_AMOUNT;
        this.hollowFocusCost = (hollowBaseFocusCost = FOCUS_COST);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p, hollowFocusCost));

        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADED_BLOCK_AMOUNT);
            initializeDescription();
        }
    }
}