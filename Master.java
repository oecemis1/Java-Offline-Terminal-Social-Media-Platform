import java.util.ArrayList;

public class Master{
    public void messageForAllUsers(ArrayList<User> users,String message){
        String[] temp = message.split(" ");
        for (int i = 0; i < users.size(); i++) {
            message = "";
            for (int j = 0; j < temp.length; j++) {
                if(j!=0){
                    message += temp[j] + " ";
                }
                else if(j== temp.length-1){
                    message += temp[j];
                }
                else{
                    message +=  "Share " + users.get(i).getUsername() + " ";
                }
            }
            users.get(i).Share(message,users);
        }
    }

}
