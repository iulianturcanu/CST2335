package com.example.androidlabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "Toolbar activity";
    private String toastMsg = "This is the initial message";
    Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setActionBar(toolbar);


        Log.i(ACTIVITY_NAME, "in onCreate()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu );
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void optionOne(){
        Toast toast = Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void optionTwo(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View inflator = inflater.inflate(R.layout.dialog_activity, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(inflator);

        EditText e = (EditText) inflator.findViewById(R.id.dialog_text);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                toastMsg = e.getText().toString();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        builder.show();
    }//end optionTwo()


    public void optionThree(View view){
        Snackbar.make(view, "Go Back?", Snackbar.LENGTH_LONG)
                .setAction("Back", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .show();
    }



    public void onOverflow(){
        Toast toast = Toast.makeText(getApplicationContext(), "You clicked on the overflow menu", Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item1:
                optionOne();
                return true;
            case R.id.menu_item2:
                optionTwo();
                return true;
            case R.id.menu_item3:
                optionThree(findViewById(R.id.snackbar));
                return true;
            case R.id.overflow:
                onOverflow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}//END CLASS




