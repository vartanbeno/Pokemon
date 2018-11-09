<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Board</title>
</head>
<body>

<h2>Choose which board to view</h2>

<form method="POST" autocomplete="off">
	<label for="game">Opponent: </label>
	<select name="game" required>
		<option value="" selected disabled>Opponent</option>
		<c:forEach items="${opponentChallengerGames}" var="game">
			<option value="${game.id}">${game.challenger.username}</option>
		</c:forEach>
		<c:forEach items="${opponentChallengeeGames}" var="game">
			<option value="${game.id}">${game.challengee.username}</option>
		</c:forEach>
	</select>
	
	<br>
	
	<button type="submit">View Board</button>
</form>

</body>
</html>
