<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>Challenge a Player</h2>

<form method="POST" autocomplete="off">
	<label for="user">User: </label>
	<select name="user" required>
		<option value="" selected disabled>Choose a user</option>
		<c:forEach items="${usernames}" var="username">
			<option value="${username}">${username}</option>
		</c:forEach>
	</select>
	
	<br>
	
	<button type="submit">Challenge</button>
</form>

</body>
</html>
