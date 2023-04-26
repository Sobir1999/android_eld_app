package com.iosix.eldblesample.viewModel;

import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.getDayTime1;
import static com.iosix.eldblesample.enums.Day.getDayTimeFromZ;
import static com.iosix.eldblesample.enums.Day.stringToDate;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.AddDvirActivity;
import com.iosix.eldblesample.activity.LGDDActivity;
import com.iosix.eldblesample.adapters.RecyclerViewLastAdapter;
import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.interfaces.AllDaysAdd;
import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.repository.DayDaoRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DayDaoViewModel extends AndroidViewModel {
    private final DayDaoRepository repository;
    private final CompositeDisposable disposables;
    private int index = 0;
    private List<DayEntity> dayEntities = new ArrayList<>();

    public DayDaoViewModel(@NonNull Application application) {
        super(application);
        repository = new DayDaoRepository(application);
        disposables = new CompositeDisposable();
    }

    public void getMgetAllDays(Context context, RecyclerView last_recycler_view, DvirViewModel dvirViewModel,StatusDaoViewModel statusDaoViewModel) {
        Disposable disposable = repository.getGetAllDays()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dayEntities ->{
                        RecyclerViewLastAdapter lastAdapter = new RecyclerViewLastAdapter(context, dayEntities,dvirViewModel,statusDaoViewModel);
                        last_recycler_view.setAdapter(lastAdapter);
                        lastAdapterClicked(lastAdapter,context,dvirViewModel);
                    }, throwable -> {
                });
        disposables.add(disposable);
    }

    public void getAllDays(AllDaysAdd daysAdd) {
        Disposable disposable = repository.getGetAllDays()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(daysAdd::getAllDays, throwable -> {
                });
        disposables.add(disposable);
    }

    public void getDaysforLGDD(String mParams, DvirViewModel dvirViewModel, TextView textFrag){
        Disposable disposable = repository.getGetAllDays()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dayEntities ->{
                    this.dayEntities = dayEntities;
                    dvirViewModel.getCurrentName().postValue(mParams);
                    for (int i = 0; i < dayEntities.size(); i++) {
                        if (dayEntities.get(i).getDay().equals(mParams)){
                           index = i;
                        }
                    }
                    textFrag.setText(getDayTime1(mParams));

                }, throwable -> {
                });
        disposables.add(disposable);
    }

    public void getDaysforInspection(String mParams, DvirViewModel dvirViewModel, TextView textFrag){
        Disposable disposable = repository.getGetAllDays()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dayEntities ->{
                    this.dayEntities = dayEntities;
                    dvirViewModel.getCurrentName().postValue(mParams);
                    for (int i = 0; i < dayEntities.size(); i++) {
                        if (dayEntities.get(i).getDay().equals(mParams)){
                            index = i;
                        }
                    }
                    textFrag.setText(mParams);

                }, throwable -> {
                });
        disposables.add(disposable);
    }

    public void clickNextButton(TextView textFrag,DvirViewModel dvirViewModel){
        if (index > 0) {
            index--;
            textFrag.setText(getDayTime1(dayEntities.get(index).getDay()));
            dvirViewModel.getCurrentName().postValue(dayEntities.get(index).getDay());
        }
    }
    public void clickPrevButton(TextView textFrag,DvirViewModel dvirViewModel){
        if (index < dayEntities.size()-1) {
            index++;
            textFrag.setText(getDayTime1(dayEntities.get(index).getDay()));
            dvirViewModel.getCurrentName().postValue(dayEntities.get(index).getDay());
        }
    }

    public void clickNextButtonForIns(TextView textFrag,DvirViewModel dvirViewModel){
        if (index > 0) {
            index--;
            textFrag.setText(dayEntities.get(index).getDay());
            dvirViewModel.getCurrentName().postValue(dayEntities.get(index).getDay());
        }
    }
    public void clickPrevButtonForIns(TextView textFrag,DvirViewModel dvirViewModel){
        if (index < dayEntities.size()-1) {
            index++;
            textFrag.setText(dayEntities.get(index).getDay());
            dvirViewModel.getCurrentName().postValue(dayEntities.get(index).getDay());
        }
    }

    public void insertDay(DayEntity entity){
        Disposable disposable = repository.insertDay(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                },throwable -> {
                });
        disposables.add(disposable);
    }

//    }
    public void deleteAllDays() {
        Disposable disposable = repository.deleteAllDays()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            for (int i = 0; i < 8; i++) {
                                insertDay(new DayEntity(getDayFormat(Day.getCalculatedDate(i))));
                            }
                        });

        disposables.add(disposable);
    }

    public void deleteLastDay(DayEntity entity) {
        Disposable disposable = repository.deleteDay(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        insertDay(new DayEntity(getDayFormat(Day.getCalculatedDate(0))));
                });

        disposables.add(disposable);
    }

    public void lastAdapterClicked(RecyclerViewLastAdapter lastAdapter,Context context,DvirViewModel dvirViewModel) {
        lastAdapter.setListener(new RecyclerViewLastAdapter.LastDaysRecyclerViewItemClickListener() {
            @Override
            public void onclickItem(String s) {
//                if (daysArray.contains(s)) {
//                    daysArray.remove(s);
//                } else {
//                    daysArray.add(s);
//                }
            }

            @Override
            public void onclickDvir(String s, boolean a) {

                if (a) {
                    Intent intent = new Intent(context, LGDDActivity.class);
                    intent.putExtra("position", 3);
                    intent.putExtra("currDay", s);
                    dvirViewModel.getCurrentName().postValue(s);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, AddDvirActivity.class);
                    intent.putExtra("currDay", s);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onclickSignature(String s) {
                Intent intent = new Intent(context, LGDDActivity.class);
                intent.putExtra("currDay", s);
                intent.putExtra("position", 2);
                dvirViewModel.getCurrentName().postValue(s);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
        super.onCleared();
    }

}
