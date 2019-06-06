package HollowMod.cards;

import HollowMod.actions.RecoverDashAction;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.SoundEffects;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import HollowMod.hollowMod;
import HollowMod.characters.TheBugKnight;

import static HollowMod.hollowMod.makeCardPath;

public class attackRecoilStrike extends AbstractHollowCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("RecoilStrike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("attackRecoilStrike.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 1;

    private static final int DAMAGE = 9;
    private static int intVal;


    // /STAT DECLARATION/


    public attackRecoilStrike() {// This one and the one right under the imports are the most important ones, don't forget them
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;

    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Parry.getKey()));
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        //this Deals Damage
        AbstractDungeon.actionManager.addToBottom(new RecoverDashAction(p, 1,(this.upgraded)));


        //AbstractDungeon.actionManager.addToBottom(new FetchAction(AbstractDungeon.player.discardPile, (card -> card.hasTag(CardTagEnum.DASH)), 1));


}       //this "Hopefully" allows a player to get dash cards back from the discard. probabyl gonna break tbh


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            rawDescription = (UPGRADE_DESCRIPTION);
            this.initializeDescription();
        }
    }
}