package com.example.appsiukas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends Activity {
    Context context;
    TextView wordView;
    TextView lettersView;
    TextView lifesView;
    Button guessButton;
    List<Character> guestLetters = new ArrayList<>();
    String word = "SALIAMONAS";
    String wordCopy;
    Integer lifes;
    Integer initialLifes;
    String difficulty;
    Integer wordLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        context = getApplicationContext();
        difficulty = sharedPreferences.getString("difficulty", "Lengvas");
        wordLength = sharedPreferences.getInt("wordLength", 6);
        lifes = sharedPreferences.getInt("lifes", 5);
        initialLifes = lifes;
        wordView = (TextView) findViewById(R.id.word);
        lettersView = (TextView) findViewById(R.id.letters);
        lifesView = (TextView) findViewById(R.id.lifes);
        guessButton = (Button) findViewById(R.id.guessButton);
        lifesView.setText(lifes.toString());
        openDictionary(getFileIdByDificulty());
        initGuestLetters();
        setWord();
        setGuestLetters();
    }

    private void openDictionary(int fileId) {
        try {
            Resources res = getResources();
            InputStream is = res.openRawResource(fileId);
            byte[] b = new byte[is.available()];
            is.read(b);
            String line = new String(b);
//            FileInputStream fis =  openFileInput(fileName);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader bufferedReader = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String line = bufferedReader.readLine();

            pickWordFromDictionary(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pickWordFromDictionary(String line) {
        List<String> sortedWords = new ArrayList<>();
        String[] words = line.split(",");
        for (String word : words) {
            if (word.length() == this.wordLength){
                sortedWords.add(word);
            }
        }
        if (!sortedWords.isEmpty()) {
            Random random = new Random();
            this.word = sortedWords.get(random.nextInt(sortedWords.size())).toUpperCase();
        }

    }

    public void onGuessClick(View view) {
        String guessLetter =  ((EditText) findViewById(R.id.guestLetter)).getText().toString();
        if (!guessLetter.trim().isEmpty()) {
            Character letter = guessLetter.toUpperCase().toCharArray()[0];
            Boolean found  = false;
            for (Character guestLetter : this.guestLetters){
                if (guestLetter == letter){
                    found = true;
                    break;
                }
            }
            if (found) {
                Toast.makeText(GameActivity.this,
                        "Raidė jau spėta", Toast.LENGTH_LONG).show();
            }
            else {
                this.guestLetters.add(letter);
                setGuestLetters();
                setWord();
                checkGuess(letter);
                checkForEndGame();
            }
        }
    }

    private void setWord() {
        StringBuilder sb = new StringBuilder();
        this.wordCopy = this.word;
        char[] wordsChars = this.wordCopy.toCharArray();
        for (int i = 0; i < wordsChars.length; i++) {
            Character letter = wordsChars[i];
            Boolean found = false;
            for (Character guestLetter : this.guestLetters) {
                if (guestLetter.equals(letter)) {
                    found = true;
                    break;
                }

            }
            if (!found) {
                wordsChars[i] = '_';
            }
            sb.append(wordsChars[i]).append(" ");
        }
        this.wordCopy = sb.toString();
        this.wordCopy = this.wordCopy.substring(0, this.wordCopy.length() - 1);
        wordView.setText(this.wordCopy);
    }

    private void formatWord() {

    }

    private void setGuestLetters() {
        StringBuilder sb = new StringBuilder();
        if (guestLetters != null && !guestLetters.isEmpty()) {
            for (Character letter : guestLetters) {
                sb.append(letter).append(", ");
            }
            String letters = sb.toString();
            this.lettersView.setText(letters.substring(0, letters.length() - 2));
        }
        else {
            this.lettersView.setText("");
        }
    }

    private void initGuestLetters() {
        char firsLetter = word.charAt(0);
        char lastLetter = word.charAt(word.length()-1);
        this.guestLetters.add(firsLetter);
        if (firsLetter != lastLetter) {
            this.guestLetters.add(word.charAt(word.length() - 1));
        }
    }

    private void checkGuess(Character guestLetter) {
        Boolean found = false;
        for (Character letter : word.toCharArray()) {
            if (letter == guestLetter) {
                found = true;
                break;
            }
        }
        if (!found) {
            this.lifes--;
            lifesView.setText(lifes.toString());
        }
    }

    private void checkForEndGame() {
        if (lifes == 0){
            this.guessButton.setEnabled(false);
            Toast.makeText(GameActivity.this,
                    "OOF! Pralaimėjote, eikit knygų paskaityti", Toast.LENGTH_LONG).show();
            wordView.setText(word);
            saveGame();
        }
        else if (!this.wordCopy.contains("_")){
            this.guessButton.setEnabled(false);
            Toast.makeText(GameActivity.this,
                    "Mldc... Laimėjai", Toast.LENGTH_LONG).show();
            saveGame();
        }
    }

    private int getFileIdByDificulty() {
        switch (this.difficulty) {
            case "Lengvas":
                return R.raw.easy_words;
            case "Vidutinis":
                return R.raw.medium_words;
            case "Sunkus":
                return R.raw.hard_words;
            default:
                return R.raw.easy_words;
        }
    }

    private void saveGame() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String lifesString = lifes.toString() + "/" + initialLifes;
        HistoryDatabaseHandler dbhandler = new HistoryDatabaseHandler(this);
        dbhandler.addEntry(new HistoryEntry(word, currentDate, lifesString));
    }
}

