package com.iosix.eldblesample.retrofit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.Driver;
import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.Vehicle;
import com.iosix.eldblesample.models.VehicleData;
import com.iosix.eldblesample.models.eld_records.BufferRecord;
import com.iosix.eldblesample.models.eld_records.CachedEngineRecord;
import com.iosix.eldblesample.models.eld_records.CashedMotionRecord;
import com.iosix.eldblesample.models.eld_records.DriverBehaviorRecord;
import com.iosix.eldblesample.models.eld_records.EmissionsRecord;
import com.iosix.eldblesample.models.eld_records.EngineLiveRecord;
import com.iosix.eldblesample.models.eld_records.FuelRecord;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.NewTimeRecord;
import com.iosix.eldblesample.models.eld_records.PowerOnRecord;
import com.iosix.eldblesample.models.eld_records.TransmissionRecord;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.util.List;

public class ApiRepository {
    private APIInterface apiInterface;

    public ApiRepository(Application application) {
    }


    public void sendBufferRecord(BufferRecord record) {
        new sendBufferRecord(apiInterface).execute(record);
    }

    private static class sendBufferRecord extends AsyncTask<BufferRecord, Void, Void> {
        private APIInterface anInterface;

        public sendBufferRecord(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(BufferRecord... records) {
            anInterface.sendBuffer(records[0]);
            return null;
        }
    }


    public void sendEnginecache(CachedEngineRecord record) {
        new sendEnginecache(apiInterface).execute(record);
    }

    private static class sendEnginecache extends AsyncTask<CachedEngineRecord, Void, Void> {
        private APIInterface anInterface;

        public sendEnginecache(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(CachedEngineRecord... records) {
            anInterface.sendEnginecache(records[0]);
            return null;
        }
    }


    public void sendPoweron(PowerOnRecord record) {
        new sendPoweron(apiInterface).execute(record);
    }

    private static class sendPoweron extends AsyncTask<PowerOnRecord, Void, Void> {
        private APIInterface anInterface;

        public sendPoweron(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(PowerOnRecord... records) {
            anInterface.sendPoweron(records[0]);
            return null;
        }
    }

    public void sendNewtime(NewTimeRecord record) {
        new sendNewtime(apiInterface).execute(record);
    }

    private static class sendNewtime extends AsyncTask<NewTimeRecord, Void, Void> {
        private APIInterface anInterface;

        public sendNewtime(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(NewTimeRecord... records) {
            anInterface.sendNewtime(records[0]);
            return null;
        }
    }

    public void sendMotion(CashedMotionRecord record) {
        new sendMotion(apiInterface).execute(record);
    }

    private static class sendMotion extends AsyncTask<CashedMotionRecord, Void, Void> {
        private APIInterface anInterface;

        public sendMotion(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(CashedMotionRecord... records) {
            anInterface.sendMotion(records[0]);
            return null;
        }
    }

    public void sendLive(LiveDataRecord record) {
        new sendLive(apiInterface).execute(record);
    }

    private static class sendLive extends AsyncTask<LiveDataRecord, Void, Void> {
        private APIInterface anInterface;

        public sendLive(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(LiveDataRecord... records) {
            anInterface.sendLive(records[0]);
            return null;
        }
    }

    public void sendDriverbehavior(DriverBehaviorRecord record) {
        new sendDriverbehavior(apiInterface).execute(record);
    }

    private static class sendDriverbehavior extends AsyncTask<DriverBehaviorRecord, Void, Void> {
        private APIInterface anInterface;

        public sendDriverbehavior(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(DriverBehaviorRecord... records) {
            anInterface.sendDriverbehavior(records[0]);
            return null;
        }
    }

    public void sendEmission(EmissionsRecord record) {
        new sendEmission(apiInterface).execute(record);
    }

    private static class sendEmission extends AsyncTask<EmissionsRecord, Void, Void> {
        private APIInterface anInterface;

        public sendEmission(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(EmissionsRecord... records) {
            anInterface.sendEmission(records[0]);
            return null;
        }
    }

    public void sendEnginelive(EngineLiveRecord record) {
        new sendEnginelive(apiInterface).execute(record);
    }

    private static class sendEnginelive extends AsyncTask<EngineLiveRecord, Void, Void> {
        private APIInterface anInterface;

        public sendEnginelive(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(EngineLiveRecord... records) {
            anInterface.sendEnginelive(records[0]);
            return null;
        }
    }

    public void sendFuel(FuelRecord record) {
        new sendFuel(apiInterface).execute(record);
    }

    private static class sendFuel extends AsyncTask<FuelRecord, Void, Void> {
        private APIInterface anInterface;

        public sendFuel(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(FuelRecord... records) {
            anInterface.sendFuel(records[0]);
            return null;
        }
    }

    public void sendTransmission(TransmissionRecord record) {
        new sendTransmission(apiInterface).execute(record);
    }

    private static class sendTransmission extends AsyncTask<TransmissionRecord, Void, Void> {
        private APIInterface anInterface;

        public sendTransmission(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(TransmissionRecord... records) {
            anInterface.sendTransmission(records[0]);
            return null;
        }
    }


    public void createUser(Student student){
        new createUser(apiInterface).execute(student);
    }

    private static class createUser extends AsyncTask<Student,Void,Void>{
        private APIInterface apiInterface;

        public createUser(APIInterface apiInterface){this.apiInterface = apiInterface;}

        @Override
        protected Void doInBackground(Student... students) {
            apiInterface.createUser(students[0]);
            return null;
        }
    }

    public void getUser(){
        new getUser(apiInterface).execute();
    }

    private static class getUser extends AsyncTask<User,Void,Void>{
        private APIInterface apiInterface;

        public getUser(APIInterface apiInterface){this.apiInterface = apiInterface;}

        @Override
        protected Void doInBackground(User... users) {
            apiInterface.getUser();
            return null;
        }
    }

    public void getAllDrivers(){
        new getAllDrivers(apiInterface).execute();
    }

    private static class getAllDrivers extends AsyncTask<Data,Void,Void>{
        private APIInterface apiInterface;

        public getAllDrivers(APIInterface apiInterface){this.apiInterface = apiInterface;}

        @Override
        protected Void doInBackground(Data... users) {
            apiInterface.getAllDrivers();
            return null;
        }
    }

    public void getAllVehicles(){
        new getAllDrivers(apiInterface).execute();
    }

    private static class getAllVehicles extends AsyncTask<VehicleData,Void,Void>{
        private APIInterface apiInterface;

        public getAllVehicles(APIInterface apiInterface){this.apiInterface = apiInterface;}

        @Override
        protected Void doInBackground(VehicleData... users) {
            apiInterface.getAllDrivers();
            return null;
        }
    }

    public void refreshToken(String refreshToken){
        new refreshToken(apiInterface).execute(refreshToken);
    }

    private static class refreshToken extends AsyncTask<String,Void,Void>{
        private APIInterface apiInterface;

        public refreshToken(APIInterface apiInterface){this.apiInterface = apiInterface;}

        @Override
        protected Void doInBackground(String... refreshTokens) {
            apiInterface.refreshToken(refreshTokens[0]);
            return null;
        }
    }

}
