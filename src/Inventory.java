import java.util.ArrayList;

public class Inventory
{
    ArrayList<Item> items = new ArrayList<>();
    int maxSize = 15; // увеличим, так как добавились предметы

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

    // Суммарная броня от всех предметов
    int getTotalArmor()
    {
        int total = 0;
        for (Item item : items)
        {
            if (item.type == ItemType.ARMOR)
            {
                total += item.bonus;
            }
        }
        return total;
    }

    // Бонус урона в зависимости от класса и типа оружия
    int getDamageBonus(ClassType classType)
    {
        int maxBonus = 0;
        for (Item item : items)
        {
            if (item.type == ItemType.WEAPON)
            {
                // Проверяем соответствие оружия классу
                boolean canUse = false;
                if (classType == ClassType.MELEE && item.name.contains("Меч")) canUse = true;
                if (classType == ClassType.ARCHER && item.name.contains("Лук")) canUse = true;
                if (classType == ClassType.MAGE && item.name.contains("Книга")) canUse = true;
                if (canUse && item.bonus > maxBonus)
                {
                    maxBonus = item.bonus;
                }
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