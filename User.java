import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class User {
    FileOperations fileOperator;
    Message messageOperator;
    public ArrayList<User> followers;
    private String username;
    private String bio;

    private Master master = new Master();

    public User(){
        fileOperator = new FileOperations("user");
        followers = new ArrayList<>(0);
        messageOperator = new Message();
    }

    public User(String username,String bio){
        fileOperator = new FileOperations("user");
        followers = new ArrayList<>(0);
        messageOperator = new Message();
        setUsername(username);
        setBio(bio);
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio(){
        return this.bio;
    }
    public void createUser(String user, String bio){
        //Designation of user file names and insertion into timeline and profile directories
        String timelineFileName = user.concat(".txt");
        String profilesFileName = user.concat(".txt");
        fileOperator.addToDirectory(timelineFileName,"timelines");
        fileOperator.addToDirectory(profilesFileName,"profiles");
        this.setUsername(user);
        this.setBio(bio);

        //Writing the initial profile information of the user, with an extra feature
        PrintWriter w;
        try{
            String path = fileOperator.returnPath(user,"profiles");
            FileWriter fw = new FileWriter(path);
            w = new PrintWriter(fw);
            w.println("User Name: " + user);
            w.println("Bio: " + bio);

            //If the follow feature needs to be tested with manual time entry this can be used.
            if(username.equals("OldtimeInserter")) {
                w.println("17/06/1987 15:04 this is a time insertion test");
            }
            w.flush();
            w.close();
        }
        catch (FileNotFoundException e){
            System.out.println("User not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(user + " has been created!");
    }
    public void Follow(String input,ArrayList<User> users){
        String follower = "";
        String followTarget = "";

        boolean followConfirm = false;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                input = input.substring(i + 1);
                break;
            }
        }
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i)==' '){
                follower = input.substring(0,i);
                followTarget = input.substring(i+1);
                break;
            }
        }
        if(follower.equals(followTarget)){
            System.out.println("Follower and Follow Target are the same");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(followTarget)){
                for (int j = 0; j < users.size(); j++) {
                    if(users.get(j).getUsername().equals(follower)){
                        int copyCount = 0;
                        for (int k = 0; k < users.get(i).followers.size(); k++) {
                            if (users.get(i).followers.get(k).getUsername().equals(follower)) {
                                copyCount++;
                            }
                        }
                        if (copyCount == 0) {
                            users.get(i).followers.add(users.get(j));
                            fileOperator.insertToTimeline("timelines",users.get(j).getUsername(),users.get(i).getUsername());
                            followConfirm = true;
                            break;
                        }
                        else {
                            System.out.println("Already Following ");
                            break;
                        }
                    }
                    else{
                        System.out.println("Follower User: " + follower + " not found!");
                    }
                }
                break;
            }
        }
        if(followConfirm) {
            System.out.println(follower + " is following " + followTarget);
        }
    }
    public void Unfollow(String input,ArrayList<User> users){
        String follower = "";
        String unfollowTarget = "";

        boolean unfollowConfirm = false;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                input = input.substring(i + 1);
                break;
            }
        }
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i)==' '){
                follower = input.substring(0,i);
                unfollowTarget = input.substring(i+1);
                break;
            }
        }
        if(follower.equals(unfollowTarget)){
            System.out.println("Follower and Unfollow Target are the same!");
            return;
        }
        boolean existnt = true;
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(unfollowTarget)){
                for (int j = 0; j < users.size(); j++) {
                    if(users.get(j).getUsername().equals(follower)){
                        boolean follownt = true;
                        for (int k = 0; k < users.get(i).followers.size(); k++) {
                            if(users.get(i).followers.get(k).getUsername().equals(follower)){
                                users.get(i).followers.remove(users.get(j));
                                fileOperator.removeFromTimeline("timelines",follower,unfollowTarget);
                                existnt = false;
                                follownt = false;
                                unfollowConfirm = true;
                                break;
                            }
                        }
                        if(follownt){
                            System.out.println("Already not following");
                        }
                        break;
                    }
                    else{
                        System.out.println("User trying to unfollow does not exist!");
                    }
                }
                break;
            }
        }
        if(existnt){
            System.out.println("Atleast one of the users does not exist");
        }
        if(unfollowConfirm){
            System.out.println(follower + " is unfollowing "+ unfollowTarget);
        }
    }
    public void Share(String input,ArrayList<User> users){
        String username = "";
        String message = "";

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                input = input.substring(i + 1);
                break;
            }
        }
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i)==' '){
                username = input.substring(0,i);
                message = input.substring(i+1);
                break;
            }
        }
        if(username.equals("master")){
            master.messageForAllUsers(users, "master " + message);
        }
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        String[] TimeAndMessage = new String[2];
        TimeAndMessage[0] = timeStamp;
        TimeAndMessage[1] = message;
        messageOperator.setMessage(message);
        messageOperator.setMessager(username);
        messageOperator.updateTimeLineofAt(TimeAndMessage,users,username);
        messageOperator.updateTimelineOfFollowers(TimeAndMessage,users,username);
        fileOperator.appendToFile(TimeAndMessage, "profiles", username);
        System.out.println(username + " shared the following message: " + message);
    }
    public void Edit(String input,ArrayList<User> users){
        //Edits the selected message
        //Since it has not been explained in detail, this function does this:
        //Finds the line matching with the message from the profile file of the user
        //If there is more than one of the same line it changes the line latest added
        //Rewriting only the message part, not updating the timestamp
        //Rewrites the file with the changes made
        String username = "";
        String oldMessage = "";
        String newMessage = "";
        //Removal of the Edit operator from the input string
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                input = input.substring(i + 1);
                break;
            }
        }
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i)==' '){
                username = input.substring(0,i);
                input = input.substring(i+1);
                break;
            }
        }
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i)=='|'){
                oldMessage = input.substring(0,i);
                newMessage = input.substring(i+1);
                break;
            }
        }
        int messageCount = fileOperator.scanForMessage("profiles",username,oldMessage);
        fileOperator.changeLine("profiles",username,oldMessage,newMessage,messageCount);
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(username)){
                for (int j = 0; j < users.get(i).followers.size(); j++) {
                    fileOperator.changeLine("timelines",users.get(i).followers.get(j).getUsername(),oldMessage,newMessage,messageCount);
                }
                messageOperator.setMessager(username);
                messageOperator.setMessage(newMessage);
                String []mentioned = messageOperator.getTaggedInMessage(users);
                for (int j = 0; j <mentioned.length ; j++) {
                    fileOperator.changeLine("timelines",mentioned[j],oldMessage,newMessage,messageCount);
                }
            }
        }
    }
}
