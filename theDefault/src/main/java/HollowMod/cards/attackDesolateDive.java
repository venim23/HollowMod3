package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.VoidPower;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class attackDesolateDive extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("DesolateDive");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("attackDesolateDive.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;

    private static final int FOCUS_COST = 5;
    private static final int UPGRADE_FOCUS_COST = 4;



    private static final int DAMAGE = 11;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/


    public attackDesolateDive() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, FOCUS_COST);
        this.SetCardHeader(CardHeaders.Spell);
        this.baseDamage = DAMAGE;
        this.tags.add(CardTagEnum.SPELL);
        this.tags.add(CardTagEnum.SOULFOCUS);
        this.isMultiDamage = true;
        this.hollowFocusCost = (hollowBaseFocusCost = FOCUS_COST);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Quake.getKey()));
        AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p,hollowFocusCost));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(p, multiDamage , damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeFocusCost(UPGRADE_FOCUS_COST);
            initializeDescription();
        }
    }
}