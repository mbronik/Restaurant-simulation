package bin.MainMenu;

import java.util.Scanner;

public class SystemIn {
    Scanner in;

    public SystemIn() {
        in = new Scanner(System.in);
    }

    public int anInt(){
        String temp = in.nextLine();
        try {
            return Integer.parseInt(temp);
        }catch (NumberFormatException e){
            System.out.println("It's not an integer, please try again");
            return anInt();
        }
    }
    public int anIntPositive(){
        int temp = anInt();
        if(temp>0){
            return temp;
        }else{
            System.out.println("It isn't a positive number");
            return anIntPositive();
        }
    }
    public int anIntPositive(int max){
        int temp = anInt();
        if(temp>0 && temp<=max){
            return temp;
        }else{
            System.out.println("Out of range");
            return anIntPositive(max);
        }
    }
    public int anIntPositiveWithZero(int max){
        int temp = anInt();
        if(temp>=0 && temp<=max){
            return temp;
        }else{
            System.out.println("Out of range");
            return anIntPositive(max);
        }
    }

    public double aDouble(){
        String temp = in.nextLine();
        try {
            return Double.parseDouble(temp);
        }catch (NumberFormatException e){
            System.out.println("It's not a real number, please try again");
            return aDouble();
        }
    }
    public double aDoublePositive(){
        double temp = aDouble();
        if(temp>0){
            return temp;
        }else{
            System.out.println("It isn't a positive number");
            return aDoublePositive();
        }
    }

    public String aString(){
        return in.nextLine();
    }
}
