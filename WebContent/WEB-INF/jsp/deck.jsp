<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"deck": {
		"id": ${deck.id},
		"cards": [
			<c:forEach items="${cards}" var="card" varStatus="loop">
				{
					"t": "${card.type}",
					"n": "${card.name}"
				}<c:if test="${!loop.last}">,</c:if>
			</c:forEach>
		]
	}
}
