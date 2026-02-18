public class Player
{
    String name;
    int maxHp = 100;
    int currentHp;
    int baseDamage = 10;
    Inventory inventory = new Inventory();

    public Player()
    {
        this.name = "Hero";
        this.currentHp = maxHp;
    }

    void takeItem(Item item, Room room)
    {
        if (inventory.addItem(item))
        {
            room.loot.remove(item);
            System.out.println("Вы взяли: " + item.name);
        }
    }

    void dropItem(Item item, Room room)
    {
        room.loot.add(item);
        System.out.println("Вы выбросили: " + item.name);
    }

    void usePotion()
    {
        Item potion = inventory.usePotion();
        if (potion != null)
        {
            currentHp = Math.min(maxHp, currentHp + potion.bonus);
            System.out.println("Вы использовали " + potion.name + " и восстановили " + potion.bonus + " HP.");
            System.out.println("Текущее здоровье: " + currentHp + "/" + maxHp);
        }
        else
        {
            System.out.println("У вас нет зелий!");
        }
    }

    void attack(Monster monster)
    {
        int totalDamage = baseDamage + inventory.getDamageBonus();
        monster.hp -= totalDamage;
        System.out.println("Вы нанесли " + totalDamage + " урона монстру.");
        if (monster.hp <= 0)
        {
            System.out.println("Монстр повержен!");
        }
    }

    void takeDamage(int damage)
    {
        currentHp -= damage;
        if (currentHp < 0) currentHp = 0;
        System.out.println("Монстр атакует! Вы получили " + damage + " урона.");
        System.out.println("Ваше здоровье: " + currentHp + "/" + maxHp);
    }

    boolean isAlive()
    {
        return currentHp > 0;
    }

    void showStatus()
    {
        System.out.println("Игрок: " + name + " | HP: " + currentHp + "/" + maxHp);
    }
}