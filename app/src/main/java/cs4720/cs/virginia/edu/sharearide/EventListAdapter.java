package cs4720.cs.virginia.edu.sharearide;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by McNulty-PC on 9/30/2015.
 */
public class EventListAdapter extends BaseAdapter {
    ArrayList<Event> data;
    Context c;
    public EventListAdapter (Context context, ArrayList<Event> datas)
    {
        this.data = datas;
        c=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //View v = convertView;
        Event ev = getItem(position);

        //if (convertView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(c);
            if(ev.isExpanded()) {
                convertView = li.inflate(R.layout.inflated_event_row, null);
            }
            else {
                convertView = li.inflate(R.layout.compressed_event_row, null);
            }
       // }

        if (ev != null) {
            TextView name = (TextView) convertView.findViewById(R.id.editText2);
            if (name != null) {
                name.setText(ev.getName());
            }
            if(ev.isExpanded()) {

                TextView date = (TextView) convertView.findViewById(R.id.editText3);
                TextView loc = (TextView) convertView.findViewById(R.id.editText4);
                TextView desc = (TextView) convertView.findViewById(R.id.editText5);

                if (date != null) {
                    date.setText(ev.getDate());
                }

                if (loc != null) {
                    loc.setText(ev.getLocation());
                }
                if (desc != null) {
                    desc.setText(ev.getDescription());
                }
            }
            else {
                //TextView name = (TextView) v.findViewById(R.id.id);
            }
        }

        return convertView;
    }
    public void updateData(ArrayList<Event> newlist) {
        data.clear();
        data.addAll(newlist);
        this.notifyDataSetChanged();
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Event getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
