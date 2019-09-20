
/**
 * A card represents one of the cards in a standard deck. It has a rank, 
 * a suit, and is either turned up or turned down.
 * 
 * @author      Annabelle Perng
 * @version     11.08.2018
 */
public class Card
{
    // instance variables - replace the example below with your own
    private int rank;
    private String suit;
    private boolean isFaceUp;

    /**
     * Constructor for objects of Card class.
     * 
     * @param   rank        the card's rank, where 1 ≤ rank ≤ 13
     * @param   suit        the card's suit; "c" for clubs, "d" for diamonds,
     *                      "h" for hearts, "s" for spades
     * @param   isFaceUp    true if the card is initially turned face up
     */
    public Card(int rank, String suit, boolean isFaceUp)
    {
        this.rank = rank;
        this.suit = suit;
        this.isFaceUp = isFaceUp;
    }
    
    /**
     * Returns the numerical rank of the card, where 1 ≤ rank ≤ 13. 1 denotes
     * an Ace, while 11, 12, and 13 denote Jacks, Queens, and Kings, respectively.
     * 
     * @return  an integer denoting the rank of the card
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Returns the suit of the card expressed in a single character string –
     * "c" for clubs, "d" for diamonds, "h" for hearts, "s" for spades.
     * 
     * @return  the suit of the card
     */
    public String getSuit()
    {
        return suit;
    }

    /**
     * Determines whether the card is red.
     * 
     * @return  true if the card belongs to the heart or diamond suits; otherwise,
     *          false
     */
    public boolean isRed()
    {
        return (suit.equals("h") || suit.equals("d"));
    }

    /**
     * Determines whether the card is face up in the pile.
     * 
     * @return  true if the card is turned face up; otherwise,
     *          false
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }

    /**
     * Turns a card face up if it is facing down.
     */
    public void turnUp()
    {
        isFaceUp = true;
    }
    
    /**
     * Turns a card face down if it is facing up.
     */
    public void turnDown()
    {
        isFaceUp = false;
    }

    /**
     * Retrieves the file name of the card. If the card is facing up,
     * the file name corresponds to a visual representation showing the card's
     * suit and rank. Otherwise, the file path to the back side of the 
     * card is returned.
     * 
     * @return  a file path to the image which represents the card in its current state
     */
    public String getFileName()
    {
        //System.out.println(System.getProperty("user.dir") + "/cards/");
        if (!(isFaceUp))
        {
            return "cards/back.gif";
        }
        
        String fileName = "";
        fileName += "cards/";
        
        if (rank == 1)
        {
            fileName += "a";
        }
        else if (rank < 10)
        {
            fileName += rank;
        }
        else
        {
            if (rank == 10)
            {
                fileName += "t";
            }
            else if (rank == 11)
            {
                fileName += "j";
            }
            else if (rank == 12)
            {
                fileName += "q";
            }
            else
            {
                fileName += "k";
            }
        }
        
        fileName += suit;
        fileName += ".gif";
        
        //System.out.println(fileName);
        
        return fileName;
    }
}
