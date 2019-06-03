package HollowMod.actions;

import HollowMod.hollowMod;
import HollowMod.powers.SoulMasterPower;
import HollowMod.powers.SoulPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IncreaseSoulMeterforCombatAction
  extends AbstractGameAction
{
  private int amount;

  public IncreaseSoulMeterforCombatAction(AbstractPlayer p, int extraMeter)
  {
    this.duration = Settings.ACTION_DUR_XFAST;
    this.actionType = ActionType.POWER;
    this.source = p;
    this.amount = extraMeter;
  }

  public void update()
  {
    if (this.source.hasPower(SoulPower.POWER_ID)){
      SoulPower soulChecker = (SoulPower)this.source.getPower(SoulPower.POWER_ID);
      soulChecker.SOUL_METER += this.amount;
      soulChecker.updateDescription();
    }
    this.isDone = true;
  }
}