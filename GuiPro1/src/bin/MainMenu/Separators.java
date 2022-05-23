package bin.MainMenu;

public class Separators {

    public static void separate() {
        for (int i = 0; i < 64; i++) {
            System.out.print("_");
        }
        System.out.println();
    }

    public static void clearConsole() {
        for (int i = 0; i < 48; i++) {
            System.out.println();
        }
    }

}
