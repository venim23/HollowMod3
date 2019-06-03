package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.InfectionPower;
import HollowMod.powers.MonarchWingsPower;
import HollowMod.powers.SoulPower;
import HollowMod.powers.VoidPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import static HollowMod.hollowMod.makeCardPath;


public class skillJinnsSoul extends AbstractHollowCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("NewJinnsSoul"); // DefaultMod.makeID("skillQuickDash_s");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillJinnsSoul.png");// "public static final String IMG = makeCardPath("skillQuickDash_s.png");
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
    private static final int UPGRADE_BASE_COST = 0;
    private static final int TEMP_HP = 0;




    // /STAT DECLARATION/


    public skillJinnsSoul() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        tags.add(CardTagEnum.ALLY);
        tags.add(CardTagEnum.SOULFOCUS);
        tags.add(CardTagEnum.INFECTION);
        tags.add(CardTagEnum.VOID);
        this.exhaust = true;
        this.magicNumber = (this.baseMagicNumber = TEMP_HP);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int addtotemp = TEMP_HP;
        if (p.hasPower(SoulPower.POWER_ID)) {
            addtotemp += p.getPower(SoulPower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p, SoulPower.POWER_ID));
        }
        if (p.hasPower(VoidPower.POWER_ID)) {
            addtotemp += p.getPower(VoidPower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p, VoidPower.POWER_ID));
        }
        if (p.hasPower(InfectionPower.POWER_ID)) {
            addtotemp += p.getPower(InfectionPower.POWER_ID).amount;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p,p, InfectionPower.POWER_ID));
        }

        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Jinn.getKey()));

        AbstractDungeon.actionManager.addToBottom(
                new AddTemporaryHPAction(p, p, addtotemp));
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