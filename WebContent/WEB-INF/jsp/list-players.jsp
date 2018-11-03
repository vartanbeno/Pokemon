<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"players": [
		<c:forEach items="${players}" var="player" varStatus="loop">
			{ "id": ${player.id}, "user": "${player.username}" }<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
