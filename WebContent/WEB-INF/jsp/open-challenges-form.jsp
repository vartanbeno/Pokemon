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

<h2>Open Challenges Issued Against You</h2>
<h4>You have ${fn:length(challengesAgainstMe)} open challenge(s) issued against you.</h4>

<c:forEach items="${challengesAgainstMe}" var="challenge">
	<span>${challenge.challenger.username}</span>
	
	<form method="POST" action="${pageContext.request.contextPath}/Poke/Challenge/${challenge.id}/Accept">
		<input type="hidden" name="version" value="${challenge.version}" />
		<select name="deck" required>
			<option value="" selected disabled>Choose a deck</option>
			<c:forEach items="${decks}" var="deck">
				<option value="${deck.id}">${deck.id}</option>
			</c:forEach>
		</select>
		<button type="submit">Accept</button>
	</form>
	
	<form method="POST" action="${pageContext.request.contextPath}/Poke/Challenge/${challenge.id}/Refuse">
		<input type="hidden" name="version" value="${challenge.version}" />
		<button type="submit">Refuse</button>
	</form>
</c:forEach>

<h2>Open Challenges Issued By You</h2>
<h4>You have issued ${fn:length(challengesAgainstOthers)} challenge(s) against other players.</h4>

<c:forEach items="${challengesAgainstOthers}" var="challenge">
	<span>${challenge.challengee.username}</span>
	
	<form method="POST" action="${pageContext.request.contextPath}/Poke/Challenge/${challenge.id}/Withdraw">
		<input type="hidden" name="version" value="${challenge.version}" />
		<button type="submit">Withdraw</button>
	</form>
</c:forEach>

</body>
</html>
