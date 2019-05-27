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

    public HornetsHelpAction(final AbstractPlayer p, final AbstractMonster m,
                             final int magicNumber, final boolean upgraded,
                             final DamageInfo.DamageType damageTypeForTurn, final boolean freeToPlayOnce,
                             final int energyOnUse)

    {
        this.freeToPlayOnce = false;
        this.p = p;
        this.magicNumber = magicNumber;
        this.freeToPlayOnce = freeToPlayOnce;
        actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (upgraded) {
            ++effect;
        }
        int repVal = magicNumber;
        int BaseEffect = effect;
        int BlockEffect = BaseEffect;
        int DamEffect = BaseEffect;
        if (p.hasPower(DexterityPower.POWER_ID)){
            BlockEffect = (BaseEffect + p.getPower(DexterityPower.POWER_ID).amount);
        }
        if (p.hasPower(StrengthPower.POWER_ID)){
            DamEffect = (BaseEffect + p.getPower(StrengthPower.POWER_ID).amount);
        }

        if (effect > 0) {
            for (int i = 0; i < repVal; ++i) {

                AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(p, DamEffect, damageType), AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p,p,BlockEffect));
            }
            if (!freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }

        isDone = true;
    }
}
