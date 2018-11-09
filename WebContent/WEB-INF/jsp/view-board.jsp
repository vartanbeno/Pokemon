<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"board": {
		"id": ${game.id},
		"players": [${game.challenger.id}, ${game.challengee.id}],
		"decks": [${game.challengerDeck.id}, ${game.challengeeDeck.id}],
		"play": {
			"${game.challenger.id}": {
				"handsize": ${fn:length(game.challengerHandCards)},
				"decksize": ${fn:length(game.challengerDeck.cards)},
				"discardsize": ${fn:length(game.challengerDiscardedCards)},
				"bench": [
					<c:forEach items="${game.challengerBenchedCards}" var="card" varStatus="loop">
						${card.card.id}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				]
			},
			"${game.challengee.id}": {
				"handsize": ${fn:length(game.challengeeHandCards)},
				"decksize": ${fn:length(game.challengeeDeck.cards)},
				"discardsize": ${fn:length(game.challengeeDiscardedCards)},
				"bench": [
					<c:forEach items="${game.challengeeBenchedCards}" var="card" varStatus="loop">
						${card.card.id}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				]
			}
		}
	}
}
