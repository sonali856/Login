package com.example.sonaligujarathi.login;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends Activity implements AdapterView.OnItemSelectedListener
{
    String collegeName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Spinner college;
    private EditText phoneNo;
    String mPhonenumber;
    private Button buttonRegister;
    ArrayAdapter<CharSequence> adapter;
    private static final String REGISTER_URL = "http://192.168.1.104:8080/cyklo/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        college = (Spinner) findViewById(R.id.spinCollege);
        editTextUsername = (EditText) findViewById(R.id.etUserName);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        editTextEmail = (EditText) findViewById(R.id.etEmail);
        phoneNo = (EditText) findViewById(R.id.etNumber);
        buttonRegister = (Button) findViewById(R.id.btnSave);


        college.setOnItemSelectedListener(this);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.college_names, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        college.setAdapter(adapter);



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == buttonRegister) {
                    registerUser();
                }
            }
        });
    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        collegeName = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



    private void registerUser() {

        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String phone = phoneNo.getText().toString();
        register(username,password,collegeName,email,phone);
    }

    private void register(String username, String password,String college,String email,String phone) {
        String urlSuffix = "?&username="+username+"&password="+password+"&college="+college+"&email_id="+email+"&phone_id="+phone;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            String TAG_SUCCESS="success";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Integer result=jsonObject.getInt(TAG_SUCCESS);
                    if (result == 1)
                        Toast.makeText(getApplicationContext(), " Registered", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Not Registered Try Again ", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }
            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferedReader.readLine();
                    Toast.makeText(getApplicationContext(), "hiii "+result, Toast.LENGTH_SHORT).show();
                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
}
