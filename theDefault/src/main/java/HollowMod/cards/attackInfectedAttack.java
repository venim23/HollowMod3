package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.InfectionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

public class attackInfectedAttack extends AbstractDefaultCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with attackInfectedAttack
     * And then you can do custom ones like 5 and ENEMY if you want.
     * I'll leave some comments on things you might consider replacing with what.
     *
     * Of course, delete all the comments and add anything you want (For example, if you're making a skill card template you'll
     * likely want to replace that new DamageAction with a gain Block one, and add baseBlock instead, or maybe you want a
     * universal template where you delete everything unnecessary - up to you)
     *
     * You can create templates for anything you ever want to. Cards, relics, powers, orbs, etc. etc. etc.
     */

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
    private static final int POISON = 3;
    private static final int UPGRADE_PLUS_POISON = 2;

    private static final int INFECTION = 2;

    // /STAT DECLARATION/


    public attackInfectedAttack() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = POISON;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = INFECTION;
        tags.add(CardTagEnum.INFECTION);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        // This should do the damage
        logger.info("Beginning to damage");
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
        //this should apply the poison
        logger.info("Beginning to poison");
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new InfectionPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
        //AbstractDungeon.actionManager.addToBottom(
        //new ApplyPowerAction(p, p, new DexterityPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
        logger.info("Beginning to infect");
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