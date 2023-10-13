public class Costumer {
    private String socialSecurityNumber;
    private String name;
    private int subscriptionDate;


    public Costumer(String personalNumber, String name, int subscriptionDate) {
        this.socialSecurityNumber = personalNumber;
        this.name = name;
        this.subscriptionDate = subscriptionDate;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
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
        return socialSecurityNumber + " " + name + "\n" + subscriptionDate;
    }
}
