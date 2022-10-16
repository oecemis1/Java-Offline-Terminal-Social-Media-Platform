import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{

    public User userInterface; //User object used to use User methods in main

    private ArrayList<User> users = new ArrayList<>(0);

    public FileOperations fileOperations; //FileOperations object used to use User methods in main

    private final Loader Operator = new Loader();


    public Main(){
        fileOperations = new FileOperations("main");
        purge();
        fileOperations = new FileOperations("main");
        userInterface = new User();
    }

    public void Create(String input){
        String username = "";
        String bio = "";

        //Elimination of the operator choice
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                input = input.substring(i + 1);
                break;
            }
        }

        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i)==' '){
                username = input.substring(0,i);
                bio = input.substring(i+1);
                break;
            }
        }

        //these are sparated beacuse if they weren't, userInterface would create a extra file called .txt in timeline and profile directories
        //since the main constructor has the line : userInterface = new User();
        userInterface.createUser(username,bio);
        this.users.add(new User(username,bio));
    }

    public int operatorChooser(String in){
        //Operators are divided into the classes, Create is implemented in Main and Follow Unfollow and Edit are implemented in User and Save is implemented in Loader
        String[] StringT = in.split(" ");
        switch (StringT[0]){
            case "Create":
                Create(in);
                return 1;

            case "Follow":
                userInterface.Follow(in,this.users);
                return 2;

            case "Unfollow":
                userInterface.Unfollow(in,this.users);
                return 3;

            case "Share":
                userInterface.Share(in,this.users);
                return 4;

            case "Edit":
                userInterface.Edit(in,this.users);
                return 5;

            case "Save":
                Operator.Save(in,users);
                return 6;

            case "Exit.":
                return 11111;
        }
        return 0;
    }
    public void TerminateMessage(){
        //Optional Exit message
        System.out.println("Terminating all of the employees...");
        System.out.println("Action Completed in 15 minutes!");
        System.out.println("Eliminating the CEO...");
        System.out.println("Action Completed in 5 seconds!");
        System.out.println("Releasing the corruption and election fraud files to the public...");
        System.out.println("Action Completed in 35 seconds!");
        System.out.println("Destroying the servers...");
        System.out.println("Action Completed in 1 minute!");
        System.out.println("Blowing up the headquarters...");
        System.out.println("Action Completed in 2 minutes 35 seconds!");
        System.out.println("Fake Twitter is destroyed successfully!");
    }
    public void InteractiveMode(){
        System.out.println("===Interactive Mode===");

        boolean ender = false;
        Scanner In = new Scanner(System.in);
        String userIn;

        while(!ender){

            System.out.println("Please type your command");
            userIn = In.nextLine();

            if(operatorChooser(userIn)==11111){
                System.out.println("Would you like to purge the directories?");
                Scanner keys = new Scanner(System.in);
                if(keys.nextLine().equals("yes")){
                    this.purge();
                }
                //this.TerminateMessage();
                ender = true;
            }

        }
    }

    public void AutomaticMode(String filename){
        System.out.println("===Automatic Mode===");

        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader(filename));
            String line = r.readLine();
            while (line != null) {
                operatorChooser(line);
                line = r.readLine();
            }
            r.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Command File Not Found!");
        }
        catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Would you like to purge the directories?");
        Scanner keys = new Scanner(System.in);
        if(keys.nextLine().toLowerCase().equals("yes")){
            purge();
        }

    }

    public void loadPreviousInstace(String directory, ArrayList<User> users){
        System.out.println("===Load Mode===");
        this.users = Operator.Load(users,directory);
        System.out.println("Loaded instance from " + directory + "!");
        System.out.println(this.users);
        InteractiveMode();
    }

    public void purge(){
        //Optional function that resets profile and timeline directories
        fileOperations.deleteDirectories("timelines");
        fileOperations.deleteDirectories("profiles");
    }

    public static void main(String[] args) {
        Main FakeTwitter = new Main();


        switch (args.length){
            case 0:
                FakeTwitter.InteractiveMode();
                break;
            case 1:
                FakeTwitter.AutomaticMode(args[0]);
                break;
            case 2:
                FakeTwitter.loadPreviousInstace(args[1],FakeTwitter.users);
                break;
        }
        //FakeTwitter.purge();
    }

}
