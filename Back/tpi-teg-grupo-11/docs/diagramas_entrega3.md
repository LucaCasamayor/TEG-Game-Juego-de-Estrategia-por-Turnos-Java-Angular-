
## SavedGames
- **savedGameId**
- **code**

**Métodos:**
- getSavedGames()
- getSavedGame(savedGameId)
- setGameStatus(code, gameStatusId)

---

## GameStates
- **gameStatusId**
- **turnId**
- **data**

**Métodos:**
- getGameState()
- updateGameState(turnId, data)

---

## Settings
- **settingId**
- **mapId**
- **timePerTurn**
- **isAIProfile**
- **objectiveTypeId**
- **mapTypeId**
- **roomPassword**
- **roomTypeId**

**Métodos:**
- getSettings(settingId)
- createSettings(mapId, timePerTurn, isAIProfile, objectiveType, mapTypeId, roomType, roomPassword)
- setSettings()

---

## Games
- **gameId**
- **gameUUID**
- **settingId**
- **gameStatusId**
- **creationDate**

**Métodos:**
- getGameById(gameId)
- getGamesByUsername(username)
- getGamesByCreationDate(creationDate)
- saveGame(settings)
- generateGameUUID()

---

## RoomTypes
- **roomTypeId**
- **type**

**Métodos:**
- getRoomTypes()
- setRoomType(type)

---

## ObjectiveTypes
- **objectiveTypeId**
- **type**

**Métodos:**
- getObjectiveTypes()
- setObjectiveType(type)
- setObjectiveTypeId()

---

## Objectives
- **objectiveId**
- **description**
- **objectiveTypeId**

**Métodos:**
- getObjectives()
- setObjective(desc)
- setObjectiveId()

---

## Colors
- **colorId**
- **color**

**Métodos:**
- getColorById(colorId)
- setColor(color)
- getColor()

---

## PlayerTypes
- **playerTypeId**
- **description**

**Métodos:**
- getPlayerTypes()
- getPlayerTypesById(playerTypeId)
- setPlayerType(description)

---

## MapTypes
- **mapTypeId**
- **type**

---

## Maps
- **mapId**
- **name**
- **mapTypeId**

**Métodos:**
- getMap()

---

## Players
- **playerId**
- **gameId**
- **colorId**
- **objectiveId**
- **turnOrder**
- **isWinner**
- **turnLost**
- **playerTypeId**

**Métodos:**
- attack(originId, destinationId, n)
- move(originId, destinationId, n)
- regroup(originId, destinationId, n)
- tradeCards(cards)
- receiveCard(card)
- checkObjectives(state)
- getPlayerByPlayer(player)
- createPlayer(player)
- deletePlayer(playerId)
- getPlayerGame(game)

---

## PlayerGameStats
- **playerGameStatsId**
- **playerId**
- **gameId**
- **armiesLost**
- **armiesDefeated**
- **territoriesConquered**
- **territoriesLost**
- **cardsTraded**

---

## Users
- **userId**
- **username**
- **password**
- **email**
- **imgURL**
- **isActive**
- **creationDate**

**Métodos:**
- registerUser()
- changePassword()
- changeEmail()
- changeAvatar()
- deleteUser()
- changeUsername()

---

## UserStats
- **userStatsId**
- **userId**
- **armiesLost**
- **armiesDefeated**
- **territoriesConquered**
- **territoriesLost**
- **gamesWon**
- **gamesPlayed**
- **roundsPlayed**

**Métodos:**
- updateStats()

---

## PlayersAchievements
- **playerAchievementsId**
- **achievementId**
- **userId**
- **date**

**Métodos:**
- setPlayerAchievement(achievementId, userId)
- getPlayerAchievements(userId)

---

## Achievements
- **achievementId**
- **name**
- **description**
- **icon**

**Métodos:**
- getAllAchievements()
- getAchievementById(achievementId)

---

## Cards
- **cardId**
- **territoryId**
- **symbolId**

**Métodos:**
- validTrade(card1, card2)

---

## Symbols
- **symbolId**
- **symbol**
- **imgURL**

**Métodos:**
- getSymbolsById(symbolId)

---

## Territories
- **territoryId**
- **regionId**

**Métodos:**
- getTerritoryId()
- getTerritoriesByRegion(regionId)
- getTerritoriesByPlayer(player)
- getBorderByTerritory(territory)
- getTerritoryFromObjective()

---

## Regions
- **regionId**
- **name**
- **armyBonus**

**Métodos:**
- getRegion(regionId)

---

## TerritoriesStates
- **territoryStateId**
- **territoryId**
- **playerId**
- **armyCount**

**Métodos:**
- changeOwner(newOwner)
- addArmies(n)
- removeArmies(n)

---

## TurnPhase
- **turnPhaseId**
- **description**

**Métodos:**
- getTurnPhase()
- updateTurnPhase(description)

---

## Turns
- **turnId**
- **gameId**
- **turnPhaseId**

**Métodos:**
- nextPhase()
- endTurn()
- startTimer()
- restartTimer()
- pauseTimer()
- getTurnById(turnId)

---

## Pacts
- **pactId**
- **playerAId**
- **playerBId**
- **borderId**
- **firstValidTurn**
- **endPactTurn**

**Métodos:**
- initializePact(turn, border)
- breakPact()

---

## Movements
- **movementId**
- **turnId**
- **borderId**
- **armyCount**
- **movementType**

**Métodos:**
- isValidMovement(movement)

---

## Borders
- **borderId**
- **territory1Id**
- **territory2Id**
- **crossesRegions**

**Métodos:**
- getBorders()

---

## Dices
- **diceId**
- **attackId**
- **attackerDice**
- **defenderDice**

**Métodos:**
- throwDice()

---

## MovementTypes
- **movementTypeId**
- **movementType**
