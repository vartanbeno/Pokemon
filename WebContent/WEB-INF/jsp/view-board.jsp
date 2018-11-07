<%@ page language="java" contentType="application/json"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
	"board": {
		"players": [${game.challenger.id}, ${game.challengee.id}],
		"decks": [${game.challengerDeck.id}, ${game.challengeeDeck.id}]
	}
}
