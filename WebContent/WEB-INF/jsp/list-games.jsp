<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"games": [
		<c:forEach items="${games}" var="game" varStatus="loop">
			{
				"id": ${game.id},
				"challenger": ${game.challenger.id},
				"challengee": ${game.challengee.id},
				"challengerDeck": ${game.challengerDeck.id},
				"challengeeDeck": ${game.challengeeDeck.id}
			}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
