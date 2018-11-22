<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"discard": [
		<c:forEach items="${discard}" var="discardedCard" varStatus="loop">
			${discardedCard.card.id}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	],
	"detailedDiscard": [
		<c:forEach items="${discard}" var="discardedCard" varStatus="loop">
			{ "id": ${discardedCard.card.id}, "type": "${discardedCard.card.type}", "name": "${discardedCard.card.name}" }
			<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
