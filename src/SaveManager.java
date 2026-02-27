import java.io.*;
import java.util.*;

public class SaveManager
{
    static final String SAVE_FILE = "save.txt";

    public static void save(Player player, Level level, int roomIndex)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(SAVE_FILE)))
        {
            out.println("1");
            // Игрок
            out.println(player.classType.name());
            out.println(player.currentHp);
            out.println(player.coins);
            // Текущий уровень и комната
            out.println(level.number);
            out.println(roomIndex);
            // Количество комнат в уровне
            out.println(level.rooms.size());
            // Сохраняем каждую комнату
            for (int i = 0; i < level.rooms.size(); i++)
            {
                Room room = level.rooms.get(i);
                // Флаги
                out.println(room.isExit ? "1" : "0");
                out.println(room.isShop ? "1" : "0");
                // Предметы в комнате (loot)
                out.println(room.loot.size());
                for (Item item : room.loot)
                {
                    out.println(item.name);
                    out.println(item.type.name());
                    out.println(item.bonus);
                    out.println(item.price);
                }
                // Монстр
                if (room.monster != null && room.monster.isAlive())
                {
                    out.println("1");
                    out.println(room.monster.name);
                    out.println(room.monster.hp);
                    out.println(room.monster.damage);
                    out.println(room.monster.coinDrop);
                }
                else
                {
                    out.println("0");
                }
                // Соседи
                out.println(room.neighbors.size());
                for (int neighbor : room.neighbors)
                {
                    out.println(neighbor);
                }
                // Товары в магазине (если есть)
                if (room.isShop)
                {
                    out.println(room.shopItems.size());
                    for (Item item : room.shopItems)
                    {
                        out.println(item.name);
                        out.println(item.type.name());
                        out.println(item.bonus);
                        out.println(item.price);
                    }
                }
            }
            // Инвентарь игрока
            out.println(player.inventory.size());
            for (int i = 0; i < player.inventory.size(); i++)
            {
                Item item = player.inventory.getItem(i);
                out.println(item.name);
                out.println(item.type.name());
                out.println(item.bonus);
                out.println(item.price);
            }
            System.out.println("Игра сохранена.");
        }
        catch (IOException e)
        {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // Загрузка игры. Возвращает массив: [Player, Level, roomIndex] или null
    public static Object[] load()
    {
        File file = new File(SAVE_FILE);
        if (!file.exists())
        {
            return null;
        }
        try (Scanner scanner = new Scanner(file))
        {
            // Версия
            String version = scanner.nextLine(); // пока не используем

            // Игрок
            Player player = new Player();
            String className = scanner.nextLine();
            player.classType = ClassType.valueOf(className);
            player.currentHp = Integer.parseInt(scanner.nextLine());
            player.coins = Integer.parseInt(scanner.nextLine());

            // Уровень и комната
            int levelNumber = Integer.parseInt(scanner.nextLine());
            int roomIndex = Integer.parseInt(scanner.nextLine());
            int roomCount = Integer.parseInt(scanner.nextLine());

            // Восстанавливаем уровень
            Level level = new Level();
            level.number = levelNumber;
            level.rooms = new ArrayList<>();

            for (int i = 0; i < roomCount; i++)
            {
                Room room = new Room();
                // Флаги
                room.isExit = scanner.nextLine().equals("1");
                room.isShop = scanner.nextLine().equals("1");

                // Предметы в комнате
                int lootSize = Integer.parseInt(scanner.nextLine());
                for (int j = 0; j < lootSize; j++)
                {
                    String name = scanner.nextLine();
                    ItemType type = ItemType.valueOf(scanner.nextLine());
                    int bonus = Integer.parseInt(scanner.nextLine());
                    int price = Integer.parseInt(scanner.nextLine());
                    room.loot.add(new Item(name, type, bonus, price));
                }

                // Монстр
                int hasMonster = Integer.parseInt(scanner.nextLine());
                if (hasMonster == 1)
                {
                    String mName = scanner.nextLine();
                    int mHp = Integer.parseInt(scanner.nextLine());
                    int mDamage = Integer.parseInt(scanner.nextLine());
                    int mCoin = Integer.parseInt(scanner.nextLine());
                    room.monster = new Monster(mName, mHp, mDamage, mCoin);
                }

                // Соседи
                int neighborCount = Integer.parseInt(scanner.nextLine());
                for (int j = 0; j < neighborCount; j++)
                {
                    room.neighbors.add(Integer.parseInt(scanner.nextLine()));
                }

                // Товары магазина
                if (room.isShop)
                {
                    int shopSize = Integer.parseInt(scanner.nextLine());
                    for (int j = 0; j < shopSize; j++)
                    {
                        String name = scanner.nextLine();
                        ItemType type = ItemType.valueOf(scanner.nextLine());
                        int bonus = Integer.parseInt(scanner.nextLine());
                        int price = Integer.parseInt(scanner.nextLine());
                        room.shopItems.add(new Item(name, type, bonus, price));
                    }
                }

                level.rooms.add(room);
            }

            // Инвентарь игрока
            int invSize = Integer.parseInt(scanner.nextLine());
            player.inventory = new Inventory();
            for (int i = 0; i < invSize; i++)
            {
                String name = scanner.nextLine();
                ItemType type = ItemType.valueOf(scanner.nextLine());
                int bonus = Integer.parseInt(scanner.nextLine());
                int price = Integer.parseInt(scanner.nextLine());
                Item item = new Item(name, type, bonus, price);
                player.inventory.addItem(item);
            }

            // Восстанавливаем базовые статы игрока
            switch (player.classType)
            {
                case MELEE:
                    player.maxHp = 120;
                    player.baseDamage = 8;
                    player.baseArmor = 5;
                    break;
                case ARCHER:
                    player.maxHp = 100;
                    player.baseDamage = 12;
                    player.baseArmor = 3;
                    break;
                case MAGE:
                    player.maxHp = 80;
                    player.baseDamage = 16;
                    player.baseArmor = 1;
                    break;
            }

            System.out.println("Игра загружена.");
            return new Object[]{player, level, roomIndex};
        }
        catch (Exception e)
        {
            System.out.println("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}