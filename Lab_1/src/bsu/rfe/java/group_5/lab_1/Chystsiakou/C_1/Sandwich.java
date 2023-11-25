package bsu.rfe.java.group_5.lab_1.Chystsiakou.C_1;

import java.util.Objects;

public class Sandwich extends Food{
    private String filling1;
    private String filling2;
    public Sandwich(String filling1, String filling2){
        super("Бутерброд");
        this.filling1 = filling1;
        this.filling2 = filling2;
    }
    public String getFilling1 (){
        return this.filling1;
    }
    public String getFilling2 (){
        return this.filling2;
    }
    public void setFilling1(String filling1){
        this.filling1 = filling1;
    }
    public void setFilling2(String filling2){
        this.filling2 = filling2;
    }
    public void printFullname() {
        System.out.println(this.name + " c начинкой 1 " + filling1 + " и начинкой 2 " + filling2);
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj))
        {
            if(!(obj instanceof Sandwich)) return false;
            return (filling1.equals(((Sandwich) obj).filling1) && filling2.equals(((Sandwich) obj).filling2));
        } else return false;
    }

    public void consume(){
        System.out.println(this.name + " с начинкой 1 " + this.filling1 + " и начинкой 2 " + this.filling2 + " съеден");
    }
    public int calculateCalories() {
        int sum = 300;
        if (Objects.equals(this.filling1, "чеснок") || Objects.equals(this.filling2, "чеснок")) sum +=20;
        else if (Objects.equals(this.filling1, "чеснок") && Objects.equals(this.filling2, "чеснок")) sum +=40;

        if (Objects.equals(this.filling1, "кетчуп") || Objects.equals(this.filling2, "кетчуп")) sum +=15;
        else if (Objects.equals(this.filling1, "кетчуп") && Objects.equals(this.filling2, "кетчуп")) sum +=30;

        if (Objects.equals(this.filling1, "курица") || Objects.equals(this.filling2, "курица")) sum +=150;
        else if (Objects.equals(this.filling1, "курица") && Objects.equals(this.filling2, "курица")) sum +=300;


        return sum;
    }
}
