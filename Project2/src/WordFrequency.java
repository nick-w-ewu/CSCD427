import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by nicho_000 on 4/24/2016.
 */
public class WordFrequency
{
    static Hashtable<String, String> skippedWords = new Hashtable<>();
    public static void main(String[] args)
    {
        String html = "";
        if(args.length == 0)
        {
            System.out.println("Usage: BufMgrTester URL [ignore_file_name]");
        }
        else if(args.length == 1)
        {
            html = args[0];
        }
        else if(args.length == 2)
        {
            html = args[0];
            processStopWords(args[1]);
        }

        BPlusTree tree = new BPlusTree();
        try
        {
            System.out.println("Parsing HTML, please hang on...\n");
            Document doc = Jsoup.connect(html).get();
            String body = doc.body().text(), temp;
            String[] words = body.split(" ");
            for(String word : words)
            {
                temp = strip(word);
                if(isAccepted(temp))
                {
                    tree.insertWord(temp);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to parse html file, you can manually enter words if you would like");
        }
        Scanner input = new Scanner(System.in);
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
                    input.nextLine();
                    System.out.println("Enter the word you wish to insert into the tree:");
                    word1 = input.nextLine();
                    tree.insertWord(word1);
                    break;
                case 5:
                    input.nextLine();
                    System.out.println("Enter the word you wish to serach for:");
                    word1 = input.nextLine();
                    tree.search(word1);
                    break;
                case 6:
                    input.nextLine();
                    System.out.println("Enter a word for the lower boundary of the range search:");
                    word1 = input.nextLine();
                    System.out.println("Enter a word for the upper boundary of the range search:");
                    word2 = input.nextLine();
                    tree.rangeSearch(word1, word2);
                    break;
            }//end switch
            userSelection = displayMenu(input);
        }//end while
        System.out.println("Thank You for using the B+ Tree Manager, we are cleaning up and exiting...");
    }

    private static void processStopWords(String file)
    {
        try
        {
            Scanner fin = new Scanner(new File(file));
            String temp;
            while(fin.hasNext())
            {
                temp = fin.nextLine();
                skippedWords.put(temp, temp);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Failed to process ignored words list, continuing without it.\n");
        }
    }

    private static boolean isAccepted(String temp)
    {
        try
        {
            int i = Integer.parseInt(temp);
            return false;
        }
        catch (Exception e)
        {
            if(temp.length() == 0)
            {
                return false;
            }
            String test = skippedWords.get(temp);
            if(test == null)
            {
                return true;
            }
        }
        return false;
    }

    private static String strip(String word)
    {
        if(word.length() <= 1)
        {
            return word.toLowerCase();
        }
        char first = word.charAt(0), last = word.charAt(word.length()-1);
        int start = 0, end = word.length();
        if(!Character.isAlphabetic(first))
        {
            start = 1;
        }
        if(!Character.isAlphabetic(last))
        {
            end--;
        }
        String temp = word.substring(start, end);
        return temp.toLowerCase();
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
            System.out.println("6). Search a Range");
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
