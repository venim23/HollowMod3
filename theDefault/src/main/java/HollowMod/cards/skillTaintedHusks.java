package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.powers.InfectionPower;
import HollowMod.powers.SiblingSoulPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;

import static HollowMod.hollowMod.makeCardPath;

public class skillTaintedHusks extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("TaintedHusks");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillTaintedHusks.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;

    private static final int INFECTION = 3;
    private static final int POISON = 2;

    private static final int BOUNCES = 2;
    private static final int UPGRADE_PLUS_BOUNCES = 1;
    // /STAT DECLARATION/

    public skillTaintedHusks() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = (this.baseMagicNumber = BOUNCES);
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber = INFECTION;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (randomMonster != null) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new PotionBounceEffect(p.hb.cY, p.hb.cX, randomMonster.hb.cX, this.hb.cY), 0.3F));
        }
        AbstractDungeon.actionManager.addToBottom(new BouncingFlaskAction(randomMonster, POISON, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new InfectionPower(p,defaultSecondMagicNumber), defaultSecondMagicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BOUNCES);
            initializeDescription();
        }
    }
}