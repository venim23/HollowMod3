package HollowMod.cards;


import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class HollowSpellCard extends AbstractDefaultCard {

    public int SOUL_COST;
	// this is a custom card type that extends the abstractdefaultcardtype, this hopefully will allow an easier ability to use "Focus" cards as spell costs. 

    public HollowSpellCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                               final CardType type, final CardColor color,
                               final CardRarity rarity, final CardTarget target, int soulcost) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
		
		this.SOUL_COST = soulcost;
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m)
	{
      this.cantUseMessage = "Not enough #ySõul.";
      return new HollowMod.actions.FocusSoulAction(AbstractDungeon.player, this.SOUL_COST).canFocusSoulAction();
		//Calls an action that checks if the player can use that ability or not
  }

   
}