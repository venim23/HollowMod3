package HollowMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javafx.scene.effect.Effect;

public class HornetsHelpAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int magicNumber;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean upgraded;
    private int damage;
    private int block;

    public HornetsHelpAction(final AbstractPlayer p, final AbstractMonster m,
                             final int damage, final int block, final int magicNumber)

    {
        this.freeToPlayOnce = false;
        this.p = p;
        this.magicNumber = magicNumber;
        this.freeToPlayOnce = freeToPlayOnce;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        this.damage = damage;
        this.block = block;
    }

    @Override
    public void update() {
       /*
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (upgraded) {
            effect++;
        }
         */
        /*if (p.hasPower(DexterityPower.POWER_ID)){
            BlockEffect = (BaseEffect + p.getPower(DexterityPower.POWER_ID).amount);
        }
        if (p.hasPower(StrengthPower.POWER_ID)){
            this.damage = (BaseEffect + p.getPower(StrengthPower.POWER_ID).amount);
        }
        */
        if (this.block >0 || this.damage> 0) {
            for (int i = 0; i < this.magicNumber; ++i) {

                AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, damageType), AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,this.block));
            }
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }

        isDone = true;
    }
}
