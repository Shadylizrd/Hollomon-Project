# Hollomon Card Trader
Java client program for trading and collecting **Hollomon** cards over TCP sockets.

> Archived project. The Hollomon server that this client talked to was a short-lived server and is no longer online, so the client can't log in any more. However, the sorting logic and offline tests still compile and run — see Running the offline tests.

### Features
- **Command-line menu** used to: list your cards, show credits, browse offers, and buy and sell card by ID
- **Rarity sorting**: all cards are automatically ordered by `UNIQUE -> RARE -> UNCOMMON -> COMMON`, then by name, then by ID (using a custom `compareTo` method on `Card`)
- **Market filter**: you can choose to hide all cards that are below `RARE` when browsing offers
- **Auto-sell**: list every `COMMON` or `UNCOMMON` card you own at one price all at once
- **Protocol parsing**: reads the output stream from the server, in expected order `CARD`/id/name/rank/last-sale and translates it into `Card` objects

### Built with
- Java 14+ (uses switch expressions)
- `java.net.Socket` with buffered readers/writers — uses no external libraries
A small functional interface (`ReaderTask`) so response-handling lambdas can throw `IOException`

### Project structure
```
src/
├── cards/
│   ├── Card.java          # Card model + rarity-based ordering
│   └── CardSort.java      # Selection sort and rarity filter
├── main/
│   ├── Connect.java       # Entry point: login, socket requests, auto-sell
│   ├── Action.java        # Menu and turning choices into protocol commands
│   └── ReaderTask.java    # Functional interface for reading responses
├── testing/
│   ├── Test.java          # Sorting/filtering check on sample cards
│   ├── TestReader.java    # Parses cardList.txt as if it came from the server
│   ├── TestCompare.java   # Comparable/equals practice
│   └── cardList.txt       # Sample server response
└── hollomonCheckConnect.jar  # Connection checker supplied with the module
```
### How the program worked 💔
Each request to the server opened a fresh connection. You would need to resent the login details each time, which is why after the first connection the entered details (by the user) are saved and fed back to the server on subsequent connections.

the initial order of interaction with the CLI would look like this:
```
<username>
<password>
<COMMAND>
```
After logging in you are given a number of choices via the terminal, and need only to enter the number next to the menu option to proceed.
```
1. List all cards          # CARDS
2. show credits            # CREDITS
3. list available cards    # OFFERS
4. buy card                # BUY <cardId>
5. sell card               # SELL <cardId>
6. auto-sell               # loops over the SELL command
7. terminate               # ends the loop
```
Each option would send a command to the server which would then send the requested data.

### Offline tests
I did write some tests for this project, these are _not_ Junit tests, they are manual tests that work with expected network data. `TestReader.java` in particular reads from `cardList.txt` line by line to emulate reading from the socket. The other two tests work on ensuring my logic was sound. The tests themselves are fairly rudimentary but **they are still runnable**, so if you'd like to run anything from this project then feel free :)

### What I learned
- Socket programming and designing around a line-based text protocol
- Implementing `Comparable` and `equals` for custom ordering
- An introduction to functional interfaces and lambdas, including working around exceptions
- Testing network-dependent code offline by feeding it an expected response
