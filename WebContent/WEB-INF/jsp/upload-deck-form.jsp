<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload a Deck</title>
</head>
<body>

 <h2>Upload a Deck</h2>
 <p>You currently have ${numberOfDecks} deck(s).</p>
 <p>Upload a deck of ${numberOfCards} cards. One line per card.</p>
 
 <p>
 	Types are e/p/t (energy/pokemon/trainer). Names can be anything
 	<br>
 	<b>Format:</b> [type] "[name]"
 	<br>
 	<b>Example:</b> p "Charizard"
 </p>
 
 <form method="POST" action="${pageContext.request.contextPath}/Deck">
	<textarea name="deck" rows="${numberOfCards}" cols="100"></textarea>
	
	<br><br>
	
	<button type="submit">Create</button>
</form>

 </body>
</html>
