package HollowMod.cards;

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
import com.megacrit.cardcrawl.powers.EntanglePower;

import static HollowMod.hollowMod.makeCardPath;

public class attackAbyssShriek extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("AbyssShriek");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("attackAbyssShriek.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;

    private static final int VOID = 3;

    private static final int DAMAGE = 16;
    private static final int UPGRADE_PLUS_DMG = 6;

    // /STAT DECLARATION/


    public attackAbyssShriek() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        tags.add(CardTagEnum.SPELL);
        tags.add(CardTagEnum.VOID);
        this.isMultiDamage = true;

        this.magicNumber = this.baseMagicNumber = VOID;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //all this shit is basically to let Void increase the damage of this card, annoying because the multidamage is an array.
        int[] finalDamage = multiDamage;
        if (p.hasPower(VoidPower.POWER_ID)) {
            for (int i =0; i < finalDamage.length; i++){
                    finalDamage[i] += p.getPower(VoidPower.POWER_ID).amount;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Scream.getKey()));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(p, finalDamage , damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VoidPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new EntanglePower(p)));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}