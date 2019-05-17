package HollowMod.cards;

import HollowMod.actions.SFXVAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.MonarchWingsPower;
import HollowMod.powers.ShadeLordPower;
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
import com.megacrit.cardcrawl.powers.ThornsPower;

import static HollowMod.hollowMod.makeCardPath;

public class skillShadowDash extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("ShadowDash"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillShadowDash.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION



    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;  // COST = 1
    private static final int VOID = 2;  // COST = 1
    private static final int BASEBLOCK = 2;  // COST = 1



    // /STAT DECLARATION/


    public skillShadowDash() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.baseBlock = BASEBLOCK;
        tags.add(CardTagEnum.DASH);
        tags.add(CardTagEnum.VOID);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p,p,new VoidPower(p, VOID), VOID));
        int blockval = VOID;
        if (p.hasPower(VoidPower.POWER_ID)){
            blockval = p.getPower(VoidPower.POWER_ID).amount + VOID;
        }
        if (p.hasPower(MonarchWingsPower.POWER_ID)){
            blockval = blockval * 2;
        }
        if (p.hasPower(ShadeLordPower.POWER_ID)){
            blockval = blockval/2;
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p,p, new ThornsPower(p,blockval),blockval));
        }
        // janky AF
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p,p, block));
        AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.Evade.getKey()));
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p,p, blockval));

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            rawDescription = (UPGRADE_DESCRIPTION);
            initializeDescription();
        }
    }
}