<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Challenge a Player</title>
</head>
<body>

<h2>Challenge a Player</h2>

<form method="POST" autocomplete="off">

	<label for="player">Challengee: </label>
	<select name="player" required>
		<option value="" selected disabled>Choose a player</option>
		<c:forEach items="${users}" var="user">
			<option value="${user.id}">${user.username}</option>
		</c:forEach>
	</select>
	
	<br>
	
	<label for="deck">Deck: </label>
	<select name="deck" required>
		<option value="" selected disabled>Choose a deck</option>
		<c:forEach items="${decks}" var="deck">
			<option value="${deck.id}">${deck.id}</option>
		</c:forEach>
	</select>
	
	<br>
	
	<button type="submit">Challenge</button>
</form>

</body>
</html>
