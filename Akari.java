/**
 * Akari represents a single puzzle of the game Akari.
 * Project: Akari Puzzle Game, 
 * @version 2021
 */
import java.util.ArrayList; 

public class Akari
{
    private String filename; // the name of the puzzle file
    private int size;        // the board is size x size
    private Space[][] board; // the board is a square grid of spaces of various types

    /**
     * Constructor for objects of class Akari. 
     * Creates and initialises the fields with the contents of filename. 
     * The layout of a puzzle file is described on the page; 
     * you may assume the layout is always correct. 
     */
    public Akari(String filename)
    {
        // TODO 3
        // get the board default starter values
        this.filename = filename;
        FileIO f = new FileIO(this.filename);
        ArrayList<String> boardStarter = f.getLines();
        
        //set size
        this.size = Integer.parseInt(boardStarter.get(0));
        boardStarter.remove(0);
        
        //initiate board with empty space
        this.board = new Space[this.size][this.size];
        for(int row = 0; row  < this.size; row++)
        {
            for(int col = 0; col < this.size; col++)
            {
                this.board[row][col] = Space.EMPTY;
            }
        }
        
        //Placing dark squares based on boardStarter rules/values
        Space[] values = Space.values();
        for (int i = 0; i < boardStarter.size(); i++) 
        {
            String rules = boardStarter.get(i);
            for(String rule: rules.split(" "))
            {
                for(int j = 1; j < rule.length(); j++)
                {
                    int row = this.parseIndex(rule.charAt(0));
                    int col = this.parseIndex(rule.charAt(j));
                    this.board[row][col] = values[i];
                }
            }
        }
    }
    
    /**
     * Uses the example file from the  page.
     */
    public Akari()
    {
        this("Puzzles/p7-e7.txt");
    }
    
    /**
     * Returns the name of the puzzle file.
     */
    public String getFilename()
    {
        // TODO 1a
        return this.filename;
    }
    
    /**
     * Returns the size of the puzzle.
     */
    public int getSize()
    {
        // TODO 1b
        return this.size;
    }
    
    /**
     * Returns true iff k is a legal index into the board. 
     */
    public boolean isLegal(int k)
    {
        // TODO 5
        return k < this.size && k > -1;
    }
    
    /**
     * Returns true iff r and c are both legal indices into the board. 
     */
    public boolean isLegal(int r, int c)
    {
        // TODO 6
        return this.isLegal(r) && this.isLegal(c);
    }
    
    /**
     * Returns the contents of the square at r,c if the indices are legal, 
     * o/w throws an illegal argument exception. 
     */
    public Space getBoard(int r, int c)
    {
        // TODO 7
        if (this.isLegal(r,c))
        {
            return this.board[r][c];
        }
        throw new IllegalArgumentException(r + "," + c + "are in illegal coordinates.");
    }
    
    /**
     * Returns the int equivalent of x. 
     * If x is a digit, returns 0 -> 0, 1 -> 1, etc; 
     * if x is an upper-case letter, returns A -> 10, B -> 11, etc; 
     * o/w throws an illegal argument exception. 
     */
    public static int parseIndex(char x)
    {
        // TODO 2
        if (Character.isDigit(x) || Character.isUpperCase(x))
        {
            return Character.getNumericValue(x);
        }
        throw new IllegalArgumentException(x + " is not a number or a letter.");
    }
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * If r,c is empty, a bulb is placed; if it has a bulb, that bulb is removed.
     */
    public void leftClick(int r, int c)
    {
        // TODO 8
        if (this.isLegal(r,c))
        {
            if(this.board[r][c] == Space.EMPTY)
            {
                this.board[r][c] = Space.BULB;
            }
            else if (this.board[r][c] == Space.BULB)
            {
                this.board[r][c] = Space.EMPTY;
            }
        }
    }
    
    
    /**
     * Sets all mutable squares on the board to empty.
     */
    public void clear()
    {
        // TODO 4
        for (int row = 0; row < this.size; row++)
        {
            for (int col = 0; col < this.size; col++)
            {
                if (Space.isMutable(this.board[row][col])) 
                {
                    this.board[row][col] = Space.EMPTY;
                }
            }
        }
    }
    
    /**
     * Returns the number of bulbs adjacent to the square at r,c. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public int numberOfBulbs(int r, int c)
    {
        // TODO 14
        if (!this.isLegal(r,c)) 
        {
            
            throw new IllegalArgumentException(r + "," + c + "are in illegal coordinates.");
        }
        
        int[][] move = {
            // up, down, left, right
            {1, -1, 0, 0} , // row
            {0, 0, -1, 1} , //col
        };
        
        int count = 0;
        for (int i = 0; i < 4; i++)
        {
            int row = r + move[0][i];
            int col = c + move[1][i];
            if (!this.isLegal(row,col))
            {
                continue;
            }
            if (this.getBoard(row,col) == Space.BULB) 
            {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Returns true iff the square at r,c is lit by a bulb elsewhere. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public boolean canSeeBulb(int r, int c)
    {
        // TODO 15
        if (!this.isLegal(r,c)) 
        {

            throw new IllegalArgumentException(r + "," + c + "are in illegal coordinates.");
        }
        
        int[][] move = {
            // up, down, left, right
            {1, -1, 0, 0} , // row
            {0, 0, -1, 1} , //col
        };
        
        for(int i = 0; i < 4; i++)
        {
            int row = r + move[0][i];
            int col = c + move[1][i];
            while (this.isLegal(row,col))
            {
                Space tile = this.getBoard(row,col);
                if (tile == Space.BULB)
                { 
                    return true;
                }
                
                if (tile != Space.BULB && tile != Space.EMPTY)
                {
                    break;
                }
                
                row += move[0][i];
                col += move[1][i];
            }
        }
        
        return false;
    }
    
    /**
     * Returns an assessment of the state of the puzzle, either 
     * "Clashing bulb at r,c", 
     * "Unlit square at r,c", 
     * "Broken number at r,c", or
     * three ticks, as on the LMS page. 
     * r,c must be the coordinates of a square that has that kind of error. 
     * If there are multiple errors on the board, you may return any one of them. 
     */
    public String isSolution()
    {
        // TODO 16
        ArrayList<Space> numbers =  new ArrayList<Space>();
        numbers.add(Space.ZERO);
        numbers.add(Space.ONE);
        numbers.add(Space.TWO);
        numbers.add(Space.THREE);
        numbers.add(Space.FOUR);
        
        for (int row = 0; row < this.size; row++)
        {
            for (int col = 0; col < this.size; col++)
            {
                Space tile = this.board[row][col];
                if (tile == Space.BULB && this.canSeeBulb(row, col))
                {
                    return "Clashing bulb at " + row + "," + col;
                }
                
                if (tile == Space.EMPTY && !this.canSeeBulb(row, col)) 
                {
                    return "Unlit square at " + row + "," + col;
                }
                
                int num = numbers.indexOf(tile);
                if ( num > -1 && this.numberOfBulbs(row, col) != num)
                {
                    return "Broken number at " + row + "," + col;
                }
            }
        }
        
        return "✓✓✓";
    }
}