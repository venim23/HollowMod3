package HollowMod.cards;

import HollowMod.patches.CardTagEnum;
import HollowMod.powers.VoidPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import HollowMod.hollowMod;
import HollowMod.characters.TheBugKnight;

import static HollowMod.hollowMod.makeCardPath;

public class attackVengefulVoid extends AbstractHollowCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with attackVengefulVoid
     * And then you can do custom ones like 8 and ENEMY if you want.
     * I'll leave some comments on things you might consider replacing with what.
     *
     * Of course, delete all the comments and add anything you want (For example, if you're making a skill card template you'll
     * likely want to replace that new DamageAction with a gain Block one, and add baseBlock instead, or maybe you want a
     * universal template where you delete everything unnecessary - up to you)
     *
     * You can create templates for anything you ever want to. Cards, relics, powers, orbs, etc. etc. etc.
     */

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("VengefulVoid");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("attackVengefulVoid.png");
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

    private static final int VOID = 2;

    private static final int TIMES_HIT = 2;

    private static final int UPGRADE_PLUS_TIMES_HIT = 1;

    private static final int DAMAGE = 6;

    // /STAT DECLARATION/


    public attackVengefulVoid() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = TIMES_HIT;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = VOID;
        this.isMultiDamage = true;
        this.exhaust = true;
        tags.add(CardTagEnum.SPELL);
        tags.add(CardTagEnum.VOID);

    }

    public float calculateModifiedCardDamage(final AbstractPlayer player, final AbstractMonster mo, final float tmp) {
        int void_bonus = 0;

        if ((mo != null) && (player.hasPower(VoidPower.POWER_ID))){
            void_bonus += player.getPower(VoidPower.POWER_ID).amount;
        }

        return tmp + void_bonus;
    }



    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Fireball.getKey()));

        for (int i = this.magicNumber; i > 0; i--){
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, (damage), damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VoidPower(p, this.defaultSecondMagicNumber), this.defaultSecondMagicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_TIMES_HIT);
            initializeDescription();
        }
    }
}