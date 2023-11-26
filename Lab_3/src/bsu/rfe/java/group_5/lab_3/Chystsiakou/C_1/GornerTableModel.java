package bsu.rfe.java.group_5.lab_3.Chystsiakou.C_1;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double[][] Matrix;
    private Double from;
    private Double to;
    private Double step;
    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
        Matrix = new Double[this.getRowCount()][this.getColumnCount()];
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
        return 4;
    }
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Double[][] getTable(){
        return Matrix;
    }
    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        switch (col){
            case 0:{
                Matrix[row][col] = x;
                return x;
            }

            case 1: {
                Double result = 0.0;
                int it = 0;
                Double coefficient_now = coefficients[it];
                for (int i = coefficients.length - 1; i > 0; i--) {
                    result = (result + coefficient_now) * x;
                    coefficient_now = coefficients[++it];
                }
                result += coefficient_now;
                Matrix[row][col] = result;
                return result;
            }
            case 2:{
                Double result = 0.0;
                int it = coefficients.length - 1;
                Double coefficient_now = coefficients[it];
                for (int i = 0; i < coefficients.length - 1; i++){
                    result = (result + coefficient_now) * x;
                    coefficient_now = coefficients[--it];
                }
                result += coefficient_now;
                Matrix[row][col] = result;
                return result;
            }
            case 3:{
                Double result = Math.abs((Double)getValueAt(row,1) - (Double)getValueAt(row,2));
                Matrix[row][col] = result;
                return  result;
            }
            default:{
                System.out.println("Неизвестная ошибка!");
                return 0;
            }
    }
}
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            case 2:
                return  "Значение многочлена при обратных коэффициентах";
            default:
                return "Разница между нормальным и обратным значением многочлена";
        }
    }
    public Class<?> getColumnClass(int col) {
    return Double.class;
    }
}
