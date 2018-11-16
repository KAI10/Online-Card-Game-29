# Online-Card-Game-29
This is a java implementation of popular card game 29. Players on the same local-net can play. Multiple games at the same time is supported.

## Rules:
* You are initially given 4 cards. Then you have to select a call (you may have to wait for some time for the responses from other players). A call is a guess of total how many points your team (you and your partner) may get throughout the game. See “Points Instructions” for further information.
* The highest caller will be eligible to set the trump card (which will be of no use unfortunately as we could not add the feature). Trump card refers to a suite which has the highest priority when determining the winner of each round.
* Next you will be given 8 cards and the first player (among the 4 players who first connected with the server) has to start the game. For the next rounds, winner of the previous round has to start the game.
* Once a round is started by someone you have to play a card of the same suite as that of the starter as long as you have that suite. In case you don’t have any card of that suite, you may play whichever card you want.
* Winner of each round is determined by checking who has played the card of the most priority of the starting suite.
* At the end of the game, total points are calculated for both teams. If the caller team manages to get no less point than the call, then the team wins and the other team loses and vice versa.

## Points Instruction:  
<table>
  <tr>
    <th> Card </th>
    <td>J</td><td>9</td><td>A</td>
    <td>10</td><td>K</td><td>Q</td>
    <td>8</td><td>7</td>
  </tr>
  <tr>
    <th>Points</th>
    <td>3</td><td>2</td><td>1</td>
    <td>1</td><td>0</td><td>0</td>
    <td>0</td><td>0</td>
  </tr>
  <tr>
    <th>Priority</th>
    <td>7</td><td>6</td><td>5</td>
    <td>4</td><td>3</td><td>2</td>
    <td>1</td><td>0</td>
  </tr>
</table>

## Screenshots
<table>
  <tr>
    <td><img src="https://drive.google.com/uc?id=1_sNWScSjHeQggUYJnBoVxml44fuscR0t" width="400" /></td>
    <td><img src="https://drive.google.com/uc?id=1V5sXmE3cJDxUDB3B4jXapHPP65yX1SW8" width="400" /></td>
  </tr>
</table>
