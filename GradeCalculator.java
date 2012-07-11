/**
 * GradeCalculator Class for CSC212 :: hw3p2
 *
 * @author      Devon Blandin
 * @contact     dblandin@gmail.com
 * @date        7/3/12
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GradeCalculator extends JFrame
{
    // frame
    private JFrame frame;
    private final int WIDTH;
    private final int HEIGHT;
    private final int NUM_ASSIGNMENTS;

    // arrays for assignment labels and text fields
    private JLabel[] assignmentsLs;
    private JTextField[] assignmentsTFs;

    // result labels and text fields
    private JLabel totalL;
    private JTextField totalTF;
    private JLabel gradeL;
    private JTextField gradeTF;

    // buttons
    private JButton calculateGradeB;
    private JButton clearB;

    // event handler
    private ButtonHandler buttonHandler;
    private KeyPressHandler keyPressHandler;

    // default constructor
    //sets title to "Grade Calculator", numAssignments to 6, WIDTH to 300
    public GradeCalculator()
    {
        // setup frame values
        super("Grade Calculator");
        this.NUM_ASSIGNMENTS = 6;
        this.WIDTH = 300;
        this.HEIGHT = this.NUM_ASSIGNMENTS * 50 + 100;

    }
    // convenience constructor
    // sets 'numAssignments' to numAssignments
    public GradeCalculator(int numAssignments)
    {
        // setup frame values
        super("Grade Calculator");
        this.NUM_ASSIGNMENTS = numAssignments;
        this.WIDTH = 300;
        this.HEIGHT = this.NUM_ASSIGNMENTS * 50 + 100;
    }
    public void setupGui()
    {
        // setup assignment arrays
        this.assignmentsLs = new JLabel[this.NUM_ASSIGNMENTS];
        this.assignmentsTFs = new JTextField[this.NUM_ASSIGNMENTS];

        for (int i = 0; i < this.NUM_ASSIGNMENTS; i++)
        {
            this.assignmentsLs[i] = new JLabel("Homework " + (i + 1) + ": ", SwingConstants.RIGHT);
            this.assignmentsTFs[i] = new JTextField();
        }

        this.calculateGradeB = new JButton("Determine Grade");
        this.clearB = new JButton("Clear");

        this.gradeTF = new JTextField("");
        this.totalTF = new JTextField("0");
        this.gradeTF.setHorizontalAlignment(SwingConstants.CENTER);
        this.gradeTF.setEnabled(false);
        this.totalTF.setHorizontalAlignment(SwingConstants.CENTER);
        this.totalTF.setEnabled(false);

        this.setSize(this.WIDTH, this.HEIGHT);

        this.setLayout(new GridLayout(this.NUM_ASSIGNMENTS + 3, 2));

        for (int i = 0; i < this.NUM_ASSIGNMENTS; i++)
        {
            this.add(this.assignmentsLs[i]);
            this.add(this.assignmentsTFs[i]);
        }

        this.add(new JLabel("Total: ", SwingConstants.RIGHT));
        this.add(this.totalTF);
        this.add(new JLabel("Grade: ", SwingConstants.RIGHT));
        this.add(this.gradeTF);

        this.add(this.calculateGradeB);
        this.add(this.clearB);

        buttonHandler = new ButtonHandler();

        keyPressHandler = new KeyPressHandler();

        calculateGradeB.addActionListener(buttonHandler);
        clearB.addActionListener(buttonHandler);

        for (int i = 0; i < this.NUM_ASSIGNMENTS; i++)
        {
            this.assignmentsTFs[i].addActionListener(keyPressHandler);
        }
    }

    public JLabel[] getAssignmentsLs()
    {
        return this.assignmentsLs;
    }

    public static void main(String[] args)
    {
        GradeCalculator gradeCalculator;
        final int NUM_ASSIGNMENTS;
        String response = JOptionPane.showInputDialog("Number of Assignments:");

        // catch cancel button press
        if (response == null)
            System.exit(0);

        // get assignment number input.
        // default to default constructor (6 assignments)
        if (isInteger(response))
        {
            NUM_ASSIGNMENTS = Integer.parseInt(response);
            gradeCalculator = new GradeCalculator(NUM_ASSIGNMENTS);
        }
        else
            gradeCalculator = new GradeCalculator();

        gradeCalculator.setupGui();
        gradeCalculator.setVisible(true);
        gradeCalculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } // end main

    // handles determine grade and clear button presses
    private class KeyPressHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            determineGrade();
        }
    }

    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // check what button was pushed
            // if clear button was pushed...
            if (e.getSource() == clearB)
            {
                clearInputs();
            }
            // otherwise, if calculate button was pushed...
            else
            {
                determineGrade();
            }

        } //end method actionPerformed
    } //end class ButtonHandler

    private void clearInputs() {
        // clear the assignment and result text fields
        for (int i = 0; i < assignmentsTFs.length; i++)
        {
            assignmentsTFs[i].setText("");
            totalTF.setText("0");
            gradeTF.setText("");
        }
    }

    private void determineGrade ()
    {
        // validate input to between 0 and 100 inclusive
        for (int i = 0; i < assignmentsTFs.length; i++)
        {
            if (assignmentsTFs[i].getText() == null || assignmentsTFs[i].getText().isEmpty() || !isInteger(assignmentsTFs[i].getText()) || Integer.parseInt(assignmentsTFs[i].getText()) < 0)
                assignmentsTFs[i].setText("0");
            else if (Integer.parseInt(assignmentsTFs[i].getText()) > 100)
            {
                assignmentsTFs[i].setText("100");
            }
        }

        // add up assignment scores
        int sum = 0;
        for (int i = 0; i < assignmentsTFs.length; i++)
        {
            sum += Integer.parseInt(assignmentsTFs[i].getText());
        }

        // determine the lowest score
        int lowest = 0;
        for (int i = 0; i < assignmentsTFs.length; i++)
        {
            if (Integer.parseInt(assignmentsTFs[i].getText()) < Integer.parseInt(assignmentsTFs[lowest].getText()))
                lowest = i;
        }

        // remove lowest assignment score from sum and output sum
        sum -= Integer.parseInt(assignmentsTFs[lowest].getText());
        totalTF.setText("" + sum + "/" + ((assignmentsTFs.length - 1) * 100));

        // calculate average
        int average = sum / (assignmentsTFs.length - 1);

        // determine letter grade
        char grade = 'A';
        if (average < 60)
            grade = 'F';
        else if (average < 70)
            grade = 'D';
        else if (average < 80)
            grade = 'C';
        else if (average < 90)
            grade = 'B';

        // output letter grade
        gradeTF.setText("" + grade);
    }

    // returns true if String input is an integer
    public static boolean isInteger( String input )
    {
        try
        {
            Integer.parseInt( input );
            return true;
        }
        catch ( Exception e)
        {
            return false;
        }
    }

} // end GradeCalculator class