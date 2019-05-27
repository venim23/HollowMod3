package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.VoidPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class attackHerrahsAnger extends AbstractHollowCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("HerrahsAnger");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("attackHerrahsAnger.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;

    private static final int TIMES_HIT = 4;
    private static final int UPGRADE_TIMES_HIT = 2;

    private static final int DAMAGE = 3;


    // /STAT DECLARATION/


    public attackHerrahsAnger() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.tags.add(CardTagEnum.ALLY);

        this.magicNumber = this.baseMagicNumber = TIMES_HIT;
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
        //all this shit is basically to let Void increase the damage of this card, annoying because the multidamage is an array.
        /*
        int void_bonus = 0;

        if (p.hasPower(VoidPower.POWER_ID)){
            void_bonus += p.getPower(VoidPower.POWER_ID).amount;
        }

        */
        for (int i = this.magicNumber; i > 0; i--){
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_TIMES_HIT);
            initializeDescription();
        }
    }
}