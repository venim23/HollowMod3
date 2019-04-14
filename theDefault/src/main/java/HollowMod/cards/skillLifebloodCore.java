package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;

import static HollowMod.hollowMod.makeCardPath;

public class skillLifebloodCore extends AbstractDefaultCard {

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("LifebloodCore");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillLifebloodCore.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;



    // /STAT DECLARATION/

    public skillLifebloodCore() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.isEthereal = true;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int newlife = AbstractDungeon.player.currentBlock;
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p,p,newlife));
        AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(p,p,newlife));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isEthereal = false;
            rawDescription = (UPGRADE_DESCRIPTION);
            initializeDescription();
        }
    }
}