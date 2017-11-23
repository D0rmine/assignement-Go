import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    private GoBoard board;
    private List<Coordinate> listOfPieceVerified;

    public GameLogic(GoBoard board) {
        this.board = board;
        listOfPieceVerified = new ArrayList<>();
    }

    public void checkTeritory(int player, int x, int y) {
        if (board.getPiece(x, y) == ((player % 2) + 1))
            neutralSet(x, y);
        if (board.getPiece(x, y) != 0)
            return;
        board.changePieceIn(x,y,player + 2);
        if (x > 0) {
            checkTeritory(player, (x - 1), y);
            if (y > 0) {
                checkTeritory(player, (x - 1), (y - 1));
            }
        }
        if (x < 7) {
            checkTeritory(player, (x + 1), y);
            if (y < 7) {
                checkTeritory(player, (x + 1), (y + 1));
            }
        }
    }

    private void neutralSet(int x, int y) {
        if (board.getPiece(x, y) == 1 || board.getPiece(x, y) == 2)
            return;
        board.changePieceIn(x,y,5);
        if (x > 0) {
            neutralSet((x - 1), y);
            if (y > 0) {
                neutralSet((x - 1), (y - 1));
            }
        }
        if (x < 7) {
            neutralSet((x + 1), y);
            if (y < 7) {
                neutralSet((x + 1), (y + 1));
            }
        }
    }

    public void captureOpposingPiece(int x, int y) {
        int opposing = board.getOpposing();
        // see if an opponent piece is near
        listOfPieceVerified.clear();
        if (board.getPiece(x - 1, y) == opposing) {
            if (!hasEscape(new Coordinate(x - 1, y), opposing))
                removeCapturedPiece();
        }
        listOfPieceVerified.clear();
        if (board.getPiece(x + 1, y) == opposing) {
            if (!hasEscape(new Coordinate(x + 1, y), opposing))
                removeCapturedPiece();
        }
        listOfPieceVerified.clear();
        if (board.getPiece(x, y - 1) == opposing) {
            if (!hasEscape(new Coordinate(x, y - 1), opposing))
                removeCapturedPiece();
        }
        listOfPieceVerified.clear();
        if (board.getPiece(x, y + 1) == opposing) {
            if (!hasEscape(new Coordinate(x, y + 1), opposing))
                removeCapturedPiece();
        }
        listOfPieceVerified.clear();

    }

    private boolean checkEscapeNear(Coordinate coordinate) {
        if (board.getPiece(coordinate.getX() - 1, coordinate.getY()) == 0) {
            return true;
        }
        if (board.getPiece(coordinate.getX() + 1, coordinate.getY()) == 0) {
            return true;
        }
        if (board.getPiece(coordinate.getX(), coordinate.getY() - 1) == 0) {
            return true;
        }
        if (board.getPiece(coordinate.getX(), coordinate.getY() + 1) == 0) {
            return true;
        }

        return false;
    }

    public boolean hasEscape(Coordinate coordinate, int stonePossessor) {
        Coordinate tmp;
        boolean nearEscape = checkEscapeNear(coordinate);

        listOfPieceVerified.add(coordinate);
        if (nearEscape)
            return true;

        //checkIf currentPlayerNear
        if (board.getPiece(coordinate.getX() - 1, coordinate.getY()) == stonePossessor) {
            tmp = new Coordinate(coordinate.getX() - 1, coordinate.getY());

            if (groupHasEscape(tmp, stonePossessor))
                return true;
        }
        if (board.getPiece(coordinate.getX() + 1, coordinate.getY()) == stonePossessor) {
            tmp = new Coordinate(coordinate.getX() + 1, coordinate.getY());

            if (groupHasEscape(tmp, stonePossessor))
                return true;
        }
        if (board.getPiece(coordinate.getX(), coordinate.getY() - 1) == stonePossessor) {
            tmp = new Coordinate(coordinate.getX(), coordinate.getY() - 1);

            if (groupHasEscape(tmp, stonePossessor))
                return true;
        }
        if (board.getPiece(coordinate.getX(), coordinate.getY() + 1) == stonePossessor) {
            tmp = new Coordinate(coordinate.getX(), coordinate.getY() + 1);

            if (groupHasEscape(tmp, stonePossessor))
                return true;
        }
        return false;
    }

    private boolean groupHasEscape(Coordinate tmp, int currentPlayer) {
        if (!checkIfCoordinateAlreadyTested(tmp)) {
            if (hasEscape(tmp, currentPlayer))
                return true;
        }
        return false;
    }

    private boolean checkIfCoordinateAlreadyTested(Coordinate tmp) {
        for (Coordinate coordinate : listOfPieceVerified) {
            if (tmp.getX() == coordinate.getX() && tmp.getY() == coordinate.getY())
                return true;
        }

        return false;
    }

    private void removeCapturedPiece() {
        for (Coordinate coordinate : listOfPieceVerified) {
            board.changePieceIn(coordinate.getX(), coordinate.getY(), 0);
        }
    }
}
