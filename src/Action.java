import java.io.BufferedWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Action {
    private final Scanner scanner = new Scanner(System.in);

    public Action() {}

    public int menu() {
        System.out.println(" ==== Menu ==== ");
        System.out.println("");
        System.out.println(" 1. list all cards");
        System.out.println(" 2. show credits");
        System.out.println(" 3. list available cards");
        System.out.println(" 4. buy card");
        System.out.println(" 5. sell card");
        System.out.println(" 6. auto-sell");
        System.out.println(" 7. terminate");

        System.out.println("");
        System.out.print("> enter choice: ");
        return scanner.nextInt();
    }

    // will run methods for each maybe?
    public String select(int choice) {
        return switch (choice) {
            case 1 -> "CARDS";
            case 2 -> "CREDITS";
            case 3 -> "OFFERS";
            case 4 -> BUY();
            case 5 -> SELL();
            case 6 -> SELL();
            default ->  "";
        };
    }
    public void filterOffers(ArrayList<Card> cards) {
        System.out.println(" 1. Show all cards");
        System.out.println(" 2. Show cards rare and up");
        String choice = scanner.next();
        if (choice.equals("2")) { CardSort.selectRank(cards); }
    }

    public String BUY() {
        System.out.print("Enter card ID: ");
        String cardID = scanner.next();
        return "BUY " + cardID;
    }

    public String SELL() {
        System.out.print("Enter card ID: ");
        String cardID = scanner.next();
        System.out.print("Enter sell price: ");
        String price = scanner.next();
        return "SELL "+ cardID + " "+ price;
    }   

    public void autoSell(BufferedWriter write, int price, ArrayList<Card> cards) {
        for (Card card : cards) {
            if(card.getRank().equals("COMMON"))
                
        }

    }
}
