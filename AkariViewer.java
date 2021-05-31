/**
 * AkariViewer represents an interface for a player of Akari.
 * Project: Akari Puzzle Game
 * @version 2021
 */
import java.awt.*;
import java.awt.event.*; 

public class AkariViewer implements MouseListener
{    
    private Akari puzzle;    // the internal representation of the puzzle
    private SimpleCanvas sc; // the display window
    private int size;        // sets size of puzzle
    
    /*
     * Sets size of tile (initially it was a fixed tilesize = 50. At puzzle size 7 to 15 canvas shows correctly, 
     * but when size is 25,puzzle canvas does not show all rows,
     * so to make it adjustable to accomodate size 25, it should be "size" + 35.
    */
    private final int tilesize = size + 35;
    private final int offset =  tilesize / 30; //gaps between the tiles (code from Fifteen Puzzle)
    private final Color backColor = Color.black; //sets background color to black (code from Fifteen Puzzle)
    
    /**
     * Constructor for objects of class AkariViewer.
     * Sets all fields and displays the initial puzzle.
     */
    public AkariViewer(Akari puzzle)
    {
        // TODO 10
        this.puzzle = puzzle;
        size = puzzle.getSize(); 
        sc = new SimpleCanvas("Akari Puzzle", size * tilesize, (size + 2) * tilesize, backColor); // size + 2 means that we added two rows below the puzzle (on the height)
        sc.setFont(new Font("Times", 10, tilesize / 6 * 3)); //set font of texts (code from Fifteen Puzzle)
        displayPuzzle();
        sc.addMouseListener(this);
    }
    
    /**
     * Selects from among the provided files in folder Puzzles. 
     * The number xyz selects pxy-ez.txt. 
     */
    public AkariViewer(int n)
    {
        this(new Akari("Puzzles/p" + n / 10 + "-e" + n % 10 + ".txt"));
    }
    
    /**
     * Uses the provided example file on the LMS page.
     */
    public AkariViewer()
    {
        this(77);
    }
    
    /**
     * Returns the internal state of the puzzle.
     */
    public Akari getPuzzle()
    {
        // TODO 9a
        return this.puzzle;
    }
    
    /**
     * Returns the canvas.
     */
    public SimpleCanvas getCanvas()
    {
        // TODO 9b
        return sc;
    }
    
    /**
     * Displays the initial puzzle; see the LMS page for a suggested layout. 
     */
    private void displayPuzzle()
    {
        // TODO 11
        for (int row = 0; row < size; row++)
        {
            for (int col = 0; col < size; col++)
            {   
                Color color = Color.black; //sets as initial color of tile
                int number = -1; // just to initialize number variable
                
                if(puzzle.getBoard(row, col) == Space.EMPTY)
                {
                    if (puzzle.canSeeBulb(row, col))
                    {
                        color = Color.yellow;
                    }
                    else
                    {
                        color = Color.white;
                    }
                }
                else if(puzzle.getBoard(row,col) == Space.BULB)
                {
                    color = Color.yellow;
                }
                else if (puzzle.getBoard(row,col) == Space.ZERO)
                {
                    number = 0;
                }
                else if (puzzle.getBoard(row,col) == Space.ONE)
                {
                    number = 1;
                }
                else if (puzzle.getBoard(row,col) == Space.TWO)
                {
                    number = 2;
                }
                else if (puzzle.getBoard(row,col) == Space.THREE)
                {
                    number = 3;
                }
                else if(puzzle.getBoard(row,col) == Space.FOUR)
                {
                    number = 4;
                }
                
                drawTile(col, row, color);
                
                if (number > -1)
                {
                    sc.drawString(number, (int) (tilesize * (col + 0.45 - (1) * 0.1)), 
                        (int) (tilesize * (row + 0.6)), 
                        Color.white);
                }
                
                else if (puzzle.getBoard(row,col) == Space.BULB)
                {
                    sc.drawString("\u2600", (int) (tilesize * (col + 0.45 - (1) * 0.1)), //draw sun, we chose sun than bulb unicode as bulb does not show in all operating systems
                        (int) (tilesize * (row + 0.6)),Color.black);
                }
                
            }
        }


        sc.drawRectangle(0, size * tilesize + offset, size * tilesize  + offset, 
                    (size + 1) * tilesize  + offset, Color.white); // create a white area below the puzzle canvas
        sc.drawString("SOLVED?", tilesize, (tilesize * (size + 1)), Color.black);
        sc.drawString("CLEAR",tilesize * 5, (tilesize * (size + 1)), Color.black);
        sc.drawRectangle(0, (size + 1)  * tilesize + offset, size * tilesize  + offset, 
                    (size + 2) * tilesize  + offset, Color.white); // create a white area below the puzzle canvas

    }
    
    /**
     * HELPER METHOD for displayPuzzle 
     * Draws a tile for the puzzle
     */
    private void drawTile(int x, int y, Color c)
    {
        sc.drawRectangle(x * tilesize + offset, y * tilesize + offset, 
            (x + 1) * tilesize - offset, (y + 1) * tilesize - offset, c);
    }
        
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * Updates both puzzle and the display. 
     */
    public void leftClick(int r, int c)
    {
        // TODO 12 

        //this is to make "Solved" and "Clear" clickable
        if (r < size)
        {
            puzzle.leftClick(r,c);
            displayPuzzle();
        }
        else
        {
            displayPuzzle();
            if ((r == size) && (c == 1) || (r == size) && (c == 2))
            {
                sc.drawString(puzzle.isSolution(), tilesize, (tilesize * (size + 2) ), Color.black);
            }
            
            if ((r == size) && (c == 6) || (r == size) && (c == 5))
            {
                puzzle.clear();
                displayPuzzle();
            }
        }
    }
    
    // TODO 13
    public void mousePressed (MouseEvent e) {}
    public void mouseClicked (MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
        //find which row and col the mx,my is located then update puzzle
        leftClick(e.getY() / tilesize, e.getX() / tilesize);
    }
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited  (MouseEvent e) {}
}
