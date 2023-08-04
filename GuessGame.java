import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GuessGame extends JFrame implements ActionListener {
    private int randomNumber;
    private int attempts = 6;
    private JTextField userGuess;
    private JLabel feedback;
    private JButton submitButton;
    private JLabel imageLabel;
    private JButton playAgainButton;

    public GuessGame() {
        super("Guessing Game");
        setLayout(new FlowLayout());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);

        Random rand = new Random();
        randomNumber = rand.nextInt(100);
        System.out.println("Generated random number: " + randomNumber);

        userGuess = new JTextField(10);
        userGuess.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        userGuess.setFont(new Font("Arial", Font.PLAIN, 20));

        feedback = new JLabel("Enter your guess (0-99): ");
        submitButton = new JButton("Submit");
        playAgainButton = new JButton("Play Again");

        submitButton.setPreferredSize(new Dimension(120, 40));
        playAgainButton.setPreferredSize(new Dimension(120, 40));

        submitButton.setBackground(Color.GREEN);
        playAgainButton.setBackground(Color.YELLOW);

        submitButton.addActionListener(this);
        playAgainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        setLocationRelativeTo(null);
        try {
            File lockedChestFile = new File("C:\\Users\\rajve\\OneDrive\\Desktop\\locked_chest.png");
            File openedChestFile = new File("C:\\Users\\rajve\\OneDrive\\Desktop\\opened_chest.png");

            BufferedImage lockedChestImage = ImageIO.read(lockedChestFile);
            BufferedImage openedChestImage = ImageIO.read(openedChestFile);

            if (lockedChestImage == null || openedChestImage == null) {
                System.err.println("Error: Failed to load image files");
            } else {
                ImageIcon lockedChestIcon = new ImageIcon(lockedChestImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH));
                ImageIcon openedChestIcon = new ImageIcon(openedChestImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH));

                imageLabel = new JLabel(lockedChestIcon);

                add(imageLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(feedback);
        add(userGuess);
        add(submitButton);
        add(playAgainButton);
        playAgainButton.setVisible(false);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String input = userGuess.getText();
            if (isValidInput(input)) {
                int guess = Integer.parseInt(input);
                attempts--;
                if (guess == randomNumber) {
                    feedback.setText("Congratulations! You won!");
                    submitButton.setVisible(false);
                    userGuess.setVisible(false);
                    try {
                        File openedChestFile = new File("C:\\Users\\rajve\\OneDrive\\Desktop\\opened_chest.png");
                        BufferedImage openedChestImage = ImageIO.read(openedChestFile);

                        if (openedChestImage == null) {
                            System.err.println("Error: Failed to load opened chest image file");
                        } else {
                            ImageIcon openedChestIcon = new ImageIcon(openedChestImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH));
                            imageLabel.setIcon(openedChestIcon);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    playAgainButton.setVisible(true);
                } else if (attempts == 0) {
                    feedback.setText("You lost! The number was " + randomNumber);
                    submitButton.setEnabled(false);
                    playAgainButton.setVisible(true);
                } else if (guess < randomNumber) {
                    feedback.setText("Too low! " + attempts + " attempts left.");
                } else {
                    feedback.setText("Too high! " + attempts + " attempts left.");
                }
                userGuess.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number (0-99).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isValidInput(String input) {
        if (input.isEmpty()) {
            return false;
        }
        try {
            int number = Integer.parseInt(input);
            return number >= 0 && number <= 99;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void resetGame() {
        Random rand = new Random();
        randomNumber = rand.nextInt(100);
        attempts = 6;
        feedback.setText("Enter your guess (0-99): ");
        submitButton.setVisible(true);
        userGuess.setVisible(true);
        playAgainButton.setVisible(false);
        submitButton.setEnabled(true);
        userGuess.setText("");
        try {
            File lockedChestFile = new File("C:\\Users\\rajve\\OneDrive\\Desktop\\locked_chest.png");
            BufferedImage lockedChestImage = ImageIO.read(lockedChestFile);

            if (lockedChestImage == null) {
                System.err.println("Error: Failed to load locked chest image file");
            } else {
                ImageIcon lockedChestIcon = new ImageIcon(lockedChestImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH));
                imageLabel.setIcon(lockedChestIcon);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GuessGame();
    }
}
