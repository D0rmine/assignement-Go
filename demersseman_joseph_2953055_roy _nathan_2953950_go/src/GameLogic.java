import sun.security.ssl.Debug;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    private GoBoard board;
    private List<Coordinate> listOfPieceVerified;
    private Stone[][] renderLastTurnWhite;
    private Stone[][] renderLastTurnBlack;
    private int LastScoreWhite;
    private int LastScoreBlack;

    public GameLogic(GoBoard board) {
        this.board = board;
        renderLastTurnBlack = null;
        renderLastTurnWhite = null;
        LastScoreBlack = 0;
        LastScoreWhite = 0;
        listOfPieceVerified = new ArrayList<>();
    }

    public void CheckTeritoryFromPiece(int x, int y){
        if (x > 0) {
            if(board.getPiece((x - 1), y) == 0)
                checkTeritory(board.getPiece(x, y), (x - 1), y);
        }
        if (x < GoBoard.NUMBER_OF_LINE) {
            if(board.getPiece((x + 1), y) == 0)
                checkTeritory(board.getPiece(x, y), (x + 1), y);
        }
        if (y < GoBoard.NUMBER_OF_LINE) {
            if(board.getPiece(x, (y + 1)) == 0)
                checkTeritory(board.getPiece(x, y), (x + 1), y);
        }
        if (y > 0) {
            if(board.getPiece(x, (y - 1)) == 0)
                checkTeritory(board.getPiece(x, y), 1, (y - 1));
        }
    }

    private void checkTeritory(int player, int x, int y) {
        if (board.getPiece(x, y) != 0)
            return;
        board.changePieceIn(x,y,player + 2);
        if (x > 0) {
            if (board.getPiece((x - 1), y) == ((player % 2) + 1)) {
                neutralSet(x, y);
                return;
            }
                checkTeritory(player, (x - 1), y);
        }
        if (x < GoBoard.NUMBER_OF_LINE) {
            if (board.getPiece((x + 1), y) == ((player % 2) + 1)) {
                neutralSet(x, y);
                return;
            }
            checkTeritory(player, (x + 1), y);
        }
        if (y < GoBoard.NUMBER_OF_LINE) {
            if (board.getPiece(x , y + 1) == ((player % 2) + 1)) {
                neutralSet(x, y);
                return;
            }
                checkTeritory(player, x, y + 1);
        }
        if (y > 0) {
            if (board.getPiece(x, (y - 1)) == ((player % 2) + 1)) {
                neutralSet(x, y);
                return;
            }
            checkTeritory(player, x , (y + 1));
        }
    }

    private void neutralSet(int x, int y) {
        if (board.getPiece(x, y) == -1)
            return;
        if (board.getPiece(x, y) == 1 || board.getPiece(x, y) == 2)
            return;
        if ( board.getPiece(x, y) == 5)
            return;
        board.changePieceIn(x,y,5);
        if (x > 0) {
            neutralSet((x - 1), y);
        }
        if (x < GoBoard.NUMBER_OF_LINE) {
            neutralSet(x + 1 , y);
        }
        if (y < GoBoard.NUMBER_OF_LINE) {
            neutralSet(x, y + 1);
        }
        if (y > 0) {
            neutralSet(x, (y - 1));
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

        listOfPieceVerified.add(coordinate);

        //check if the piece has a escape near him
        if (checkEscapeNear(coordinate))
            return true;

        //check if a piece near him has the same color, if it is true check if the piece has also some escapes
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

    private boolean IsTheSameBoard(Stone[][] render1, Stone[][] render2)
    {
        if (render2 == null)
            return false;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (render1[i][j].getPiece() != render2[i][j].getPiece())
                    return false;
            }
        }
        return true;
    }

    private Stone[][] copyBoard(Stone[][] Render){
        Stone[][] newRender = new Stone[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                newRender[i][j] = new Stone(0);
                newRender[i][j].setPiece(Render[i][j].getPiece());
            }
        }
        return newRender;
    }

    public boolean CheckKORule(Stone[][] render, int player, int score){
        if (IsTheSameBoard(render, renderLastTurnWhite) && player == 1 && renderLastTurnBlack != null)
        {
            board.setRender(renderLastTurnBlack);
            board.setScore(LastScoreWhite, player);
            return true;
        }
        else if (player == 1) {
            renderLastTurnWhite = copyBoard(render);
            LastScoreWhite = score;
            return false;
        }
        else if (IsTheSameBoard(render,renderLastTurnBlack) && renderLastTurnWhite != null)
        {
            board.setRender(renderLastTurnWhite);
            board.setScore(LastScoreBlack, player);
            return true;
        }
        else {
            renderLastTurnBlack = copyBoard(render);
            LastScoreBlack = score;
            return false;
        }
    }

    public boolean sucideRule(int cx, int cy, int playerPiece)
    {
        return hasEscape(new Coordinate(cx, cy), playerPiece);
    }
}
