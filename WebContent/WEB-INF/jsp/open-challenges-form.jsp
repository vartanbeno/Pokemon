<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Open Challenges</title>
</head>
<body>

<h2>Open Challenges</h2>
<h4>You have ${fn:length(challenges)} open challenges.</h4>

<c:forEach items="${challenges}" var="challenge">
	<span>${challenge.challenger.username}</span>
	
	<form method="POST" action="${pageContext.request.contextPath}/AcceptChallenge">
		<input type="hidden" name="challenger" value="${challenge.challenger.id}" />
		<button type="submit">Accept</button>
	</form>
	
	<form method="POST" action="${pageContext.request.contextPath}/RefuseChallenge">
		<input type="hidden" name="challenger" value="${challenge.challenger.id}" />
		<button type="submit">Refuse</button>
	</form>
</c:forEach>

</body>
</html>