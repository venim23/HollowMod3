package HollowMod.cards;

import HollowMod.hollowMod;
import HollowMod.util.Utilities;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import HollowMod.util.Utilities;

import java.util.ArrayList;
import java.util.HashMap;


public class CardHeaders
{
    public static final String ID = hollowMod.makeID("CardHeaders");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private final static HashMap<Integer, CardHeader> All = new HashMap<>();

    public final static CardHeader None = CreateCardHeader(0);
    public final static CardHeader Dash = CreateCardHeader(1);
    public final static CardHeader Spell = CreateCardHeader(2);
    public final static CardHeader Ally = CreateCardHeader(3);
    public final static CardHeader AllyDash = CreateCardHeader(4);
    public final static CardHeader DashSpell = CreateCardHeader(5);
    public final static CardHeader AllySpell = CreateCardHeader(6);
    public final static CardHeader AllyDashSpell = CreateCardHeader(7);


    private static CardHeader CreateCardHeader(int id)
    {
        CardHeader s = new CardHeader(id, uiStrings.TEXT[id]);
        All.put(id, s);
        return s;
    }

    public static CardHeader GetByID(int id)
    {
        return All.get(id);
    }

    public static int Count()
    {
        return All.size() - 1;
    }

    public static ArrayList<AbstractHollowCard> GetAbstractHollowCards()
    {
        ArrayList<AbstractHollowCard> result = new ArrayList<>();
        AddAbstractHollowCards(AbstractDungeon.srcCommonCardPool.group, result);
        AddAbstractHollowCards(AbstractDungeon.srcUncommonCardPool.group, result);
        AddAbstractHollowCards(AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static ArrayList<AbstractHollowCard> GetCardsWithCardHeader(CardHeader cardheader)
    {
        ArrayList<AbstractHollowCard> result = new ArrayList<>();
        AddCardsWithCardHeader(cardheader, AbstractDungeon.srcCommonCardPool.group, result);
        AddCardsWithCardHeader(cardheader, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCardsWithCardHeader(cardheader, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }


    public static void AddAbstractHollowCards(ArrayList<AbstractCard> source, ArrayList<AbstractHollowCard> destination)
    {
        for (AbstractCard c : source)
        {
            AbstractHollowCard card = Utilities.SafeCast(c, AbstractHollowCard.class);
            if (card != null)
            {
                destination.add(card);
            }
        }
    }

    public static void AddCardsWithCardHeader(CardHeader cardheader, ArrayList<AbstractCard> source, ArrayList<AbstractHollowCard> destination)
    {
        for (AbstractCard c : source)
        {
            AbstractHollowCard card = Utilities.SafeCast(c, AbstractHollowCard.class);
            if (card != null && cardheader.Equals(card.GetCardHeader()))
            {
                destination.add(card);
            }
        }
    }
}