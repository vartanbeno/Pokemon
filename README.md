# Pokemon

This is a web application built using various architectural patterns. The goal was to get familiar with those patterns. The HTML is therefore minimalistic, containing no styling whatsoever. As a matter of fact, most of the data returned to the user is in JSON format.

**Tip:** Use the Google Chrome extension [JSON Formatter](https://chrome.google.com/webstore/detail/json-formatter/bcjindcccaagfpapjjmafapmmgkkhgoa?hl=en), which makes JSON easy to read.

## Getting Started

### Prerequisites 

1. Download [Apache Tomcat](http://tomcat.apache.org/) (I used Tomcat 8).
2. Open your favourite Java IDE, such as [Eclipse](https://www.eclipse.org/) or [IntelliJ](https://www.jetbrains.com/idea/).
    - This project was made in Eclipse, so you might get a smoother run using it.
3. Import the project.
4. Deploy the project to the Tomcat server.
5. Go to [localhost:8080/Pokemon/Register](http://localhost:8080/Pokemon/Register) and play around with the project!
    - This is assuming that the context root of the project is `Pokemon`.

### Running the Application (with Docker)

Running the application can very easily be done with Docker. Simply run `docker-compose up`, and the rest will be taken care of. You can also specify the `-d` or `--detach` option to run it in detached mode.

The [docker-compose.yml](docker-compose.yml) file spawns 2 containers: one for Tomcat, and another for the MySQL database. When the latter is first run, [this SQL script](mysql-init/init.sql) is executed to set up the tables.

It's important to mention that for the Tomcat container to communicate with the database, before generating the WAR file, I changed the MySQL hostname in [MyResources.properties](src/MyResources.properties) from `localhost:3306` to `db`.

That's because the container used for the database has `db` as a name for its service. You could also set it to `pokemon-db`, since that's the name of the container.

To run the convenient main methods in [src/main](src/main), make sure the MySQL hostname in the properties file is set to `localhost:3306`, since port 3306 is exposed on the running database container.

## Usage

First, register a user into the database by visiting `/Register` and creating an account.

- You're better off creating 2+ users, so that you can use the various features of the app.
- To log out, visit `/Logout`.
- To log in, visit `/Login`.
    
### Uploading a Deck

To challenge a player, you first need to upload a deck. You can do that at `/UploadDeck`. Make sure to follow the desired formatting to avoid any errors. An example deck is provided [here](WebContent/deck-example.txt).

### Viewing Your Deck

To view the deck you uploaded, visit `/ViewDeck`.

### Challenging Players

Once you have a deck, you can challenge players to games! To do that, visit `/ChallengePlayer`, where you get a dropdown of other players you can challenge. Once you challenge a player, a challenge will be created, with the status set to open.

If you go to `/OpenChallenges`, you can see a list of challenges issued against you _by_ other players, and a list of challenges that you have thrown _to_ other players.

### Accepting a Challenge

If you want to accept a challenge issued against you, go to `/OpenChallenges`, and click on the `Accept` button on the challenge you want to accept. This sets the challenge status to accepted.

Accepting a challenge creates a game between the challenger and the challengee. More on that later.

### Refusing a Challenge

If you want to refuse a challenge issued against you, go to `/OpenChallenges`, and click on the `Refuse` button on the challenge you want to accept. This sets the challenge status to refused.

### Withdrawing From a Challenge

If you want to go back on your steps and withdraw from a challenge that you issued to another player, go to `/OpenChallenges`, and click on the `Withdraw` button on the challenge you want to withdraw from. This sets the challenge status to withdrawn.

### List of Players, Challenges, and Games

1. To see the list of players (i.e. users), visit `/ListPlayers`.
2. To see the list of challenges, visit `/ListChallenges`.
3. To see the list of games, visit `/ListGames`.

You must be logged in to do any of those.

### Viewing Your Game Board

To view your game board, visit `/ViewBoard?game=[id]`, where the game parameter is the ID of an ongoing game that you are part of.

You must be part of a game to view its board.

### Drawing a Card

To draw a card, visit `/DrawCard?game=[id]`, where `id` is the specific game's ID. You must be part of the game to draw a card. Drawing a card adds it to your hand.

### Viewing Your Hand

To view your hand, visit `/ViewHand?game=[id]`, with the appropriate game ID parameter.

### Playing Pokemon to the Bench

To play a Pokemon card to the bench, visit `/PlayPokemonToBench?game=[id]&card=[position]`, where the `game` parameter is the ID of a game that you are a part of, and the `card` parameter is the position of the card in your hand that you want to send to the bench, using a zero-based index (e.g. position 2 corresponds to the 3rd card in your hand).

The maximum amount of Pokemon on your bench at any given time is 5, so you might get a fail message.

You can only send Pokemon to the bench. If you attempt to send a card that isn't a Pokemon to the bench, you will get a fail message.

### Retiring From a Game

To retire from a game, visit `/Retire?game=[id]`, where the `game` parameter is the ID of a game that you are a part of. Once you retire from a game, you can no longer draw cards or play Pokemon to the bench. You can still view its board and the hand you had right before retiring.

When you retire from a game, your status is declared retired, and your opponent is declared the winner.

## Acknowledgement

I made use of the [SoenEA](http://soenea.htmlweb.com/) (Software Engineering Enterprise Application) framework to set up the connection to the database, making use of the `DbRegistry` class, which parses the [MyResources.properties](src/MyResources.properties) file to get the necessary credentials.

The framework was developed by Stuart Thiel, a professor at Concordia University. Per his thesis, it is used to "aid in the rapid development of dependable Web Enterprise Applications."

The project's file structure follows the recommended file structure from the thesis, which can be found in the Appendix, on page 73.

