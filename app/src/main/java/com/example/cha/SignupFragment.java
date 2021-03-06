package com.example.cha;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

public class SignupFragment extends Fragment {

    TextInputEditText username, email, department, password, cPassword;
    String uName, eMail, dept, Pass, cPass;
    String[] sData, sField;
    Button onSignup,onAlready;
    LoginCallBack loginCallBack;
    ProgressBar progressBar;

    public void setLoginCallBack(LoginCallBack loginCallBack)
    {
        this.loginCallBack = loginCallBack;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        loginCallBack.setTitle("signUp");
        username = v.findViewById(R.id.input_un);
        email = v.findViewById(R.id.input_email);
        department = v.findViewById(R.id.input_dept);
        password = v.findViewById(R.id.input_pass);
        cPassword = v.findViewById(R.id.input_cpass);
        onSignup = v.findViewById(R.id.sign_but);
        onAlready = v.findViewById(R.id.already);
        progressBar = v.findViewById(R.id.progress);
        onClickButtons();
        return v;
    }

    public void onClickButtons() {
        onSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress bar
                progressBar.setVisibility(View.VISIBLE);
                if (checkValidations()) {
                    //creating signup data's
                    sData = new String[4];
                    sData[0] = uName;
                    sData[1] = eMail;
                    sData[2] = dept;
                    sData[3] = Pass;

                    sField = new String[4];
                    sField[0] = "username";
                    sField[1] = "email";
                    sField[2] = "department";
                    sField[3] = "password";
                    //setting server address
                    StringBuilder link = new StringBuilder();
                    link.append("http://").append(SplashScreen.ip.getString("ip","none")).append("/hunt/Signup.php");
                    //sending data to Database
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Database database = new Database(String.valueOf(link), sField, sData);
                            if (database.onStart()) {
                                if (database.onComp()) {
                                    String temp = database.getData();
                                    if (temp.equals("Signup Success")) {
                                        Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT).show();
                                        getParentFragmentManager().popBackStack();
                                    } else {
                                        Toast.makeText(getContext(), temp, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        onAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginCallBack!=null)
                {
                    loginCallBack.pop();
                }
            }
        });
    }



    public boolean checkValidations() {
        boolean validation = false;
        uName = String.valueOf(username.getText());
        eMail = String.valueOf(email.getText());
        dept = String.valueOf(department.getText());
        Pass = String.valueOf(password.getText());
        cPass = String.valueOf(cPassword.getText());
        if (uName.isEmpty() || eMail.isEmpty() || dept.isEmpty() || Pass.isEmpty() || cPass.isEmpty()) {
            Toast.makeText(getContext(), "All Fields are required", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else if (uName.length() > 10) {
            Toast.makeText(getContext(), "username Too Long", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else if (!eMail.contains("@nie.ac.in")) {
            Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else if (dept.length() > 5) {
            Toast.makeText(getContext(), "Department too long", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else if (!Pass.equals(cPass)) {
            Toast.makeText(getContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else if (Pass.length() > 10) {
            Toast.makeText(getContext(), "Password too long", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else {
            validation = true;
        }
        return validation;
    }
}