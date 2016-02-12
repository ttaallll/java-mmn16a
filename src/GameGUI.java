import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;


public class GameGUI extends JFrame implements ActionListener {

    private static String exitString = "Exit";
    private static String colorString = "Color";
    private static String undoString = "Undo";
    private static String clearString = "Clear";

    private static String filledString = "Filled";
    private static String emptyString = "Empty";

    private GameNetwork gameNetwork;

    private JLabel questionLabel;
    private JButton answerButton1;
    private JButton answerButton2;
    private JButton answerButton3;
    private JButton answerButton4;


    private JLabel timeLeftLabel;
    private JLabel pointsLabel;
    private JLabel serverLabel;
    private JLabel questionsCountLabel;

    private String correctAnswer;
    private int countCorrectAnswers;
    private int numOfQuestions;

    private Timer timer;
    private int timeForQuestion;

    private Timer updateLabelTime;
    private int currentLeftTime;


    public GameGUI(GameNetwork gn, int timeForQuestion) {
        super("Canvas");

        this.timeForQuestion = timeForQuestion;
        this.gameNetwork = gn;
        timer = new Timer();




        createToolbarButtons();
        createQuestionLabel();
        createCreditLabel();

        setSize(800, 800);
        setVisible(true);

        serverLabel.setText(gn.getServer());
        getNextQuestion();

        updateLabelTime = new Timer();
        updateLabelTime.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentLeftTime--;
                timeLeftLabel.setText(Integer.toString(currentLeftTime) + " secdons left");
            }
        }, 0, 1000);
    }

    private void createCreditLabel() {
        JLabel labelMe = new JLabel("Tal Pais (:", SwingConstants.CENTER);
        labelMe.setSize(100, 30);
        setLabelFontSize(labelMe);
        add(labelMe, BorderLayout.SOUTH);
    }

    private void createQuestionLabel() {
        questionLabel = new JLabel("Question", SwingConstants.CENTER);
        questionLabel.setFont(questionLabel.getFont ().deriveFont (40.0f));
        add(questionLabel, BorderLayout.NORTH);
    }

    // handle the case of getting new question from the server
    // and show it to the gui
    private void getNextQuestion() {
        String[] questionVals = gameNetwork.getNextQuestion();

//        questionLabel.setText(questionVals[0]);

        questionLabel.setText(String.format("<html><div style=\"width:%dpx;\">%s</div><html>",
                (int)(this.getContentPane().getSize().width *(2.0f/3.0f)),
                questionVals[0]));

        answerButton1.setText(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 50, questionVals[1]));
        answerButton2.setText(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 50, questionVals[2]));
        answerButton3.setText(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 50, questionVals[3]));
        answerButton4.setText(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 50, questionVals[4]));
        correctAnswer = questionVals[5];

        // try to cancel the timer of the question timeout, and reschedule it with the initial seconds (10 is default)
        try {
            timer.cancel();
        } catch (Exception e) { }
        currentLeftTime = timeForQuestion;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkCorrectAnswer("");
            }
        }, 1000 * timeForQuestion);
    }

    private void setLabelFontSize(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = label.getWidth();

        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = label.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
    }

    // when one of the buttons is pressed
    public void actionPerformed(ActionEvent e) {

        String btnPressed = e.getActionCommand();

        JButton srcBtn = (JButton) e.getSource();

        String property = (String)srcBtn.getClientProperty("id");

        if (btnPressed.equals(exitString)) {
            System.exit(0);
        }
        else if (
                property.equals("Answer1") ||
                property.equals("Answer2") ||
                property.equals("Answer3") ||
                property.equals("Answer4")
                ) {
            checkCorrectAnswer(property);
        }

    }

    // check if we pressed the correct answer, is so add one to counter
    // show next question
    private void checkCorrectAnswer(String answer) {
        numOfQuestions++;

        boolean isCorrect = false;

        if (answer.equals("Answer1") && correctAnswer.equals("0")) {
            countCorrectAnswers++;
            isCorrect = true;
        }
        else if (answer.equals("Answer2") && correctAnswer.equals("1")) {
            countCorrectAnswers++;
            isCorrect = true;
        }
        else if (answer.equals("Answer3") && correctAnswer.equals("2")) {
            countCorrectAnswers++;
            isCorrect = true;
        }
        else if (answer.equals("Answer4") && correctAnswer.equals("3")) {
            countCorrectAnswers++;
            isCorrect = true;
        }

        pointsLabel.setText(Integer.toString(countCorrectAnswers) + " correct answers");
        if (isCorrect)
            pointsLabel.setForeground(Color.green);
        else
            pointsLabel.setForeground(Color.red);

        questionsCountLabel.setText(Integer.toString(numOfQuestions) + " questions");

        getNextQuestion();
    }

    private void createToolbarButtons() {

        JPanel toolbar1 = new JPanel();
        toolbar1.setLayout(new GridLayout(2,4));

        answerButton1 = addButtonTo("Answer1", toolbar1);
        answerButton2 = addButtonTo("Answer2", toolbar1);
        answerButton3 = addButtonTo("Answer3", toolbar1);
        answerButton4 = addButtonTo("Answer4", toolbar1);

        timeLeftLabel = addLabelTo("TimeLeft", toolbar1);
        pointsLabel = addLabelTo("Points", toolbar1);
        questionsCountLabel = addLabelTo("Questions Count", toolbar1);
        serverLabel = addLabelTo("Server", toolbar1);


        add(toolbar1, BorderLayout.CENTER);
    }

    private JButton addButtonTo(String name, JPanel to) {
        JButton btn = new JButton(name);
        btn.addActionListener(this);
        btn.putClientProperty("id", name);
        btn.setFont(new Font("Arial", Font.PLAIN, 40));
        to.add(btn);

        return btn;
    }

    private JLabel addLabelTo( String name, JPanel to) {
        JLabel lbl = new JLabel(name);
        to.add(lbl);
        return lbl;
    }
}
