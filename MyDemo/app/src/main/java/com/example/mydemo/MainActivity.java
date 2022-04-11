package com.example.mydemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ServicePackage.IMyDemoInterface;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView textViewDisplayResult;
    private EditText editTextFirstValue, editTextNextValue;
    private Button buttonAdd,buttonSubtract, buttonMultiply, buttonDivide, buttonClearData,buttonBind;
    private IMyDemoInterface aidlObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize UI
        textViewDisplayResult = findViewById(R.id.display_result);
        editTextFirstValue = findViewById(R.id.enter_first_value);
        editTextNextValue = findViewById(R.id.enter_next_value);
        buttonAdd = findViewById(R.id.addition);
        buttonSubtract = findViewById(R.id.subtract);
        buttonDivide = findViewById(R.id.division);
        buttonMultiply = findViewById(R.id.multiply);
        buttonClearData = findViewById(R.id.clear_data);
        buttonBind = findViewById(R.id.bind_service);

        //click listener
        buttonAdd.setOnClickListener(this);
        buttonSubtract.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonClearData.setOnClickListener(this);
        buttonBind.setOnClickListener(this);
        //bindToDemoService();
    }
    private void bindToDemoService() {
        //Intent serviceIntent = new Intent ("connect_to_demo_service");
        Toast.makeText(MainActivity.this, "Service connecting", Toast.LENGTH_SHORT).show();
        Intent serviceIntentExplicit = new Intent();
        serviceIntentExplicit.setComponent(new ComponentName("com.example.myservicedemo", "com.example.myservicedemo.MyService"));


         boolean qaq = bindService(
//                getExplicitIntent(this,serviceIntent),
                serviceIntentExplicit,
                serviceConnectionObject,Context.BIND_AUTO_CREATE);
//        startService(serviceIntentExplicit);
        if (!qaq) {
            throw new AssertionError();
        }

        Toast.makeText(MainActivity.this, serviceConnectionObject.toString(), Toast.LENGTH_SHORT).show();
    }

    ServiceConnection serviceConnectionObject = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(MainActivity.this, "Service connected", Toast.LENGTH_SHORT).show();
            aidlObject = IMyDemoInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    @Override
    public void onClick (View view) {
        switch(view.getId())
        {
            case R.id.addition:
                verifyAndCalculate(1);
                break;
            case R.id.subtract:
                verifyAndCalculate(2);
                break;
            case R.id.multiply:
                verifyAndCalculate(3);
                break;
            case R.id.division:
                verifyAndCalculate(4);
                break;
            case R.id.clear_data:
                editTextNextValue.setText(null);
                editTextFirstValue.setText(null);
                textViewDisplayResult.setText(null);
                break;
            case R.id.bind_service:
                bindToDemoService();
                break;

        }
    }

    private void verifyAndCalculate(int operator) {
        if(isAnyValueMissing())
        {
            Toast.makeText(this,"Please enter the values", Toast.LENGTH_SHORT).show();
        } else{
            int result,firstValue,nextValue;
            firstValue = Integer.parseInt(editTextFirstValue.getText().toString());
            nextValue = Integer.parseInt(editTextNextValue.getText().toString());

            try{
                result = aidlObject.calculateData(firstValue,nextValue,operator);
                textViewDisplayResult.setText(""+result);
            }catch (RemoteException e){
                e.printStackTrace();
            }
            //            result = performCalculation(firstValue,nextValue,operator);
//            textViewDisplayResult.setText(""+result);
        }


    }

//    private int performCalculation(int firstValue, int nextValue, int operator) {
//
//        switch (operator) {
//            case 1:
//                return firstValue + nextValue;
//            case 2:
//                return firstValue - nextValue;
//            case 3:
//                return firstValue * nextValue;
//            case 4:
//                return  firstValue / nextValue;
//            default:
//                Log.e("Error: ","Invalid Operator");
//                return 0;
//
//        }
//    }
    private boolean isAnyValueMissing() {
        return editTextFirstValue.getText().toString().isEmpty() ||
                editTextNextValue.getText().toString().isEmpty();
    }

}