package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.MonarchWingsPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillDoubleDash extends AbstractHollowCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("DoubleDash"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillDoubleDash.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;  // COST = 1

    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private static final int BLOCK_NEXT = 3;
    private static final int UPGRADED_BLOCK_NEXT = 2;



    // /STAT DECLARATION/


    public skillDoubleDash() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        this.SetCardHeader(CardHeaders.Dash);
        tags.add(CardTagEnum.DASH);
        this.magicNumber = (this.baseMagicNumber = BLOCK_NEXT);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Evade.getKey()));

        int doubleblockcheck = magicNumber;
        if (p.hasPower(MonarchWingsPower.POWER_ID)){
            doubleblockcheck += doubleblockcheck;
        }
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p,p, this.block));


        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new NextTurnBlockPower(p, doubleblockcheck), doubleblockcheck));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADED_BLOCK_NEXT);
            initializeDescription();
        }
    }
}