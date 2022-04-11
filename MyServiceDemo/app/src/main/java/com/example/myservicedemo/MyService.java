package com.example.myservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import ServicePackage.IMyDemoInterface;

public class MyService extends Service {
    public MyService() {
    }

    private final ServicePackage.IMyDemoInterface.Stub stubObject = new ServicePackage.IMyDemoInterface.Stub() {
        @Override
        public int calculateData(int firstValue, int nextValue,int operator) throws RemoteException {
            switch (operator) {
            case 1:
                return firstValue + nextValue;
            case 2:
                return firstValue - nextValue;
            case 3:
                return firstValue * nextValue;
            case 4:
                return  firstValue / nextValue;
            default:
                Log.e("Error: ","Invalid Operator");
            return 0;
        }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return stubObject;
    }
}