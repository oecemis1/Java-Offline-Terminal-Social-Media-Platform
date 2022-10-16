import java.util.ArrayList;

public class Message{

    private String messager;
    private ArrayList<String> taggedInMessage = new ArrayList<>(0);
    private String message;

    private final FileOperations fileOperator;

    public Message(){
        fileOperator = new FileOperations("message");
    }

    public void setMessager(String messager){
        this.messager = messager;
    }

    public String getMessager(){
        return this.messager;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    private void setTaggedInMessage(ArrayList<User> users){
        taggedInMessage = new ArrayList<>(0);
        String[] str = message.split("@");
        for (int i = 0; i < str.length; i++) {
            String[] temp = str[i].split(" ");
            str[i] = temp[0];
        }
        String []temp = new String[str.length-1];
        for (int j = 0; j < str.length; j++) {
            if(j!=str.length-1) {
                temp[j] = str[j + 1];
            }
        }
        str = temp;
        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < users.size(); j++) {
                if(str[i].equals(users.get(j).getUsername())){
                    taggedInMessage.add(str[i]);
                }
            }
        }

    }

    public String[] getTaggedInMessage(ArrayList<User> users){
        taggedInMessage = new ArrayList<>(0);
        String[] str = message.split("@");
        for (int i = 0; i < str.length; i++) {
            String[] temp = str[i].split(" ");
            str[i] = temp[0];
        }
        String []temp = new String[str.length-1];
        for (int j = 0; j < str.length; j++) {
            if(j!=str.length-1) {
                temp[j] = str[j + 1];
            }
        }
        str = temp;
        for (int i = 0; i < str.length; i++) {
            for (int j = 0; j < users.size(); j++) {
                if(str[i].equals(users.get(j).getUsername())){
                    taggedInMessage.add(str[i]);
                }
            }
        }
        return taggedInMessage.toArray(new String[0]);
    }

    public void updateTimeLineofAt(String[] inputs,ArrayList<User> users,String user){
        setTaggedInMessage(users);
        String[] Modified = new String[2];
        Modified[0] = inputs[0];
        Modified[1] = user + " " + inputs[1];
        for (int i = 0; i < taggedInMessage.size(); i++) {
            fileOperator.appendToFile(Modified, "timelines", taggedInMessage.get(i));
        }
    }

    public void updateTimelineOfFollowers(String[] inputs,ArrayList<User> users,String user){
        setTaggedInMessage(users);
        String[] Modified = new String[2];
        Modified[0] = inputs[0];
        Modified[1] = user + " " + inputs[1];
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(user)){
                for (int j = 0; j < users.get(i).followers.size(); j++) {
                    if(!taggedInMessage.contains(users.get(i).followers.get(j).getUsername())) {
                        fileOperator.appendToFile(Modified, "timelines", users.get(i).followers.get(j).getUsername());
                    }
                }
            }
        }
    }
}
