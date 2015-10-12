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
    Context c;
    public InviteListAdapter (Context context, ArrayList<String> datas)
    {
        this.data = datas;
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

        if (ev != null) {
            TextView name = (TextView) convertView.findViewById(R.id.textView15);
            name.setText(ev);
        }
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox2);
        checkBox.setTag(ev);
        return convertView;
    }
    public void updateData(ArrayList<String> newlist) {
        data.clear();
        data.addAll(newlist);
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
