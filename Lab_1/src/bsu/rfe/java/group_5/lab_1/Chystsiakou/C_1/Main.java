package bsu.rfe.java.group_5.lab_1.Chystsiakou.C_1;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static int equalsInBreakfast(Food obj, Food[] breakfast){
        int result = 0;
        for (Food fd : breakfast){
            if (obj.equals(fd)) {
                result++;
            }
        }
        return result;
    }
    public static void main(String[] args) {
        Food[] breakfast = new Food[20];
        int itemsSoFar = 0;
        boolean sortFlag = false;
        boolean caloriesFlag = false;
        //Заполнение завтрака продуктами

        for (String arg : args){
            if(arg.equals("-sort")) {
                sortFlag = true;
                continue;
            }
            if(arg.equals("-calories")) {
                caloriesFlag = true;
                continue;
            }
            String[] parts = arg.split("/");
            try {
                Class myClass = null;
                try {
                    //Reflection API
                    myClass = Class.forName("bsu.rfe.java.group_5.lab_1.Chystsiakou.C_1." + parts[0]);
                } catch (ClassNotFoundException e) {
                    System.out.println("Продукт не может быть включен в завтрак");
                    throw new RuntimeException(e);
                }
                if (parts.length==1) {
                    Constructor constructor = null;
                    try {
                        constructor = myClass.getConstructor();
                    } catch (NoSuchMethodException e) {
                        System.out.println("Продукт не может быть включен в завтрак");
                        throw new RuntimeException(e);
                    }
                    try {
                        breakfast[itemsSoFar] = (Food)constructor.newInstance(null);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                } else
                if (parts.length==2) {

                    Constructor constructor = null;
                    try {
                        constructor = myClass.getConstructor(String.class);
                    } catch (NoSuchMethodException e) {
                        System.out.println("Продукт не может быть включен в завтрак");
                        throw new RuntimeException(e);
                    }
                    try {
                        breakfast[itemsSoFar] = (Food)constructor.newInstance(parts[1]);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (parts.length==3) {

                    Constructor constructor = null;
                    try {
                        constructor = myClass.getConstructor(String.class,String.class);
                    } catch (NoSuchMethodException e) {
                        System.out.println("Продукт не может быть включен в завтрак");
                        throw new RuntimeException(e);
                    }
                    try {
                        breakfast[itemsSoFar] = (Food)constructor.newInstance(parts[1],parts[2]);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
            }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            itemsSoFar++;
        }
        //Время считать калории!
        if (caloriesFlag) {
            int caloriesSum = 0;
            for (Food item : breakfast) {
                if (item != null)
                    caloriesSum +=item.calculateCalories();
                else break;
            }
            System.out.println("Общая калорийность завтрака: " + caloriesSum);
        }
        System.out.println(equalsInBreakfast(breakfast[0], breakfast));
        if (sortFlag) {
            Arrays.sort(breakfast, new Comparator() {
                public int compare(Object f1, Object f2) {
                    if (f1 == null) return 1;
                    if (f2 == null) return -1;
                    return ((Food) f1).getName().length() - ((Food) f2).getName().length();
                }
            });
            for (Food item : breakfast) {
                if (item != null)
                    item.printFullname();
                else break;
            }
        }
        //Время завтракать!
        for (Food item : breakfast) {
            if (item != null)
                item.consume();
            else break;
        }
        System.out.println("Всё, поели");

    }

}