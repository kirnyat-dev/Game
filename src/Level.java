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

    void generateLevel()
    {
        int roomCount = 7 + rand.nextInt(3);
        System.out.println("Генерация уровня " + number + " с " + roomCount + " комнатами...");

        for (int i = 0; i < roomCount; i++)
        {
            Room room = new Room();
            int itemCount = rand.nextInt(4);
            for (int j = 0; j < itemCount; j++)
            {
                room.addLoot(randomItem());
            }
            rooms.add(room);
        }

        int monsterCount = 2 + rand.nextInt(2);
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

        generateConnections();
    }

    Item randomItem()
    {
        int type = rand.nextInt(3);
        switch (type)
        {
            case 0: return new Item("Меч", ItemType.WEAPON, 5 + rand.nextInt(6));
            case 1: return new Item("Зелье здоровья", ItemType.POTION, 15 + rand.nextInt(16));
            default: return new Item("Камень", ItemType.JUNK, 0);
        }
    }

    Monster randomMonster()
    {
        String[] names = {"Гоблин", "Скелет", "Зомби", "Орк"};
        String name = names[rand.nextInt(names.length)];
        int hp = 20 + rand.nextInt(31);
        int damage = 5 + rand.nextInt(11);
        return new Monster(name, hp, damage);
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