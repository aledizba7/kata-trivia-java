package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// REFACTOR ME
public class Game implements IGame {
   private static final int BOARD_SIZE = 12;
   private static final int WINNING_COINS = 6;
   private static final int MAX_QUESTIONS = 50;
   public static class Player {
      private String name;
      private int place = 1;
      private int purse = 0;
      private boolean inPenaltyBox = false;

      public Player(String name) {
         this.name = name;
      }

      public String getName() { return name; }
      public int getPlace() { return place; }
      public void setPlace(int place) { this.place = place; }
      public int getPurse() { return purse; }
      public void setPurse(int purse) { this.purse = purse; }
      public boolean isInPenaltyBox() { return inPenaltyBox; }
      public void setInPenaltyBox(boolean inPenaltyBox) { this.inPenaltyBox = inPenaltyBox; }
   }

   List<Player> playersList = new ArrayList<>();

   public static class QuestionDeck {
      private LinkedList<String> popQuestions = new LinkedList<>();
      private LinkedList<String> scienceQuestions = new LinkedList<>();
      private LinkedList<String> sportsQuestions = new LinkedList<>();
      private LinkedList<String> rockQuestions = new LinkedList<>();

      public QuestionDeck() {
         for (int i = 0; i < MAX_QUESTIONS; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast("Science Question " + i);
            sportsQuestions.addLast("Sports Question " + i);
            rockQuestions.addLast(createRockQuestion(i));
         }
      }

      private String createRockQuestion(int index) {
         return "Rock Question " + index;
      }

      public String nextQuestion(String category) {
         if (category.equals("Pop")) return popQuestions.removeFirst();
         if (category.equals("Science")) return scienceQuestions.removeFirst();
         if (category.equals("Sports")) return sportsQuestions.removeFirst();
         if (category.equals("Rock")) return rockQuestions.removeFirst();
         return null;
      }
   }

   private QuestionDeck questionDeck = new QuestionDeck();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
   }

   public boolean hasEnoughPlayers() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      return addPlayer(playerName);
   }

   public boolean addPlayer(String playerName) {
      playersList.add(new Player(playerName));

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + playersList.size());
      return true;
   }

   public int howManyPlayers() {
      return playersList.size();
   }

   public void roll(int roll) {
      Player player = playersList.get(currentPlayer);
      System.out.println(player.getName() + " is the current player");
      System.out.println("They have rolled a " + roll);

      boolean currentPlayerInPenaltyBox = player.isInPenaltyBox();
      if (currentPlayerInPenaltyBox) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            System.out.println(player.getName() + " is getting out of the penalty box");
            int currentPlayerPosition = player.getPlace() + roll;
            if (currentPlayerPosition > BOARD_SIZE)
               currentPlayerPosition = currentPlayerPosition - BOARD_SIZE;
            player.setPlace(currentPlayerPosition);

            System.out.println(player.getName()
                  + "'s new location is "
                  + currentPlayerPosition);
            System.out.println("The category is " + currentCategory(currentPlayerPosition));
            askQuestion(currentCategory(currentPlayerPosition));
         } else {
            System.out.println(player.getName() + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         int currentPlayerPosition = player.getPlace() + roll;
         if (currentPlayerPosition > BOARD_SIZE)
            currentPlayerPosition = currentPlayerPosition - BOARD_SIZE;
         player.setPlace(currentPlayerPosition);

         System.out.println(player.getName()
               + "'s new location is "
               + currentPlayerPosition);
         System.out.println("The category is " + currentCategory(currentPlayerPosition));
         askQuestion(currentCategory(currentPlayerPosition));
      }

   }

   private void askQuestion(String category) {
      System.out.println(questionDeck.nextQuestion(category));
   }

   private String currentCategory(int place) {
      if (place - 1 == 0)
         return "Pop";
      if (place - 1 == 4)
         return "Pop";
      if (place - 1 == 8)
         return "Pop";
      if (place - 1 == 1)
         return "Science";
      if (place - 1 == 5)
         return "Science";
      if (place - 1 == 9)
         return "Science";
      if (place - 1 == 2)
         return "Sports";
      if (place - 1 == 6)
         return "Sports";
      if (place - 1 == 10)
         return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      Player player = playersList.get(currentPlayer);
      boolean currentPlayerInPenaltyBox = player.isInPenaltyBox();
      if (currentPlayerInPenaltyBox) {
         if (isGettingOutOfPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            int currentPlayerCoins = player.getPurse() + 1;
            player.setPurse(currentPlayerCoins);
            System.out.println(player.getName()
                  + " now has "
                  + currentPlayerCoins
                  + " Gold Coins.");

            boolean winner = !playerHasWon();
            currentPlayer++;
            if (currentPlayer == playersList.size())
               currentPlayer = 0;

            return winner;
         } else {
            currentPlayer++;
            if (currentPlayer == playersList.size())
               currentPlayer = 0;
            return true;
         }

      } else {

         System.out.println("Answer was corrent!!!!");
         int currentPlayerCoins = player.getPurse() + 1;
         player.setPurse(currentPlayerCoins);
         System.out.println(player.getName()
               + " now has "
               + currentPlayerCoins
               + " Gold Coins.");

         boolean winner = !playerHasWon();
         currentPlayer++;
         if (currentPlayer == playersList.size())
            currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      Player player = playersList.get(currentPlayer);
      System.out.println("Question was incorrectly answered");
      System.out.println(player.getName() + " was sent to the penalty box");
      boolean currentPlayerInPenaltyBox = true;
      player.setInPenaltyBox(currentPlayerInPenaltyBox);

      currentPlayer++;
      if (currentPlayer == playersList.size())
         currentPlayer = 0;
      return true;
   }

   private boolean playerHasWon() {
      Player player = playersList.get(currentPlayer);
      int currentPlayerCoins = player.getPurse();
      return (currentPlayerCoins == WINNING_COINS);
   }
}
