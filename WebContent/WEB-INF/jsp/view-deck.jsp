<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"deck": {
		"id": ${deck.player.id},
		"cards": [
			<c:forEach items="${deck.cards}" var="card" varStatus="loop">
				{
					"id": ${card.id},
					"t": "${card.type}",
					"n": "${card.name}",
					"b": "${card.basic}"
				}<c:if test="${!loop.last}">,</c:if>
			</c:forEach>
		]
	}
}
