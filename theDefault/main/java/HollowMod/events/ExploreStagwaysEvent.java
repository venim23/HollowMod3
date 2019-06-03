package HollowMod.events;

import HollowMod.characters.TheBugKnight;
import HollowMod.hollowMod;
import HollowMod.util.SoundEffects;
import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static HollowMod.hollowMod.makeEventPath;

public class ExploreStagwaysEvent extends AbstractImageEvent {


    public static final String ID = hollowMod.makeID("TheStagways");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("stagstate1.png");
    public static final String IMG2 = makeEventPath("stagstate2.png");
    public static final String IMG3 = makeEventPath("stagstate3.png");
    public static final String IMG4 = makeEventPath("stagstate4.png");
    private static int goldcost = 75;
    private int stations = 0;

    private int screenNum = 0; // The initial screen we will see when encountering the event - screen 0;


    public ExploreStagwaysEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);


        if (AbstractDungeon.player.gold >= goldcost) {
            this.imageEventText.setDialogOption(OPTIONS[1] + goldcost + OPTIONS[2]);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[3] + OPTIONS[4], true);
        }
        imageEventText.setDialogOption(OPTIONS[0]); // Leave


    }

    public void onEnterRoom()
    {
        if (Settings.AMBIANCE_ON) {
            CardCrawlGame.sound.playV(SoundEffects.Stag.getKey(), 2.0F);
        }
    }

    @Override
    protected void buttonEffect(int i) { // This is the event:
        switch (screenNum) {
            case 0: // While you are on screen number 0 (The starting screen)
                switch (i) {
                    case 0: // Proceed

                        AbstractDungeon.player.loseGold(75);
                        this.stations += 1;

                        //ADD EVENT TWO TO THE POOL
                        this.imageEventText.clearAllDialogs();
                        if (AbstractDungeon.player.gold >= goldcost) {
                            this.imageEventText.setDialogOption(OPTIONS[1] + goldcost + OPTIONS[2]);
                        } else {
                            this.imageEventText.setDialogOption(OPTIONS[3] + goldcost +OPTIONS[4], true);
                        }
                        this.imageEventText.setDialogOption( OPTIONS[0]);

                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        screenNum = 2;
                        this.imageEventText.loadImage(IMG2);

                        // Screen set the screen number to 1. Once we exit the switch (i) statement,
                        // we'll still continue the switch (screenNum) statement. It'll find screen 1 and do it's actions
                        // (in our case, that's the final screen, but you can chain as many as you want like that)



                        break; // Onto screen 1 we go.
                    case 1: // If you press button the second button (Button at index 1), in this case: Deinal
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
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
                   /* case 2: // If you press button the third button (Button at index 2), in this case: Acceptance
                        CardCrawlGame.sound.playV(SoundEffects.DreamNail.getKey(), 1.4F);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 5;
                        break; */
                }
                break;
            case 1: // Leave Screen
                switch (i) {
                    case 0: // If you press the first (and this should be the only) button,
                        openMap(); // You'll open the map and end the event.
                        break;
                }
                break;
            case 2: // Station 2
                switch (i) {
                    case 0: // Proceed

                        CardCrawlGame.sound.playV(SoundEffects.Stag1.getKey(), 2.0F);
                        AbstractDungeon.player.loseGold(75);
                        this.stations += 1;

                        //ADD EVENT TWO TO THE POOL
                        this.imageEventText.clearAllDialogs();
                        if (AbstractDungeon.player.gold >= goldcost) {
                            this.imageEventText.setDialogOption(OPTIONS[1] + goldcost + OPTIONS[2]);
                        } else {
                            this.imageEventText.setDialogOption(OPTIONS[3] + goldcost + OPTIONS[4], true);
                        }
                        this.imageEventText.setDialogOption(OPTIONS[0]);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 3;
                        this.imageEventText.loadImage(IMG3);


                        break;
                    case 1: // Leave
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        this.imageEventText.setDialogOption(OPTIONS[5]);
                        this.imageEventText.setDialogOption(OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 5;

                        break; // Onto screen 1 we go.
                }
                break;
            case 3: // Station 3
                switch (i) {
                    case 0: // Proceed
                        CardCrawlGame.sound.playV(SoundEffects.Stag2.getKey(), 2.0F);
                        AbstractDungeon.player.loseGold(75);
                        this.stations += 1;

                        //ADD EVENT TWO TO THE POOL
                        this.imageEventText.setDialogOption(OPTIONS[7]);
                        this.imageEventText.setDialogOption(OPTIONS[0]);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();

                        screenNum = 4;
                        this.imageEventText.loadImage(IMG4);


                        break; // Onto screen 1 we go.
                    case 1: // If you press button the second button (Button at index 1), in this case: Deinal

                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.updateDialogOption(1, OPTIONS[0]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 5;

                        break; //
                }
                break;
            case 4: // Station 4
                switch (i) {
                    case 0: // Proceed
                        CardCrawlGame.sound.playV(SoundEffects.Stag3.getKey(), 2.0F);
                        AbstractDungeon.player.loseGold(75);
                        this.stations += 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);// 1. Change the first button to the [Leave] button
                        this.imageEventText.clearRemainingOptions();// 2. and remove all others
                        screenNum = 5;


                        break; // Onto screen 1 we go.
                }
                break;
            case 5: // PRELEAVE
                switch (i) {
                    case 0: // Proceed
                        payout(this.stations);
                        this.imageEventText.updateDialogOption(0, OPTIONS[0]);// 1. Change the first button to the [Leave] button
                        this.imageEventText.clearRemainingOptions();// 2. and remove all others
                        screenNum = 1;
                        break; // Onto screen 1 we go.
                }
                break;
        }
    }

    private void payout(int i){
        switch (i){
            case 0: //max hp
                break;
            case 1: //stag pass
                break;
            case 2: //the last stag
                break;
            case 3:
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