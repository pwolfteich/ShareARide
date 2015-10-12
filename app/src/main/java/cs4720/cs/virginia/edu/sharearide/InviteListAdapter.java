package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by McNulty-PC on 10/11/2015.
 */
public class InviteListAdapter extends BaseAdapter {
    ArrayList<String> data;
    ArrayList<String> invited;
    Context c;
    public InviteListAdapter (Context context, ArrayList<String> datas, ArrayList<String> invites)
    {
        this.data = datas;
        this.invited = invites;
        c=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //View v = convertView;
        String ev = getItem(position);

        if (convertView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(c);
            convertView = li.inflate(R.layout.friend_row, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox2);

        if (ev != null) {
            TextView name = (TextView) convertView.findViewById(R.id.textView15);
            name.setText(ev);
            checkBox.setChecked(invited.contains(ev));
        }
        checkBox.setTag(ev);
        return convertView;
    }
    public void updateData(ArrayList<String> newlist, ArrayList<String> invites) {
        data.clear();
        data.addAll(newlist);
        invited.clear();
        invited.addAll(invites);
        this.notifyDataSetChanged();
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
