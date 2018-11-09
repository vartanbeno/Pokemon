<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Play Pokemon to Bench</title>
</head>
<body>

<h2>Play Pokemon to Bench</h2>
<h4>The following Pokemon cards are in your hand. Pick one to play to the bench.</h4>
<p>Note: You currently have ${benchSize} Pokemon on the bench.</p>

<c:forEach items="${cards}" var="card">
	<form method="POST">
		<input type="hidden" name="card" value="${card.id}" />
		<button type="submit">${card.name}</button>
	</form>
</c:forEach>

</body>
</html>
