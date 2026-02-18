public class Monster
{
    String name;
    int hp;
    int damage;

    public Monster(String name, int hp, int damage)
    {
        this.name = name;
        this.hp = hp;
        this.damage = damage;
    }

    boolean isAlive()
    {
        return hp > 0;
    }

    void show()
    {
        System.out.println("Монстр: " + name + " (HP: " + hp + ")");
    }
}