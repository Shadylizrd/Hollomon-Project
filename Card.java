import java.util.Objects;

public class Card implements Comparable<Card>{
    private String id;
    private String name;
    private String rank;
    private String lastSale;
    
    public Card (String id, String name, String rank, String lastSale) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.lastSale = lastSale;
    }

    public String getRank() { return rank; }
    public String toString() { return id + " | " + name + " | " + rank + " | " + lastSale; }

    @Override
    // set the custom compare to thingys!
    // COMMON -> UNCOMMON -> RARE -> UNIQUE
    public int compareTo(Card card) {
        if (this.rank.equals(card.rank)) {
            int nameCompared = this.name.compareTo(card.name);
            if (nameCompared == 0) { return this.id.compareTo(card.id); }
            return nameCompared;
        } 
        if (this.rank.equals("UNIQUE")) return -1;
    
        if (this.rank.equals("RARE")) {
            if (card.rank.equals("UNIQUE")) return 1;
            return -1;
        }
        if (this.rank.equals("UNCOMMON")) {
            if (card.rank.equals("UNIQUE") || card.rank.equals("RARE")) return 1;
            return -1;
        }

        else return 1;
    } 

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || other.getClass() != getClass()) return false;
        Card card = (Card) other;
        return Objects.equals(this.rank, card.rank) 
            && Objects.equals(this.name, card.name)
            && Objects.equals(this.id, card.id);
    }

}


