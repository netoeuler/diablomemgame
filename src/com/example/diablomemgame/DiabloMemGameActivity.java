package com.example.diablomemgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DiabloMemGameActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	public void onClick(View v) {
		Intent i = null;
		
		switch(v.getId()){
			case R.id.new_game_button: i = new Intent(this, NewGame.class); break;			
			case R.id.about_button: i = new Intent(this, About.class); break;
			case R.id.exit_button: finish(); break;
		}		
		
		if (i != null)
			startActivity(i);
	}
}