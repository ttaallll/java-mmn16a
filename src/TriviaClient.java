/**
 * Created by userdev on 2/5/2016.
 */

import javax.swing.*;
import java.io.*;
import java.net.*;

public class TriviaClient {
    public TriviaClient() {
        try {

            String[] details = askUserForDetails();

            String ipStr = details[0];
            String timeForQuestion = details[1];


            GameNetwork gn = new GameNetwork(ipStr, 3333);
            GameGUI gg = new GameGUI(gn, Integer.parseInt(timeForQuestion));
            gg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException ioe) {

        }
    }

    private String[] askUserForDetails() {

        String defaultIp = "127.0.0.1";
        String defaultTimeForQuestion = "10";


        try {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("enter server ip, for localhost press enter");
            String ipStr = inFromUser.readLine();
            if (ipStr.equals(""))
                ipStr = defaultIp;

            System.out.println("enter time for question, for 10 seconds press enter");
            String timeForQuestion = inFromUser.readLine();
            if (timeForQuestion.equals(""))
                timeForQuestion = defaultTimeForQuestion;

            return new String[]{ipStr, timeForQuestion};
        }
        catch (IOException ioe) {

        }

        return new String[]{defaultIp, defaultTimeForQuestion};
    }

    public static void main(String[] args) {
       new TriviaClient();
    }
}
