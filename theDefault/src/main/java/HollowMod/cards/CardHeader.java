package HollowMod.cards;

public final class CardHeader
{
    public final int ID;
    public final String NAME;

    public CardHeader(int id, String name)
    {
        ID = id;
        NAME = name;
    }

    public boolean Equals(CardHeader other)
    {
        return other != null && ID == other.ID;
    }
}
