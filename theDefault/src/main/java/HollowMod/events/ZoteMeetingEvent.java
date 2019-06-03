package HollowMod.events;

import HollowMod.hollowMod;
import HollowMod.monsters.eventZote;
import HollowMod.relics.GreatKnightMask;
import HollowMod.relics.LifeEnderRelic;
import HollowMod.util.SoundEffects;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static HollowMod.hollowMod.makeEventPath;

public class ZoteMeetingEvent extends AbstractImageEvent {


    public static final String ID = hollowMod.makeID("ZoteMeetingEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("ZoteMeetingEvent.png");
    private AbstractRelic relicMetric;
    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;


    public ZoteMeetingEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        // The first dialogue options available to us.
        imageEventText.setDialogOption(OPTIONS[1]); // BattleZote
        imageEventText.setDialogOption(OPTIONS[2]); // Pay Zote

    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.playV(SoundEffects.Zote.getKey(), 2.0F);
        }
    }

    public void update()
    {
        super.update();
        if (!RoomEventDialog.waitForInput) {
            buttonEffect(this.roomEventText.getSelectedOption());
        }
    }

    @Override
    protected void buttonEffect(int i) { // This is the event:
        switch (screenNum) {
            case 0: // While you are on screen number 0 (The starting screen)
                switch (i) {
                    case 0: // Fight Zote
                    {
                        this.screenNum = 2;
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(eventZote.ID);


                        AbstractDungeon.getCurrRoom().rewards.clear();
                        if (Settings.isDailyRun) {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(30));
                        } else {
                            AbstractDungeon.getCurrRoom().addGoldToRewards(AbstractDungeon.miscRng.random(25, 35));
                        }

                        if (AbstractDungeon.player.hasRelic(GreatKnightMask.ID)) {
                            AbstractDungeon.getCurrRoom().addRelicToRewards(new Circlet());
                        } else {
                            AbstractDungeon.getCurrRoom().addRelicToRewards(new GreatKnightMask());
                        }

                        this.enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = eventZote.ID;
                        return;
                        // Screen set the screen number to 1. Once we exit the switch (i) statement,
                        // we'll still continue the switch (screenNum) statement. It'll find screen 1 and do it's actions
                        // (in our case, that's the final screen, but you can chain as many as you want like that)


                        // Onto screen 1 we go.
                    }
                    case 1: // Pay Zote
                        CardCrawlGame.sound.play("GOLD_JINGLE");
                        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.LONG, false);
                        // Shake the screen
                        if (AbstractDungeon.player.hasRelic(LifeEnderRelic.ID)) {
                            this.relicMetric = RelicLibrary.getRelic(Circlet.ID).makeCopy();
                        }
                        else {
                            this.relicMetric = RelicLibrary.getRelic(LifeEnderRelic.ID).makeCopy();
                        }
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), this.relicMetric);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;

                        // Same as before. A note here is that you can also do
                        // imageEventText.clearAllDialogs();
                        // imageEventText.setDialogOption(OPTIONS[1]);
                        // imageEventText.setDialogOption(OPTIONS[4]);
                        // (etc.)
                        // And that would also just set them into slot 0, 1, 2... in order, just like what we do in the very beginning

                        break; // Onto screen 1 we go.
                }
                this.imageEventText.clearRemainingOptions();
                break;
            case 1: // Welcome to screenNum = 1;
                switch (i) {
                    case 0: // If you press the first (and this should be the only) button,
                        openMap(); // You'll open the map and end the event.
                        break;
                }
                break;
            default: // Welcome to screenNum = 1;
                openMap(); // You'll open the map and end the event.
                break;
        }
    }


}
