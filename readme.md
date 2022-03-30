### Design decisions
Used hexagonal architecture to implement this project, with focus on testability (hence the usage of, for example, Clock, configuration files, etc.).

Used strategy pattern in cash deduction functionality. This allows an easy extensibility.

Numeric values that determine rules of the game are configurable. This includes win chances, reward multipliers and starting credits.

I wanted the API to be as RESTful as possible, hence there are no verbs in URLs, such as `playGame` etc.

Used Spring (Boot) as a framework to run this application, but I shied away from using Spring in domain logic. That's why the bean configuration lies outside of domain.

One player can play multiple games, but a game can be played by only one player.

Used h2 database as my in-memory choice because of simplicity.

### Known limitations
No functionality supporting multiple currencies. 

No integration tests - used extensive suite of unit tests instead. 

Fetching all games history for a given player is not optimal. 
I have misread the requirement in the beginning and did not have time and energy to refactor this. 

No functionality to end (lock) the game in a rare occasion that user would run out of credits. 
Any further bet would raise an exception about insufficient credits.

Domain services are a bit cluttered with logs. This could have been avoided by using Decorator pattern, but I didn't want to overengineer so much.

I could have provided more extensibility in RoundService (probably with strategy pattern), to allow easier changes to game mechanics. This time I didn't do it.
IRL I would discuss this with Product Owner or someone else decisive.

### Running the game
./mvnw install && ./mvnw exec:java

### Playing the game
Use suite of http requests in resources/client.http to play the game. 

First start the game with a request, then copy game ID from the response body. 

Use this ID to play rounds in this game, or to fetch that game's history. 

Try placing an illegal bet and see what happens.

Enjoy, \
Wojciech Jenczalik