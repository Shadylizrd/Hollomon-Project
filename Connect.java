import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class Connect {
    private String username;
    private String password;
    private Scanner scanner = new Scanner(System.in);
    private Action action = new Action();
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
        int choice = 0;
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
        if (task.equals("CARDS")) {
            myCards.forEach(card -> System.out.println(card.toString()));
            while (reader.readLine() != null) { /* dump the new outputs */}
        }

        else if (task.equals("OFFERS")) {
            ArrayList<Card> cardSales = readCards(reader);
            cardSales.forEach(card -> System.out.println(card.toString()));
        }
        
        else {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
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
                try {
                    System.out.println("schlepen");
                    System.out.println("loop no: "+ loops);
                    System.out.println("");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {  }
                
            } catch (IOException e) { } 
    }

    public static void main(String[] args) {
        Connect connection = new Connect("class", "anyoneindicatelocal");
        
        connection.run();
    }

}
