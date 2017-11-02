package tk.lonamiwebs.hidpo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.animate_in, R.anim.animate_out);
    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        public int getCount()
        {
            return thumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null)
            {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(64, 64));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setPadding(8, 8, 8, 8);
            } else
            {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(thumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] thumbIds = {
                R.drawable.lock, R.drawable.lock_blue, R.drawable.lock_sea, R.drawable.lock_purple,
                R.drawable.lock_green, R.drawable.lock_orange, R.drawable.lock_pink, R.drawable.lock_red
        };
    }
}
