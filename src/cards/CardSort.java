package cards;

import java.util.ArrayList;

/**
 * CardSort class, sorts all card objects via selection sort.
 */
public class CardSort {

    public static <T> void swap(ArrayList<T> cards, int i, int j) {
        T temp = cards.get(i);
        cards.set(i, cards.get(j));
        cards.set(j, temp);
    }

    public static <T extends Comparable<T>> int findMinIndex(
        ArrayList<T> cards, int start
    ) {
        int minIndex = start;
        for (int i = start; i < cards.size(); i++) {
            if (cards.get(i).compareTo(cards.get(minIndex)) < 0)
                minIndex = i;
        }
        return minIndex;
    }

    public static <T extends Comparable<T>> void selectionSort(
        ArrayList<T> cards
    ){
        for(int i = 0; i < cards.size(); i++){
            int minIndex = findMinIndex(cards, i);
            swap(cards, i, minIndex);
        }
    }

    // so fun writing this!
    /**
     * Sorts a reduced card list, removing all cards below the "RARE" rarity.
     * @param <T> allows for abstract typing (Card).
     * @param cards the arrayList of cards to be sorted.
     */
    public static <T> void selectRank(ArrayList<Card> cards) {
        ArrayList<Card> selectedCards = new ArrayList<>();
        for (Card card : cards) {
            String rank = card.getRank();
            if (rank.equals("UNIQUE") || rank.equals("RARE")) 
                selectedCards.add(card);
            else break;
        }

        cards.clear();
        cards.addAll(selectedCards);
    }

} 
