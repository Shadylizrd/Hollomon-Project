import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add (new Card("12345", "Dog", "COMMON", "0")); 
        cards.add (new Card("67890", "Cat", "RARE", "0"));
        cards.add (new Card("67890", "rat", "RARE", "0"));
        cards.add (new Card("12345", "Butler", "COMMON", "0")); 
        cards.add (new Card("67890", "gnat", "UNIQUE", "0"));
        cards.add (new Card("67890", "Cat", "UNIQUE", "0"));
        cards.add (new Card("67890", "zat", "COMMON", "0"));
        cards.add (new Card("67890", "Hat", "UNCOMMON", "0"));

        System.out.println("List:");
        cards.forEach(card -> System.out.println(card.toString()));
        System.out.println("");
        
        CardSort.selectionSort(cards);
        System.out.println("Sorted list:");
        cards.forEach(card -> System.out.println(card.toString()));
        System.out.println("");

        CardSort.selectRank(cards);
        System.out.println("Selected list:");
        cards.forEach(card -> System.out.println(card.toString()));


    }
}
