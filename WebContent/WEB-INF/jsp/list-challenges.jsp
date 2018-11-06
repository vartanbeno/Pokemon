<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"challenges": [
		<c:forEach items="${challenges}" var="challenge" varStatus="loop">
			{
				"id": ${challenge.id},
				"challenger": ${challenge.challenger.id},
				"challengee": ${challenge.challengee.id},
				"status": ${challenge.status}
			}<c:if test="${!loop.last}">,</c:if>
		</c:forEach>
	]
}
