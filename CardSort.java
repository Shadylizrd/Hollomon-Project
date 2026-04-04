import java.util.ArrayList;

public class CardSort {

    // merge by rank
    // paralell bubble sort name 
    // selection into rank 
    // each array of ranks gets assigned to a worker in a threadpull 
    // then runs like a bubble sort or whatever
    // if cards are equal then collapse them?

    // constantly list all non unique cards for sale

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
    // allows the list of cards viewed to be smaller!
    public static <T> void selectRank(ArrayList<Card> cards) {
        ArrayList<Card> selectedCards = new ArrayList<>();;
        for (Card card : cards) {
            String rank = card.getRank();
            if (rank.equals("UNIQUE") || rank.equals("RARE")) 
                selectedCards.add(card);
            else break;
        }
        // new ArrayList methods learnt :)
        // helps keep the methods static?
        cards.clear();
        cards.addAll(selectedCards);
    }

} 
