
import java.io.*;
import java.util.ArrayList;

public class FileOperations{

    String damn_right;

    public FileOperations(String modifier){
        if(modifier.equals("main")) {
            boolean ken10000011010;
            File FILECREATOR = new File("timelines");
            ken10000011010 = FILECREATOR.mkdir();
            FILECREATOR = new File("profiles");
            ken10000011010 = FILECREATOR.mkdir();
        }

        this.damn_right = "part2";
    }

    public String returnPath(String fileName, String directoryName){
        //Creates a string that combines the directory path and and file name to get the file's path inside the directory
        File temp = new File(directoryName);

        String path = temp.getAbsolutePath();
        char slash = 92;
        path = path + slash + fileName + ".txt";
        return path;
    }

    public void deleteDirectories(String file){ //Optional directory deleter.
        boolean ken10000011010;
        File FILEDELETER = new File(file);
        if(!FILEDELETER.isDirectory()) {
            ken10000011010 = FILEDELETER.delete();
        }
        else {
            File []array = FILEDELETER.listFiles();
            for(int i = 0;i<array.length;i++) {
                deleteDirectories(array[i].getAbsolutePath());
            }
            ken10000011010 = FILEDELETER.delete();
        }
    }

    public boolean compareTime(String insertionTime,String lineTime){ //returns false if time in the timeline file is bigger
        //Comparison done by comparing first the year then the month then the day... etc.
        String[] iTimeSplit = insertionTime.split(" ");
        String[] lTimeSplit = lineTime.split(" ");

        String iTimeYear = iTimeSplit[0].substring(6);
        String lTimeYear = lTimeSplit[0].substring(6);

        if(Integer.parseInt(lTimeYear)>Integer.parseInt(iTimeYear)){
            return false;
        }
        else if(Integer.parseInt(lTimeYear)==Integer.parseInt(iTimeYear)){
            String iTimeMonth = iTimeSplit[0].substring(3,5);
            String lTimeMonth = lTimeSplit[0].substring(3,5);

            if(Integer.parseInt(lTimeMonth)>Integer.parseInt(iTimeMonth)){
                return false;
            }
            else if(Integer.parseInt(lTimeMonth)==Integer.parseInt(iTimeMonth)){
                String iTimeDay = iTimeSplit[0].substring(0,2);
                String lTimeDay = lTimeSplit[0].substring(0,2);

                if(Integer.parseInt(lTimeDay)>Integer.parseInt(iTimeDay)){
                    return false;
                }
                else if(Integer.parseInt(lTimeDay)==Integer.parseInt(iTimeDay)){
                    String iTimeHour = iTimeSplit[1].substring(0,2);
                    String lTimeHour = lTimeSplit[1].substring(0,2);

                    if(Integer.parseInt(lTimeHour)>Integer.parseInt(iTimeHour)){
                        return false;
                    }
                    else if(Integer.parseInt(lTimeHour)==Integer.parseInt(iTimeHour)){
                        String iTimeMinute = iTimeSplit[1].substring(3);
                        String lTimeMinute = lTimeSplit[1].substring(3);
                        if(Integer.parseInt(lTimeMinute)>=Integer.parseInt(iTimeMinute)){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                    else{
                        return true;
                    }
                }
                else{
                    return true;
                }
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }

    public void addToDirectory(String file,String directory) {
        boolean ken10000011010;
        File FILEADDER;
        try {
            FILEADDER = new File(directory, file);
            ken10000011010 = FILEADDER.createNewFile();
            BufferedWriter w = new BufferedWriter(new FileWriter(returnPath(file.substring(0,file.length()-4), directory)));
            w.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void appendToFile(String[] input,String directory, String fileName){
        String path = this.returnPath(fileName,directory);

        PrintWriter k;
        try{
            k = new PrintWriter(new FileWriter(path,true));
            k.println(input[0] + " " + input[1]);
            k.flush();
            k.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public int scanForMessage(String directory,String fileName, String message){
        //Used in Edit to get the number of duplicate lines inside the file that is going to have its line edited
        String path = this.returnPath(fileName,directory);
        int count = 0;

        BufferedReader r;
        try{
            r = new BufferedReader(new FileReader(path));
            String line = r.readLine();
            while(line!=null){
                //If the line in the file contains the message, the timestamp from the line is removed anc checked for equality
                if(line.contains(message)){
                    //Removal of the timestamp
                    String[] temp = line.split(" ");
                    String lineWithoutTime = "";
                    for (int i = 2; i < temp.length; i++) {
                        lineWithoutTime += temp[i] + " ";
                    }
                    //Equality check
                    if(lineWithoutTime.substring(0,lineWithoutTime.length()-1).equals(message)){
                        count++;
                    }
                }
                line = r.readLine();
            }
        }
        catch(FileNotFoundException e){
            System.out.println("User not found!");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return count;
    }

    public void changeLine(String directory,String fileName,String message,String newMessage,int count){
        //This is the function used in Edit function in the User class
        String path = this.returnPath(fileName,directory);
        BufferedReader r;
        BufferedWriter w;
        try{
            int tempCount = 0;
            r = new BufferedReader(new FileReader(path));
            w = new BufferedWriter(new FileWriter("01101011 01100101 01101110 01100100 01110010 01101001 01100011 01101011 00100000 01101100 01100001 01101101 01100001 01110010 00100000 01101001 01110011 00100000 01110100 01101000 01100101 00100000 01100111 01101111 01100001 01110100.txt"));
            String line = r.readLine();
            while(line!=null){
                //Same thing with scanForMessage if the line contains the message it removes the timestamp and checks for equality
                if(line.contains(message)){
                    String[] temp = line.split(" ");
                    String time = temp[0] + " " + temp[1] + " ";
                    String lineWithoutTime = "";
                    String UserWhoWroteTheMessage = "";
                    switch (directory){
                        case "profiles":
                            for (int i = 2; i < temp.length; i++) {
                                lineWithoutTime += temp[i] + " ";
                            }
                            break;
                        case "timelines":
                            UserWhoWroteTheMessage = temp[2];
                            for (int i = 3; i < temp.length; i++) {
                                lineWithoutTime += temp[i] + " ";
                            }
                            break;
                    }
                    //Temp count is used to reach the latest copy of the same line
                    if (lineWithoutTime.substring(0,lineWithoutTime.length()-1).equals(message)) {
                        tempCount++;
                    }
                    //When this equality is met, reader is at the last copy of the same line
                    //At this point the line is changed and the whole file is written into a temporary file
                    if (tempCount == count) {
                        String newTemp = "";
                        switch (directory){
                            default:
                                newTemp = time + newMessage + "(edited)";
                                break;
                            case "timelines":
                                newTemp = time + UserWhoWroteTheMessage + " " + newMessage + "(edited)" ;
                                break;
                        }
                        w.write(newTemp);
                        w.flush();
                        w.write("\n");
                        w.flush();
                    } else {
                        w.write(line);
                        w.flush();
                        w.write("\n");
                        w.flush();
                    }
                }
                else{
                    w.write(line);
                    w.flush();
                    w.write("\n");
                    w.flush();
                }
                line = r.readLine();
            }
            r.close();
            w.close();
            //Rewriting the file with the changed line
            w = new BufferedWriter(new FileWriter(path));
            r = new BufferedReader(new FileReader("01101011 01100101 01101110 01100100 01110010 01101001 01100011 01101011 00100000 01101100 01100001 01101101 01100001 01110010 00100000 01101001 01110011 00100000 01110100 01101000 01100101 00100000 01100111 01101111 01100001 01110100.txt"));
            line = r.readLine();
            while(line!=null){
                w.write(line);
                w.flush();
                w.write("\n");
                w.flush();
                line = r.readLine();
            }
            w.close();
            r.close();

            //deletion of the temporary file
            boolean ken10000011010;
            File TEMPDELETE = new File("01101011 01100101 01101110 01100100 01110010 01101001 01100011 01101011 00100000 01101100 01100001 01101101 01100001 01110010 00100000 01101001 01110011 00100000 01110100 01101000 01100101 00100000 01100111 01101111 01100001 01110100.txt");
            ken10000011010 = TEMPDELETE.delete();
        }
        catch(FileNotFoundException e){
            System.out.println("User not found!");
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void insertToTimeline(String directory,String fileName,String followedUser){
        //Setting paths for the profile file of the user followed, and the timeline file of the user following
        String path = this.returnPath(fileName,directory);
        String pathFollowed = this.returnPath(followedUser,"profiles");
        //Turning the files into arraylists for ease of operation
        BufferedReader reader;
        ArrayList<String> timelineArray = new ArrayList<>(0);
        ArrayList<String> profileArray = new ArrayList<>(0);
        try{
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while(line!=null){
                timelineArray.add(line);
                line = reader.readLine();
            }
            reader = new BufferedReader(new FileReader(pathFollowed));
            line = reader.readLine();
            int count = 0;
            while(line!=null){
                if(count>=2) {
                    //Insertion of the username of the person followed to match the profile.txt lines to the timeline.txt lines
                    //Ex. x.txt has the line 12/03/2020 12:30 Hello! and the timeline equivalent is 12/03/2020 12:30 x Hello!
                    //this for loop does that.
                    String[] temps = line.split(" ");
                    line = "";
                    for (int i = 0; i < temps.length; i++) {
                        if(i==2){
                            if(i!=temps.length-1) {
                                line += followedUser + " " + temps[i] + " ";
                            }
                            else{
                                line += followedUser + " " + temps[i];
                            }
                        }
                        else if(i==temps.length-1){
                            line += temps[i];
                        }
                        else{
                            line += temps[i] + " ";
                        }
                    }
                    profileArray.add(line);
                }
                else{
                    count++;
                }
                line = reader.readLine();
            }
            //Taking each line from the profiles file and comparing to the timeline file to see if there are copies of the same line, TLDR: check for mentions
            for (String value : profileArray) {
                int copy = 0;
                for (String s : timelineArray) {
                    if (s.equals(value)) {
                        copy++;
                    }
                }
                //If there are no copy lines for the selected profiles line, it is inserted via checking the timestamps of the timeline files
                if (copy == 0) {
                    int index = 0;
                    for (String s : timelineArray) {
                        String timelineTime = s.substring(0, 16);
                        String profileTime = value.substring(0, 16);
                        if (compareTime(profileTime, timelineTime)) {
                            index++;
                        }
                    }
                    timelineArray.add(index, value);
                }
            }
            //Finally rewriting the timeline file with the lines newly inserted from the profiles file
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (String s : timelineArray) {
                writer.write(s);
                writer.write("\n");
                writer.flush();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Timeline not found!");
        } catch (IOException e) {
            System.out.println("INSERT TO TIMELINE");
            e.printStackTrace();
        }
    }


    public void removeFromTimeline(String directory,String fileName,String followedUser){
        //Inverse of remove from timeline
        String path = returnPath(fileName,directory);
        String pathFollowed = returnPath(followedUser,"profiles");
        BufferedReader r;
        BufferedWriter w;
        ArrayList<String> timelineArray = new ArrayList<>(0);
        try {
            r = new BufferedReader(new FileReader(path));
            String line = r.readLine();
            while(line!=null){
                timelineArray.add(line);
                line = r.readLine();
            }
            r.close();
            r = new BufferedReader(new FileReader(pathFollowed));
            line = r.readLine();
            while(line!=null){
                if(line.toLowerCase().contains("bio:") || line.toLowerCase().contains("user name:")){
                    line = r.readLine();
                }
                else {
                    String[] temp1 = line.split(" ");
                    if (line.length() != 0) {
                        temp1[2] = followedUser + " " + temp1[2];
                    }
                    String message = "";
                    for (int i = 0; i < temp1.length; i++) {
                        if (i != temp1.length - 1) {
                            message += temp1[i] + " ";
                        } else {
                            message += temp1[i];
                        }
                    }
                    for (int i = 0; i < timelineArray.size(); i++) {
                        if (timelineArray.get(i).equals(message) && !message.contains("@" + fileName)) {
                            timelineArray.remove(i);
                        }
                    }
                    line = r.readLine();
                }
            }
            w = new BufferedWriter(new FileWriter(path));
            for (int i = 0; i < timelineArray.size(); i++) {
                w.write(timelineArray.get(i) + "\n");
                w.flush();
            }
            w.close();
        } catch (FileNotFoundException e) {
            System.out.println("User not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
