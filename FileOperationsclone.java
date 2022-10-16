import java.io.*;
import java.util.ArrayList;

public class FileOperationsclone implements Serializable {

    String lane;

    public FileOperationsclone(String modifier){
        if(modifier.equals("main")) {
            boolean ken10000011010;
            File FILECREATOR = new File("timelines");
            ken10000011010 = FILECREATOR.mkdir();
            FILECREATOR = new File("profiles");
            ken10000011010 = FILECREATOR.mkdir();
        }

        this.lane = "shade";
    }

    public String returnPath(String fileName, String directoryName){
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

    public boolean compareTime(String insertionTime,String lineTime){ //returns false if time in the file line is bigger
        String[] iTimeSplit = insertionTime.split(" ");
        String[] lTimeSplit = lineTime.split(" ");

        String iTimeYear = iTimeSplit[0].substring(6);
        String lTimeYear = lTimeSplit[0].substring(6);

        if(Integer.parseInt(lTimeYear)>Integer.parseInt(iTimeYear)){
            return false;
        }
        else{
            String iTimeMonth = iTimeSplit[0].substring(3,5);
            String lTimeMonth = lTimeSplit[0].substring(3,5);

            if(Integer.parseInt(lTimeMonth)>Integer.parseInt(iTimeMonth)){
                return false;
            }
            else{
                String iTimeDay = iTimeSplit[0].substring(0,2);
                String lTimeDay = lTimeSplit[0].substring(0,2);

                if(Integer.parseInt(lTimeDay)>Integer.parseInt(iTimeDay)){
                    return false;
                }
                else{
                    String iTimeHour = iTimeSplit[1].substring(0,2);
                    String lTimeHour = lTimeSplit[1].substring(0,2);

                    if(Integer.parseInt(lTimeHour)>Integer.parseInt(iTimeHour)){
                        return false;
                    }
                    else{
                        String iTimeMinute = iTimeSplit[1].substring(3);
                        String lTimeMinute = lTimeSplit[1].substring(3);
                        if(Integer.parseInt(lTimeMinute)>Integer.parseInt(iTimeMinute)){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                }
            }
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
        String path = this.returnPath(fileName,directory);
        int count = 0;

        BufferedReader r;
        try{
            r = new BufferedReader(new FileReader(path));
            String line = r.readLine();
            while(line!=null){
                if(line.contains(message)){
                    String[] temp = line.split(" ");
                    String lineWithoutTime = "";
                    for (int i = 2; i < temp.length; i++) {
                        lineWithoutTime += temp[i] + " ";
                    }
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

    public void changeLine(String directory,String fileName,String message,String newMessage,int count,String mode){
        if(mode.equals("t")){
            fileName += "_timeline";
        }
        String path = this.returnPath(fileName,directory);
        BufferedReader r;
        BufferedWriter w;
        try{
            int tempCount = 0;
            r = new BufferedReader(new FileReader(path));
            w = new BufferedWriter(new FileWriter("temp.txt"));
            String line = r.readLine();
            while(line!=null){
                if(line.contains(message)){
                    String[] temp = line.split(" ");
                    String time = temp[0] + " " + temp[1] + " ";
                    String lineWithoutTime = "";
                    for (int i = 2; i < temp.length; i++) {
                        lineWithoutTime += temp[i] + " ";
                    }
                    if (lineWithoutTime.substring(0,lineWithoutTime.length()-1).equals(message)) {
                        tempCount++;
                    }
                    if (tempCount == count) {
                        String newTemp = time + newMessage;
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

            w = new BufferedWriter(new FileWriter(path));
            r = new BufferedReader(new FileReader("temp.txt"));
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

            boolean ken10000011010;
            File TEMPDELETE = new File("temp.txt");
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
        fileName += "_timeline";

        String path = this.returnPath(fileName,directory);
        String pathFollowed = this.returnPath(followedUser,"profiles");
        BufferedReader followedFile;
        ArrayList<String> followedLineArray = new ArrayList<>(0);
        try{
            followedFile = new BufferedReader(new FileReader(pathFollowed));
            String temp = followedFile.readLine();
            while(temp!=null){
                followedLineArray.add(temp);
                temp = followedFile.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("User to Follow Not Found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader r;
        BufferedWriter w;
        try{
            String message = "04/07/2021 18:30 this is a test";
            r = new BufferedReader(new FileReader(path));
            String[] temp = message.split(" ");
            String insertionTime = temp[0] + " " + temp[1];
            ArrayList<String> lineArray = new ArrayList<>(0);
            String line = r.readLine();
            while (line != null) {
                lineArray.add(line);
                line = r.readLine();
            }
            r.close();
            if(lineArray.size()!=0) {
                for (int u = 0; u < lineArray.size(); u++) { //lineArray is 0
                    temp = lineArray.get(u).split(" ");
                    String lineTime = temp[0] + " " + temp[1];
                    if (compareTime(insertionTime, lineTime)) {
                        if (u + 1 != lineArray.size()) {
                            temp = lineArray.get(u + 1).split(" ");
                            if (u == 0) {
                                temp[1] += "";
                            }
                            lineTime = temp[0] + " " + temp[1];
                            if (!compareTime(insertionTime, lineTime)) {
                                lineArray.add(u + 1, message);
                                u = lineArray.size();
                            }
                        } else {
                            lineArray.add(message);
                            u = lineArray.size();
                        }
                    } else {
                        lineArray.add(u, message);
                        u = lineArray.size();
                    }
                }
            }
            else{
                lineArray.add(message);
            }
            w = new BufferedWriter(new FileWriter(path));
            for (int y = 0; y < lineArray.size(); y++) { //line array is 0
                w.write(lineArray.get(y) + "\n");
                w.flush();
            }
            w.close();
        }
        catch (FileNotFoundException e){
            System.out.println("User not found!");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
