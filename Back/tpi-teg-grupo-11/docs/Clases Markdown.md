
# Database Diagram Structure

## UsersStats
### Responsabilities: 
Represents all-time statistics for registered users.
```
PK | userStatsId
---|------------
FK | userId
   | gamesPlayed
   | armiesLost
   | armiesDefeated
   | territoriesConquered
   | territoriesLost
   | gamesPlayed
   | gamesWon
   | roundsPlayed
---|------------
   |updateStats()
```

## ObjectiveTypes
### Responsabilities:
Represents different objective types (General, Destruction and Conquest)
```
PK | objectiveTypeId
---|----------------
   | type
```

## Objectives
### Responsabilities:
Represents a win condition.
```
PK | idObjectives
---|-------------
   | description
FK | objectiveTypeId
---|-------------
   |assignPlayerObjective(player)
   |isAccomplished()
```

## Maps
### Responsabilities:
Groups regions.
```
PK | mapId
---|------
   | name
```

## Users
### Responsabilities:
Represents registered users.
```
PK | userId
---|-------
   | username
   | password
   | imgURL
   | isActive
   | creationDate
---|-------
   |registerUser()
   |changeUsername()
   |changePassword()
   |changeAvatar()
   |deleteUser()
```

## Colors
### Responsabilities:
Represents the available colors for players in a game.
```
PK | colorId
---|--------
   | color
```

## PlayerTypes
### Responsabilities:
Represents different player types (User, BOT_EASY, BOT_MEDIUM, BOT_HARD)
```
PK | playerTypeId
---|-------------
   | description
```

## PlayersGameStats
### Responsabilities:
Represents in-game statistics for players.
```
PK | playerGameStats
---|----------------
FK | playerId
   | armiesLost
   | armiesDefeated
   | territoriesConquered
   | territoriesLost
   | cardsTraded
```

## Regions
### Responsabilities:
Groups territories and gives bonifications.
```
PK | regionId
---|----------
   | name
   | numberArmyOfTerritories
FK | mapId
---|----------
   |calculateBonification()
```

## Players
### Responsabilities:
Represents a human player or a bot.
```
PK | playerId
---|----------
FK | gameId
FK | userId
FK | objectiveId
FK | colorId
   | turnOrder
   | isWinner
   | lastLost
FK | playerTypeId
---|----------
   | deployArmies(destination, n)
   | attack(origin, destination, n)
   | regroup(origin, destination, n)
   | tradeCards(cards)
   | receiveCard(card)
   | checkObjectivesStates()
```

## Games
### Responsabilities:
Represents the current state of a game.
```
PK | gameId
---|--------
   | gameURL
   | settings
   | gameState
   | creationDate
```

## Borders
### Responsabilities:
Represents adjacency between territories.
```
PK | borderId
---|----------
FK | territoryId
FK | territoryAId
   | crossesRegions
```

## Territories
### Responsabilities:
Represents a territory on the modelMapper.
```
PK | territoryId
---|------------
   | name
FK | regionId
```

## TerritoriesStates
### Responsabilities:
Represents the current state of a territory on the modelMapper.
```
PK | territoryState
---|---------------
FK | gameId
FK | territoryId
FK | playerId
   | armyCount
---|---------------
   | changeOwner(newOwner)
   | addArmies(n)
   | removeArmies(n)
```

## Pacts
### Responsabilities:

```
PK | pactId
---|--------
FK | playerAId
FK | playerBId
FK | borderId
FK | isTurnId
FK | endTurnId
---|--------
   | initiatePact(border)
   | breakPact()
```

## CardStates
### Responsabilities:
Represents the current state of a card in the game.
```
PK | cardStateId
---|------------
FK | cardId
FK | turnId
FK | playerId
   | used
---|--------
   | useCard()
   | dealStartingCards(player)
   | dealConquestCard(player)
   | returnCardToDeck(card)
```

## Turns
### Responsabilities:
Represents the current state of a turn in a game.
```
PK | turnId
---|--------
FK | gameId
FK | turnPhaseId
---|--------
   | nextPhase()
   | endTurn()
   | startTimer()
   | restartTimer()
   | pauseTimer()
```

## Cards
### Responsabilities:
Represents a territory card used for army trades.
```
PK | cardId
---|--------
FK | territoryId
FK | symbolId
---|--------
   | validTrade(card1, card2)
```

## Symbols
### Responsabilities:
Represents different card symbols used for trading armies.
```
PK | symbolId
---|----------
   | symbol
   | imgURL
```

## Movements
### Responsabilities:
Logs movement of armies each turn.
```
PK | turnId
---|--------
FK | startTerritoryId
FK | endTerritoryId
   | armyCount
```

## Dices
### Responsabilities:
Logs dice outcomes for each confrontation.
```
PK | diceId
---|--------
FK | attackId
   | attackerDice
   | defenderDice
```

## SavedGames
### Responsabilities:
Logs a specific gamestate to allow continuation from that point.
```
PK | idSavedGames
---|-------------
   | code
FK | gameStateId
```

## GamesStates
### Responsabilities:
Logs current gamestate in a turn.
```
PK | gameStateId
---|------------
FK | turnId
   | date
```

## TurnPhases
### Responsabilities:
Represents different phases of a turn (Deploy, Attack, Regroup).
```
PK | turnPhaseId
---|------------
   | description
```


