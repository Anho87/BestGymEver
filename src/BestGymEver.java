import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BestGymEver {

    protected final String outPutFile = "CustomerTrainedDataFile";
    protected LocalDate localDate = LocalDate.now();
    protected Path inPath = Paths.get("CostumersDataFile");
    protected JTextArea textArea = new JTextArea();
    protected List<Costumer> costumerList = new ArrayList<>();
    protected boolean test = false;

    public static void main(String[] args) {
        BestGymEver gym = new BestGymEver();
        gym.mainProgram();
    }

    public void mainProgram() {
        readFromDataFile(costumerList, inPath);
        frame();
    }

    public List<Costumer> readFromDataFile(List<Costumer> costumerList, Path inPath) {
        try (Scanner scanner = new Scanner(inPath)) {
            while (scanner.hasNext()) {
                String firstLine;
                String secondLine;
                firstLine = scanner.nextLine();
                if (scanner.hasNext()) {
                    secondLine = scanner.nextLine();
                    Costumer c = createCostumer(firstLine, secondLine);
                    costumerList.add(c);

                }
            }
            return costumerList;
        } catch (NoSuchFileException e) {
            System.out.println("Filen kunde inte hittas");
            e.printStackTrace();
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.println("Blev fel när filen lästes in");
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Något gick fel");
            e.printStackTrace();
            System.exit(0);
        }
        return costumerList;
    }

    public Costumer createCostumer(String firstLine, String secondLine) {
        Costumer c = new Costumer();
        String socialSecurityNumber = "";
        String name = "";
        int subscriptionDate;
        String[] firstLineParts = firstLine.split(",");
        if (firstLineParts.length == 2) {
            socialSecurityNumber = firstLineParts[0].trim();
            name = firstLineParts[1].trim();
        }
        try {
            subscriptionDate = Integer.parseInt(secondLine.replace("-", "").trim());
            c = new Costumer(socialSecurityNumber, name, subscriptionDate);
            return c;
        } catch (NumberFormatException e) {
            System.out.println("Blev fel när filen lästes in");
            e.printStackTrace();
            System.exit(0);
        }
        return c;
    }

    public String checkIfCustomerExists(String input, List<Costumer> costumerList, String outPutFile) {
        for (Costumer c : costumerList) {
            if (c.getName().equalsIgnoreCase(input) || c.getSocialSecurityNumber().equalsIgnoreCase(input)) {
                if (getActiveSubscription(c)) {
                    if(!test){
                        writeToCostumerTrainedDataFile(c, outPutFile);
                        setTextArea(1);
                        return null;
                    }else{
                        return "Paying member!";
                    }
                } else {
                    setTextArea(2);
                    return "Member but no subscription!";
                }
            }
        }
        setTextArea(3);
        return "Not a member!";
    }

    
    public void setTextArea(int access) {
        Font customFont = new Font("Arial", Font.BOLD, 44);
        textArea.setFont(customFont);
        switch (access) {
            case 1 -> {
                textArea.setForeground(Color.GREEN);
                textArea.append("ACCESS GRANTED\n" + "betalande kund");
            }
            case 2 -> {
                textArea.setForeground(Color.RED);
                textArea.append("ACCESS DENIED\n" + "ej betalande kund");
            }
            case 3 -> {
                textArea.setForeground(Color.RED);
                textArea.append("ACCESS DENIED\n" + "obehörig person");
            }
        }
    }

    public boolean getActiveSubscription(Costumer c) {
        LocalDate localDateOneYearAgo = localDate.minusYears(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        int dateOneYearAgo = Integer.parseInt(dtf.format(localDateOneYearAgo));
        return c.getSubscriptionDate() > dateOneYearAgo;
    }

    public void writeToCostumerTrainedDataFile(Costumer c, String outPutFile) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outPutFile, true)))) {
            if(!test){
                writer.print("\n" + c.getName() + " " + c.getSocialSecurityNumber() + " " + localDate);
            }else{
                writer.print("\n" + c.getName() + " " + c.getSocialSecurityNumber());
            }
        } catch (FileNotFoundException e) {
            System.out.println("hitta inte filen" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas");
        }
    }

    public void printTrainedDataFileToWindow() {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(outPutFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("hitta inte filen" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas");
        }
        JFrame frame = new JFrame("Kunder som tränat");
        frame.setSize(290, 400);
        frame.setResizable(false);

        JTextArea textArea = new JTextArea();
        textArea.setBorder(new LineBorder(Color.BLACK));
        textArea.append(stringBuilder.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void frame() {
        JFrame frame = new JFrame("Best Gym Ever");
        frame.setSize(430, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField textField = new JTextField(15);
        JButton button1 = new JButton("Sök medlem");
        JButton button2 = new JButton("Kunder som tränat");
        textArea.setBackground(Color.BLACK);
        textArea.setEditable(false);

        topPanel.add(textField);
        topPanel.add(button1);
        topPanel.add(button2);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(textArea);

        button1.addActionListener(e -> {
            textArea.setText("");
            checkIfCustomerExists(textField.getText().trim(), costumerList, outPutFile);
        });

        button2.addActionListener(e -> printTrainedDataFileToWindow());
        frame.setVisible(true);
    }
}