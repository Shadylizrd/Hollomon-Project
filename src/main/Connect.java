package main;

import java.util.ArrayList;
import java.util.Scanner;

import cards.Card;
import cards.CardSort;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * The main class of this project.
 * This class connects to the socket and makes use of all the other classes.
 * The main method of the project is @see {@link main.Connect#run()}. 
 * @param scanner defines a scanner so that the program can take inputs.
 * @param action initialises an Action object so that all methods may be accessed.
 * @param myCards initialises an empty ArrayList to store all Card objects in.
 */
public class Connect {
    private final String username;
    private final String password;
    private final Scanner scanner = new Scanner(System.in);
    private final Action action = new Action();
    private ArrayList<Card> myCards = new ArrayList<>();

    /**
     * Initialising step, stores:
     * @param username
     * @param password
     * for continued use.
     */
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

    /** !! Entry point for menu loop !!
     * 
     * First checks that your login credentials gave access to the server.
     * Then loops through the menu from @see {@link main.Action#menu()}, and checks input from @see {@link main.Action#select(int)}
     * If 6 is selected temporarily breaks out of the general loop to run @see {@link main.Connect#autoSell()}.
     * 'reader' is an instance of ReaderTask and has the execute method defined here so that different tasks may be carried
     * out ontop of the same framework.
     */
    public void run() {
        int choice;
        if (checkLogin())
            while ((choice = action.menu()) != 7) {
                if (choice == 6) { autoSell(); continue; }

                String task = action.select(choice);

                // lambda shorthand for implementing anonymous classes
                sendRequest(task, reader -> {
                    loginResponse(reader);
                    taskResponse(reader, task);
                }); 
            }
    }

    public void loginResponse(BufferedReader reader) throws IOException {
        this.myCards = readCards(reader);
    }

    /**
     * Conducts the action specified by @param task using a switch statement.
     * @param reader necessary to read the output from the server, passed downwards to next methods.
     * @param task selected task from the user.
     * @throws IOException in case of stream being closed unexpectedly.
     */
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

    public boolean checkLogin() {
        try (
                Socket sock = new Socket ("netsrv.cim.rhul.ac.uk", 1812);
                BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            ) {
                writer.write(username); writer.newLine();
                writer.write(password); writer.newLine();
                writer.newLine();
                writer.flush();

                String line = reader.readLine();
                System.out.println(line);
                if (line.equals("User " +username+ " logged in successfully."))
                    return true;

                
            } catch (IOException e) { System.err.println("Connection failed: " + e.getMessage());}
            return false;
    }

    public void sendRequest(String task, ReaderTask readerTask) {
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

                readerTask.execute(reader);

            } catch (IOException e) {
                System.err.println("Connection failed: " + e.getMessage());
            }
    }

    /**
    * Creates the list of cards that will be sold.
    * Selects based on specified rank.
    */ 
    public ArrayList<Card> listCards(String listRank) {
        ArrayList<Card> toSell = new ArrayList<>();
        for (Card card : myCards) {
            String rank = card.getRank();
            if (rank.equals(listRank))
                toSell.add(card);
        }
        return toSell;
    }

    /**
     * autoSell bulk sells all cards of the selected type, COMMON or UNCOMMON.
     * It sets the reader.execute() method to only have @see {@link main.Connect#loginResponse(BufferedReader)}
     * as it only needs to generate the list of cards, which is done at each login, to give value to myCards
     * 
     * enacts a for-each loop to go through each Card object in myCards and generate the request command text
     * using @see {@link main.Action#autoSellTask(Card, String)}
     */
    public void autoSell() {
        sendRequest("CARDS", reader -> loginResponse(reader));

        System.out.println(" 1. Sell all common cards");
        System.out.println(" 2. Sell all uncommon cards");
        String choice = scanner.nextLine();

        ArrayList<Card> toSell;

        if (choice.equals("1")) toSell = listCards("COMMON");
        else if (choice.equals("2")) toSell = listCards("UNCOMMON");
        else {
            System.out.println("Invalid input");
            return;
        }

        System.out.print("How much would you like to sell all the cards for: ");
        String price = scanner.nextLine();

        if (toSell.isEmpty()) {
            System.out.println("No common or uncommon cards to sell.");
            return;
        }

        System.out.println("Auto-selling " +toSell.size()+ " cards...");

        for (Card card : toSell) {
            // sets task as SELL cardId cost
            String task = action.autoSellTask(card, price);
            // sends the request to run login and executes login + task response
            // helper method 
            sendRequest(task, reader -> {
                loginResponse(reader);
                taskResponse(reader, task);
            });
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

    /**
     * This is here due to a game feature. Each time you logged into the server there was a chance you 
     * would be given a new card. This was one of the only ways to get RARE and UNIQUE cards. While this 
     * does not follow proper coding standards it is here as a relic.
     * 
     * It would also keep track of the number of log-ins that had happened.
     */
    public void HAMMERTIME() {
        int loops = 0;
        while (true)
            try (
                Socket sock = new Socket ("netsrv.cim.rhul.ac.uk", 1812);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            ) {
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
        Connect connection = new Connect();
        
        connection.run();
    }

}
