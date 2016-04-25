import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by nicho_000 on 4/24/2016.
 */
public class WordFrequency
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        BPlusTree tree = new BPlusTree();
        int userSelection = displayMenu(input), node;
        String word1, word2;

        while(userSelection != -1)
        {
            switch(userSelection)
            {
                case 1:
                    tree.printWords();
                    break;
                case 2:
                    tree.printTreeLayout();
                    break;
                case 3:
                    node = intInput(input, "Enter the node you wish to display");
                    BPlusTree.BTNode requested = tree.getNode(node);
                    tree.printNode(requested);
                    break;
                case 4:
                    System.out.println("Enter the word you wish to insert into the tree:");
                    word1 = input.nextLine();
                    tree.insertWord(word1);
                    break;
                case 5:
                    System.out.println("Enter the word you wish to serach for:");
                    word1 = input.nextLine();
                    tree.search(word1);
                    break;
                case 6:
                    System.out.println("Enter a word for the lower boundary of the range search:");
                    word1 = input.nextLine();
                    System.out.println("Enter a word for the upper boundary of the range search:");
                    word2 = input.nextLine();
                    tree.rangeSearch(word1, word2);
                    break;
            }//end switch
            userSelection = displayMenu(input);
        }//end while
        System.out.println("Thank You for using the Buffer Manager, we are cleaning up and exiting...");
    }

    private static int displayMenu(Scanner input)
    {
        int userSelection = 0;

        String prompt = "Please select an option from the list above:";
        while (!(userSelection == 1 || userSelection == 2 || userSelection == 3 || userSelection == 4 || userSelection == -1
                || userSelection == 5 || userSelection == 6))
        {
            System.out.println("Please select an option from the list below:");
            System.out.println("1). Print All Words");
            System.out.println("2). Display Tree Layout");
            System.out.println("3). Display a Node from the Tree");
            System.out.println("4). Insert a Word");
            System.out.println("5). Search a Word");
            System.out.println("5). Search a Range");
            System.out.println("-1). Quit");

            userSelection = intInput(input, prompt);
            if (!(userSelection == 1 || userSelection == 2 || userSelection == 3 || userSelection == 4 || userSelection == -1
                    || userSelection == 5 || userSelection == 6))
            {
                System.out.println("That is not a valid choice for this program, please try again.\n");
            }//end if
        }//end while
        return userSelection;
    }//end displayMenu

    private static int intInput(Scanner input, String prompt)
    {
        int ui;
        while (true)
        {
            try
            {
                System.out.println(prompt);
                ui = input.nextInt();
                if (ui <-1)
                {
                    System.out.println("Negative numbers, other then -1 to exit, are not accepted as input by this program.");
                    throw new InputMismatchException();
                }
                return ui;
            }//end try
            catch (Exception e)
            {
                System.out.println("There was an error with the input please try again.");
                input.nextLine();
            }//end catch
        }//end while
    }//end intInput
}
