package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.GlowingWombPower;
import HollowMod.powers.InfectionPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class powerGlowingWomb extends AbstractHollowCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("NewGlowingWomb"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("powerGlowingWomb.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;


    private static final int COST = 1;
    private static final int UPGRADE_BASE_COST = 0;  // COST = 1

    private static final int BLOCK = 3;


    private static final int INFECTION = 3;





    // /STAT DECLARATION/


    public powerGlowingWomb() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        tags.add(CardTagEnum.INFECTION);
        this.magicNumber = (this.baseMagicNumber = BLOCK);
        this.defaultSecondMagicNumber = defaultBaseSecondMagicNumber = INFECTION;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new GlowingWombPower(p, p ,this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new InfectionPower(p, INFECTION), INFECTION));

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