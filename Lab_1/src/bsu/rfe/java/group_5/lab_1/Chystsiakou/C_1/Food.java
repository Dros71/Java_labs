package bsu.rfe.java.group_5.lab_1.Chystsiakou.C_1;

public abstract class Food implements Consumable, Nutritious{
    String name;
    public Food(String name){
        this.name = name;
    }
    public abstract void printFullname();
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Food)) return false;
        if (name==null || ((Food) obj).name == null) return false;
        return (name.equals(((Food) obj).name));
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
