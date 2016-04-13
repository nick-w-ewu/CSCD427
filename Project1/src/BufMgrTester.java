import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by nicho on 4/6/2016.
 */
public class BufMgrTester
{
    public static void main(String[] args)
    {
        int size = 0;
        if(args.length == 0)
        {
            System.out.println("Usage: BufMgrTester a_number");
            System.exit(-1);
        }
        else
        {
            try
            {
                size = Integer.parseInt(args[0]);
            }
            catch(Exception e)
            {
                System.out.println("Usage: BufMgrTester a_number");
                System.exit(-1);
            }
        }
        BufHashTbl mappings = new BufHashTbl();
        BufMgr manager = new BufMgr(size, mappings);
        Scanner input = new Scanner(System.in);
        int userSelection = displayMenu(input), page;

        while(userSelection != -1)
        {
            switch(userSelection)
            {
                case 1:
                    int numberPages = intInput(input, "Enter the number of pages to create");
                    createPages(numberPages, manager);
                    break;
                case 2:
                    page = intInput(input, "Enter the page you wish to retrieve");
                    requestPage(page, manager);
                    break;
                case 3:
                    page = intInput(input, "Enter the page you wish to update");
                    updatePage(page, manager, input);
                    break;
                case 4:
                    page = intInput(input, "Enter the page you wish to release");
                    relinquishPage(page, manager);
                    break;
            }//end switch
            manager.printFrameTable();
            userSelection = displayMenu(input);
        }//end while
        System.out.println("Thank You for using the Buffer Manager, we are cleaning up and exiting...");
        manager.cleanUp();
    }

    private static void relinquishPage(int page, BufMgr manager)
    {
        manager.unpin(page);
    }

    private static void updatePage(int page, BufMgr manager, Scanner input)
    {
        PageFrame frame = manager.getPage(page);
        ArrayList<String> contents = frame.getContents();
        for(String s : contents)
        {
            System.out.println(s);
        }
        readPageUpdate(frame, input);
    }

    private static void readPageUpdate(PageFrame frame, Scanner input)
    {
        input.nextLine();
        System.out.println("Enter the contents you would like to add to the file,\n when you are finished enter **");
        String toAdd = input.nextLine();
        while(!toAdd.equals("**"))
        {
            frame.addContents(toAdd);
            toAdd = input.nextLine();
        }
    }

    private static void requestPage(int page, BufMgr manager)
    {
        PageFrame frame = manager.pin(page);
        if(frame == null)
        {
            System.out.println("Page not found, it may not exist or there was an error reading it");
        }
        else
        {
            ArrayList<String> contents = frame.getContents();
            for(String s : contents)
            {
                System.out.println(s);
            }
        }
    }

    private static int displayMenu(Scanner input)
    {
        int userSelection = 0;

        String prompt = "Please select an option from the list above:";
        while (!(userSelection == 1 || userSelection == 2 || userSelection == 3 || userSelection == 4 || userSelection == -1))
        {
            System.out.println("Please select an option from the list below:");
            System.out.println("1). Create Pages");
            System.out.println("2). Request a Page");
            System.out.println("3). Update a Page");
            System.out.println("4). Relinquish a Page");
            System.out.println("-1). Quit");

            userSelection = intInput(input, prompt);
            if (!(userSelection == 1 || userSelection == 2 || userSelection == 3 || userSelection == 4 || userSelection == -1))
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

    private static void createPages(int n, BufMgr manager)
    {
        if(n < 1)
        {
            System.out.println("We cannot create 0 or a negative number of pages please try again");
            return;
        }
        for(int i = 0; i < n; i++)
        {
            manager.createPage(i);
        }
    }
}
