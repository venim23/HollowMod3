package HollowMod.actions;

import HollowMod.patches.CardTagEnum;
import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class RecoverDashAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private int magicNumber;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean upgraded;
    private int cards;
    private int energy = 0;

    public RecoverDashAction(final AbstractPlayer p, int cards, boolean upgraded)

    {
        this.p = p;
        actionType = ActionType.CARD_MANIPULATION;
        this.upgraded = upgraded;
        this.cards = cards;
        if (this.upgraded){
            this.energy = -1;
        }

    }

    @Override
    public void update() {

        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new FetchAction(AbstractDungeon.player.discardPile, card -> card.hasTag(CardTagEnum.DASH), cards, fetchedCards -> {
                    for (AbstractCard card : fetchedCards) {
                        card.modifyCostForTurn(this.energy);
                    }
                }));


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
        isDone = true;
    }
}
