<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"board": {
		"id": ${gameBoard.game.id},
		"version": ${gameBoard.game.version},
		"players": [${gameBoard.game.challenger.id}, ${gameBoard.game.challengee.id}],
		"decks": [${gameBoard.game.challengerDeck.id}, ${gameBoard.game.challengeeDeck.id}],
		"play": {
			"${gameBoard.game.challenger.id}": {
				"status": "${challengerStatus}",
				"handsize": ${fn:length(game.challengerHand)},
				"decksize": ${fn:length(gameBoard.game.challengerDeck.cards)},
				"discardsize": ${fn:length(game.challengerDiscarded)},
				"bench": [
					<c:forEach items="${game.challengerBench}" var="card" varStatus="loop">
						${card.card.id}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				]
			},
			"${gameBoard.game.challengee.id}": {
				"status": "${challengeeStatus}",
				"handsize": ${fn:length(game.challengeeHand)},
				"decksize": ${fn:length(gameBoard.game.challengeeDeck.cards)},
				"discardsize": ${fn:length(game.challengeeDiscarded)},
				"bench": [
					<c:forEach items="${game.challengeeBench}" var="card" varStatus="loop">
						${card.card.id}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				]
			}
		}
	}
}
