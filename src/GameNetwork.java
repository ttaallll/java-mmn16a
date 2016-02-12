import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by userdev on 2/5/2016.
 */
public class GameNetwork {

    private Socket clientSocket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;

    private String ipStr;

    public GameNetwork(String ip, int port) throws IOException {
        String sentence;
        String modifiedSentence;

        this.ipStr = ip;

        clientSocket = new Socket(ip, port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));

    }

    public String getServer() {
        return this.ipStr;
    }

    // ask for command "question" from the server
    // wait for result and parse it
    // return the question parsed
    public String[] getNextQuestion() {
        try {
            outToServer.writeBytes("question" + '\n');

            String rawData = inFromServer.readLine();
//            System.out.println("raw data " + rawData);
            String rawQuestion = new String(rawData.getBytes("UTF-8"), "UTF-8");
//            System.out.println("raw question " + rawQuestion);

            return rawQuestion.split("///");
        }
        catch (IOException ioe) {

        }

        return new String[]{};
    }
}
