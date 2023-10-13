import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BestGymEver {


    Path inPath = Paths.get("CostumersDataFile");

    public static void main(String[] args) {
        new BestGymEver();
    }

    public BestGymEver() {
        List<Costumer> costumerList = new ArrayList<>();
        readFromDataFile(costumerList);
        for (Costumer cos:costumerList) {
            System.out.println(cos);
        }
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
        } catch (Exception e) {
            System.out.println("Något gick fel");
            e.printStackTrace();
            System.exit(0);
        }
    }
    public Costumer createCostumer(String firstLine, String secondLine) {
        long personalNumber = 0;
        String name = "";
        int subscriptionDate = 0;
        String[] firstLineParts = firstLine.split(",");
        if (firstLineParts.length == 2) {
            personalNumber = Long.parseLong(firstLineParts[0].trim());
            name = firstLineParts[1].trim();
        }
        subscriptionDate = Integer.parseInt(secondLine.replace("-","").trim());
        Costumer c = new Costumer(personalNumber,name,subscriptionDate);
        return c;
    }
    public boolean getActiveSubscription(Costumer c) {
    }
}
