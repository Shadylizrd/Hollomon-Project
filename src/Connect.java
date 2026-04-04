import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class Connect {
    private final String username;
    private final String password;
    private final Scanner scanner = new Scanner(System.in);
    private final Action action = new Action();
    private ArrayList<Card> myCards = new ArrayList<>();

    public Connect() {
        System.out.print("Username: ");
      		this.username = scanner.nextLine();
        System.out.print("Password: ");
        this.password = scanner.nextLine();
    }

    // for testing only
    public Connect(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void run() {
        // will check your choice each time
        int choice;
        while ((choice = action.menu()) != 6) {
            String task = action.select(choice);
            // try with resources
            try (
                Socket sock = new Socket ("netsrv.cim.rhul.ac.uk", 1812);
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            ) {
                // actions of the try block
                // puts the text in the buffer -> not directly to the server
                writer.write(username); writer.newLine();
                writer.write(password); writer.newLine();
                writer.write(task); writer.newLine();
                writer.newLine();
                writer.flush();

                loginResponse(reader);
                taskResponse(reader, task);

            } catch (IOException e) { }
        }
    }

    // read this and display info?
    public void loginResponse(BufferedReader reader) throws IOException {
        this.myCards = readCards(reader);
    }

    public void taskResponse(BufferedReader reader, String task) throws IOException {
        switch (task) {
            case "CARDS" -> {
                myCards.forEach(card -> System.out.println(card.toString()));
                while (reader.readLine() != null) { /* dump the new outputs */}
            }
            
            case "OFFERS" -> {
                ArrayList<Card> cardSales = readCards(reader);
                action.filterOffers(cardSales);
                cardSales.forEach(card -> System.out.println(card.toString()));
            }
            
            default -> {
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();
                }
            }
        }
    }

    public ArrayList<Card> readCards(BufferedReader reader) throws IOException{
        ArrayList<Card> cards = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.equals("OK")) break;

            if (line.equals("CARD")) {
                String id       = reader.readLine();
                String name     = reader.readLine();
                String rank     = reader.readLine();
                String lastSale = reader.readLine();
                cards.add(new Card(id, name, rank, lastSale));
            }
        }
        CardSort.selectionSort(cards);
        return cards;
    }

    public void HAMMERTIME() {
        int loops = 0;
        while (true)
            try (
                Socket sock = new Socket ("netsrv.cim.rhul.ac.uk", 1812);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            ) {
                // actions of the try block
                // puts the text in the buffer -> not directly to the server
                System.out.println("HAMMER TIME");
                writer.write(username); writer.newLine();
                writer.write(password); writer.newLine();
                writer.newLine();
                
                writer.flush();
                System.out.println("HAMMER SUCCESSFUL");

                loops ++;
                System.out.println("schlepen");
                System.out.println("loop no: "+ loops);
                System.out.println("");
                Thread.sleep(3000);

            } catch (IOException e) { 
                System.err.println("Connection failed: "+ e.getMessage());
                loops++; 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
                    
                
                
    }

    public static void main(String[] args) {
        Connect connection = new Connect("class", "anyoneindicatelocal");
        
        connection.run();
    }

}
