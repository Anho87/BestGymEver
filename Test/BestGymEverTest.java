import org.junit.Test;

import java.io.*;
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
    public void checkSubscription() {
        assertFalse(gym.getActiveSubscription(c1));
        assertTrue(gym.getActiveSubscription(c2));
    }

    @Test
    public void outputTest() {
        gym.writeToCostumerTrainedDataFile(c2,outPutTestFile);
    }
}