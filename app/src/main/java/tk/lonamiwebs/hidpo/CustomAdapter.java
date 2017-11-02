package tk.lonamiwebs.hidpo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lonami on 14/12/2014.
 */

public class CustomAdapter extends ArrayAdapter<File>
{
    private final Context context;
    private final ArrayList<File> itemsArrayList;

    Map<Integer, View> views = new HashMap<Integer, View>();

    public CustomAdapter(Context context, ArrayList<File> itemsArrayList)
    {
        super(context, R.layout.browser_item, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public View getView(int position)
    {
        return views.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.browser_item, parent, false);

        // 3. Get the two text view from the rowView
        ImageView icon = (ImageView) rowView.findViewById(R.id.itemIcon);
        TextView name = (TextView) rowView.findViewById(R.id.itemName);
        TextView description = (TextView) rowView.findViewById(R.id.itemDescription);

        // 4. Set the text for textView
        icon.setImageResource(itemsArrayList.get(position).isDirectory() ? R.drawable.folder : R.drawable.file);
        name.setText(itemsArrayList.get(position).getName());
        description.setText(itemsArrayList.get(position).isDirectory() ? "Directory" : "File");

        views.put(position, rowView);

        // 5. return rowView
        return rowView;
    }
}