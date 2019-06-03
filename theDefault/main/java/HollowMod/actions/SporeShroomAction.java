package HollowMod.actions;

import HollowMod.hollowMod;
import HollowMod.powers.BaldurShellPower;
import HollowMod.powers.SoulMasterPower;
import HollowMod.powers.SoulPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SporeShroomAction
  extends AbstractGameAction
{
  private int amt;
  private AbstractPlayer p;
  public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());

  public SporeShroomAction(int maxCards)
  {
    this.duration = Settings.ACTION_DUR_FAST;
    this.actionType = ActionType.CARD_MANIPULATION;
    this.amt = maxCards;
	//this.cost = cost - (this.source.getPower("SoulMaster").amount);
	// to account for powers that reduce the cost of soul
  }

  
  
  public void update()
  {
    if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
    {
      this.isDone = true;
      return;
    }
    if (AbstractDungeon.player.hand.isEmpty()){
      this.isDone = true;
      return;
    } else {
      AbstractDungeon.handCardSelectScreen.open("Choose up to 3 cards to discard.", this.amt, true, true,false,false, true );
      for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group){
        p.hand.moveToDiscardPile(c);
        c.triggerOnManualDiscard();
        GameActionManager.incrementDiscard(false);
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, 2));
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
          if ((!monster.isDead) && (!monster.isDying)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, p, new PoisonPower(monster, p, 1), 1));
          }
        }
      }
    }
    this.isDone = true;
  }
}