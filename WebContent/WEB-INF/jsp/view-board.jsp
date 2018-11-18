<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"board": {
		"id": ${game.id},
		"version": ${game.version},
		"players": [${game.challenger.id}, ${game.challengee.id}],
		"decks": [${game.challengerDeck.id}, ${game.challengeeDeck.id}],
		"play": {
			"${game.challenger.id}": {
				"status": "${challengerStatus}",
				"handsize": ${fn:length(game.challengerHand)},
				"decksize": ${fn:length(game.challengerDeck.cards)},
				"discardsize": ${fn:length(game.challengerDiscarded)},
				"bench": [
					<c:forEach items="${game.challengerBench}" var="card" varStatus="loop">
						${card.card.id}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				]
			},
			"${game.challengee.id}": {
				"status": "${challengeeStatus}",
				"handsize": ${fn:length(game.challengeeHand)},
				"decksize": ${fn:length(game.challengeeDeck.cards)},
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
