package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.potions.InfectionPotion;
import HollowMod.powers.InfectionPower;
import HollowMod.powers.SoulPower;
import HollowMod.powers.VoidPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillLuriensSpire extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("LuriensSpire");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillLuriensSpire.png");
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

    private static final int COST = 1;




    // /STAT DECLARATION/


    public skillLuriensSpire() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        tags.add(CardTagEnum.ALLY);
    }


    // Actions the card should do.
    //I want this card to draw 1(2) cards, generate 2 soul and then be exhausted
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
       int valSwitch = 0;
       if (p.hasPower(InfectionPower.POWER_ID)){
            valSwitch = p.getPower(InfectionPower.POWER_ID).amount;
        }
       AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, InfectionPower.POWER_ID, valSwitch));

       if ((p.hasPower(VoidPower.POWER_ID)) && (this.upgraded)){
           AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,  new VoidPower(p, valSwitch), valSwitch));
       } else {
           AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,  new SoulPower(p, valSwitch), valSwitch));
       }



    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = (UPGRADE_DESCRIPTION);
            initializeDescription();
        }
    }
}