import java.util.*;

/**
 * A Solitaire object keeps track of a game of Solitaire, where a 
 * stock, waste, 7 piles, and 4 foundations are manipulated by the
 * user with the end goal of transferring all cards to the foundations.
 * 
 * @author  Annabelle Perng
 * @version 11.08.2018
 */
public class Solitaire
{
    /**
     * Initializes a new Solitaire game.
     * 
     * @param   args    arguments for the command line
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;
    //private int victories;
    //private boolean hasWon;

    /**
     * Constructor for objects of Solitaire class. Initializes the stock,
     * waste, piles, and foundations, shuffles the cards, then deals
     * the cards.
     */
    public Solitaire()
    {
        foundations = new Stack[4];
        piles = new Stack[7];
        stock = new Stack<Card>();
        waste = new Stack<Card>();

        createStock();
        //victories = 0;
        deal();

        display = new SolitaireDisplay(this);
    }

    /**
     * Creates the stock, which will initially contain all 52
     * playing cards, sorted in random order.
     */
    private void createStock()
    {
        ArrayList<Card> allCards = new ArrayList<Card>();
        String suit="";
        for (int j=0; j<4; j++)
        {
            if (j==0)
            {
                suit="c";
            }
            else if (j==1)
            {
                suit="d";
            }
            else if (j==2)
            {
                suit="h";
            }
            else
            {
                suit="s";
            }

            for (int k=1; k<=13; k++)
            {
                Card c = new Card(k, suit, false);
                allCards.add(c);
            }
        }

        for (int i=0; i<52; i++)
        {
            int temp = (int)(Math.random()*allCards.size());
            Card c = allCards.get(temp);
            stock.push(c);
            allCards.remove(temp);
        }
    }

    /**
     * Deals the cards into each of the piles, with 1 card dealt into the 
     * leftmost pile, 2 cards dealt into the second-to-leftmost pile, etc.
     * Turns up the card at the top of each pile.
     */
    private void deal()
    {
        for (int i=0; i<7; i++)
        {
            piles[i] = new Stack<Card>();
            for (int j=0; j<=i; j++)
            {
                Card dealedCard = stock.pop();
                if (j==i)
                {
                    dealedCard.turnUp();
                }
                piles[i].push(dealedCard);
            }
        }
    }

    /**
     * Deals three cards from the stock onto the waste. The last dealt
     * card is turned face up.
     */
    private void dealThreeCards()
    {
        int counter = 3;

        while (counter>0 && !stock.isEmpty())
        {
            Card dealedCard = stock.pop();
            dealedCard.turnUp();
            waste.push(dealedCard);
            counter--;
        }
    }

    /**
     * Resets the stock by transferring all cards from the waste back onto the stock.
     */
    private void resetStock()
    {
        while (!waste.isEmpty())
        {
            stock.push(waste.pop());
            stock.peek().turnDown();
        }
    }

    /**
     * Returns the card at the top of the stock; returns null if the stock is empty.
     * 
     * @return  the first card in the stock
     */
    public Card getStockCard()
    {
        if (stock.isEmpty())
        {
            return null;
        }
        return stock.peek();
    }

    /**
     * Returns the card at the top of the waste; returns null if the waste is empty.
     * 
     * @return  the first card in the waste
     */
    public Card getWasteCard()
    {
        if (waste.isEmpty())
        {
            return null;
        }
        return waste.peek();
    }

    /**
     * Returns the card at the top of the foundation; returns null if foundation is empty.
     * 
     * @param   index   the index corresponding to the foundation pile of interest; 0 <= index <= 3
     * @return          the card at the top of the foundation at index index
     */
    public Card getFoundationCard(int index)
    {
        if (foundations[index]==null || foundations[index].isEmpty())
        {
            return null;
        }
        return foundations[index].peek();
    }

    /**
     * Returns a reference to the selected pile.
     * 
     * @param   index   the index corresponding to the pile of interest; 0 <= index <= 6
     * @return          a reference to the pile at index index
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    /**
     * Called when the stock is clicked; interprets this action as either
     * a selection / deselection of the stock. If the selected stock is non-empty,
     * three cards are dealt from the stock to the waste. Otherwise, all the cards
     * from the waste are moved back onto the stock.
     */
    public void stockClicked()
    {
        if (!display.isWasteSelected() && !display.isPileSelected())
        {
            if (!stock.isEmpty())
            {
                dealThreeCards();
            }
            else
            {
                resetStock();
            }
        }
        System.out.println("stock clicked");
    }

    /**
     * Called when the waste is clicked; interprets this action as either
     * a selection / deselection of the waste. (If the waste is non-empty,
     * the top-most card may be moved onto one of the piles or one of the foundations
     * if such a move is legal.)
     */
    public void wasteClicked()
    {
        if (display.isWasteSelected())
        {
            display.unselect();
        }
        else
        {
            if (!waste.isEmpty() && !display.isPileSelected())
            {
                display.selectWaste();
            }
        }
        System.out.println("waste clicked");
    }

    
    /**
     * Called when a foundation pile is clicked; interprets this action as either
     * a selection / deselection of the foundation. If the user has already
     * selected a pile or the waste, the card at the top of the pile/waste is
     * moved to the foundation, if such a move is legal.
     * 
     * @param   index   the index corresponding to the foundation pile of interest; 0 <= index <= 3
     */
    public void foundationClicked(int index)
    {
        if (foundations[index]==null)
        {
            foundations[index]=new Stack<Card>();
        }

        if (display.isWasteSelected() && !waste.isEmpty())
        {
            if (canAddToFoundation(waste.peek(), index))
            {
                foundations[index].push(waste.pop());
            }
        }
        else if (display.isPileSelected() && !piles[display.selectedPile()].isEmpty())
        {
            int selected = display.selectedPile();
            if (canAddToFoundation(piles[selected].peek(), index))
            {
                foundations[index].push(piles[selected].pop());
            }

            else
            {
                display.unselect();
            }
        }
        
        System.out.println("foundation #" + index + " clicked");
    }

    /**
     * Called when a pile is clicked; interprets this action as either
     * a selection / deselection of the pile. If the user has already
     * selected a pile, the previously selected pile will be moved onto
     * the pile at index index, if such a move is legal.
     * 
     * If the user has already selected the waste, the card at the top of the waste
     * will be moved onto the pile at index index, if such a move is legal.
     * 
     * @param   index   the index corresponding to the pile of interest; 0 <= index <= 6
     */
    public void pileClicked(int index)
    {
        if (display.isWasteSelected() && !waste.isEmpty())
        {
            if (canAddToPile(waste.peek(), index))
            {
                piles[index].push(waste.pop());
                display.unselect();
            }
        }
        else if (display.isPileSelected())
        {
            int selected = display.selectedPile();
            if (selected == index)
            {
                display.unselect();
            }
            else
            {
                Stack<Card>topCards = removeFaceUpCards(selected);
                //System.out.println("apple");
                if (!topCards.isEmpty() && canAddToPile(topCards.peek(),index))
                {
                    addToPile(topCards, index);
                    //System.out.println("banan");
                }
                else //can't move; returns popped cards to original deck
                {
                    addToPile(topCards, selected);
                    //System.out.println("carrot");
                }
            }
            display.unselect();
        }
        else
        {
            if (!(piles[index].isEmpty()))
            {
                //System.out.println(index);
                piles[index].peek().turnUp();
            }
            display.selectPile(index);
        }

        //System.out.println("pile selected " + display.isPileSelected());
        System.out.println("pile #" + index + " clicked");
    }

    /**
     * Determines if a card may be added to a pile.
     * 
     * @param   card    the card of interest
     * @param   index   the index corresponding to the pile to be added to
     * 
     * @return  true if the parameter card can be added to the pile (its
     *          rank is one less than the card at the top of the pile, and
     *          its color is opposite); otherwise,
     *          false
     */
    private boolean canAddToPile(Card card, int index)
    {
        if (piles[index].isEmpty())
        {
            return (card.getRank()==13);
        }
        else
        {
            Card top = piles[index].peek();
            return (card.getRank() == top.getRank() - 1 && card.isRed() != top.isRed());
        }
    }

    /**
     * Removes and returns all the face up cards from the designated pile.
     * 
     * @param   index   the index corresponding to the pile to be removed from;
     *                  0 <= index <= 6
     * @return          the face-up cards in the pile at index index
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card>faceUp = new Stack<Card>();
        while (!(piles[index].isEmpty()) && (piles[index].peek().isFaceUp()))
        {
            faceUp.push(piles[index].pop());
        }
        return faceUp;
    }

    /**
     * Removes elements from cards, and adds them to the given pile.
     * 
     * @precondition    the Stack of cards can be legally added to the pile at index index
     * @param   cards   the index corresponding with the pile to be added to
     * @param   index   the cards to be added to the pile
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        while (!cards.isEmpty())
        {
            piles[index].push(cards.pop());
        }
    }

    /**
     * Determines if a given card can be legally added to the
     * designated foundation pile. A card may be added to an empty foundation if it
     * is an Ace. A card may be added to a non-empty foundation if it has the same suit
     * and subsequent rank to the card at the top of the foundation.
     * 
     * @param   card    the card to be (potentially) added to the foundation
     * @param   index   the index corresponding to the foundation to be (potentially)
     *                  added to; 0 <= index <= 4
     * 
     * @return  true if the card can be legally added to the foundation at index index; otherwise,
     *          false
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        if (foundations[index]==null || foundations[index].isEmpty())
        {
            return (card.getRank()==1);
        }

        Card cardBefore = foundations[index].peek();
        if ((card.getSuit().equals(cardBefore.getSuit())) &&
            (card.getRank() == cardBefore.getRank() + 1))
        {
            return true;
        }

        return false;
    }
}