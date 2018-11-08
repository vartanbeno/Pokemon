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
5. Go to [localhost:8080/Pokemon/Register](localhost:8080/Register) and play around with the project!
    - This is assuming that the context root of the project is `Pokemon`.

### Getting the Database Set Up

See [here](docker-mysql8-db/README.md) for more info. Make sure the credentials match the ones [here](src/MyResources.properties).

Once you have it set up, you can run [Setup](src/Setup.java) to create the necessary tables.

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

### List of Players and Challenges

1. To see the list of players (i.e. users), visit `/ListPlayers`.
2. To see the list of challenges, visit `/ListChallenges`.

### Viewing Your Game Board

To view your game board, visit `/ViewBoard?game=[id]`, where `id` is the specific game's ID attribute. You must be part of the game to be able to view its board.

There are more features left to be implemented. Will update soon.

## Acknowledgement

I made use of the [SoenEA](http://soenea.htmlweb.com/) (Software Engineering Enterprise Application) framework to set up the connection to the database, making use of the `DbRegistry` class, which parses the [MyResources.properties](src/MyResources.properties) file to get the necessary credentials.

The framework was developed by Stuart Thiel, a professor at Concordia University. Per his thesis, it is used to "aid in the rapid development of dependable Web Enterprise Applications."
