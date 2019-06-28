package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.InfectionPower;
import HollowMod.powers.SoulPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static HollowMod.hollowMod.makeCardPath;

public class attackInfectionAssault extends AbstractHollowCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("InfectionAssault");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

    public static final String IMG = makeCardPath("attackInfectionAssault.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;

    private static final int DAMAGE = 8;
    private static final int BONUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_BONUS_DAMAGE = 2;


    // /STAT DECLARATION/


    public attackInfectionAssault() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, getimg(AbstractDungeon.ascensionLevel), COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_DAMAGE;
        tags.add(CardTagEnum.INFECTION);
    }

    private static String getimg(int alevel){

        String IMG1 = IMG;
        if (alevel >=15){
            IMG1 = makeCardPath("attackInfectionAssault2.png");
        }
        return IMG1;
    }




    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int newDamage = baseDamage;
        if (p.hasPower(InfectionPower.POWER_ID)){
            newDamage = (baseDamage + (magicNumber * (p.getPower(InfectionPower.POWER_ID).amount)));
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, newDamage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    public void applyPowers()
    {
        super.applyPowers();
        int newval = this.magicNumber;
        this.defaultSecondMagicNumber = 0;
        this.defaultBaseSecondMagicNumber = 0;
        if (AbstractDungeon.player.hasPower(InfectionPower.POWER_ID)) {
            for ( int i = (AbstractDungeon.player.getPower(InfectionPower.POWER_ID).amount); i > 0; i--) {
                this.defaultBaseSecondMagicNumber += newval;
            }
        }
        if (this.defaultBaseSecondMagicNumber > 0)
        {
            this.rawDescription = (DESCRIPTION + EXTENDED_DESCRIPTION[0]);
            initializeDescription();
        }
    }

    public void onMoveToDiscard()
    {
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_BONUS_DAMAGE);
            initializeDescription();
        }
    }
}