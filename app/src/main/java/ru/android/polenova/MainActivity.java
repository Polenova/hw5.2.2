package ru.android.polenova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText editLogin;
    private EditText editPassword;
    private Button buttonOK;
    private Button buttonRegistration;
    private CheckBox checkBoxSaveFile;
    private String loginFromFile = null;
    private String passwordFromFile = null;
    private BufferedReader bufferedReader = null;
    private SharedPreferences sharedPreferences;
    private String keyShared = "save_external";
    private String fileInfoInternal = "File_Internal";
    private String fileInfoExternal = "File_External";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE);
        checkBoxSaveFile.setChecked(sharedPreferences.getBoolean(keyShared, false));
        checkBoxSaveFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor myEditor = sharedPreferences.edit();
                myEditor.putBoolean(keyShared, checkBoxSaveFile.isChecked());
                myEditor.apply();
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    private void getInfo() {
        String[] stringInfo;
        if (checkBoxSaveFile.isChecked()) {
            try {
                FileReader fileReader = new FileReader(new File(getExternalFilesDir(null), fileInfoExternal));
                bufferedReader = new BufferedReader(fileReader);
                stringInfo = bufferedReader.readLine().split(";");
                loginFromFile = stringInfo[0].toString();
                passwordFromFile = stringInfo[1].toString();
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(fileInfoInternal)));
                stringInfo = bufferedReader.readLine().split(";");
                loginFromFile = stringInfo[0].toString();
                passwordFromFile = stringInfo[1].toString();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bufferedReader == null) {
            Toast.makeText(this, R.string.empty_info, Toast.LENGTH_SHORT).show();
        } else if (loginFromFile.equals(editLogin.getText().toString()) && passwordFromFile.equals(editPassword.getText().toString())) {
            Toast.makeText(this, R.string.ok_info, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, R.string.error_info, Toast.LENGTH_SHORT).show();
        }
    }


    private void saveInfo() {
        String stringLogin = editLogin.getText().toString();
        String stringPassword = editPassword.getText().toString();
        if (stringLogin.equals("")) {
            Toast.makeText(this, R.string.empty_login, Toast.LENGTH_SHORT).show();
        } else if (stringPassword.equals("")) {
            Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
        } else {
            BufferedWriter bufferedWriter = null;
            if (checkBoxSaveFile.isChecked()) {
                File file = new File(getExternalFilesDir(null), fileInfoExternal);
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(file));
                    bufferedWriter.write(stringLogin + ";" + stringPassword);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, R.string.saved_info_external, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    FileOutputStream fileOutputStreamLogin = openFileOutput(fileInfoInternal, MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriterLogin = new OutputStreamWriter(fileOutputStreamLogin);
                    bufferedWriter = new BufferedWriter(outputStreamWriterLogin);
                    bufferedWriter.write(stringLogin + ";" + stringPassword);
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, R.string.saved_info_internal, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        editLogin = findViewById(R.id.editTextLogin);
        editPassword = findViewById(R.id.editTextPassword);
        buttonOK = findViewById(R.id.buttonOK);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        checkBoxSaveFile = findViewById(R.id.checkBoxSaveFile);
    }
}
