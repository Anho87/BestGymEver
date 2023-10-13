public class Costumer {
   private  long personalNumber;
    private String name;
    private int subscriptionDate;
    

    public Costumer(long personalNumber, String name, int subscriptionDate) {
        this.personalNumber = personalNumber;
        this.name = name;
        this.subscriptionDate = subscriptionDate;
    }

    public long getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(long personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(int subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    @Override
    public String toString() {
        return "Costumer{" +
                "personalNumber=" + personalNumber +
                ", name='" + name + '\'' +
                ", subscriptionDate=" + subscriptionDate +
                '}';
    }
}
