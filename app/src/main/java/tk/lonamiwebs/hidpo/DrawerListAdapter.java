package tk.lonamiwebs.hidpo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lonami on 28/05/2015.
 */
public class DrawerListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<NavItem> navItems;


    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        this.context = context;
        this.navItems = navItems;
    }

    @Override
    public int getCount() {
        return navItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        } else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.description);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText(navItems.get(position).title);
        subtitleView.setText(navItems.get(position).description);
        iconView.setImageResource(navItems.get(position).icon);

        return view;
    }



    // Um, MultiSelect?
    ArrayList<Boolean> selectedItemsIds;

    public void toggleSelection(int position)
    {
        selectView(position, !selectedItemsIds.get(position));
    }

    public void removeSelection()
    {
        selectedItemsIds = new ArrayList<Boolean>();
    }

    public void selectView(int position, boolean value)
    {
        if (value)
            selectedItemsIds.add(position, value);
        else
            selectedItemsIds.remove(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedItemsIds.size();
    }

    public ArrayList<Boolean> getSelectedIds() {
        return selectedItemsIds;
    }
}
