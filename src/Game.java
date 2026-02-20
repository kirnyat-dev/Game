import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Game
{
    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();

    public static void main(String[] args)
    {
        Player player = null;
        Level currentLevel = null;
        int currentRoomIndex = 0;

        // Проверка сохранения
        Object[] loaded = SaveManager.load();
        if (loaded != null)
        {
            System.out.println("Найдено сохранение. Загрузить? (1 - да, 0 - новая игра)");
            String choice = scanner.nextLine();
            if (choice.equals("1"))
            {
                player = (Player) loaded[0];
                currentLevel = (Level) loaded[1];
                currentRoomIndex = (int) loaded[2];
            }
        }

        if (player == null)
        {
            // Новая игра - выбор класса
            System.out.println("Выберите класс:");
            System.out.println("1. Мечник (много брони, малый урон)");
            System.out.println("2. Лучник (средне)");
            System.out.println("3. Маг (мало брони, большой урон)");
            int classChoice = readInt("Ваш выбор: ", 1, 3);
            ClassType classType = null;
            switch (classChoice)
            {
                case 1: classType = ClassType.MELEE; break;
                case 2: classType = ClassType.ARCHER; break;
                case 3: classType = ClassType.MAGE; break;
            }
            player = new Player(classType);
            currentLevel = new Level(1);
            currentRoomIndex = 0;
        }

        Room currentRoom = currentLevel.getRoom(currentRoomIndex);

        System.out.println("Добро пожаловать в подземелье, " + player.classType + " " + player.name + "!");
        System.out.println("Вы находитесь на уровне " + currentLevel.number + ", комната " + (currentRoomIndex + 1));
        currentRoom.look();

        boolean gameOver = false;

        while (!gameOver && player.isAlive())
        {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Осмотреться");
            System.out.println("2. Инвентарь");
            System.out.println("3. Взять предмет");
            System.out.println("4. Выбросить предмет");
            System.out.println("5. Использовать зелье");
            System.out.println("6. Атаковать монстра");
            System.out.println("7. Перейти в другую комнату");
            System.out.println("8. Спуститься на следующий уровень (если есть выход)");
            System.out.println("9. Статус игрока");
            System.out.println("10. Торговец (если в комнате)");
            System.out.println("11. Сохранить игру");
            System.out.println("0. Выйти из игры");

            int choice = readInt("Ваш выбор: ", 0, 11);

            switch (choice)
            {
                case 1:
                    System.out.println("Комната " + (currentRoomIndex + 1));
                    currentRoom.look();
                    break;
                case 2:
                    player.inventory.show();
                    break;
                case 3:
                    takeItem(player, currentRoom);
                    break;
                case 4:
                    dropItem(player, currentRoom);
                    break;
                case 5:
                    player.usePotion();
                    break;
                case 6:
                    attackMonster(player, currentRoom);
                    break;
                case 7:
                    int newIndex = moveRoom(player, currentRoom);
                    if (newIndex != -1)
                    {
                        currentRoomIndex = newIndex;
                        currentRoom = currentLevel.getRoom(currentRoomIndex);
                        System.out.println("Вы перешли в комнату " + (currentRoomIndex + 1));
                        currentRoom.look();
                    }
                    break;
                case 8:
                    if (currentRoom.isExit)
                    {
                        if (currentLevel.number < 10)
                        {
                            currentLevel = new Level(currentLevel.number + 1);
                            currentRoomIndex = 0;
                            currentRoom = currentLevel.getRoom(0);
                            System.out.println("Вы спустились на уровень " + currentLevel.number);
                            currentRoom.look();
                        }
                        else
                        {
                            System.out.println("Поздравляем! Вы прошли все 10 уровней! Победа!");
                            gameOver = true;
                        }
                    }
                    else
                    {
                        System.out.println("В этой комнате нет выхода на следующий уровень.");
                    }
                    break;
                case 9:
                    player.showStatus();
                    break;
                case 10:
                    if (currentRoom.isShop)
                    {
                        openShop(player, currentRoom);
                    }
                    else
                    {
                        System.out.println("Здесь нет торговца.");
                    }
                    break;
                case 11:
                    SaveManager.save(player, currentLevel, currentRoomIndex);
                    break;
                case 0:
                    System.out.println("Выйти из игры? (1 - да, 0 - нет)");
                    int confirm = readInt("", 0, 1);
                    if (confirm == 1)
                    {
                        System.out.println("Выход из игры.");
                        gameOver = true;
                        break;
                    }
            }

            if (!player.isAlive())
            {
                System.out.println("Вы погибли... Игра окончена.");
                gameOver = true;
            }
        }
        scanner.close();
    }

    static int readInt(String prompt, int min, int max)
    {
        while (true)
        {
            System.out.print(prompt);
            try
            {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) return value;
                else System.out.println("Введите число от " + min + " до " + max);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Введите число.");
            }
        }
    }

    static void takeItem(Player player, Room room)
    {
        if (room.loot.isEmpty())
        {
            System.out.println("В комнате нет предметов.");
            return;
        }
        System.out.println("Выберите предмет (номер):");
        for (int i = 0; i < room.loot.size(); i++)
        {
            System.out.println((i+1) + ". " + room.loot.get(i));
        }
        int itemChoice = readInt("Номер предмета (0 - отмена): ", 0, room.loot.size());
        if (itemChoice == 0) return;
        Item item = room.loot.get(itemChoice - 1);
        player.takeItem(item, room);
    }

    static void dropItem(Player player, Room room)
    {
        if (player.inventory.size() == 0)
        {
            System.out.println("Инвентарь пуст.");
            return;
        }
        System.out.println("Выберите предмет из инвентаря (номер):");
        player.inventory.show();
        int itemChoice = readInt("Номер предмета (0 - отмена): ", 0, player.inventory.size());
        if (itemChoice == 0) return;
        Item item = player.inventory.getItem(itemChoice - 1);
        player.inventory.removeItem(itemChoice - 1);
        player.dropItem(item, room);
    }

    static void attackMonster(Player player, Room room)
    {
        if (room.monster == null || !room.monster.isAlive())
        {
            System.out.println("Здесь нет монстра.");
            return;
        }
        player.attack(room.monster);
        if (!room.monster.isAlive())
        {
            player.addCoins(room.monster.coinDrop);
            room.monster = null;
        }
        else
        {
            player.takeDamage(room.monster.damage);
        }
    }

    static int moveRoom(Player player, Room currentRoom)
    {
        if (currentRoom.monster != null && currentRoom.monster.isAlive())
        {
            System.out.println("Вы пытаетесь убежать от монстра...");
            if (rand.nextInt(100) < 50)
            {
                player.takeDamage(currentRoom.monster.damage);
                if (!player.isAlive()) return -1;
                if (rand.nextInt(100) < 60)
                {
                    System.out.println("Монстр преграждает путь! Вы не можете уйти.");
                    return -1;
                }
                else
                {
                    System.out.println("Вам удалось вырваться!");
                }
            }
            else
            {
                System.out.println("Монстр промахнулся, вы убегаете!");
            }
        }

        if (currentRoom.neighbors.isEmpty())
        {
            System.out.println("Из этой комнаты нет выходов.");
            return -1;
        }
        System.out.println("Доступные выходы:");
        for (int i = 0; i < currentRoom.neighbors.size(); i++)
        {
            System.out.println((i+1) + ". Комната " + (currentRoom.neighbors.get(i) + 1));
        }
        int moveChoice = readInt("Выберите выход (номер, 0 - отмена): ", 0, currentRoom.neighbors.size());
        if (moveChoice == 0) return -1;
        return currentRoom.neighbors.get(moveChoice - 1);
    }

    static void openShop(Player player, Room room)
    {
        System.out.println("Вы заходите в лавку торговца.");
        ArrayList<Item> shopItems = room.shopItems; // используем фиксированный ассортимент

        boolean shopping = true;
        while (shopping)
        {
            System.out.println("\n--- Лавка ---");
            System.out.println("У вас монет: " + player.coins);
            System.out.println("Товары:");
            for (int i = 0; i < shopItems.size(); i++)
            {
                System.out.println("  " + (i+1) + ". " + shopItems.get(i));
            }
            System.out.println("  " + (shopItems.size()+1) + ". Продать предмет");
            System.out.println("  " + (shopItems.size()+2) + ". Выйти из лавки");

            int choice = readInt("Ваш выбор: ", 1, shopItems.size()+2);
            if (choice >= 1 && choice <= shopItems.size())
            {
                Item item = shopItems.get(choice-1);
                if (player.inventory.size() < player.inventory.maxSize)
                {
                    if (player.spendCoins(item.price))
                    {
                        player.inventory.addItem(item);
                        shopItems.remove(choice-1);
                        System.out.println("Вы купили " + item.name);
                    }
                }
                else
                {
                    System.out.println("Инвентарь полон! Сначала освободите место.");
                }
            }
            else if (choice == shopItems.size()+1)
            {
                if (player.inventory.size() == 0)
                {
                    System.out.println("Нечего продавать.");
                }
                else
                {
                    System.out.println("Выберите предмет для продажи:");
                    player.inventory.show();
                    int sellChoice = readInt("Номер предмета (0 - отмена): ", 0, player.inventory.size());
                    if (sellChoice > 0)
                    {
                        Item item = player.inventory.getItem(sellChoice-1);
                        int sellPrice = item.price / 2;
                        System.out.println("Продать " + item.name + " за " + sellPrice + " монет? (1 - да, 0 - нет)");
                        int confirm = readInt("", 0, 1);
                        if (confirm == 1)
                        {
                            player.inventory.removeItem(sellChoice-1);
                            player.addCoins(sellPrice);
                            System.out.println("Предмет продан.");
                        }
                    }
                }
            }
            else
            {
                shopping = false;
            }
        }
    }
}