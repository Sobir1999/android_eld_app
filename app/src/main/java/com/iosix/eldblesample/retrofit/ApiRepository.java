package com.iosix.eldblesample.retrofit;

import android.app.Application;
import android.os.AsyncTask;

import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

public class ApiRepository {
    private APIInterface apiInterface;

    public ApiRepository(Application application) {
    }

    public void sendEldData(SendExampleModelData model) {
        new sendEldData(apiInterface).execute(model);
    }

    private static class sendEldData extends AsyncTask<SendExampleModelData, Void, Void> {
        private APIInterface anInterface;

        public sendEldData(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(SendExampleModelData... models) {
            anInterface.sendData(models[0]);
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
