package HollowMod.actions;

import HollowMod.hollowMod;
import HollowMod.powers.SoulPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FocusSoulAction
  extends AbstractGameAction
{
  private int cost;
  public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());
  
  public FocusSoulAction(AbstractPlayer p, int cost)
  {
    this.duration = Settings.ACTION_DUR_XFAST;
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.source = p;
    this.cost = cost;
	//this.cost = cost - (this.source.getPower("SoulMaster").amount);
	// to account for powers that reduce the cost of soul
  }
  
  public boolean canFocusSoulAction()
  {
    boolean canUse = false;
    if (this.source.getPower(SoulPower.POWER_ID).amount >= this.cost) {
      canUse = true;
    }
    return canUse;
  }
  
  
  public void update()
  {
    if (canFocusSoulAction())
    {
      this.source.getPower(SoulPower.POWER_ID).reducePower(this.cost);
      //Remove this, and use ReducePowerAction instead.
      this.source.getPower(SoulPower.POWER_ID).updateDescription();
      if (this.source.getPower(SoulPower.POWER_ID).amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.source, this.source, SoulPower.POWER_ID));
      }
    }
    this.isDone = true;
  }
}