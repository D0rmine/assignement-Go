public class GameLogic {
    public GoBoard board;
    public void checkTeritory(int player, int x, int y){
        if (board.render[x][y].getPiece() == ((player % 2) + 1))
            neutralSet( x, y);
        if (board.render[x][y].getPiece() != 0)
            return;
        board.render[x][y].setPiece(player + 2);
        if (x > 0)
        {
            checkTeritory(player, (x - 1), y);
            if (y > 0)
            {
                checkTeritory(player, (x - 1), (y - 1));
            }
        }
        if (x < 7)
        {
            checkTeritory(player, (x + 1), y);
            if (y < 7)
            {
                checkTeritory(player, (x + 1), (y + 1));
            }
        }
    }
    private void neutralSet( int x, int y)
    {
        if (board.render[x][y].getPiece() == 1 || board.render[x][y].getPiece() == 2)
            return;
        board.render[x][y].setPiece(5);
        if (x > 0)
        {
            neutralSet((x - 1), y);
            if (y > 0)
            {
                neutralSet((x - 1), (y - 1));
            }
        }
        if (x < 7)
        {
            neutralSet((x + 1), y);
            if (y < 7)
            {
                neutralSet((x + 1), (y + 1));
            }
        }
    }
}
