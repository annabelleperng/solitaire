import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * A SolitaireDisplay object deals with the graphics of the Solitaire game 
 * and handles any mouse-based interaction from the user. (Depending on
 * where the user clicks, the SolitaireDisplay selects/deselects different piles
 * on the screen).
 * 
 * @author  Annabelle Perng
 * @version 11.08.2018
 */
public class SolitaireDisplay extends JComponent implements MouseListener
{
    private static final int CARD_WIDTH = 73;
    private static final int CARD_HEIGHT = 97;
    private static final int SPACING = 5;  //distance between cards
    private static final int FACE_UP_OFFSET = 15;  //distance for cascading face-up cards
    private static final int FACE_DOWN_OFFSET = 5;  //distance for cascading face-down cards

    private JFrame frame;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Solitaire game;

    /**
     * Constructor of objects of SolitaireDisplay class.
     * 
     * @param   game    the game to be displayed onscreen
     */
    public SolitaireDisplay(Solitaire game)
    {
        this.game = game;

        frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);

        this.setPreferredSize(new Dimension(CARD_WIDTH * 7 + SPACING * 8, CARD_HEIGHT * 2
                              + SPACING * 3 + FACE_DOWN_OFFSET * 7 + 13 * FACE_UP_OFFSET));
        this.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Constructs a visual representation of the solitaire game, including 
     * the stock, waste, piles, and foundations.
     * 
     * @param   g   the display window which the game is to be shown on
     */
    public void paintComponent(Graphics g)
    {
        //background
        g.setColor(new Color(0, 128, 0));
        g.fillRect(0, 0, getWidth(), getHeight());

        //face down
        drawCard(g, game.getStockCard(), SPACING, SPACING);

        //stock
        drawCard(g, game.getWasteCard(), SPACING * 2 + CARD_WIDTH, SPACING);
        if (selectedRow == 0 && selectedCol == 1)
            drawBorder(g, SPACING * 2 + CARD_WIDTH, SPACING);

        //aces
        for (int i = 0; i < 4; i++)
            drawCard(g, game.getFoundationCard(i), SPACING * (4 + i)
                     + CARD_WIDTH * (3 + i), SPACING);

        //piles
        for (int i = 0; i < 7; i++)
        {
            Stack<Card> pile = game.getPile(i);
            int offset = 0;
            for (int j = 0; j < pile.size(); j++)
            {
                drawCard(g, pile.get(j), SPACING + (CARD_WIDTH + SPACING) * i,
                         CARD_HEIGHT + 2 * SPACING + offset);
                if (selectedRow == 1 && selectedCol == i && j == pile.size() - 1)
                    drawBorder(g, SPACING + (CARD_WIDTH + SPACING) * i,
                               CARD_HEIGHT + 2 * SPACING + offset);

                if (pile.get(j).isFaceUp())
                    offset += FACE_UP_OFFSET;
                else
                    offset += FACE_DOWN_OFFSET;
            }
        }
    }

    /**
     * Displays a card, if it is non-null, at position (x,y).
     * 
     * @param   g       the display window which the game is to be shown on
     * @param   card    the card to be displayed
     * @param   x       integer coordinate corresponding to the horizontal position of the card
     * @param   y       integer coordinate corresponding to the vertical position of the card
     * 
     */
    private void drawCard(Graphics g, Card card, int x, int y)
    {
        if (card == null)
        {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
        else
        {
            //System.out.println(System.getProperty("user.dir"));
            String fileName = card.getFileName();
            //System.out.println("DEBUGGING: THE FILE NAME YOU GOT WAS " + fileName);
            if (!new File(fileName).exists())
                throw new IllegalArgumentException("bad file name:  " + fileName);
            Image image = new ImageIcon(fileName).getImage();
            g.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /**
     * Tracks if the mouse has left the area of the display.
     * 
     * @param   e   the mouse-based user interaction
     */
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * Tracks if the mouse is within the area of the display.
     * 
     * @param   e   the mouse-based user interaction
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Tracks a release of the mouse.
     * 
     * @param   e   the mouse-based user interaction
     */
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * Tracks a press of the mouse.
     * 
     * @param   e   the mouse-based user interaction
     */
    public void mousePressed(MouseEvent e)
    {
    }

    /**
     * Tracks if the mouse has been clicked.
     * 
     * @param   e   the mouse-based user interaction
     */
    public void mouseClicked(MouseEvent e)
    {
        //none selected previously
        int col = e.getX() / (SPACING + CARD_WIDTH);
        int row = e.getY() / (SPACING + CARD_HEIGHT);
        if (row > 1)
            row = 1;
        if (col > 6)
            col = 6;

        if (row == 0 && col == 0)
            game.stockClicked();
        else if (row == 0 && col == 1)
            game.wasteClicked();
        else if (row == 0 && col >= 3)
            game.foundationClicked(col - 3);
        else if (row == 1)
            game.pileClicked(col);
        repaint();
    }

    /**
     * Draws the border around a card.
     * 
     * @param   g       the display window which the game is to be shown on
     * @param   x       integer coordinate corresponding to the horizontal position of the card
     * @param   y       integer coordinate corresponding to the vertical position of the card
     */
    private void drawBorder(Graphics g, int x, int y)
    {
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        g.drawRect(x + 1, y + 1, CARD_WIDTH - 2, CARD_HEIGHT - 2);
        g.drawRect(x + 2, y + 2, CARD_WIDTH - 4, CARD_HEIGHT - 4);
    }

    /**
     * Unselects any clicked pile.
     */
    public void unselect()
    {
        selectedRow = -1;
        selectedCol = -1;
    }

    /**
     * Determines if the waste pile has been selected.
     * 
     * @return  true if the waste has been selected by the user; otherwise,
     *          false
     */
    public boolean isWasteSelected()
    {
        return selectedRow == 0 && selectedCol == 1;
    }

    /**
     * Selects the waste pile.
     */
    public void selectWaste()
    {
        selectedRow = 0;
        selectedCol = 1;
    }

    /**
     * Determines if any of the seven piles has been selected.
     * 
     * @return  true if a pile has been selected by the user; otherwise,
     *          false
     */
    public boolean isPileSelected()
    {
        return selectedRow == 1;
    }

    /**
     * Returns information denoting which of the seven piles has been selected,
     * if one of the piles has been selected.
     * 
     * @return  an integer (0 ≤ integer ≤ 6) corresponding to the pile which has been selected
     *          (or -1, if no pile is selected)
     */
    public int selectedPile()
    {
        if (selectedRow == 1)
            return selectedCol;
        else
            return -1;
    }

    /**
     * Selects the pile at int index, where 0 ≤ index ≤ 6.
     * 
     * @param   index   the index of the pile to be selected
     */
    public void selectPile(int index)
    {
        selectedRow = 1;
        selectedCol = index;
    }
}