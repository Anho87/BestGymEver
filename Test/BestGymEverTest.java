import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class BestGymEverTest {

    BestGymEver gym = new BestGymEver();

    Costumer c1 = new Costumer("198505251234", "Andreas Holmberg", 20180307);
    Costumer c2 = new Costumer("200101011234L", "Aaron Holmberg", 20230307);

    @Test
    public void inputTest() {
        String stringOne = "7703021234, Alhambra Aromes";
        String stringTwo = "2023-07-01";
        Costumer c3 = gym.createCostumer(stringOne, stringTwo);
        assert (c3.getSocialSecurityNumber().equals("7703021234"));
        assert (c3.getName().equals("Alhambra Aromes"));
        assert (c3.getSubscriptionDate() == 20230701);
    }
    @Test
    public void dataTest() {
        List<Costumer> costumerList = new ArrayList<>();
        gym.readFromDataFile(costumerList);
        assert (costumerList.get(0).getSocialSecurityNumber().equals("7703021234"));
        assert (costumerList.get(0).getName().equals("Alhambra Aromes"));
        assert (costumerList.get(0).getSubscriptionDate() == 20230701);
        assert (costumerList.get(1).getSocialSecurityNumber().equals("8204021234"));
        assert (costumerList.get(1).getName().equals("Bear Belle"));
        assert (costumerList.get(1).getSubscriptionDate() == 20191202);
    }
    @Test
    public void outputTest() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Test/CustomerTrainedDataFileTest", true)))) {
            if (gym.getActiveSubscription(c1)) {
                writer.print("\n"+c1);
            }
            if (gym.getActiveSubscription(c2)) {
                writer.print("\n"+c2);
            }
        } catch (FileNotFoundException e) {
            System.out.println("hitta inte filen" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Filen kunde inte läsas");
        }
    }
}