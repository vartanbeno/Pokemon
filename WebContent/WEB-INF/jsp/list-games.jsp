<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"games": [
		<c:forEach items="${games}" var="game" varStatus="loop">
			{
				"id": ${game.id},
				"players": [${game.challenger.id}, ${game.challengee.id}],
				"decks": [${game.challengerDeck.id}, ${game.challengeeDeck.id}]
			}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
