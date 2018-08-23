package hds.aplications.com.mycp.helpers;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.User;


public class SAppData {
    private static SAppData ourInstance = new SAppData();

    private User user;
    private Accommodation accommodation;

    public static SAppData getInstance() {
        return ourInstance;
    }

    private SAppData() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
}
