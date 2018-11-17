<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"decks": [
		<c:forEach items="${decks}" var="deck" varStatus="loop">
			${deck.id}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
