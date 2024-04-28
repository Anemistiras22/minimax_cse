import java.util.*;

public class gameCSE {
    private final char EMPTY = '-';
    private final char PLAYER = 'P';
    private final char AI = 'A';
    private final char[][] BOARD = new char[3][3];
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        gameCSE game = new gameCSE();
        game.initializeBoard();
        game.printBoard();

        char winner = game.playGame();
        if (winner == game.PLAYER)
            System.out.println("Player wins!");
        else if (winner == game.AI)
            System.out.println("PC wins!");
        else
            System.out.println("Draw!");
    }

    private char playGame() {
        char currentPlayer = PLAYER;
        while (true) {
            if (currentPlayer == PLAYER) {
                playerMove(currentPlayer);
            } else {
                computerMove(currentPlayer);
            }
            printBoard();
            
            char winner = checkWinner(currentPlayer == PLAYER ? 'P' : 'A');

            if (winner != EMPTY) {
                return winner;
            } else if (isBoardFull()) {
                return EMPTY;
            }
            currentPlayer = (currentPlayer == PLAYER) ? AI : PLAYER;
        }
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                BOARD[i][j] = EMPTY;
            }
        }
        
        int middleRow = 1;
        int middleCol = new Random().nextBoolean() ? 0 : 2;
        BOARD[middleRow][middleCol] = 'S';
    }

    private void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(BOARD[i][j] + " ");
            }
            System.out.println();
        }
    } 

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (BOARD[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void playerMove(char player) {
        System.out.println("Enter your move (row column letter): ");
        int row, col;
        char letter;
        while (true) {
            try {
                row = scanner.nextInt() - 1;
                col = scanner.nextInt() - 1;
                letter = scanner.next().charAt(0);
                if (row < 0 || row >= 3 || col < 0 || col >= 3 || BOARD[row][col] != EMPTY || (letter != 'C' && letter != 'S' && letter != 'E')) {
                    throw new IllegalArgumentException("Invalid input.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input.\nEnter your move (row column letter): ");
                scanner.nextLine(); 
            }
        }
        BOARD[row][col] = letter;
    }
    
    private void computerMove(char player) {
        System.out.println("Computer's turn...");
        int[] move = bestMove(player);
        int row = move[0];
        int col = move[1];
        char letter = (char) move[2];
        BOARD[row][col] = letter;
    }
    
    private int[] bestMove(char player) {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[]{0, 0, 0};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (BOARD[i][j] == EMPTY) {
                    for (char letter : new char[]{'C', 'S', 'E'}) {      //check all letter moves
                        BOARD[i][j] = letter;                           //simulate move with a letter
                        int score = minimax(BOARD, false);       //calculate score with move
                        BOARD[i][j] = EMPTY;                          //reset board 
                        if (score > bestScore) {
                            bestScore = score;
                            move[0] = i;
                            move[1] = j;
                            move[2] = letter;
                        }
                    }
                }
            }
        }
        return move;
    }
    
    private int minimax(char[][] board, boolean isMax) {
        char result = checkWinner(isMax ? PLAYER : AI);

        if (result != EMPTY) {
            return score(result);
        }
    
        if (isMax) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        for (char letter : new char[]{'C', 'S', 'E'}) {
                            board[i][j] = letter;
                            int score = minimax(board, false);
                            board[i][j] = EMPTY;
                            bestScore = Math.max(score, bestScore);
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        for (char letter : new char[]{'C', 'S', 'E'}) {
                            board[i][j] = letter;
                            int score = minimax(board, true);
                            board[i][j] = EMPTY;
                            bestScore = Math.min(score, bestScore);
                            
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    private int score(char winner) {
        if (winner == PLAYER)
            return -1;
        else if (winner == AI)
            return 1;
        else
            return 0;
    }
    
    private char checkWinner(char playerLetter) {
        for (int i = 0; i < 3; i++) {
            if ((BOARD[i][0] == 'C' && BOARD[i][1] == 'S' && BOARD[i][2] == 'E') ||
                (BOARD[i][0] == 'E' && BOARD[i][1] == 'S' && BOARD[i][2] == 'C')) {
                    return playerLetter;
            }
            if ((BOARD[0][i] == 'C' && BOARD[1][i] == 'S' && BOARD[2][i] == 'E') ||
                (BOARD[0][i] == 'E' && BOARD[1][i] == 'S' && BOARD[2][i] == 'C')) {
                    return playerLetter;
            }
        }
        if ((BOARD[0][0] == 'C' && BOARD[1][1] == 'S' && BOARD[2][2] == 'E') ||
            (BOARD[0][0] == 'E' && BOARD[1][1] == 'S' && BOARD[2][2] == 'C') ||
            (BOARD[0][2] == 'C' && BOARD[1][1] == 'S' && BOARD[2][0] == 'E') ||
            (BOARD[0][2] == 'E' && BOARD[1][1] == 'S' && BOARD[2][0] == 'C')) {
                return playerLetter;
        }
    
        boolean boardFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (BOARD[i][j] == EMPTY) {
                    boardFull = false;
                    break;
                }
            }
        }
        if (boardFull) {
            return 'D'; 
        }
        return EMPTY; 
    }

}
