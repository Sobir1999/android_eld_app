package com.iosix.eldblesample.retrofit;

import android.os.AsyncTask;

import com.iosix.eldblesample.models.Student;

public class ApiRepository {
    private APIInterface apiInterface;

    public ApiRepository() {
    }

    private static class createUser extends AsyncTask<Student, Void, Void> {
        private APIInterface anInterface;

        public createUser(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(Student... students) {
            anInterface.createUser(students[0]);
            return null;
        }
    }
}
