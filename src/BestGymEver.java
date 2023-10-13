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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Skriv in namn eller personnummer: ");
        String input = scanner.nextLine();
        checkIfCustomerExists(costumerList, input);
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
        }catch(NumberFormatException e){
            System.out.println("Blev fel när filen lästes in");
            e.printStackTrace();
            System.exit(0);
        }catch(Exception e) {
            System.out.println("Något gick fel");
            e.printStackTrace();
            System.exit(0);
        }
    }
    public Costumer createCostumer(String firstLine, String secondLine) {
        String socialSecurityNumber = "";
        String name = "";
        int subscriptionDate = 0;
        String[] firstLineParts = firstLine.split(",");
        if (firstLineParts.length == 2) {
            socialSecurityNumber = firstLineParts[0].trim();
            name = firstLineParts[1].trim();
        }
        subscriptionDate = Integer.parseInt(secondLine.replace("-","").trim());
        Costumer c = new Costumer(socialSecurityNumber,name,subscriptionDate);
        return c;
    }
    public void checkIfCustomerExists(List<Costumer> costumerList, String input){
        boolean found = false;
        for (Costumer c:costumerList) {
            if(c.getName().equalsIgnoreCase(input)||c.getSocialSecurityNumber().equalsIgnoreCase(input)){
                writeToCostumerTrainedDataFile(c);
                found = true;
                break;
            }
        }
        if(!found){
            System.out.println("Finns ingen medlem med detta namn eller personnummer, denna person får ej vara här!");
        }
    }
    public boolean getActiveSubscription(Costumer c) {
        LocalDate localDateOneYearAgo = localDate.minusYears(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMdd");
        int dateOneYearAgo = Integer.parseInt(dtf.format(localDateOneYearAgo));
        return c.getSubscriptionDate() > dateOneYearAgo;
    }
    public void writeToCostumerTrainedDataFile(Costumer c){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outPutFile, true)))) {
            if(getActiveSubscription(c)){
                writer.print("\n" + c.getName() + " " + c.getSocialSecurityNumber() + " " + localDate);
                System.out.println("Denna kund är en aktiv medlem!");
            }else{
                System.out.println("Den här kunden är inte en aktiv medlem! Hen måste betala först!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("hitta inte filen" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas");
        }
    }
}
