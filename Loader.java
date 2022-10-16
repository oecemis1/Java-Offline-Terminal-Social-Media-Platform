import java.io.*;
import java.util.ArrayList;

public class Loader {

    /*
        ----------------------------
        Project has:
        	directories:
        		timelines
        		profiles
        	operation mode
        ----------------------------
        Main has:
        	userlist(users)
        ----------------------------
        User has:
        	followers(ArrayList<String> followers
        	String username(?)
        	String bio(?)
        	all important User objects are tied to the Main class
        ----------------------------
        Message has:
        	nothing imporant while loading
        	all the messager and message operations
        	are handled and used by the User class
        ----------------------------
        FileOperations has:
            nothing important
            basically an interface
        ----------------------------
    */

    public FileOperations fileInterface;
    public User userInterface;
    public Message messageInterface;

    public Loader(){
        fileInterface = new FileOperations("main");
        userInterface = new User();
        messageInterface = new Message();
    }

    public String returnBinaryPath(String fileName,String directoryName,char mode){
        //Creates a string that combines the directory path and and file name to get the file's path inside the directory
        File temp = new File(directoryName);

        if(mode == 'f') {
            String path = temp.getAbsolutePath();
            char slash = 92;
            path = path + slash + fileName + ".bin";
            return path;
        }
        else if(mode == 'd'){
            String path = temp.getAbsolutePath();
            char slash = 92;
            path = path + slash + fileName;
            return path;
        }
        return "";
    }

    public void createSaveFile(String Directory){
        boolean ken10000011010;
        File saveDestination = new File(Directory);
        ken10000011010 = saveDestination.mkdir();
        File FILECREATOR = new File(Directory,"timelines");
        ken10000011010 = FILECREATOR.mkdir();
        FILECREATOR = new File(Directory,"profiles");
        ken10000011010 = FILECREATOR.mkdir();
        FILECREATOR = new File(Directory,"users");
        ken10000011010 = FILECREATOR.mkdir();
    }

    public void copyProfilesAndTimeline(String directory){
        String timelinePath = returnBinaryPath("timelines",directory,'d');
        String profilesPath = returnBinaryPath("profiles",directory,'d');
        ObjectOutputStream writer;
        ObjectInputStream reader = null;
        BufferedReader originReader;
        File origin = new File("timelines");
        String[] files = origin.list();
        for (int i = 0; i < files.length; i++) {
            String originTimelinePath = fileInterface.returnPath(files[i].substring(0,files[i].length()-4),"timelines");
            String copytimelinePath = returnBinaryPath(files[i].substring(0,files[i].length()-4),timelinePath,'f');
            try {
                originReader = new BufferedReader(new FileReader(originTimelinePath));
                writer = new ObjectOutputStream(new FileOutputStream(copytimelinePath));
                String line = originReader.readLine();
                while(line!=null){
                    writer.writeUTF(line);
                    line = originReader.readLine();
                }
                writer.close();
                originReader.close();

                /*reader = new ObjectInputStream(new FileInputStream(copytimelinePath));
                System.out.println("timeline");
                while (true){
                    System.out.println(reader.readUTF());
                }*/

            } catch (FileNotFoundException e) {
                System.out.println("User not found!");
            } catch (EOFException e) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        origin = new File("profiles");
        files = origin.list();
        for (int i = 0; i < files.length; i++) {
            String originProfilePath = fileInterface.returnPath(files[i].substring(0,files[i].length()-4),"profiles");
            String copyprofilePath = returnBinaryPath(files[i].substring(0,files[i].length()-4),profilesPath,'f');
            try{
                writer = new ObjectOutputStream(new FileOutputStream(copyprofilePath));
                originReader = new BufferedReader(new FileReader(originProfilePath));
                String line = originReader.readLine();
                while (line!=null){
                    writer.writeUTF(line);
                    line = originReader.readLine();
                }
                writer.close();
                originReader.close();

                /*reader = new ObjectInputStream(new FileInputStream(copyprofilePath));
                System.out.println("profile");
                while (true){
                System.out.println(reader.readUTF());
                }*/
            }
            catch (FileNotFoundException e) {
                System.out.println("User not found!");
            }
            catch (EOFException e) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDataToFolder(String directory,User user){
        String path = returnBinaryPath("users",directory,'d');
        path = returnBinaryPath(user.getUsername(), path,'f');

        ObjectOutputStream writer;
        ObjectInputStream reader = null;
        try {
            writer = new ObjectOutputStream(new FileOutputStream(path));
            writer.writeUTF("USERNAME " + user.getUsername());
            writer.writeUTF("BIO " + user.getBio());
            writer.writeUTF("FOLLOWERS");
            for (int i = 0; i < user.followers.size(); i++) {
                writer.writeUTF(user.followers.get(i).getUsername());
            }
            writer.close();
            reader = new ObjectInputStream(new FileInputStream(path));
            String line = reader.readUTF();
            while(true){
                //System.out.println(line);
                line = reader.readUTF();
            }
        } catch (FileNotFoundException e) {
            System.out.println("User not found!");
        }
        catch (EOFException e){
            try{
                reader.close();
            }
            catch (IOException e1){
                e.printStackTrace();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void Save(String input,ArrayList<User> users){
        String Directory = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                Directory = input.substring(i + 1);
                break;
            }
        }
        createSaveFile(Directory);
        copyProfilesAndTimeline(Directory);
        for (int i = 0; i < users.size(); i++) {
            addDataToFolder(Directory,users.get(i));
        }
        System.out.println("INSTANCE SAVED!");
    }

    private void copyTo(String originPath,String targetDirectory){
        File origin = new File(originPath);
        String[] files = origin.list();
        for (int i = 0; i < files.length; i++) {
            BufferedWriter copier = null;
            ObjectInputStream reader = null;
            String originpath = returnBinaryPath(files[i].substring(0,files[i].length()-4), originPath, 'f');
            try {
                reader = new ObjectInputStream(new FileInputStream(originpath));
                copier = new BufferedWriter(new FileWriter(fileInterface.returnPath(files[i].substring(0,files[i].length()-4), targetDirectory)));
                String line = reader.readUTF();
                while (true) {
                    //System.out.println(line);
                    copier.write(line + "\n");
                    copier.flush();
                    line = reader.readUTF();
                }
            } catch (FileNotFoundException e) {
                System.out.println("Load path not found");
            }
            catch (EOFException e){
                try {
                    reader.close();
                }
                catch (IOException e1){
                    e1.printStackTrace();
                }
                finally {
                    try {
                        copier.close();
                    }
                    catch (IOException e2){
                        e2.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

    private void assignTo(String path,ArrayList<User> users){
        ObjectInputStream reader = null;
        String username = "";
        String bio = "";
        ArrayList<String> followerList = new ArrayList<>(0);
        try {
            boolean enable = false;
            reader = new ObjectInputStream(new FileInputStream(path));
            String line = reader.readUTF();
            while (true){
                if(!enable) {
                    if (line.contains("USERNAME")) {
                        String[] temp = line.split(" ");
                        username = temp[1];
                    } else if (line.contains("BIO")) {
                        String[] temp = line.split(" ");
                        for (int i = 1; i < temp.length; i++) {
                            if (i != temp.length - 1) {
                                bio += temp[i] + " ";
                            } else {
                                bio += temp[i];
                            }
                        }
                    } else if (line.contains("FOLLOWERS")) {
                        enable = true;
                    }
                }
                else{
                    followerList.add(line);
                }
                line = reader.readUTF();
            }
        }
        catch (FileNotFoundException e){
            System.out.println("User not found!");
        }
        catch(EOFException e){
            try{
                reader.close();
            }
            catch (IOException e1){
                e1.printStackTrace();
            }
            finally {
                users.add(new User(username,bio));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setFollowers(String path,ArrayList<User> users){
        ObjectInputStream reader = null;
        String username = "";
        ArrayList<String> followerList = new ArrayList<>(0);
        try {
            boolean enable = false;
            reader = new ObjectInputStream(new FileInputStream(path));
            String line = reader.readUTF();
            while (true){
                if(!enable) {
                    if (line.contains("USERNAME")) {
                        String[] temp = line.split(" ");
                        username = temp[1];
                    }
                    else if (line.contains("FOLLOWERS")) {
                        enable = true;
                    }
                }
                else{
                    followerList.add(line);
                }
                line = reader.readUTF();
            }
        }
        catch (FileNotFoundException e){
            System.out.println("User not found!");
        }
        catch(EOFException e){
            try{
                reader.close();
            }
            catch (IOException e1){
                e1.printStackTrace();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        User temp = new User();
        for (int j = 0; j < followerList.size(); j++) {
            for (int k = 0; k < users.size(); k++) {
                if(users.get(k).getUsername().equals(followerList.get(j))){
                    temp.followers.add(users.get(k));
                }
            }
        }
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(username)){
                users.get(i).followers = temp.followers;
            }
        }
    }

    public ArrayList<User> Load(ArrayList<User> users,String directory){
        users = new ArrayList<>(0);
        String timelineOriginpath = returnBinaryPath("timelines",directory,'d');
        String profilesOriginpath = returnBinaryPath("profiles",directory,'d');
        copyTo(timelineOriginpath,"timelines");
        copyTo(profilesOriginpath,"profiles");
        String usersetupOrigin = returnBinaryPath("users",directory,'d');
        File f = new File(usersetupOrigin);
        String[] files = f.list();
        for (int i = 0; i < files.length; i++) {
            String origin = returnBinaryPath(files[i].substring(0,files[i].length()-4),usersetupOrigin,'f');
            assignTo(origin,users);
        }
        for (int i = 0; i < files.length; i++) {
            String origin = returnBinaryPath(files[i].substring(0,files[i].length()-4),usersetupOrigin,'f');
            setFollowers(origin,users);
        }
        return users;
    }

}
