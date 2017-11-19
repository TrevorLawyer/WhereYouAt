package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterUserActivity extends Activity {

    EditText regPassword, regEmail, regPhoneNumber, confirmPass, username, codeEntered;
    Button regRegister, confirmCode;
    String email, phoneNumber, password, passwordCheck, usernameString, valString;
    Boolean emailTest, phoneTest, passwordTest, usernameBool = false;
    //Radio botton
    RadioGroup radioGroup;
    RadioButton text, emailCode;
    static int num;

    //Session for Email
    static Session session;
    //athentication
    private FirebaseAuth auth;

    //firebase database
    DatabaseReference firebaseDatabase;
    //user contact info
    UserClass userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //declare variables
        regEmail = (EditText) findViewById(R.id.EditTextEmail);
        regPhoneNumber = (EditText) findViewById(R.id.EditPhoneNumber);
        regPassword = (EditText) findViewById(R.id.EditTextPassword2);
        confirmPass = (EditText) findViewById(R.id.EditTextConfirmPassword);
        regRegister = (Button) findViewById(R.id.registerButtonRegister);
        username = (EditText) findViewById(R.id.EditTextUsername2);
        radioGroup = (RadioGroup) findViewById(R.id.radioButtonValidation);
        text = (RadioButton) findViewById(R.id.radioButtonText);
        emailCode = (RadioButton) findViewById(R.id.radioButtonEamil);
        codeEntered = (EditText) findViewById(R.id.editTextCodeEntered);
        confirmCode = (Button) findViewById(R.id.registerButtonConfirmCode);

        // athentication intialization
        auth = FirebaseAuth.getInstance();
        // database intialization
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        // contact info
        userInfo = new UserClass();

        confirmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("val String " + valString);
                if ((valString != null)) {

                    //checks if email code matches code typed in
                    if (valString.equals(codeEntered.getText().toString().trim())) {
                        Toast.makeText(RegisterUserActivity.this, "Codes Match", Toast.LENGTH_SHORT).show();
                        // Toast.makeText(RegisterUserActivity.this, "Valid Registration", Toast.LENGTH_SHORT).show();

                        //authenticats user infomation and password
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterUserActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //if successful user added to database
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            userInfo.setEmail(email);
                                            userInfo.setPhoneNumber(phoneNumber);
                                            userInfo.setUsername(usernameString);

                                            //insert into database
                                            firebaseDatabase.child("users").child(user.getUid()).setValue(userInfo);

                                            //task is successful
                                            Toast.makeText(RegisterUserActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // failed
                                            Toast.makeText(RegisterUserActivity.this, "Account With Email Already Exist", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Codes do not Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intialize stings values
                email = regEmail.getText().toString().trim();
                password = regPassword.getText().toString();
                passwordCheck = confirmPass.getText().toString();
                phoneNumber = regPhoneNumber.getText().toString();
                usernameString = username.getText().toString();

                //calls functions
                checkEmail(email);
                checkPhoneNumber(phoneNumber);
                checkUsername(usernameString);
                checkPassword(password);

                //insert into database
//                DatabaseReference conditionRef = firebaseDatabase.child("condition");
//                conditionRef.setValue("cold");
//                //insert into database
//                firebaseDatabase.child("user").child("dd").setValue("hello");


                if (emailTest == true && passwordTest == true && phoneTest == true && usernameBool == true) {
                    //Creates random number for validation
                    num = (int) ((5000 * Math.random()) + (300 * Math.random()) + (20 * Math.random()) + Math.random());
                    //System.out.println(" randome number " + num);

                    //intializes valString
                    valString = Integer.toString(num);
                    // System.out.println(" valString number " + valString);

                    //checks which radio button is checked
                    if (text.isChecked()) {
                        //Toast.makeText(RegisterUserActivity.this, "text process", Toast.LENGTH_SHORT).show();
                        //call function to send text
                        sendMessage(phoneNumber, valString);

                    } else if (emailCode.isChecked()) {

                        //Now send the message
                        //System.out.println("Transport = " + email);

                        //Set properties and their values
                        Properties props = new Properties();
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.socketFactory.port", "465");
                        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.port", "465");

                        //Create a Session object & authenticate uid and pwd
                        session = Session.getDefaultInstance(props, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("2017mobileapps@gmail.com", "17mobileapps");
                            }
                        });

                        // call ASynch method to finish rest of email sending
                        new HttpGetTask().execute(email);

                        //System.out.println("Your email sent successfully....");
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Need to select a Validate Method", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            private void updateUI(FirebaseUser user) {
            }
        });
    }

    private void sendMessage(String x, String num2){

        try {

            //Toast.makeText(RegisterUserActivity.this, "code:  " +num2, Toast.LENGTH_SHORT).show();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(x, null, "Type the following validation code in the app " + num2, null, null);
            Toast.makeText(RegisterUserActivity.this, "Validation Code Sent (Texted)", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(RegisterUserActivity.this, "Error Sending Text", Toast.LENGTH_SHORT).show();
        }
    }
    private class HttpGetTask extends AsyncTask<String, String, Void> {


        @Override
        protected Void doInBackground(String... params) {

            try {
                //Create MimeMessage object & set values
                final Message messageobj = new MimeMessage(RegisterUserActivity.session);
                //who email is from
                messageobj.setFrom(new InternetAddress("2017mobileapps@gmail.com"));
                //who email is going to
                messageobj.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                //subject of email
                messageobj.setSubject("Validation Code");
                //body of email
                messageobj.setText("Type the following validation code in the app " + num);

                //Now send the message
                Transport.send(messageobj);

            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            System.out.println("IN Post Excute");
            Toast.makeText(RegisterUserActivity.this, "Validation Code Sent (Emailed)", Toast.LENGTH_SHORT).show();
        }
    }

    //checks if username is valid
    public void checkUsername(String x) {
        //special characters
        String specialCharacters = " !#$%&'()*+,-./:;<=>?@[]^_`{|}~";

        for (int i = 0; i < x.length(); i++) {

            //test if evey value of username is in special Chars, converts char to string
            if (specialCharacters.contains(Character.toString(x.charAt(i)))) {
                Toast.makeText(RegisterUserActivity.this, "Username Contains Special Chars", Toast.LENGTH_SHORT).show();
                usernameBool = false;
                return;
            }
        }
        if (x.equals("") || x.isEmpty()) {
            Toast.makeText(RegisterUserActivity.this, "Username is Empty", Toast.LENGTH_SHORT).show();
            usernameBool = false;
            return;
        }
        usernameBool = true;
    }

    //checks if email meets criteria
    public String checkEmail(String email) {

        //intialize stings values
        email = regEmail.getText().toString();
        //length is working
        //System.out.println("Email length : " + email.length());
        //checks if email contains @
        emailTest = false;
        if (email.contains("@") && email.contains(".com")) {
            if (email.length() > 6) {
                //Toast.makeText(RegisterUserActivity.this, "Valid Email", Toast.LENGTH_SHORT).show();
                emailTest = true;
            } else
                Toast.makeText(RegisterUserActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(RegisterUserActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        return email;
    }

    //checks if passwords were typed the same
    public String checkPassword(String password) {

        passwordTest = false;

        if (password.equals(passwordCheck)) {

            if (password.length() > 5) {
                passwordTest = true;
                return password;
            } else {
                Toast.makeText(RegisterUserActivity.this, "Min 6 Pass Chars", Toast.LENGTH_SHORT).show();
                return password;
            }

        }
        Toast.makeText(RegisterUserActivity.this, "Password Dont Match", Toast.LENGTH_SHORT).show();
        return password;
    }

    //checks phone number
    public void checkPhoneNumber(String number) {
        if (number.length() != 10 || number.isEmpty() || number.equals("")) {
            phoneTest = false;
            Toast.makeText(RegisterUserActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        phoneTest = true;

    }


//    //checks if user is signed in
//    @Override
//    public void onStart(){
//        super.onStart();
//        //checks if user is singed in (non-nul)
//        FirebaseUser currentUser = auth.getCurrentUser();
//        updateUI(currentUser);
//    }
}
