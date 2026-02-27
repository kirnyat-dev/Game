public class Item
{
    String name;
    ItemType type;
    int bonus;
    int price;

    public Item(String name, ItemType type, int bonus, int price)
    {
        this.name = name;
        this.type = type;
        this.bonus = bonus;
        this.price = price;
    }

    @Override
    public String toString()
    {
        String info = name;
        if (type == ItemType.WEAPON) info += " (+" + bonus + " урона)";
        else if (type == ItemType.ARMOR) info += " (+" + bonus + " брони)";
        else if (type == ItemType.POTION) info += " (+" + bonus + " HP)";
        info += " | цена: " + price;
        return info;
    }
}