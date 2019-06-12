package HollowMod.events;

import HollowMod.cards.*;
import HollowMod.hollowMod;
import HollowMod.util.SoundEffects;
import basemod.BaseMod;
import basemod.helpers.BaseModCardTags;
import basemod.helpers.CardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.sun.crypto.provider.DESCipher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static HollowMod.hollowMod.makeEventPath;

public class MylasSongSadEvent extends AbstractImageEvent {


    public static final String ID = hollowMod.makeID("MylasSongSadEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("MylasSongSadEvent.png");
    private List<String> InfAtts;

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;


    public MylasSongSadEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        int temp1 = (int)(AbstractDungeon.player.maxHealth * 0.10F);
        int temp2 = (int)(AbstractDungeon.player.maxHealth * 0.25F);
        this.InfAtts = new ArrayList();
        // The first dialogue options available to us.
        imageEventText.setDialogOption(OPTIONS[1]); // Kill Myla
        imageEventText.setDialogOption(OPTIONS[2] + temp2 + OPTIONS[3]); // Radiance Lament, or Secret Ally Myla Card If player has Awoken Dream Nail, or Lord of Shades
        imageEventText.setDialogOption(OPTIONS[4]); // Dream Nail Learn More

    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.play(SoundEffects.MinerBad.getKey());
        }
    }

    @Override
    protected void buttonEffect(int i) { // This is the event:
        switch (screenNum) {
            case 0: // While you are on screen number 0 (The starting screen)
                switch (i) {
                    case 0: // If you press button the first button (Button at index 0), in this case: Inspiration.
                        CardCrawlGame.sound.play(SoundEffects.MinerDeath.getKey());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);

                        replaceAttacks();

                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        break;

                    case 1: // If you press button the second button (Button at index 1), in this case: Deinal
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.LONG, false);
                        // Shake the screen
                        CardCrawlGame.sound.play(SoundEffects.DreamNail.getKey());  // Play Dream Nail
                        int dmgAmount = (int)(AbstractDungeon.player.currentHealth * 0.25F);
                        boolean MylaLived = false;
                        if ((CardHelper.hasCardWithID(attackAwokenDreamNail.ID)) || CardHelper.hasCardWithID(powerLordofShades.ID)){
                            MylaLived = true;
                        }
                        AbstractDungeon.player.damage(new DamageInfo(null, dmgAmount));

                        if (!MylaLived) {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new powerGlowingWomb(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        } else {
                            this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new skillMylasSalvation(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        }
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
                    case 2: // If you press button the third button (Button at index 2), in this case: Acceptance
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                }
                break;
            case 1: // Welcome to screenNum = 1;
                AbstractDungeon.eventList.remove(MylasSongHappyEvent.ID);
                AbstractDungeon.eventList.remove(MylasSongSadEvent.ID);
                AbstractDungeon.eventList.remove(MylasSongSadEvent.ID);
                AbstractDungeon.eventList.remove(MylasSongSadEvent.ID);
                switch (i) {
                    case 0: // If you press the first (and this should be the only) button,
                        openMap(); // You'll open the map and end the event.
                        break;
                }
                break;
        }
    }
    private void replaceAttacks()
    {
        for (Iterator<AbstractCard> i = AbstractDungeon.player.masterDeck.group.iterator(); i.hasNext();)
        {
            AbstractCard e = (AbstractCard)i.next();
            if (e.hasTag(BaseModCardTags.BASIC_STRIKE)) {
                i.remove();
            }
        }
        for (int i = 0; i < 4; i++)
        {
            AbstractCard c = new attackInfectedAttack();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            this.InfAtts.add(c.cardID);
        }
    }

}
