import java.util.ArrayList;

public class Room
{
    String description = "Обычная комната";
    ArrayList<Item> loot = new ArrayList<>();
    Monster monster = null;
    ArrayList<Integer> neighbors = new ArrayList<>();
    boolean isExit = false;
    boolean isShop = false;
    ArrayList<Item> shopItems = new ArrayList<>(); // товары в магазине (генерируются при создании уровня)

    void addLoot(Item item)
    {
        loot.add(item);
    }

    void look()
    {
        System.out.println("Вы в комнате: " + description);
        if (monster != null && monster.isAlive())
        {
            System.out.print("Монстр: ");
            monster.show();
        }
        else
        {
            System.out.println("Монстров нет.");
        }
        if (loot.isEmpty())
        {
            System.out.println("Предметов нет.");
        }
        else
        {
            System.out.println("Вы видите предметы:");
            for (int i = 0; i < loot.size(); i++)
            {
                System.out.println("  " + (i+1) + ". " + loot.get(i));
            }
        }
        if (isShop)
        {
            System.out.println("Здесь находится торговец! (можно купить предметы)");
        }
        if (isExit)
        {
            System.out.println("Здесь есть лестница вниз, на следующий уровень.");
        }
    }

    void showExits()
    {
        if (neighbors.isEmpty())
        {
            System.out.println("Из этой комнаты нет выходов.");
        }
        else
        {
            System.out.println("Доступные выходы:");
            for (int i = 0; i < neighbors.size(); i++)
            {
                System.out.println("  " + (i+1) + ". Комната " + (neighbors.get(i) + 1));
            }
        }
    }
}