import java.util.ArrayList;

public class Inventory
{
    ArrayList<Item> items = new ArrayList<>();
    int maxSize = 10;

    boolean addItem(Item item)
    {
        if (items.size() < maxSize)
        {
            items.add(item);
            return true;
        }
        else
        {
            System.out.println("Инвентарь полон!");
            return false;
        }
    }

    Item removeItem(int index)
    {
        if (index >= 0 && index < items.size())
        {
            return items.remove(index);
        }
        return null;
    }

    Item getItem(int index)
    {
        if (index >= 0 && index < items.size())
        {
            return items.get(index);
        }
        return null;
    }

    int size()
    {
        return items.size();
    }

    void show()
    {
        if (items.isEmpty())
        {
            System.out.println("Инвентарь пуст.");
        }
        else
        {
            System.out.println("Инвентарь:");
            for (int i = 0; i < items.size(); i++)
            {
                System.out.println("  " + (i+1) + ". " + items.get(i));
            }
        }
    }

    int getDamageBonus()
    {
        int maxBonus = 0;
        for (Item item : items)
        {
            if (item.type == ItemType.WEAPON && item.bonus > maxBonus)
            {
                maxBonus = item.bonus;
            }
        }
        return maxBonus;
    }

    Item usePotion()
    {
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).type == ItemType.POTION)
            {
                return items.remove(i);
            }
        }
        return null;
    }
}