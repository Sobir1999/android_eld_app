package com.iosix.eldblesample.viewModel;

import static com.iosix.eldblesample.enums.Day.changeFormat;
import static com.iosix.eldblesample.enums.Day.getCalculatedDate;
import static com.iosix.eldblesample.enums.Day.getCurrentMillis;
import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.Day.stringToDate;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.adapters.InspectionLogAdapter;
import com.iosix.eldblesample.adapters.LogRecyclerViewAdapter;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.customViews.MyListView;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.interfaces.FetchStatusCallback;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.roomDatabase.repository.StatusTableRepositories;

import org.threeten.bp.ZonedDateTime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StatusDaoViewModel extends AndroidViewModel implements Serializable {
    private final StatusTableRepositories repository;
    private final CompositeDisposable disposables;
    Status status = new Status(EnumsConstants.STATUS_OFF,null,null,"");
    int drivingTime;

    public StatusDaoViewModel(Application application) {
        super(application);
        repository = new StatusTableRepositories(application);
        disposables = new CompositeDisposable();
    }

    public void getAllDrivingStatusTime(String day, TextView textView){
        Disposable disposable = repository.getCurrDateStatuses(Arrays.asList(EnumsConstants.statuses),changeFormat(day))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    getLastActonStatus(day, new FetchStatusCallback() {
                        @Override
                        public void onSuccess(Status status, List<Status> statusess) {
                            drivingTime = 0;
                            if (status.getStatus().equals(EnumsConstants.STATUS_DR) && statuses.size() > 0) {
                                int currTime = 0;
                                int nextTime = statuses.get(0).getTime().equals(status.getTime()) ? currTime : stringToDate(statuses.get(0).getTime()).toLocalTime().toSecondOfDay();
                                drivingTime += nextTime - currTime;
                                for (int i = 0; i < statuses.size() - 1; i++) {
                                    if (statuses.get(i).getStatus().equals(EnumsConstants.STATUS_DR)) {
                                        int currStatusTime = stringToDate(statuses.get(i).getTime()).toLocalTime().toSecondOfDay();
                                        int nextStatusTime = stringToDate(statuses.get(i+1).getTime()).toLocalTime().toSecondOfDay();
                                        drivingTime += nextStatusTime - currStatusTime;
                                    }
                                }
                                if (statuses.get(statuses.size() - 1).getStatus().equals(EnumsConstants.STATUS_DR)) {
                                    int lastTime = stringToDate(statuses.get(statuses.size() - 1).getTime()).toLocalTime().toSecondOfDay();
                                    int endTime = day.equals(getDayFormat(ZonedDateTime.now())) ? getCurrentMillis() : 86400;
                                    drivingTime += endTime - lastTime;
                                }
                            } else if (status.getStatus().equals(EnumsConstants.STATUS_DR)) {
                                int startTime = 0;
                                int endTime = day.equals(getDayFormat(ZonedDateTime.now())) ? getCurrentMillis() : 86400;
                                drivingTime += endTime - startTime;
                            }else if (statuses.size() > 0){
                                for (int i = 0; i < statuses.size() - 1; i++) {
                                    if (statuses.get(i).getStatus().equals(EnumsConstants.STATUS_DR)) {
                                        int currStatusTime = stringToDate(statuses.get(i).getTime()).toLocalTime().toSecondOfDay();
                                        int nextStatusTime = stringToDate(statuses.get(i+1).getTime()).toLocalTime().toSecondOfDay();
                                        drivingTime += nextStatusTime - currStatusTime;
                                    }
                                }
                                if (statuses.get(statuses.size() - 1).getStatus().equals(EnumsConstants.STATUS_DR)) {
                                    int lastTime = stringToDate(statuses.get(statuses.size() - 1).getTime()).toLocalTime().toSecondOfDay();
                                    int endTime = day.equals(getDayFormat(ZonedDateTime.now())) ? getCurrentMillis() : 86400;
                                    drivingTime += endTime - lastTime;
                                }
                            }
                            textView.setText(intToTime(drivingTime));
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
                }, throwable -> {
                });
        disposables.add(disposable);
    }

    public void getLastActonStatus(String s, FetchStatusCallback callback) {
        Disposable disposable = repository.getmActionStatus(Arrays.asList(EnumsConstants.statuses))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    for (int i = 0; i < 9; i++) {
                        if (getDayFormat(getCalculatedDate(i)).equals(s)) {
                            for (int j = statuses.size() - 1; j >= 0; j--) {
                                if (stringToDate(statuses.get(j).getTime()).toLocalDate().compareTo(getCalculatedDate(i).toLocalDate()) < 0) {// status date is before s date
                                    status = statuses.get(j);
                                    break;
                                }
                            }
                        }
                    }
                    callback.onSuccess(status,statuses);
                }, callback::onError);
        disposables.add(disposable);
    }


    public void getCurDateStatus(CustomLiveRulerChart customLiveRulerChart, CustomStableRulerChart customRulerChart, String day) {
        Disposable disposable = repository.getCurrDateStatuses(Arrays.asList(EnumsConstants.statuses),changeFormat(day))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(arrayList -> {
                    getLastActonStatus(day, new FetchStatusCallback() {
                        @Override
                        public void onSuccess(Status status, List<Status> statuses) {
                            if (day.equals(getDayFormat(ZonedDateTime.now()))){
                                customLiveRulerChart.setArrayList(arrayList, status);
                            }else {
                                customRulerChart.setArrayList(arrayList,status);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void logRecyclerList(Context context,Status status, String day, MyListView listView){
        Disposable disposable = repository.getmAllStatus(changeFormat(day))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    LogRecyclerViewAdapter logRecyclerViewAdapter = new LogRecyclerViewAdapter(context,statuses,status,day);
                    listView.setAdapter(logRecyclerViewAdapter);
                }, throwable -> {
                });
        disposables.add(disposable);
    }

    public void inspectionLogList(Context context, String day, MyListView listView){
        Disposable disposable = repository.getmAllStatus(changeFormat(day))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> getLastActonStatus(day, new FetchStatusCallback() {
                    @Override
                    public void onSuccess(Status status, List<Status> statusess) {
                        InspectionLogAdapter inspectionLogAdapter = new InspectionLogAdapter(context,statuses,status,day);
                        listView.setAdapter(inspectionLogAdapter);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }), throwable -> {
                });
        disposables.add(disposable);
    }

    public void insertStatus(Status statusEntity) {
        repository.insertStatus(statusEntity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
        super.onCleared();
    }
}
