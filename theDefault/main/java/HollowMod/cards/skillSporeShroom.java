package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.actions.SporeShroomAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillSporeShroom extends AbstractHollowCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("SporeShroom");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillSporeShroom.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_BASE_COST = 0;
    private static final int FOCUS_COST = 3;

    private static final int HEAL_AMOUNT = 2;
    private static final int POISON_AMOUNT = 1;


    // /STAT DECLARATION/


    public skillSporeShroom() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, FOCUS_COST);
        this.tags.add(CardTags.HEALING);
        this.tags.add(CardTagEnum.SPELL);
        this.exhaust = true;
        this.hollowFocusCost = (hollowBaseFocusCost = FOCUS_COST);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p,hollowFocusCost));
        AbstractDungeon.actionManager.addToBottom(new SporeShroomAction(3));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_BASE_COST);
            initializeDescription();
        }
    }
}