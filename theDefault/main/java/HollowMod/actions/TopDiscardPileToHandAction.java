package HollowMod.actions;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class TopDiscardPileToHandAction extends AbstractGameAction {
    private AbstractPlayer p;

    public TopDiscardPileToHandAction(int amount) {
        this.p = AbstractDungeon.player;
        setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {

        if (this.p.hand.size() >= 10) {
            this.isDone = true;
            return;
        }

        if (this.p.discardPile.size() > 0) {
            for (int i = this.amount; i > 0; i--) {
                AbstractCard c = this.p.discardPile.getTopCard();
                AbstractDungeon.actionManager.addToBottom(new DiscardToHandAction(c));
            }
        }
        this.tickDuration();
        this.isDone = true;
    }
}







