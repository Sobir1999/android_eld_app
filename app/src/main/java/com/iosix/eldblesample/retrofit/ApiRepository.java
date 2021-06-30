package com.iosix.eldblesample.retrofit;

import android.app.Application;
import android.os.AsyncTask;

import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
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
}