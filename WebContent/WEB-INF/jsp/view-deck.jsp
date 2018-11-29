<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"cards": [
		<c:forEach items="${deck.cards}" var="card" varStatus="loop">
			{
				"id": ${card.id},
				"t": "${card.type}",
				"n": "${card.name}"
				<c:if test="${not empty card.basic}">,
					"b": "${card.basic}"
				</c:if>
			}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
