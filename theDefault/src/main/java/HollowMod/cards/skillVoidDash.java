package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.VoidDashPower;
import HollowMod.powers.VoidPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillVoidDash extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("VoidDash"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillVoidDash.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;  // COST = 1

    private static final int BLOCK = 15;
    private static final int UPGRADE_PLUS_BLOCK =5;

    private static final int REVENGE = 5;

    private static final int VOID = 2;


    // /STAT DECLARATION/


    public skillVoidDash() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        magicNumber = (baseMagicNumber = REVENGE);
        tags.add(CardTagEnum.DASH);
        tags.add(CardTagEnum.VOID);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.ShadowDash.getKey()));
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p,p, this.block));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new VoidDashPower(p, p, magicNumber), magicNumber));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VoidPower(p, VOID), VOID));

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}