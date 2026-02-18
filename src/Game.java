import java.util.Scanner;
import java.util.Random;

public class Game
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        Player player = new Player();
        Level currentLevel = new Level(1);
        int currentRoomIndex = 0;
        Room currentRoom = currentLevel.getRoom(currentRoomIndex);

        System.out.println("Добро пожаловать в подземелье, " + player.name + "!");
        System.out.println("Вы находитесь на уровне 1.");
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
            System.out.println("8. Спуститься на следующий уровень (только если есть выход)");
            System.out.println("9. Статус игрока");
            System.out.println("0. Выйти из игры");

            System.out.print("Ваш выбор: ");
            int choice;
            try
            {
                choice = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("Введите число.");
                continue;
            }

            switch (choice)
            {
                case 1:
                    currentRoom.look();
                    break;
                case 2:
                    player.inventory.show();
                    break;
                case 3:
                    if (currentRoom.loot.isEmpty())
                    {
                        System.out.println("В комнате нет предметов.");
                    }
                    else
                    {
                        System.out.println("Выберите предмет (номер):");
                        for (int i = 0; i < currentRoom.loot.size(); i++)
                        {
                            System.out.println((i+1) + ". " + currentRoom.loot.get(i));
                        }
                        System.out.print("Номер предмета (0 - отмена): ");
                        try
                        {
                            int itemChoice = Integer.parseInt(scanner.nextLine());
                            if (itemChoice == 0) break;
                            if (itemChoice > 0 && itemChoice <= currentRoom.loot.size())
                            {
                                Item item = currentRoom.loot.get(itemChoice - 1);
                                player.takeItem(item, currentRoom);
                            }
                            else
                            {
                                System.out.println("Неверный номер.");
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Введите число.");
                        }
                    }
                    break;
                case 4:
                    if (player.inventory.size() == 0)
                    {
                        System.out.println("Инвентарь пуст.");
                    }
                    else
                    {
                        System.out.println("Выберите предмет из инвентаря (номер):");
                        player.inventory.show();
                        System.out.print("Номер предмета (0 - отмена): ");
                        try
                        {
                            int itemChoice = Integer.parseInt(scanner.nextLine());
                            if (itemChoice == 0) break;
                            if (itemChoice > 0 && itemChoice <= player.inventory.size())
                            {
                                Item item = player.inventory.getItem(itemChoice - 1);
                                player.inventory.removeItem(itemChoice - 1);
                                player.dropItem(item, currentRoom);
                            }
                            else
                            {
                                System.out.println("Неверный номер.");
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Введите число.");
                        }
                    }
                    break;
                case 5:
                    player.usePotion();
                    break;
                case 6:
                    if (currentRoom.monster != null && currentRoom.monster.isAlive())
                    {
                        player.attack(currentRoom.monster);
                        if (!currentRoom.monster.isAlive())
                        {
                            currentRoom.monster = null;
                            System.out.println("Монстр побеждён!");
                        }
                        else
                        {
                            player.takeDamage(currentRoom.monster.damage);
                        }
                    }
                    else
                    {
                        System.out.println("Здесь нет монстра.");
                    }
                    break;
                case 7:
                    // Проверяем наличие живого монстра
                    if (currentRoom.monster != null && currentRoom.monster.isAlive())
                    {
                        System.out.println("Вы пытаетесь убежать от монстра...");
                        // 50% шанс, что монстр атакует
                        if (rand.nextInt(100) < 50)
                        {
                            player.takeDamage(currentRoom.monster.damage);
                            if (!player.isAlive())
                            {
                                System.out.println("Вы погибли... Игра окончена.");
                                gameOver = true;
                                break;
                            }
                            // 60% шанс, что игрок не сможет уйти (останется в комнате)
                            if (rand.nextInt(100) < 60)
                            {
                                System.out.println("Монстр преграждает путь! Вы не можете уйти.");
                                break; // не выполняем переход
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

                    // Если монстра нет или он мёртв, или после проверок игрок всё ещё может уйти
                    if (currentRoom.neighbors.isEmpty())
                    {
                        System.out.println("Из этой комнаты нет выходов.");
                    }
                    else
                    {
                        System.out.println("Доступные выходы:");
                        for (int i = 0; i < currentRoom.neighbors.size(); i++)
                        {
                            System.out.println((i+1) + ". Комната " + (currentRoom.neighbors.get(i) + 1));
                        }
                        System.out.print("Выберите выход (номер, 0 - отмена): ");
                        try
                        {
                            int moveChoice = Integer.parseInt(scanner.nextLine());
                            if (moveChoice == 0) break;
                            if (moveChoice > 0 && moveChoice <= currentRoom.neighbors.size())
                            {
                                int newIndex = currentRoom.neighbors.get(moveChoice - 1);
                                currentRoomIndex = newIndex;
                                currentRoom = currentLevel.getRoom(currentRoomIndex);
                                System.out.println("Вы перешли в комнату " + (currentRoomIndex + 1));
                                currentRoom.look();
                            }
                            else
                            {
                                System.out.println("Неверный номер.");
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Введите число.");
                        }
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
                case 0:
                    System.out.println("Выход из игры.");
                    gameOver = true;
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }

            if (!player.isAlive())
            {
                System.out.println("Вы погибли... Игра окончена.");
                gameOver = true;
            }
        }
        scanner.close();
    }
}