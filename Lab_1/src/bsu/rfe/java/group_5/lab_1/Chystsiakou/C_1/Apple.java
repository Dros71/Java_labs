package bsu.rfe.java.group_5.lab_1.Chystsiakou.C_1;

public class Apple extends Food{
    private String size;
    public Apple(String size){
        super("Яблоко");
        this.size = size;
    }
    public String getSize(){
        return size;
    }
    public void setSize(String size){
        this.size = size;
    }
    public void printFullname() {
        System.out.println(this.name + " " + size);
    }
    public boolean equals(Object obj){//
        if (super.equals(obj))
        {
            if (!(obj instanceof Apple)) return false;
            return size.equals(((Apple) obj).size);
        } else return false;
    }
    @Override
    public void consume() {
        System.out.println(this.name + " размера \"" + size + "\" " + "съедено");
    }
    public int calculateCalories() {
        int sum = 50;
        switch (this.size){
            case "маленькое":{
                sum -=10;
                break;
            }
            case "большое":{
                sum +=10;
                break;
            }
        }
        return sum;
    }
}
