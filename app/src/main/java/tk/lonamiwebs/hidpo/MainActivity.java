package tk.lonamiwebs.hidpo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{

    //region Variables

    ListView listview;
    String currentPath;

    ListView drawerList;
    RelativeLayout drawerPanel;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    ActionMode actionMode;

    ArrayList<NavItem> navItems = new ArrayList<NavItem>();

    Toast backtoast;

    //endregion

    //region Create

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set variables
        currentPath = Environment.getExternalStorageDirectory().toString();

        // Prepare listView
        listview = (ListView) findViewById(R.id.listView);


        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
        {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked)
            {
                ((CustomAdapter)listview.getAdapter()).getView(position).setSelected(true);
                //((View)listview.getItemAtPosition(position)).setSelected(checked);
                Log.d("onItemCheckedStateCh...", "An item has been selected at " + position + "!");
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_lock:
                        Log.d("onActionItemClicked...", "LOCK. :D");
                        // foo();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                Log.d("onCreateActionMode", "Inflated, bloooom!");
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode)
            {
                Log.d("onDestroyActionMode", "*dies*");
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                Log.d("onPrepareActionMode", "ARE, YOU, READYY");
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {
                final File item = (File) parent.getItemAtPosition(position);

                if (item.isDirectory())
                    navigate(item.getName());
                else
                    openFile(item);
            }
        });






        // Refresh list
        refreshList();

        // Populate nav bar
        navItems.add(new NavItem(getString(R.string.viewHidden), getString(R.string.viewHiddenDesc), R.drawable.lock));
        navItems.add(new NavItem(getString(R.string.settings), getString(R.string.settingsDesc), R.drawable.settings));
        navItems.add(new NavItem(getString(R.string.about), getString(R.string.aboutDesc), R.drawable.info));

        // Initializing the drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigation Drawer with options
        drawerPanel = (RelativeLayout) findViewById(R.id.drawerPanel);
        drawerList = (ListView) findViewById(R.id.navList);
        drawerList.setAdapter(new DrawerListAdapter(this, navItems));

        // Drawer Item click listeners
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });


        // Enabling hamburger
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Drawer
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    //endregion

    //region List view

    void refreshList()
    {
        listview.setAdapter(new CustomAdapter(this, enumerateFiles()));
    }

    //endregion

    //region Drawer menu

    private void selectItemFromDrawer(int position)
    {
        switch (position)
        {
            case 0: // View hidden files
                break;
            case 1: // Settings
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(R.anim.animate_in, R.anim.animate_out);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);

                break;
            case 2: // About
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(R.anim.animate_in, R.anim.animate_in);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);

                break;
        }
    }

    //endregion

    //region Menu setup

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    //endregion

    //region Physical buttons overrides

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);

        else if (!navigateUp())
            ensureExit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_MENU:

                if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                    drawerLayout.closeDrawer(Gravity.LEFT);
                else
                    drawerLayout.openDrawer(Gravity.LEFT);

                return true;

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    //endregion

    //region MultiSelect mode

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_lock:
                    // shareCurrentItem();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //actionMode = null;
            mode = null;
        }
    };

    //endregion

    //region Navigation

    boolean navigateUp()
    {
        if (currentPath.equals("/"))
            return false;

        String[] tmp = currentPath.split("/");

        currentPath = "/";
        for (int i = 1; i < tmp.length - 2; i++) // We skip first, and want to get until the n-3th last
            currentPath += tmp[i] + "/";
        currentPath += tmp[tmp.length - 2]; // Then we add n-2th last without the slash (one up)

        refreshList();

        return true;
    }

    void navigate(String folder)
    {
        if (folder.equals(".."))
            navigateUp();
        else
        {
            currentPath += "/" + folder;
            refreshList();
        }
    }

    //endregion

    //region File handling

    void openFile(File file)
    {
        try
        {
            Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            myIntent.setDataAndType(Uri.fromFile(file),mimetype);
            startActivity(myIntent);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), R.string.cantOpenFile, Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<File> enumerateFiles()
    {
        ArrayList<File> files = new ArrayList<>();

        try {
            for (File f : new File(currentPath).listFiles())
                files.add(f);
        } catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Cannot access to the selected directory", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }

        return files;
    }

    //endregion

    //region Exit

    void ensureExit()
    {
        if(backtoast != null && backtoast.getView().getWindowToken() != null)
            finish();
        else
        {
            backtoast = Toast.makeText(this, R.string.confirmExit, Toast.LENGTH_SHORT);
            backtoast.show();
        }
    }

    //endregion
}
