package hds.aplications.com.mycp.map.routing;

import android.content.Context;

import com.graphhopper.util.Helper;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.RoundaboutInstruction;

import java.text.DecimalFormat;

import hds.aplications.com.mycp.R;


public class InstructionInfo {
    private Context context;
    private Instruction instruction;
    private String text;
    private int ico;
    private String distance;

    public InstructionInfo(Instruction instruction, Context context) {
        this.instruction = instruction;
        this.context = context;
        this.processData();
    }

    public void processData(){
        int sign = instruction.getSign();
        String street = " " + instruction.getName();
        this.generateDistance();

        switch (sign){
            case Instruction.LEAVE_ROUNDABOUT:
                text = context.getString(R.string.leave_roundabout);
                ico = R.mipmap.leave_roundabout;
                break;
            case Instruction.TURN_SHARP_LEFT:
                text = context.getString(R.string.sharp_left) + street;
                ico = R.mipmap.sharp_left;
                break;
            case Instruction.TURN_LEFT:
                text = context.getString(R.string.left) + street;
                ico = R.mipmap.left;
                break;
            case Instruction.TURN_SLIGHT_LEFT:
                text = context.getString(R.string.slight_left) + street;
                ico = R.mipmap.slight_left;
                break;
            case Instruction.CONTINUE_ON_STREET:
                text = Helper.isEmpty(street) ? (context.getString(R.string.continuee)) : (context.getString(R.string.continuee_on) + street);
                ico = R.mipmap.continuee;
                break;
            case Instruction.TURN_SLIGHT_RIGHT:
                text = context.getString(R.string.slight_right) + street;
                ico = R.mipmap.slight_right;
                break;
            case Instruction.TURN_RIGHT:
                text = context.getString(R.string.right) + street;
                ico = R.mipmap.right;
                break;
            case Instruction.TURN_SHARP_RIGHT:
                text = context.getString(R.string.sharp_right) + street;
                ico = R.mipmap.sharp_right;
                break;
            case Instruction.FINISH:
                text = context.getString(R.string.finish);
                ico = R.mipmap.destination;
                break;
            case Instruction.REACHED_VIA:
                text = context.getString(R.string.reached_via) + street;
                ico = R.mipmap.continuee;
                break;
            case Instruction.USE_ROUNDABOUT:
                RoundaboutInstruction roundaboutInstruction = ((RoundaboutInstruction)instruction);
                String extraInfo = "";
                if (roundaboutInstruction.getExitNumber() != 0){
                    extraInfo = context.getString(R.string.take_exit) + " " + String.valueOf(roundaboutInstruction.getExitNumber()) + " " + context.getString(R.string.in) + street;
                }

                text = context.getString(R.string.roundabout) + " " + extraInfo;
                ico = R.mipmap.roundabout;
                break;
            default:
                text = context.getString(R.string.unknown_instruction);
                ico = R.mipmap.continuee;
        }
    }

    public void generateDistance(){
        double distance = instruction.getDistance();
        this.distance =  stringDistance(distance);
    }

    public static String stringDistance(double distance){
        DecimalFormat df = new DecimalFormat("#.#");
        String dis;

        String unit;
        if(distance >= 1000){
            unit = "km";
            dis = df.format(distance/1000);
        }
        else {
            unit = "m";
            dis = df.format(distance);
        }

        return   dis + " " + unit;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIco() {
        return ico;
    }

    public void setIco(int ico) {
        this.ico = ico;
    }

    public String getDistance() {
        return distance;
    }

}
