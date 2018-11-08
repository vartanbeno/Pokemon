<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

{
	"board": {
		"players": [${game.challenger.id}, ${game.challengee.id}],
		"decks": [${game.challengerDeck.id}, ${game.challengeeDeck.id}],
		"play": {
			"${game.challenger.id}": {
				"handsize": ${fn:length(game.challengerHand)},
				"decksize": ${fn:length(game.challengerDeck.cards)}
			},
			"${game.challengee.id}": {
				"handsize": ${fn:length(game.challengeeHand)},
				"decksize": ${fn:length(game.challengeeDeck.cards)}
			}
		}
	}
}
