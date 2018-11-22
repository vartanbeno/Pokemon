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

<c:forEach items="${users}" var="user">
	
	<form method="POST" action="${user.id}/Challenge">
	
		<label for="deck">Choose your deck: </label>
		<select name="deck" required>
			<option value="" selected disabled>Your deck(s)</option>
			<c:forEach items="${decks}" var="deck">
				<option value="${deck.id}">${deck.id}</option>
			</c:forEach>
		</select>
		
		<button type="submit">Challenge ${user.username}</button>
		
	</form>
	
	<br>
	
</c:forEach>

</body>
</html>
