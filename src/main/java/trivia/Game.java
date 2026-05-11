package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

// REFACTOR ME
public class Game implements IGame {
   ArrayList players = new ArrayList();
   int[] places = new int[6];
   int[] purses = new int[6];
   boolean[] inPenaltyBox = new boolean[6];

   LinkedList popQuestions = new LinkedList();
   LinkedList scienceQuestions = new LinkedList();
   LinkedList sportsQuestions = new LinkedList();
   LinkedList rockQuestions = new LinkedList();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
   }

   public boolean hasEnoughPlayers() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      return addPlayer(playerName);
   }

   public boolean addPlayer(String playerName) {
      places[howManyPlayers()] = 1;
      purses[howManyPlayers()] = 0;
      inPenaltyBox[howManyPlayers()] = false;
      players.add(playerName);

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      System.out.println(players.get(currentPlayer) + " is the current player");
      System.out.println("They have rolled a " + roll);

      boolean currentPlayerInPenaltyBox = inPenaltyBox[currentPlayer];
      if (currentPlayerInPenaltyBox) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
            int currentPlayerPosition = places[currentPlayer] + roll;
            if (currentPlayerPosition > 12)
               currentPlayerPosition = currentPlayerPosition - 12;
            places[currentPlayer] = currentPlayerPosition;

            System.out.println(players.get(currentPlayer)
                  + "'s new location is "
                  + currentPlayerPosition);
            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         int currentPlayerPosition = places[currentPlayer] + roll;
         if (currentPlayerPosition > 12)
            currentPlayerPosition = currentPlayerPosition - 12;
         places[currentPlayer] = currentPlayerPosition;

         System.out.println(players.get(currentPlayer)
               + "'s new location is "
               + currentPlayerPosition);
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory() == "Pop")
         System.out.println(popQuestions.removeFirst());
      if (currentCategory() == "Science")
         System.out.println(scienceQuestions.removeFirst());
      if (currentCategory() == "Sports")
         System.out.println(sportsQuestions.removeFirst());
      if (currentCategory() == "Rock")
         System.out.println(rockQuestions.removeFirst());
   }

   private String currentCategory() {
      int currentPlayerPosition = places[currentPlayer];
      if (currentPlayerPosition - 1 == 0)
         return "Pop";
      if (currentPlayerPosition - 1 == 4)
         return "Pop";
      if (currentPlayerPosition - 1 == 8)
         return "Pop";
      if (currentPlayerPosition - 1 == 1)
         return "Science";
      if (currentPlayerPosition - 1 == 5)
         return "Science";
      if (currentPlayerPosition - 1 == 9)
         return "Science";
      if (currentPlayerPosition - 1 == 2)
         return "Sports";
      if (currentPlayerPosition - 1 == 6)
         return "Sports";
      if (currentPlayerPosition - 1 == 10)
         return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      boolean currentPlayerInPenaltyBox = inPenaltyBox[currentPlayer];
      if (currentPlayerInPenaltyBox) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            int currentPlayerCoins = purses[currentPlayer] + 1;
            purses[currentPlayer] = currentPlayerCoins;
            System.out.println(players.get(currentPlayer)
                  + " now has "
                  + currentPlayerCoins
                  + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size())
               currentPlayer = 0;

            return winner;
         } else {
            currentPlayer++;
            if (currentPlayer == players.size())
               currentPlayer = 0;
            return true;
         }

      } else {

         System.out.println("Answer was corrent!!!!");
         int currentPlayerCoins = purses[currentPlayer] + 1;
         purses[currentPlayer] = currentPlayerCoins;
         System.out.println(players.get(currentPlayer)
               + " now has "
               + currentPlayerCoins
               + " Gold Coins.");

         boolean winner = didPlayerWin();
         currentPlayer++;
         if (currentPlayer == players.size())
            currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      System.out.println("Question was incorrectly answered");
      System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
      boolean currentPlayerInPenaltyBox = true;
      inPenaltyBox[currentPlayer] = currentPlayerInPenaltyBox;

      currentPlayer++;
      if (currentPlayer == players.size())
         currentPlayer = 0;
      return true;
   }

   private boolean didPlayerWin() {
      int currentPlayerCoins = purses[currentPlayer];
      return !(currentPlayerCoins == 6);
   }
}
