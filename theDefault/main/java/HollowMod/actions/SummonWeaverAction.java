package HollowMod.actions;

import HollowMod.hollowMod;
import HollowMod.monsters.monsterLittleWeaver;
import HollowMod.powers.BaldurShellPower;
import HollowMod.powers.SoulMasterPower;
import HollowMod.powers.SoulPower;
import HollowMod.util.SoundEffects;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class SummonWeaverAction
  extends AbstractGameAction
{
  //private static final Logger logger;
  private AbstractMonster m;
  private int slotToFill;

  public SummonWeaverAction(final AbstractMonster[] weavers) {
    this.slotToFill = 0;
    this.actionType = ActionType.SPECIAL;
    if (Settings.FAST_MODE) {
      this.startDuration = Settings.ACTION_DUR_FAST;
    }
    else {
      this.startDuration = Settings.ACTION_DUR_LONG;
    }
    this.duration = this.startDuration;
    final int slot = this.identifySlot(weavers);
    if (slot == -1) {
      //SummonWeaverAction.logger.info("INCORRECTLY ATTEMPTED TO CHANNEL GREMLIN.");
      return;
    }
    weavers[this.slotToFill = slot] = (this.m = this.getWeaver(slot));
    if (AbstractDungeon.player.hasRelic("Philosopher's Stone")) {
      this.m.addPower(new StrengthPower(this.m, 1));
      AbstractDungeon.onModifyPower();
    }
  }

  private int identifySlot(final AbstractMonster[] weavers) {
    for (int i = 0; i < weavers.length; ++i) {
      if (weavers[i] == null || weavers[i].isDying) {
        return i;
      }
    }
    return -1;
  }

  private AbstractMonster getWeaver(final int slot) {

    float x = 0.0f;
    float y = 0.0f;
    switch (slot) {
      case 0: {
        x = -366.0f;
        y = -4.0f;
        break;
      }
      case 1: {
        x = -170.0f;
        y = 6.0f;
        break;
      }
      default: {
        x = -366.0f;
        y = -4.0f;
        break;
      }
    }
    return new monsterLittleWeaver(x,y);
  }

  @Override
  public void update() {
    if (this.duration == this.startDuration) {
      this.m.animX = 1200.0f * Settings.scale;
      this.m.init();
      this.m.applyPowers();
      AbstractDungeon.getCurrRoom().monsters.addMonster(this.slotToFill, this.m);
      if (ModHelper.isModEnabled("Lethality")) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
      }
      if (ModHelper.isModEnabled("Time Dilation")) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
      }
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new MinionPower(this.m)));
    }
    this.tickDuration();
    if (this.isDone) {
      this.m.animX = 0.0f;
      this.m.showHealthBar();
      this.m.usePreBattleAction();
    }
    else {
      this.m.animX = Interpolation.fade.apply(0.0f, 1200.0f * Settings.scale, this.duration);
    }
  }
}