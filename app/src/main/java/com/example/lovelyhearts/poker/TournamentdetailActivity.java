package com.example.lovelyhearts.poker;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class TournamentdetailActivity extends ActionBarActivity {

    DatabaseHelper db_helper = new DatabaseHelper();
    ParseUser mUser = ParseUser.getCurrentUser();

    TextView textMsg;
    RegisteredplayersFragment rs;

    private String tournamentId = "_na";
    private Tournament tournament;

    boolean registered = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentdetail);

        if (getIntent().hasExtra("ID")) {
            tournamentId = getIntent().getExtras().getString("ID");
        }

        System.out.println("TOURNAMENT ID IS: " + tournamentId);
        ParseQuery<Tournament> query = Tournament.getQuery();
        //query.whereEqualTo("ObjectId",tournamentId);

        //need the result before displaying page, so don't run query in background
        try {
            ParseObject t = query.get(tournamentId);
            ((EditText) findViewById(R.id.editLocation)).setText(t.getString("location"));
            ((EditText) findViewById(R.id.editTime)).setText(t.getString("time"));
            ((EditText) findViewById(R.id.editDate)).setText(t.getString("date"));
            ((EditText) findViewById(R.id.editBuyin)).setText(String.valueOf(t.getInt("buyIn")));

            // this would really be total possible minus number of already assigned players
            // but that has not been implemented yet :)
            int availSeats = t.getInt("seats") * t.getInt("tableNo");
            ((EditText) findViewById(R.id.editSeatsAvail)).setText(String.valueOf(availSeats));
        } catch (ParseException e) {
            // tournament not found - handle this
            Toast.makeText(getApplicationContext(),
                    "ERROR: Tournament with ID "+tournamentId+" could not be found.",
                    Toast.LENGTH_LONG).show();
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_tournamentdetail, menu);

        boolean isAdmin = mUser.getBoolean("isAdmin");
        if(isAdmin)
        {
            getMenuInflater().inflate(R.menu.menu_tournamentdetail, menu);
        }

        else {
            textMsg = (TextView) findViewById(R.id.lbl_registerwarning);
            String mess = textMsg.getText().toString();

            if((mess.contains("You are not registered") || mess.contains("")) && registered == false) {
                textMsg.setText("You are not registered");
                getMenuInflater().inflate(R.menu.menu_usertournamentdetail, menu);
            }
            else if(registered)
            {
                getMenuInflater().inflate(R.menu.menu_registertournamentdetails, menu);
                textMsg.setText("You are registered");
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent=null;
        rs = new RegisteredplayersFragment();
        switch(id){
            //player
            case R.id.action_play:
                intent = new Intent(this,PlayActivity.class);
                startActivity(intent);
                break;
           //admin
            case R.id.action_Assigntable:
                intent = new Intent(this,TableActivity.class);
                startActivity(intent);
                break;

            case R.id.action_Register:
                textMsg.setText("You are registered");
                //Need to Do: Update data to DB
                registered = true;
                invalidateOptionsMenu();

            case R.id.action_playtournament:
                intent = new Intent(this,PlayActivity.class);
                intent.putExtra("_showPlay",true);
                intent.putExtra("_tournamentID", tournamentId);
                startActivity(intent);
                break;

//
//
//                 Fragment fm =  getFragmentManager().findFragmentById(R.id.ttl_registeredplayers);
//                 // View listView = fm.getActivity().findViewById(R.id.ttl_registeredplayers);
//                List<User> l = new ArrayList<User>();
//                User t1=new User();
//                t1.setName(username);
//                l.add(t1);



            case R.id.action_unregister:
                textMsg.setText("You are not registered");
                //Need to Do: Update data to DB
                registered = false;
                invalidateOptionsMenu();
                break;

            case R.id.action_edittournament:
                intent = new Intent(this,EditTournament.class);
                String location = ((EditText)findViewById(R.id.editLocation)).getText().toString();
                intent.putExtra("_locationName", location);
                startActivity(intent);
                ShowToast("Changes Saved");
                finish();
                break;

            case R.id.action_deletetournament:
                NavUtils.navigateUpFromSameTask(this);
                break;

            default:
                return super.onOptionsItemSelected(item);


        }
        return true;
    }


    void ShowToast(CharSequence text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void SaveChanges()
    {
        Tournament mTournament = new Tournament();
        mTournament.setLocation(((TextView) findViewById(R.id.editLocation)).getText().toString());
        mTournament.setDate(((TextView) findViewById(R.id.editDate)).getText().toString());
        mTournament.setTime(((TextView) findViewById(R.id.editTime)).getText().toString());
       // mTournament.setBuyIn((TextView) findViewById((R.id.editBuyin)).toString()
        //getText().toString());


    }


}
