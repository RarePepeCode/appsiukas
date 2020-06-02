package com.example.appsiukas;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class GameParametersActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_parameters);
    }

    public void onStartClick(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        if (isFormValid()) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(GameParametersActivity.this,
                    "Ne visi žaidimo parametrai užpildyti", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean isFormValid() {
        String wordLengthView = ((EditText) findViewById(R.id.wordLength)).getText().toString();
        RadioButton difficultyView = getValueFromRadioButton(R.id.radioDifficulty);
        RadioButton lifesView = getValueFromRadioButton(R.id.radioLifes);
        if (!wordLengthView.trim().isEmpty() && lifesView != null && difficultyView != null) {
            Integer wordLength = Integer.parseInt(wordLengthView);
            Integer lifes = Integer.parseInt(lifesView.getText().toString());
            SharedPreferences.Editor sharedEditor = getSharedPreferences("settings", MODE_PRIVATE).edit();
            sharedEditor.putInt("wordLength", wordLength);
            sharedEditor.putInt("lifes", lifes);
            sharedEditor.putString("difficulty", difficultyView.getText().toString());
            sharedEditor.apply();
            finish();
            return  true;
        }
        return  false;
    }

    private RadioButton getValueFromRadioButton(int id) {
        RadioGroup radioGroup = (RadioGroup) findViewById(id);
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        return  radioButton;
    }

}
