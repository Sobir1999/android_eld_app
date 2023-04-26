package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.iosix.eldblesample.interfaces.GeneralsCallback;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.repository.GeneralRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GeneralViewModel extends AndroidViewModel {

    private final GeneralRepository generalRepository;
    private final CompositeDisposable disposables;

    public GeneralViewModel(@NonNull Application application) {
        super(application);
        generalRepository = new GeneralRepository(application);
        disposables = new CompositeDisposable();
    }

    public void getCurrDayGenerals(String day, GeneralsCallback generalsCallback) {
        Disposable disposable = generalRepository.getCurrDayGeneral(day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalEntities -> {
                    if (generalEntities.size() > 0){
                        generalsCallback.getGenerals(
                                generalEntities.get(generalEntities.size()-1).getVehicle(),
                                generalEntities.get(generalEntities.size()-1).getTrailers(),
                                generalEntities.get(generalEntities.size()-1).getCo_driver_name(),
                                generalEntities.get(generalEntities.size()-1).getShippingDocs(),
                                generalEntities.get(generalEntities.size()-1).getFrom_info(),
                                generalEntities.get(generalEntities.size()-1).getTo_info(),
                                generalEntities.get(generalEntities.size()-1).getNote()
                        );
                    }else {
                        generalsCallback.getGenerals(null,null,null,null,"","","");
                    }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void deleteGeneral(GeneralEntity entity) {
        generalRepository.deleteGeneral(entity);
    }


    public void insertGeneral(GeneralEntity entity) {
        Completable.fromAction(() -> generalRepository.insertGeneral(entity)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
        super.onCleared();
    }
}


