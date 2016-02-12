/**
 * Created by userdev on 2/5/2016.
 */
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TriviaServer {

    public static class Question {
        public String question;
        public String answer1;
        public String answer2;
        public String answer3;
        public String answer4;
        public int correctAnswer;

        public Question(String q, String a1, String a2, String a3, String a4, int c) {
            this.question = q;
            this.answer1 = a1;
            this.answer2 = a2;
            this.answer3 = a3;
            this.answer4 = a4;
            this.correctAnswer = c;
        }
    }


    private static Question[] questions = {
            new Question("Capital of France", "Lion", "London", "Paris", "Rome", 2),
            new Question("Next planet after earth", "Saturn", "Mars", "Neptune", "Pluto", 1),
            new Question("Whats not beatles song", "Think", "Imagine", "Help", "And I Love Her", 0),
            new Question("כמה נקודות זכות צריך בשביל תואר מדעי המחשב באוניברסיטה הפתוחה?", "120", "118", "108", "112", 2),
            new Question("כמה מועדים ניתן להיבחן בקורס ללא הרשמה מחדש", "1", "2", "3", "4", 2),
            new Question("כמה זמן לפני סיום המבחן בפתוחה אסור לצאת לשירותים", "שעה", "חצי שעה", "20 דקות", "רבע שעה", 1),
            new Question("מה צריך להביא איתך לבחינה כדי שיהיה מותר להיכנס ולהיבחן", "הזמנה לבחינה", "כרטיס סטודנט והזמנה לבחינה", "תעודת זהות והזמנה לבחינה", "כרטיס סטודנט", 3),
            new Question("כמה חודשים לומדים בסמסטר קיץ בפתוחה", "חודש וחצי", "3 חודשים", "חודשיים", "חודשיים וחצי", 2),
            new Question("מה הציון עובר שצריך לקבל במבחן בפתוחה כדי לסיים קורס בהצלחה", "70", "56", "55", "60", 3),
            new Question("איזה פרטים יש להכניס לשאילתא כדי להיכנס", "תעודת זהות, סיסמה ושם משתמש", "שם משתמש וסיסמה", "מספר סטודנט, שם משתמש וסיסמה", "פייסבוק לוגין", 0),
            new Question("באיזה שעה מתחילה הבחינה בפתוחה", "10:00", "16:00", "16:30", "10:30", 2),
    };

    // start listening on port
    // when new client connects, create a new thread to handle him
    public TriviaServer(int port) throws IOException {
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(port);
        System.out.println("Started listening server on port " + port);
        while(true) {
            final Socket connectionSocket = welcomeSocket.accept();
            System.out.println("New connection accepted");

            Thread one = new Thread() {
                public void run() {
                    handleNewClient(connectionSocket);
                }
            };

            one.start();
        }
    }

    // handle client requests
    // when he ask for new question get one he didn't get yet
    public void handleNewClient(Socket connectionSocket) {

        try {

            Random rand = new Random();
            int randomNum = 0;
            Map<String, String> alreadySentQuestions = new HashMap<String, String>();

            String clientSentence;
            String capitalizedSentence;
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(connectionSocket.getOutputStream(), "UTF-8"));
            while (true) {
                clientSentence = inFromClient.readLine();
                System.out.println("Received command: " + clientSentence);

                if (clientSentence.equals("question")) {

                    if (alreadySentQuestions.size() == questions.length)
                        alreadySentQuestions.clear();

                    boolean succeeded1 = false;
                    while (!succeeded1) {
                        randomNum = rand.nextInt(questions.length);

                        if (!alreadySentQuestions.containsKey(Integer.toString(randomNum))) {
                            succeeded1 = true;
                            alreadySentQuestions.put(Integer.toString(randomNum),Integer.toString(randomNum));
                        }
                    }


                    capitalizedSentence =
                            questions[randomNum].question + "///" +
                            questions[randomNum].answer1 + "///" +
                            questions[randomNum].answer2 + "///" +
                            questions[randomNum].answer3 + "///" +
                            questions[randomNum].answer4 + "///" +
                            questions[randomNum].correctAnswer
                            + '\n';
                    connectionSocket.getOutputStream().write(capitalizedSentence.getBytes("UTF-8"));
                }
                else {
                    capitalizedSentence = clientSentence.toUpperCase() + '\n';
                    outToClient.writeBytes(capitalizedSentence);
                }
            }
        } catch (IOException ioe) {

        }
    }

    public static void main(String[] args ) {
        try {
            new TriviaServer(3333);
        } catch (IOException ioe) {

        }
    }
}
