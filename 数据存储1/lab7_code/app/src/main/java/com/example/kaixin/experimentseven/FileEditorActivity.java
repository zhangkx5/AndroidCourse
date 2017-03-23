package com.example.kaixin.experimentseven;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kaixin on 2016/11/16.
 */

public class FileEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);

        final EditText file_edit = (EditText)findViewById(R.id.file_edit);
        Button btn_save = (Button)findViewById(R.id.save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileOutputStream fileOutputStream = openFileOutput("test", MODE_PRIVATE)) {
                    fileOutputStream.write(file_edit.getText().toString().getBytes());
                    fileOutputStream.close();
                    Toast.makeText(FileEditorActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(FileEditorActivity.this, "Fail to save file", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_load = (Button)findViewById(R.id.load);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileInputStream fileInputStream = openFileInput("test")) {
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);
                    file_edit.setText(new String(contents));
                    fileInputStream.close();
                    Toast.makeText(FileEditorActivity.this, "Load successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(FileEditorActivity.this, "Fail to load file", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_clear = (Button)findViewById(R.id.clear);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file_edit.setText(null);
            }
        });

    }
}
