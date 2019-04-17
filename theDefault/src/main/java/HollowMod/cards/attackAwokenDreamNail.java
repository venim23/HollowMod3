package HollowMod.cards;

import HollowMod.actions.IncreaseSoulMeterforCombatAction;
import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.powers.SoulVialPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import static HollowMod.hollowMod.makeCardPath;

public class attackAwokenDreamNail extends CustomCard {


    // TEXT DECLARATION

    public static final String ID = hollowMod.makeID("AwokenDreamNail");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("attackAwokenDreamNail.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheBugKnight.Enums.HOLLOW_COLOR;

    private static final int COST = 3;

    private static final int DAMAGE = 18;
    private static final int UPGRADE_PLUS_DMG = 8;

    private static final int BONUS_METER = 1;

    // /STAT DECLARATION/


    public attackAwokenDreamNail() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;

    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SoulVialPower(p, p, BONUS_METER), BONUS_METER));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new SoulPower(p, 999),999));
        //AbstractDungeon.actionManager.addToBottom(new IncreaseSoulMeterforCombatAction(p , BONUS_METER));
        //int soulFill = ((((SoulPower)p.getPower(SoulPower.POWER_ID)).SOUL_METER) + BONUS_METER);
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new SoulPower(p, 1),1));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}