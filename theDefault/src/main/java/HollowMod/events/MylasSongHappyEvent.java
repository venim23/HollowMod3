package HollowMod.events;

import HollowMod.hollowMod;
import HollowMod.util.SoundEffects;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static HollowMod.hollowMod.makeEventPath;

public class MylasSongHappyEvent extends AbstractImageEvent {


    public static final String ID = hollowMod.makeID("MylasSongHappyEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("MylasSongHappyEvent.png");

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;


    public MylasSongHappyEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        int temp = (int)(AbstractDungeon.player.maxHealth * 0.25F);
        // The first dialogue options available to us.
        imageEventText.setDialogOption(OPTIONS[1]); // Remove a card
        imageEventText.setDialogOption(OPTIONS[3] + temp + OPTIONS[4]); // Heal up Some
        imageEventText.setDialogOption(OPTIONS[5]); // Dream Nail Learn More

    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.play(SoundEffects.MinerGood.getKey());
        }
    }

    @Override
    protected void buttonEffect(int i) { // This is the event:
        switch (screenNum) {
            case 0: // While you are on screen number 0 (The starting screen)
                switch (i) {
                    case 0: // If you press button the first button (Button at index 0), in this case: Inspiration.
                        {
                            if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0)
                            {
                                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);// Update the text of the event
                                AbstractDungeon.gridSelectScreen.open(
                                        CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                                .getPurgeableCards()), 1, OPTIONS[2], false);
                            }
                            this.imageEventText.updateDialogOption(0, OPTIONS[0]);// 1. Change the first button to the [Leave] button
                            this.imageEventText.clearRemainingOptions();// 2. and remove all others
                        }

                        //ADD EVENT TWO TO THE POOL

                        screenNum = 1; // Screen set the screen number to 1. Once we exit the switch (i) statement,
                        // we'll still continue the switch (screenNum) statement. It'll find screen 1 and do it's actions
                        // (in our case, that's the final screen, but you can chain as many as you want like that)



                        break; // Onto screen 1 we go.
                    case 1: // If you press button the second button (Button at index 1), in this case: Deinal

                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.LONG, false);
                        // Shake the screen
                        CardCrawlGame.sound.play(SoundEffects.MinerLyrics.getKey());  // Play Myla Singing
                        int healAmt = (int)(AbstractDungeon.player.maxHealth * 0.25F);
                        AbstractDungeon.player.heal(healAmt);
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
                    case 2: // If you press button the third button (Button at index 2), in this case: Acceptance
                        CardCrawlGame.sound.play(SoundEffects.DreamNail.getKey());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                }
                break;
            case 1: // Welcome to screenNum = 1;
                BaseMod.addEvent(MylasSongSadEvent.ID, MylasSongSadEvent.class);
                switch (i) {
                    case 0: // If you press the first (and this should be the only) button,
                        openMap(); // You'll open the map and end the event.
                        break;
                }
                break;
        }
    }

    public void update() { // We need the update() when we use grid screens (such as, in this case, the screen for selecting a card to remove)
        super.update(); // Do everything the original update()
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) { // Once the grid screen isn't empty (we selected a card for removal)
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0); // Get the card
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2))); // Create the card removal effect
            AbstractDungeon.player.masterDeck.removeCard(c); // Remove it from the deck
            AbstractDungeon.gridSelectScreen.selectedCards.clear(); // Or you can .remove(c) instead of clear,
            // if you want to continue using the other selected cards for something
        }

    }

}
