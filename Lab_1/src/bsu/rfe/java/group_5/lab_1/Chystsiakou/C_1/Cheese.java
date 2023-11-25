package bsu.rfe.java.group_5.lab_1.Chystsiakou.C_1;

public class Cheese extends Food
{
    public Cheese(){
        super("Сыр");
    }
    public void printFullname() {
        System.out.println(this.name);
    }
    public void consume() {
        System.out.println(this.name + " съеден");
    }
    public int calculateCalories() {
        return 210;
    }

}
