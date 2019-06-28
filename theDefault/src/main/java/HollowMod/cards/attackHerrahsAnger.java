package HollowMod.cards;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.*;
import HollowMod.relics.GreatKnightMask;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static HollowMod.hollowMod.makeCardPath;

public class attackHerrahsAnger extends AbstractHollowCard {



    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("HerrahsAnger");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillHerrahsNest.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;



    private static final int BASESUMMONS = 2;



    // /STAT DECLARATION/


    public attackHerrahsAnger() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.SetCardHeader(CardHeaders.Ally);
        this.tags.add(CardTagEnum.ALLY);

        this.magicNumber = this.baseMagicNumber = BASESUMMONS;
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


        int infCount = 0;
        int voidCount = 0;
        int spellCount = 0;
        int allyCount = 0;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(CardTagEnum.INFECTION)) {
                infCount++;
            }
            if (c.hasTag(CardTagEnum.ALLY)) {
                allyCount++;
            }
            if (c.hasTag(CardTagEnum.VOID)) {
                voidCount++;
            } else if (c.hasTag(CardTagEnum.SOULFOCUS)) {
                spellCount++;
            }
        }
        if (AbstractDungeon.player.hasRelic(GreatKnightMask.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ZoteNestPower(p, this.magicNumber), this.magicNumber));
        } else if ((allyCount > 5) && (allyCount > (AbstractDungeon.player.masterDeck.size() / 4))) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ZoteNestPower(p, this.magicNumber), this.magicNumber));

        } else if ((infCount > (AbstractDungeon.player.masterDeck.size() / 4)) && (infCount >= 5)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new InfNestPower(p, this.magicNumber), this.magicNumber));

        } else if (voidCount > (AbstractDungeon.player.masterDeck.size() / 4) && (voidCount >= 6)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DarkNestPower(p, this.magicNumber), this.magicNumber));

        } else if (spellCount > (AbstractDungeon.player.masterDeck.size() / 4) && (spellCount >= 7)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SoulNestPower(p, this.magicNumber), this.magicNumber));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NestPower(p, this.magicNumber), this.magicNumber));

        }


    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}