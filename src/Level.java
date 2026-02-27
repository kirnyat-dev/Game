import java.util.ArrayList;
import java.util.Random;

public class Level
{
    int number;
    ArrayList<Room> rooms = new ArrayList<>();
    Random rand = new Random();

    public Level(int number)
    {
        this.number = number;
        generateLevel();
    }

    public Level() {}

    void generateLevel()
    {
        int roomCount = 7 + rand.nextInt(3);
        System.out.println("Генерация уровня " + number + " с " + roomCount + " комнатами...");

        for (int i = 0; i < roomCount; i++)
        {
            Room room = new Room();
            int itemCount = rand.nextInt(5);
            for (int j = 0; j < itemCount; j++)
            {
                room.addLoot(randomItem(false));
            }
            rooms.add(room);
        }

        int monsterCount = 3 + rand.nextInt(3);
        for (int m = 0; m < monsterCount; m++)
        {
            int roomIndex;
            do
            {
                roomIndex = rand.nextInt(rooms.size());
            }
            while (rooms.get(roomIndex).monster != null);
            rooms.get(roomIndex).monster = randomMonster();
        }

        int exitIndex = rand.nextInt(rooms.size());
        rooms.get(exitIndex).isExit = true;

        int shopIndex;
        do
        {
            shopIndex = rand.nextInt(rooms.size());
        }
        while (shopIndex == exitIndex || rooms.get(shopIndex).monster != null);
        rooms.get(shopIndex).isShop = true;

        generateShopItems(rooms.get(shopIndex));

        generateConnections();
    }

    void generateShopItems(Room shopRoom)
    {
        // 1 оружие
        int weaponType = rand.nextInt(3);
        String weaponName = "";
        if (weaponType == 0) weaponName = "Меч";
        else if (weaponType == 1) weaponName = "Лук";
        else weaponName = "Книга заклинаний";
        int weaponBonus = 12 + rand.nextInt(6);
        int weaponPrice = weaponBonus * 6;
        shopRoom.shopItems.add(new Item(weaponName, ItemType.WEAPON, weaponBonus, weaponPrice));

        int armorBonus = 10 + rand.nextInt(6);
        int armorPrice = armorBonus * 5;
        shopRoom.shopItems.add(new Item("Щит", ItemType.ARMOR, armorBonus, armorPrice));

        int potionCount = 1 + rand.nextInt(3);
        for (int i = 0; i < potionCount; i++)
        {
            int potionBonus = 25 + rand.nextInt(16);
            int potionPrice = potionBonus * 2;
            shopRoom.shopItems.add(new Item("Зелье здоровья", ItemType.POTION, potionBonus, potionPrice));
        }
    }

    Item randomItem(boolean shopItem)
    {
        int type = rand.nextInt(3);
        String name = "";
        ItemType itemType = null;
        int bonus = 0;
        int price = 0;
        int baseBonus = shopItem ? 10 : 5;
        switch (type)
        {
            case 0:
                int weaponType = rand.nextInt(3);
                if (weaponType == 0)
                {
                    name = "Меч";
                    itemType = ItemType.WEAPON;
                    bonus = baseBonus + rand.nextInt(6);
                }
                else if (weaponType == 1)
                {
                    name = "Лук";
                    itemType = ItemType.WEAPON;
                    bonus = baseBonus + rand.nextInt(6);
                }
                else
                {
                    name = "Книга заклинаний";
                    itemType = ItemType.WEAPON;
                    bonus = baseBonus + rand.nextInt(6);
                }
                price = bonus * 5;
                break;
            case 1:
                name = "Щит";
                itemType = ItemType.ARMOR;
                bonus = baseBonus + rand.nextInt(4);
                price = bonus * 4;
                break;
            case 2:
                name = "Зелье здоровья";
                itemType = ItemType.POTION;
                bonus = (shopItem ? 25 : 15) + rand.nextInt(11);
                price = bonus * 2;
                break;
        }
        return new Item(name, itemType, bonus, price);
    }

    Monster randomMonster()
    {
        String[] names = {"Гоблин", "Скелет", "Зомби", "Орк", "Тролль"};
        String name = names[rand.nextInt(names.length)];
        int hp = 30 + rand.nextInt(41);
        int damage = 10 + rand.nextInt(16);
        int coinDrop = 10 + rand.nextInt(31);
        return new Monster(name, hp, damage, coinDrop);
    }

    void generateConnections()
    {
        int n = rooms.size();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        ArrayList<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++)
        {
            for (int j = i+1; j < n; j++)
            {
                edges.add(new int[]{i, j});
            }
        }
        for (int i = 0; i < edges.size(); i++)
        {
            int r = rand.nextInt(edges.size());
            int[] temp = edges.get(i);
            edges.set(i, edges.get(r));
            edges.set(r, temp);
        }

        int edgesAdded = 0;
        for (int[] edge : edges)
        {
            int a = edge[0];
            int b = edge[1];
            int rootA = find(parent, a);
            int rootB = find(parent, b);
            if (rootA != rootB)
            {
                rooms.get(a).neighbors.add(b);
                rooms.get(b).neighbors.add(a);
                parent[rootA] = rootB;
                edgesAdded++;
                if (edgesAdded == n-1) break;
            }
        }

        int extraEdges = n;
        for (int i = 0; i < extraEdges; i++)
        {
            int a = rand.nextInt(n);
            int b = rand.nextInt(n);
            if (a != b && !rooms.get(a).neighbors.contains(b))
            {
                rooms.get(a).neighbors.add(b);
                rooms.get(b).neighbors.add(a);
            }
        }
    }

    int find(int[] parent, int x)
    {
        if (parent[x] != x)
        {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    Room getRoom(int index)
    {
        return rooms.get(index);
    }

    int roomCount()
    {
        return rooms.size();
    }
}