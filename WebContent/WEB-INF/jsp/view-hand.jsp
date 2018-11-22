<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"hand": [
		<c:forEach items="${hand}" var="handCard" varStatus="loop">
			${handCard.card.id}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	],
	"detailedHand": [
		<c:forEach items="${hand}" var="handCard" varStatus="loop">
			{ "id": ${handCard.card.id}, "type": "${handCard.card.type}", "name": "${handCard.card.name}" }
			<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
