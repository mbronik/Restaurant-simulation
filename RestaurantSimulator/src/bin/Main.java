package bin;

import bin.MainMenu.MenuSystem;

public class Main{
    public static void main(String[] args){
        MenuSystem mainMenu = new MenuSystem();
        mainMenu.start();
        Restaurant.init();

        for(int i=0; i<10; i++) Restaurant.makeRandomOrder();
    }
}

