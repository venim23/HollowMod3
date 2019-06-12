package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SiblingSoulPower;
import HollowMod.powers.VoidPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HollowMod.hollowMod.makeCardPath;

public class skillSiblingsShadow extends AbstractHollowCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("SiblingsShadow");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillSiblingsShadow.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 0;

    private static final int CARDS_RECOVERED = 1;
    private static final int UPGRADED_CARDS_RECOVERED = 1;

    private static final int VOID = 1;


    // /STAT DECLARATION/

    public skillSiblingsShadow() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = CARDS_RECOVERED;
        this.SetCardHeader(CardHeaders.Ally);
        tags.add(CardTagEnum.ALLY);
        this.exhaust = true;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VoidPower(p,1), 1));
        AbstractDungeon.actionManager.addToBottom(new FetchAction(AbstractDungeon.player.exhaustPile, (card -> card.hasTag(CardTagEnum.VOID)), magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeMagicNumber(UPGRADED_CARDS_RECOVERED);
            initializeDescription();
        }
    }
}