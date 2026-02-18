public class Item
{
    String name;
    ItemType type;
    int bonus;

    public Item(String name, ItemType type, int bonus)
    {
        this.name = name;
        this.type = type;
        this.bonus = bonus;
    }

    @Override
    public String toString()
    {
        return name + (type == ItemType.WEAPON ? " (+" + bonus + " урона)" :
                (type == ItemType.POTION ? " (+" + bonus + " HP)" : " (мусор)"));
    }
}