package HollowMod.cards;

import HollowMod.actions.FocusSoulAction;
import HollowMod.actions.ModifyMagicNumAction;
import HollowMod.actions.SFXVAction;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UpdateCardDescriptionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import HollowMod.characters.TheBugKnight;

import static HollowMod.hollowMod.makeCardPath;

public class skillFocusHeal_s extends AbstractHollowCard {

    /*
     * "Hey, I wanna make a bunch of cards now." - You, probably.
     * ok cool my dude no problem here's the most convenient way to do it:
     *
     * Copy all of the code here (Ctrl+A > Ctrl+C)
     * Ctrl+Shift+A and search up "file and code template"
     * Press the + button at the top and name your template whatever it is for - "AttackCard" or "PowerCard" or something up to you.
     * Read up on the instructions at the bottom. Basically replace anywhere you'd put your cards name with skillFocusHeal_s
     * And then you can do custom ones like  and SELF if you want.
     * I'll leave some comments on things you might consider replacing with what.
     *
     * Of course, delete all the comments and add anything you want (For example, if you're making a skill card template you'll
     * likely want to replace that new DamageAction with a gain Block one, and add baseBlock instead, or maybe you want a
     * universal template where you delete everything unnecessary - up to you)
     *
     * You can create templates for anything you ever want to. Cards, relics, powers, orbs, etc. etc. etc.
     */

    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("FocusHealStart");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("skillFocusHeal.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 1;
    private static final int FOCUSCOST = 3;
    private static final int UPGRADED_FOCUS = 0;
    private static final int HEAL_START = 6;


    // /STAT DECLARATION/


    public skillFocusHeal_s() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, FOCUSCOST);
        this.tags.add(CardTags.HEALING);
        this.tags.add(CardTagEnum.SPELL);
        this.baseMagicNumber =  (this.magicNumber = HEAL_START);
        this.exhaust = false;
        this.hollowFocusCost = (hollowBaseFocusCost = FOCUSCOST);
        this.tags.add(CardTagEnum.SOULFOCUS);

    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.magicNumber <= 2){
            this.exhaust = true;
        }
        AbstractDungeon.actionManager.addToBottom(new SFXVAction(SoundEffects.Healing.getKey(), 1.4F));
        if (!this.upgraded) {
            AbstractDungeon.actionManager.addToTop(new FocusSoulAction(p, hollowFocusCost));
        }
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ModifyMagicNumAction(this.uuid, -2));
        AbstractDungeon.actionManager.addToBottom(new UpdateCardDescriptionAction(this));

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeFocusCost(UPGRADED_FOCUS);
            rawDescription = (UPGRADE_DESCRIPTION);
            initializeDescription();
        }
    }
}