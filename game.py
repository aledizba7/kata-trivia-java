class Player:
    def __init__(self, name: str):
        self.name = name
        self.place = 1
        self.purse = 0
        self.in_penalty_box = False

class QuestionDeck:
    def __init__(self):
        self.pop_questions = []
        self.science_questions = []
        self.sports_questions = []
        self.rock_questions = []
        
        for i in range(50):
            self.pop_questions.append(f"Pop Question {i}")
            self.science_questions.append(f"Science Question {i}")
            self.sports_questions.append(f"Sports Question {i}")
            self.rock_questions.append(self._create_rock_question(i))

    def _create_rock_question(self, index: int) -> str:
        return f"Rock Question {index}"

    def next_question(self, category: str) -> str:
        if category == "Pop":
            return self.pop_questions.pop(0)
        elif category == "Science":
            return self.science_questions.pop(0)
        elif category == "Sports":
            return self.sports_questions.pop(0)
        elif category == "Rock":
            return self.rock_questions.pop(0)
        return None

class Game:
    BOARD_SIZE = 12
    WINNING_COINS = 6
    MAX_QUESTIONS = 50

    def __init__(self):
        self.players_list = []
        self.question_deck = QuestionDeck()
        self.current_player = 0

    def has_enough_players(self) -> bool:
        return len(self.players_list) >= 2

    def add(self, player_name: str) -> bool:
        return self.add_player(player_name)

    def add_player(self, player_name: str) -> bool:
        for player in self.players_list:
            if player.name == player_name:
                return False
        
        self.players_list.append(Player(player_name))
        
        print(f"{player_name} was added")
        print(f"They are player number {len(self.players_list)}")
        return True

    def how_many_players(self) -> int:
        return len(self.players_list)

    def roll(self, roll: int):
        player = self.players_list[self.current_player]
        print(f"{player.name} is the current player")
        print(f"They have rolled a {roll}")

        if player.in_penalty_box:
            self._handle_penalty_box_turn(player, roll)
        else:
            self._handle_normal_turn(player, roll)

    def _handle_penalty_box_turn(self, player: Player, roll: int):
        if roll % 2 != 0:
            player.in_penalty_box = False
            print(f"{player.name} is getting out of the penalty box")
            self._handle_normal_turn(player, roll)
        else:
            print(f"{player.name} is not getting out of the penalty box")

    def _handle_normal_turn(self, player: Player, roll: int):
        current_player_position = player.place + roll
        if current_player_position > self.BOARD_SIZE:
            current_player_position -= self.BOARD_SIZE
        player.place = current_player_position

        print(f"{player.name}'s new location is {current_player_position}")
        print(f"The category is {self._current_category(current_player_position)}")
        self._ask_question(self._current_category(current_player_position))

    def _ask_question(self, category: str):
        print(self.question_deck.next_question(category))

    def _current_category(self, place: int) -> str:
        if place - 1 == 0: return "Pop"
        if place - 1 == 4: return "Pop"
        if place - 1 == 8: return "Pop"
        if place - 1 == 1: return "Science"
        if place - 1 == 5: return "Science"
        if place - 1 == 9: return "Science"
        if place - 1 == 2: return "Sports"
        if place - 1 == 6: return "Sports"
        if place - 1 == 10: return "Sports"
        return "Rock"

    def handle_correct_answer(self) -> bool:
        player = self.players_list[self.current_player]
        if player.in_penalty_box:
            self.current_player += 1
            if self.current_player == len(self.players_list):
                self.current_player = 0
            return True
        else:
            print("Answer was correct!!!!")
            current_player_coins = player.purse + 1
            player.purse = current_player_coins
            print(f"{player.name} now has {current_player_coins} Gold Coins.")

            winner = not self._player_has_won()
            self.current_player += 1
            if self.current_player == len(self.players_list):
                self.current_player = 0

            return winner

    def wrong_answer(self) -> bool:
        player = self.players_list[self.current_player]
        print("Question was incorrectly answered")
        print(f"{player.name} was sent to the penalty box")
        
        player.in_penalty_box = True

        self.current_player += 1
        if self.current_player == len(self.players_list):
            self.current_player = 0
        return True

    def _player_has_won(self) -> bool:
        player = self.players_list[self.current_player]
        current_player_coins = player.purse
        return current_player_coins == self.WINNING_COINS
