public class Player
{
    String name = "Hero";
    ClassType classType;
    int maxHp;
    int currentHp;
    int baseDamage;
    int baseArmor;
    int coins = 0;
    Inventory inventory = new Inventory();

    public Player(ClassType classType)
    {
        this.classType = classType;
        // Характеристики классов
        switch (classType)
        {
            case MELEE:
                maxHp = 120;
                baseDamage = 8;
                baseArmor = 5;
                break;
            case ARCHER:
                maxHp = 100;
                baseDamage = 12;
                baseArmor = 3;
                break;
            case MAGE:
                maxHp = 80;
                baseDamage = 16;
                baseArmor = 1;
                break;
        }
        this.currentHp = maxHp;
    }

    // Пустой конструктор для загрузки
    public Player() {}

    int getTotalArmor()
    {
        return baseArmor + inventory.getTotalArmor();
    }

    int getTotalDamage()
    {
        return baseDamage + inventory.getDamageBonus(classType);
    }

    void takeDamage(int damage)
    {
        int armor = getTotalArmor();
        int reducedDamage = damage - armor;
        if (reducedDamage < 1) reducedDamage = 1;
        currentHp -= reducedDamage;
        if (currentHp < 0) currentHp = 0;
        System.out.println("Монстр атакует! Вы получили " + reducedDamage + " урона (броня поглотила " + (damage - reducedDamage) + ").");
        System.out.println("Ваше здоровье: " + currentHp + "/" + maxHp);
    }

    void attack(Monster monster)
    {
        int totalDamage = getTotalDamage();
        monster.hp -= totalDamage;
        System.out.println("Вы нанесли " + totalDamage + " урона монстру.");
        if (monster.hp <= 0)
        {
            System.out.println("Монстр повержен!");
        }
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

    boolean isAlive()
    {
        return currentHp > 0;
    }

    void showStatus()
    {
        System.out.println("=== Статус ===");
        System.out.println("Класс: " + classType);
        System.out.println("HP: " + currentHp + "/" + maxHp);
        System.out.println("Монеты: " + coins);
        System.out.println("Базовый урон: " + baseDamage);
        System.out.println("Бонус урона от оружия: " + inventory.getDamageBonus(classType));
        System.out.println("Баз. броня: " + baseArmor + " | бонус брони: " + inventory.getTotalArmor());
        System.out.println("Общий урон: " + getTotalDamage() + " | Общая броня: " + getTotalArmor());
    }

    // Добавить монеты
    void addCoins(int amount)
    {
        coins += amount;
        System.out.println("Вы получили " + amount + " монет. Теперь у вас " + coins + " монет.");
    }

    // Потратить монеты
    boolean spendCoins(int amount)
    {
        if (coins >= amount)
        {
            coins -= amount;
            return true;
        }
        else
        {
            System.out.println("Недостаточно монет!");
            return false;
        }
    }
}