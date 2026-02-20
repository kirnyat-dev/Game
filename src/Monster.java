public class Monster
{
    String name;
    int hp;
    int damage;
    int coinDrop;

    public Monster(String name, int hp, int damage, int coinDrop)
    {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
        this.coinDrop = coinDrop;
    }

    boolean isAlive()
    {
        return hp > 0;
    }

    void show()
    {
        System.out.println("Монстр: " + name + " (HP: " + hp + ", урон: " + damage + ")");
    }
}