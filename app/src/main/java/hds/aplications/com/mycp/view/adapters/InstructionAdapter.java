package hds.aplications.com.mycp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;

import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.map.routing.InstructionInfo;


public class InstructionAdapter extends BaseAdapter {
    private Context context;
    private InstructionList items;
    private ListView list;

    public InstructionAdapter(Context context, InstructionList items, ListView list){
        this.context= context;
        this.items = items;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.instruction_item, parent, false);
        }

        // Set data into the view.
        TextView instructionText = (TextView) rowView.findViewById(R.id.instruction_text);
        ImageView instructionIco = (ImageView) rowView.findViewById(R.id.instruction_ico);
        TextView instructionDistance = (TextView) rowView.findViewById(R.id.instruction_distance);

        //Resources res = this.context.getResources();

        Instruction item = this.items.get(position);
        InstructionInfo instructionInfo = new InstructionInfo(item, context);

        if (position == 0){
            instructionInfo.setIco(R.mipmap.origin);
        }

        instructionText.setText(instructionInfo.getText());
        instructionIco.setImageResource(instructionInfo.getIco());
        instructionDistance.setText(instructionInfo.getDistance());

        ((TextView) rowView.findViewById(R.id.instruction_number)).setText(String.valueOf(position+1) + ".");

        return rowView;
    }
}
