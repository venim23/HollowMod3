package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillCrystalDash extends AbstractDefaultCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("CrystalDash"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillCrystalDash.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;  // COST = 1

    private static final int BLOCK_PER_CARD = 3;
    private static final int UPGRADED_BLOCK_PER_CARD = 1;



    // /STAT DECLARATION/


    public skillCrystalDash() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK_PER_CARD;
        tags.add(CardTagEnum.DASH);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = AbstractDungeon.player.hand.size();
        if (count != 0){
            AbstractDungeon.actionManager.addToTop(new DiscardAction(p, p, count,true));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, (block * count)));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADED_BLOCK_PER_CARD);
            initializeDescription();
        }
    }
}