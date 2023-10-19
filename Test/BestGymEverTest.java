import org.junit.Test;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BestGymEverTest {
    BestGymEver gym = new BestGymEver();
    protected LocalDate localDate = LocalDate.now();
    protected LocalDate localDateThreeHundredDaysAgo = localDate.minusDays(300);
    protected DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
    protected int dateThreeHundredDaysAgo = Integer.parseInt(dtf.format(localDateThreeHundredDaysAgo));
    Costumer c1 = new Costumer("198505251234", "Andreas Holmberg", 20180307);
    Costumer c2 = new Costumer("200101011234", "Aaron Holmberg", dateThreeHundredDaysAgo);
    String outPutTestFile = "Test/CustomerTrainedDataFileTest";
    protected Path inPath = Paths.get("Test/CostumersDataFileTest");

    @Test
    public void inputTest() {
        String stringOne = "7703021234, Alhambra Aromes";
        String stringTwo = "2023-07-01";
        Costumer c3 = gym.createCostumer(stringOne, stringTwo);
        assertEquals("7703021234", c3.getSocialSecurityNumber());
        assertEquals("Alhambra Aromes", c3.getName());
        assertEquals(20230701, c3.getSubscriptionDate());
    }

    @Test
    public void dataTest() {
        List<Costumer> costumerList = new ArrayList<>();
        costumerList = gym.readFromDataFile(costumerList,inPath);
        assertEquals(4, costumerList.size());
        assertEquals("7703021234", costumerList.get(0).getSocialSecurityNumber());
        assertEquals("Alhambra Aromes", costumerList.get(0).getName());
        assertEquals(20230701, costumerList.get(0).getSubscriptionDate());
        assertEquals("7608021234", costumerList.get(3).getSocialSecurityNumber());
        assertEquals("Diamanda Djedi", costumerList.get(3).getName());
        assertEquals(20230130, costumerList.get(3).getSubscriptionDate());
    }
    @Test
    public void checkIfMember(){
        gym.test = true;
        List<Costumer> costumerList = new ArrayList<>();
        costumerList = gym.readFromDataFile(costumerList,inPath);
        assertEquals("Not a member!", gym.checkIfCustomerExists("Andreas Holmberg", costumerList,outPutTestFile));
        assertEquals("Member but no subscription!",gym.checkIfCustomerExists(costumerList.get(1).getName(),costumerList,outPutTestFile));
        
        costumerList.get(1).setSubscriptionDate(dateThreeHundredDaysAgo);
        assertEquals("Paying member!", gym.checkIfCustomerExists(costumerList.get(1).getName(),costumerList,outPutTestFile));
        
    }
    @Test
    public void checkSubscription() {
        assertFalse(gym.getActiveSubscription(c1));
        assertTrue(gym.getActiveSubscription(c2));
    }
    @Test
    public void outputTest() {
        gym.test = true;
        gym.writeToCostumerTrainedDataFile(c2,outPutTestFile);
        try (BufferedReader in = new BufferedReader(new FileReader(outPutTestFile))) {
            String line;
            while ((line = in.readLine()) != null) {
                assertEquals("Aaron Holmberg 200101011234",line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("hitta inte filen" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas");
        }
    }
    
}