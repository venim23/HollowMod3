package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.InfectionPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import HollowMod.characters.TheBugKnight;
import com.megacrit.cardcrawl.powers.PoisonPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static HollowMod.hollowMod.makeCardPath;

public class attackInfectedAttack extends AbstractHollowCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("InfectedAttack");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public static final String IMG = makeCardPath("attackInfectedAttack.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;

    private static final int DAMAGE = 5;
    private static final int POISON = 4;
    private static final int UPGRADE_PLUS_POISON = 2;

    private static final int INFECTION = 3;

    // /STAT DECLARATION/


    public attackInfectedAttack() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, getimg(AbstractDungeon.ascensionLevel), COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = POISON;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = INFECTION;
        tags.add(CardTagEnum.INFECTION);
    }


    private static String getimg(int alevel){

        String IMG1 = IMG;
        if (alevel >=3){
            IMG1 = makeCardPath("attackInfectedAttack2.png");
        }
        return IMG1;
    }



    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Orange.getKey()));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        // This should do the damage
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
        //this should apply the poison
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new InfectionPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
        //AbstractDungeon.actionManager.addToBottom(
        //new ApplyPowerAction(p, p, new DexterityPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
        //this should apply the infection?
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_POISON);
            initializeDescription();
        }
    }
}