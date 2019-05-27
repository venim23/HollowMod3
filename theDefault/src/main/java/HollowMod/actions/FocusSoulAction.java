package HollowMod.actions;

import HollowMod.hollowMod;
import HollowMod.powers.*;
import HollowMod.relics.KingsBrandRelic;
import HollowMod.util.SoundEffects;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FocusSoulAction
  extends AbstractGameAction
{
  private int cost;
  private boolean hasinfect = false;
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
  
  public boolean canFocusSoulAction() {
    boolean canUse = false;


    if (this.source.hasPower(SoulMasterPower.POWER_ID)) {
      this.cost -= this.source.getPower(SoulMasterPower.POWER_ID).amount;
      if (this.cost < 1) {
        this.cost = 1;
      }
    }
    if (AbstractDungeon.player.hasRelic(KingsBrandRelic.ID)){
      if (this.source.hasPower(InfectionPower.POWER_ID)){
        if (this.source.getPower(InfectionPower.POWER_ID).amount >= this.cost){
          canUse = true;
          this.hasinfect = true;
        }
      }
    }
    if (this.source.hasPower(SoulPower.POWER_ID)) {
      if (this.source.getPower(SoulPower.POWER_ID).amount >= this.cost) {
        canUse = true;
      }
    }
    if (this.source.hasPower(WhiteLadyPower.POWER_ID)){
      canUse = true;
    }


    return canUse;
  }
  
  
  public void update()
  {
    if ((canFocusSoulAction()) && (this.hasinfect) &&(this.source.hasPower(InfectionPower.POWER_ID)) && (AbstractDungeon.player.hasRelic(KingsBrandRelic.ID)) ){
      AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.source, this.source, InfectionPower.POWER_ID ,this.cost));
      this.source.getPower(InfectionPower.POWER_ID).updateDescription();
      if (this.source.hasPower(BaldurShellPower.POWER_ID)){
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Blocker.getKey()));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(source, source, source.getPower(BaldurShellPower.POWER_ID).amount, true));
        source.getPower(BaldurShellPower.POWER_ID).flash();
      }
    }
    else if ((canFocusSoulAction()) && this.source.hasPower(SoulPower.POWER_ID))
    {
      AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.source, this.source, SoulPower.POWER_ID ,this.cost));
      this.source.getPower(SoulPower.POWER_ID).updateDescription();
      if (this.source.hasPower(BaldurShellPower.POWER_ID)){
        AbstractDungeon.actionManager.addToBottom(new SFXAction(SoundEffects.Blocker.getKey()));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(source, source, source.getPower(BaldurShellPower.POWER_ID).amount, true));
        source.getPower(BaldurShellPower.POWER_ID).flash();
      }
    }
    this.isDone = true;
  }
}