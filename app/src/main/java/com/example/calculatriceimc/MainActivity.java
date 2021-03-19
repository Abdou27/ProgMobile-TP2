package com.example.calculatriceimc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.KeyEvent.KEYCODE_5;
import static android.view.KeyEvent.KEYCODE_NUMPAD_DOT;
import static android.view.KeyEvent.KEYCODE_PERIOD;

public class MainActivity extends AppCompatActivity {

    Button calculateBMI;
    Button reset;
    EditText height;
    EditText weight;
    CheckBox valuation;
    RadioGroup group;
    TextView result;

    private String INIT_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INIT_TEXT = getString(R.string.resultTextView2);

        // On récupère toutes les vues et on attribue un listener adapté aux ceux qui en ont besoin

        calculateBMI = (Button)findViewById(R.id.calculateBMIButton);
        calculateBMI.setOnClickListener(calculateBMIListener);

        reset = (Button)findViewById(R.id.resetButton);
        reset.setOnClickListener(resetListener);

        weight = (EditText)findViewById(R.id.weightEditText);
        weight.addTextChangedListener(weightTextWatcher);

        height = (EditText)findViewById(R.id.heightEditText);
        height.addTextChangedListener(heightTextWatcher);

        valuation = (CheckBox)findViewById(R.id.valuationCheckBox);
        valuation.setOnClickListener(valuationListener);

        group = (RadioGroup)findViewById(R.id.unitRadioGroup);

        result = (TextView)findViewById(R.id.resultTextView2);
    }

    private View.OnClickListener calculateBMIListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // On récupère la taille
            String h = height.getText().toString();

            // On récupère le poids
            String w = weight.getText().toString();

            float hValue = Float.valueOf(h);

            // Puis on vérifie que la taille est cohérente
            if(hValue <= 0)
                Toast.makeText(MainActivity.this, getString(R.string.heightMustBePositive), Toast.LENGTH_SHORT).show();
            else {
                float wValue = Float.valueOf(w);
                if(wValue <= 0)
                    Toast.makeText(MainActivity.this, getString(R.string.weightMustBePositive), Toast.LENGTH_SHORT).show();
                else {
                    // Si l'utilisateur a indiqué que la taille était en centimètres
                    // On vérifie que la radio sélectionnée est la deuxième à l'aide de son identifiant
                    if (group.getCheckedRadioButtonId() == R.id.centimetersRadioButton) hValue = hValue / 100;
                    float bmi = wValue / (hValue * hValue);
                    String result_string = getString(R.string.yourBMIIs) + " " + bmi + ".";
                    if(valuation.isChecked()) result_string += interpretBMI(bmi);

                    result.setText(result_string);
                }
            }
        }
    };

    private View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            weight.getText().clear();
            height.getText().clear();
            result.setText(INIT_TEXT);
        }
    };

    private View.OnClickListener valuationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckBox c = (CheckBox) v;
            if( c.isChecked() ) {
                result.setText(INIT_TEXT);
            }
        }
    };

    private TextWatcher weightTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // On remet le texte à sa valeur par défaut
            result.setText(INIT_TEXT);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher heightTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // On remet le texte à sa valeur par défaut
            result.setText(INIT_TEXT);

            // Si le caractère entré est un point décimal, basculer vers mètres
            if (!s.toString().isEmpty()) {
                if(s.toString().charAt(s.length() - 1) == '.') {
                    RadioButton mRadio = findViewById(R.id.metersRadioButton);
                    mRadio.setChecked(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    protected String interpretBMI(float bmi) {
        // breakpoints
        float FAMINE = (float) 16.5;
        float THINNESS = (float) 18.5;
        float NORMAL_BUILD = 25;
        float OVERWEIGHT = 30;
        float MODERATE_OBESITY = 35;
        float SEVERE_OBESITY = 40;

        String return_string = "\n";

        if (bmi < FAMINE) return_string += getString(R.string.famine);
        else if (bmi < THINNESS) return_string += getString(R.string.thinness);
        else if (bmi < NORMAL_BUILD) return_string += getString(R.string.normalBuild);
        else if (bmi < OVERWEIGHT) return_string += getString(R.string.overweight);
        else if (bmi < MODERATE_OBESITY) return_string += getString(R.string.moderateObesity);
        else if (bmi < SEVERE_OBESITY) return_string += getString(R.string.severeObesity);
        else return_string += getString(R.string.morbidObesity);

        return_string += ".";

        return return_string;
    }
}