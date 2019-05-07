package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.patches.CardTagEnum;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.UIStrings;
import basemod.helpers.TooltipInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;



public abstract class AbstractHollowCard extends CustomCard {

    // Custom Abstract Cards can be a bit confusing. While this is a simple base for simply adding a second magic number,
    // if you're new to modding I suggest you skip this file until you know what unique things that aren't provided
    // by default, that you need in your own cards. For now, go check out the other cards.

    // In this example, we use a custom Abstract Card in order to define a new magic number. From here on out, we can
    // simply use that in our cards, so long as we put "extends AbstractHollowCard" instead of "extends CustomCard" at the start.
    // In simple terms, it's for things that we don't want to define again and again in every single card we make.

    public int defaultSecondMagicNumber;        // Just like magic number, or any number for that matter, we want our regular, modifiable stat
    public int defaultBaseSecondMagicNumber;    // And our base stat - the number in it's base state. It will reset to that by default.
    public boolean upgradedDefaultSecondMagicNumber; // A boolean to check whether the number has been upgraded or not.
    public boolean isDefaultSecondMagicNumberModified;
    public int hollowFocusCost;
    public int hollowBaseFocusCost;
    public boolean upgradedHollowFocusCost;
    public boolean isHollowFocusCostModified;

    private static final UIStrings focusStrings = CardCrawlGame.languagePack.getUIString(hollowMod.makeID("Focus"));
    private static final UIStrings infectionStrings = CardCrawlGame.languagePack.getUIString(hollowMod.makeID("Infection"));
    private static final UIStrings voidStrings = CardCrawlGame.languagePack.getUIString(hollowMod.makeID("Void"));


    public static final TooltipInfo focusTooltip = new TooltipInfo(focusStrings.TEXT[0], focusStrings.TEXT[1]);
    public static final TooltipInfo infectionTooltip = new TooltipInfo(infectionStrings.TEXT[0], infectionStrings.TEXT[1]);
    public static final TooltipInfo voidTooltip = new TooltipInfo(voidStrings.TEXT[0], voidStrings.TEXT[1]);



    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());
    private static ArrayList<TooltipInfo> ColorKeywordTooltip;
    // A boolean to check whether the number has been modified or not, for coloring purposes. (red/green)

    public AbstractHollowCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                              final CardType type, final CardColor color,
                              final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDefaultSecondMagicNumberModified = false;
        isHollowFocusCostModified = false;



    }

    public AbstractHollowCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                              final CardType type, final CardColor color,
                              final CardRarity rarity, final CardTarget target, final int focuscost) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDefaultSecondMagicNumberModified = false;
        isHollowFocusCostModified = false;
        this.hollowFocusCost = focuscost;
    }


    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        if (this.hollowFocusCost > 0) {
            this.cantUseMessage = "Not enough #rSoul.";
            return new HollowMod.actions.FocusSoulAction(AbstractDungeon.player, this.hollowFocusCost).canFocusSoulAction();
            //Calls an action that checks if the player can use that ability or not
        }
        return true;
    }
    public void upgradeFocusCost(int newcost)
    {
        this.hollowBaseFocusCost = newcost;
        hollowFocusCost = hollowBaseFocusCost;
        this.upgradedHollowFocusCost = true;
    }

    public void displayUpgrades() { // Display the upgrade - when you click a card to upgrade it
        super.displayUpgrades();
        if (upgradedDefaultSecondMagicNumber) { // If we set upgradedDefaultSecondMagicNumber = true in our card.
            defaultSecondMagicNumber = defaultBaseSecondMagicNumber; // Show how the number changes, as out of combat, the base number of a card is shown.
            isDefaultSecondMagicNumberModified = true; // Modified = true, color it green to highlight that the number is being changed.
        }
        if (upgradedHollowFocusCost){
            hollowFocusCost = hollowBaseFocusCost;
            isHollowFocusCostModified = true;
        }
    }

    /*@Override
    public List<TooltipInfo> getCustomTooltips() {
        String IDFOCUS = hollowMod.makeID("Focus");
        UIStrings uiStringsFOCUS = CardCrawlGame.languagePack.getUIString(IDFOCUS);


        String IDSOUL = hollowMod.makeID("Soul");
        UIStrings uiStringsSOUL = CardCrawlGame.languagePack.getUIString(IDSOUL);
        String IDINFECTION = hollowMod.makeID("Infection");
        UIStrings uiStringsINFECTION = CardCrawlGame.languagePack.getUIString(IDINFECTION);
        String IDVOID = hollowMod.makeID("Void");
        UIStrings uiStringsVOID = CardCrawlGame.languagePack.getUIString(IDVOID);
        if (ColorKeywordTooltip == null)
        {
            ColorKeywordTooltip = new ArrayList<>();


            ColorKeywordTooltip.add(new TooltipInfo(
                    uiStringsFOCUS.TEXT[0],
                    uiStringsFOCUS.TEXT[1]
            ));
            return ColorKeywordTooltip;
            ColorKeywordTooltip.add(new TooltipInfo(
                    uiStringsSOUL.TEXT[0],
                    uiStringsSOUL.TEXT[1]
            ));
            return ColorKeywordTooltip;
            ColorKeywordTooltip.add(new TooltipInfo(
                    uiStringsINFECTION.TEXT[0],
                    uiStringsINFECTION.TEXT[1]
            ));
            return ColorKeywordTooltip;
            ColorKeywordTooltip.add(new TooltipInfo(
                    uiStringsVOID.TEXT[0],
                    uiStringsVOID.TEXT[1]
            ));
            return ColorKeywordTooltip;


        }
    }*/





    @Override
    public List<TooltipInfo> getCustomTooltips() {
        ArrayList<TooltipInfo> tooltips = new ArrayList<>();

        if (hasTag(CardTagEnum.VOID))
            tooltips.add(voidTooltip);

        if ((hasTag(CardTagEnum.SPELL)) && (!hasTag(CardTagEnum.VOID)))
            tooltips.add(focusTooltip);

        if (hasTag(CardTagEnum.INFECTION))
            tooltips.add(infectionTooltip);

        return tooltips;
    }

    public void upgradeDefaultSecondMagicNumber(int amount) { // If we're upgrading (read: changing) the number. Note "upgrade" and NOT "upgraded" - 2 different things. One is a boolean, and then this one is what you will usually use - change the integer by how much you want to upgrade.
        defaultBaseSecondMagicNumber += amount; // Upgrade the number by the amount you provide in your card.
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber; // Set the number to be equal to the base value.
        upgradedDefaultSecondMagicNumber = true; // Upgraded = true - which does what the above method does.
    }
}