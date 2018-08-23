package hds.aplications.com.mycp.helpers;

import android.support.v7.app.AppCompatActivity;

import com.graphhopper.util.InstructionList;

import java.util.List;

import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.models.Destination;
import hds.aplications.com.mycp.models.Reservation;
import hds.aplications.com.mycp.models.User;

/**
 * Created by Miguel Gomez Leon on 12/8/15.
 * mgleonsc@gmail.com
 */
public class SAppData {
    private static SAppData ourInstance = new SAppData();

    private User user;
    private InstructionList instructions;
    private AppCompatActivity appCompatActivity;
    public Reservation reservation;
    public Accommodation accommodation;
    public List<Destination> destinations;

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

    public InstructionList getInstructions() {
        return instructions;
    }

    public void setInstructions(InstructionList instructions) {
        this.instructions = instructions;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }
}
