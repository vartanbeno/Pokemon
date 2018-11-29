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
5. Go to [localhost:8080/Pokemon/Poke/Player/Register](http://localhost:8080/Pokemon/Register) and play around with the project!
    - This is assuming that the context root of the project is `Pokemon`.

### Running the Application (with Docker)

Running the application can very easily be done with Docker. Simply run `docker-compose up`, and the rest will be taken care of. You can also specify the `-d` or `--detach` option to run it in detached mode.

The [docker-compose.yml](docker-compose.yml) file spawns 2 containers: one for Tomcat, and another for the MySQL database. When the latter is first run, [this SQL script](mysql-init/init.sql) is executed to set up the tables.

If you just want to run a container for the database, run the [docker-compose.db.yml](docker-compose.db.yml) file instead: `docker-compose -f docker-compose.db.yml up`

## Usage

All of the following assumes that the context root of the project is `Pokemon`, i.e. every path starts with `Pokemon/`.

First, register a user into the database by visiting `Poke/Player/Register` and creating an account.

- You're better off creating 2+ users, so that you can use the various features of the app, like challenging and playing a game.
- To log out, visit `Poke/Player/Logout`.
- To log in, visit `Poke/Player/Login`.
    
### Uploading a Deck

To challenge a player, you first need to upload a deck. You can do that at `/Poke/UploadDeck`. Make sure to follow the desired formatting to avoid any errors. An example deck is provided [here](WebContent/deck-example.txt).

### Viewing Your Deck

To view the deck you uploaded, visit `/Poke/Deck/[id]`, where `id` is the ID of the deck you just uploaded.

### Viewing Your Decks

To view all of your decks, visit `/Poke/Deck`.

### Challenging Players

Once you have a deck, you can challenge players to games! To do that, visit `/Poke/Player/Challenge`. Once you challenge a player, a challenge will be created, with the status set to open.

If you go to `/Poke/Player/OpenChallenges`, you can see a list of challenges issued against you _by_ other players, and a list of challenges that you have thrown _to_ other players.

### Accepting a Challenge

If you want to accept a challenge issued against you, go to `/Poke/Player/OpenChallenges`, and click on the `Accept` button on the challenge you want to accept. This sets the challenge status to accepted.

Accepting a challenge creates a game between the challenger and the challengee. The first turn of the game is the challenger's, and they start with 1 card draw from their deck.

### Refusing a Challenge

If you want to refuse a challenge issued against you, go to `/Poke/Player/OpenChallenges`, and click on the `Refuse` button on the challenge you want to accept. This sets the challenge status to refused.

### Withdrawing From a Challenge

If you want to go back on your steps and withdraw from a challenge that you issued to another player, go to `/Poke/Player/OpenChallenges`, and click on the `Withdraw` button on the challenge you want to withdraw from. This sets the challenge status to withdrawn.

### List of Players, Challenges, and Games

1. To see the list of players (i.e. users), visit `/Poke/Player`.
2. To see the list of challenges, visit `/Poke/Challenge`.
3. To see the list of games, visit `/Poke/Game`.

You must be logged in to do any of those.

### Viewing Your Game Board

To view your game board, visit `/Poke/Game/[id]`, where `id` is the ID of the game you want to view.

You must be part of a game to view its board.

### Ending Your Turn

To end your turn, visit `/Poke/Game/[id]/EndTurn?version=[version]`, where `id` is the ID of the game in which you want end your turn, and `version` is the version of the game.

If the `version` you provide doesn't correspond to the version of the game with the provided ID, you get a `LostUpdateException`.

Your opponent cannot play until you end your turn. All they can do is view the game board, their hand, the discard piles, and retire.

One you end your turn, if you have more than 7 cards in your hand, you discard your oldest one, and your opponent draws a card from their deck.

### Viewing Your Hand

To view your hand, visit `/Poke/Game/[id]/Hand`, where `id` is the ID of the game in which you want to view your hand.

You cannot view your opponent's hand.

### Viewing a Discard Pile

To view a discard pile, visit `/Poke/Game/[id]/Player/[id]/Discard`, where the first `id` is the ID of the game, and the second `id` is the ID of the player whose discard pile you want to view.

You can view your opponent's discard pile, not just yours.

### Playing a Card

To play a card, it must be your turn. Visit `/Poke/Game/[id]/Hand/[position]/Play?version=[version]` to play a card, where `id` is the ID of the game, `position` is the position of the card in your hand, based off a 0-based index, and `version` is the version of the game.

#### Playing a Pokemon to the Bench

If the card you select is of type Pokemon, and doesn't have a basic version (i.e. a Pokemon that's not evolved, like Charmander), then the Pokemon goes to your bench.

#### Evolving a Pokemon

You must provide a `basic` parameter, in addition to `version`. `basic` is the position of a Pokemon on your bench.

If the card you select is of type Pokemon and its basic version corresponds to the one you chose from your bench, the evolved one replaces the one on your bench. The latter goes in your discard pile. The former keeps all attached energies that the basic one had.

#### Playing a Trainer

If the card you select is of type trainer, it gets send to your discard pile.

#### Playing an Energy

You must provide a `pokemon` parameter, in addition to `version`. `pokemon` is the position of a Pokemon on your bench.

If the card you select is of type energy, it gets attached to the Pokemon you chose on the bench.

You can only play 1 energy card per turn.

### Retiring From a Game

To retire from a game, visit `/Poke/Game/[id]/Retire`, where `id` is the ID of the game you want to retire from. Once you retire from a game, you can no longer play in it. You can still view its board, the hand you had, and the discard pile you had right before retiring.

When you retire from a game, your status is declared retired, and your opponent is declared the winner.

## Acknowledgement

I made use of the [SoenEA](http://soenea.htmlweb.com/) (Software Engineering Enterprise Application) framework to set up the connection to the database, making use of the `DbRegistry` class, which parses the [MyResources.properties](src/MyResources.properties) file to get the necessary credentials.

The framework was developed by Stuart Thiel, a professor at Concordia University. Per his thesis, it is used to "aid in the rapid development of dependable Web Enterprise Applications."

The project's file structure follows the recommended file structure from the thesis, which can be found in the Appendix, on page 73.
