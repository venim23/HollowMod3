package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;

import static HollowMod.hollowMod.makeCardPath;

public class skillChannelNail extends AbstractHollowCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("ChannelNail"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillChannelNail.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;  // COST = 1

    private static final int FOCUS_COST = 3;


    private static final int CARD_DRAW = 1;
    private static final int ENERGY_GAIN = 1;



    // /STAT DECLARATION/


    public skillChannelNail() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, getimg(AbstractDungeon.ascensionLevel), COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, FOCUS_COST);
        this.SetCardHeader(CardHeaders.Spell);
        tags.add(CardTagEnum.SPELL);
        this.tags.add(CardTagEnum.SOULFOCUS);
        this.exhaust = true;
        this.magicNumber = (this.baseMagicNumber = CARD_DRAW);
    }

    private static String getimg(int alevel){

        String IMG1 = IMG;
        if (alevel >=1){
            IMG1 = makeCardPath("skillChannel_Nail2.png");
        }
        return IMG1;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p, FOCUS_COST));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, CARD_DRAW), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedBluePower(p ,ENERGY_GAIN), 1));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}