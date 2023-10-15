import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    Path inPath = Paths.get("CostumersDataFile");

    public static void main(String[] args) {
        BestGymEver gym = new BestGymEver();
        gym.mainProgram();
    }

    public void mainProgram() {
        List<Costumer> costumerList = new ArrayList<>();
        readFromDataFile(costumerList);
        frame(costumerList);

    }

    public void readFromDataFile(List<Costumer> costumerList) {
        try (Scanner scanner = new Scanner(inPath)) {
            while (scanner.hasNext()) {
                String firstLine = "";
                String secondLine = "";
                if (scanner.hasNext()) {
                    firstLine = scanner.nextLine();
                    if (scanner.hasNext()) {
                        secondLine = scanner.nextLine();
                        Costumer c = createCostumer(firstLine, secondLine);
                        costumerList.add(c);
                    }
                }
            }
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

    public void checkIfCustomerExists(List<Costumer> costumerList, String input) {
        boolean found = false;
        for (Costumer c : costumerList) {
            if (c.getName().equalsIgnoreCase(input) || c.getSocialSecurityNumber().equalsIgnoreCase(input)) {
                found = true;
                if (getActiveSubscription(c)) {
                    writeToCostumerTrainedDataFile(c);
                    System.out.println("Denna kund är en aktiv medlem!");
                    break;
                } else {
                    System.out.println("Den här kunden är inte en aktiv medlem! Hen måste betala först!");
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("Finns ingen medlem med detta namn eller personnummer, denna person får ej vara här!");
        }
    }

    public boolean getActiveSubscription(Costumer c) {
        LocalDate localDateOneYearAgo = localDate.minusYears(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMdd");
        int dateOneYearAgo = Integer.parseInt(dtf.format(localDateOneYearAgo));
        return c.getSubscriptionDate() > dateOneYearAgo;
    }

    public void writeToCostumerTrainedDataFile(Costumer c) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outPutFile, true)))) {
            writer.print("\n" + c.getName() + " " + c.getSocialSecurityNumber() + " " + localDate);
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
        JFrame frame = new JFrame("Personer som har tränat");
        frame.setSize(250, 400);

        JTextArea textArea = new JTextArea();
        textArea.setBorder(new LineBorder(Color.BLACK));
        textArea.append(stringBuilder.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void frame(List<Costumer> costumerList) {
        JFrame frame = new JFrame("Best Gym Ever");
        frame.setSize(430, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField textField = new JTextField(15);
        JButton button1 = new JButton("Sök medlem");
        JButton button2 = new JButton("Kunder som tränat");
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        topPanel.add(textField);
        topPanel.add(button1);
        topPanel.add(button2);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(textArea);
        
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfCustomerExists(costumerList, textField.getText());
                textArea.append("hej");
            }
        });


        button2.addActionListener(e -> printTrainedDataFileToWindow());
        frame.setVisible(true);
    }
}