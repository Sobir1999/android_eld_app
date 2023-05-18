package com.iosix.eldblesample.viewModel;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.VehiclesListAdapter;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleList;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.Point;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.repository.UserRepository;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final CompositeDisposable disposables;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        disposables = new CompositeDisposable();
    }

    public MutableLiveData<List<User>> getMgetDrivers() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        Disposable disposable = repository.getDrivers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users::postValue,
                        throwable -> {
                        });
        disposables.add(disposable);
        return users;
    }

    public void insertUser(User user){
        Completable.fromAction(() -> repository.insertUser(user)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void getMgetLocalDatas(EldJsonViewModel eldJsonViewModel){
        Disposable disposable = repository.getLocalDatas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(liveDataRecords -> {
                if (liveDataRecords.size() > 0){
                    ArrayList<Boolean> engine_state = new ArrayList<>();
                    ArrayList<String> vin = new ArrayList<>();
                    ArrayList<Double> speed = new ArrayList<>();
                    ArrayList<Double> odometer = new ArrayList<>();
                    ArrayList<Double> trip_distance = new ArrayList<>();
                    ArrayList<Double> engine_hours = new ArrayList<>();
                    ArrayList<Double> trip_hours = new ArrayList<>();
                    ArrayList<Double> battery_voltage = new ArrayList<>();
                    ArrayList<String> date = new ArrayList<>();
                    ArrayList<Point> point = new ArrayList<>();
                    ArrayList<Integer> gps_speed = new ArrayList<>();
                    ArrayList<Integer> course = new ArrayList<>();
                    ArrayList<Integer> number_of_satellites = new ArrayList<>();
                    ArrayList<Integer> altitude = new ArrayList<>();
                    ArrayList<Double> dop = new ArrayList<>();
                    ArrayList<Integer> sequence_number = new ArrayList<>();
                    ArrayList<String> firmware_version = new ArrayList<>();

                    for (int i = 0; i < liveDataRecords.size(); i++) {
                        engine_state.add(liveDataRecords.get(i).getEngine_state());
                        vin.add(liveDataRecords.get(i).getVin());
                        speed.add(liveDataRecords.get(i).getSpeed());
                        odometer.add(liveDataRecords.get(i).getOdometer());
                        trip_distance.add(liveDataRecords.get(i).getTrip_distance());
                        engine_hours.add(liveDataRecords.get(i).getEngine_hours());
                        trip_hours.add(liveDataRecords.get(i).getTrip_hours());
                        battery_voltage.add(liveDataRecords.get(i).getBattery_voltage());
                        point.add(liveDataRecords.get(i).getPoint());
                        date.add(liveDataRecords.get(i).getDate());
                        gps_speed.add(liveDataRecords.get(i).getGps_speed());
                        course.add(liveDataRecords.get(i).getCourse());
                        number_of_satellites.add(liveDataRecords.get(i).getNumber_of_satellites());
                        altitude.add(liveDataRecords.get(i).getAltitude());
                        dop.add(liveDataRecords.get(i).getDop());
                        sequence_number.add(liveDataRecords.get(i).getSequence_number());
                        firmware_version.add(liveDataRecords.get(i).getFirmware_version());
                    }

                    if (engine_state.size() > 0) {
                        eldJsonViewModel.sendLocalData(new LiveDataEntitiy(
                                engine_state,
                                vin,
                                speed,
                                odometer,
                                trip_distance,
                                engine_hours,
                                trip_hours,
                                battery_voltage,
                                date,
                                point,
                                gps_speed,
                                course,
                                number_of_satellites,
                                altitude,
                                dop,
                                sequence_number,
                                firmware_version
                        ));
                    }
                }
            },throwable -> {
                Log.d("Adverse",throwable.getMessage());
                Log.d("Adverse", Objects.requireNonNull(throwable.getCause()).getMessage());
            });
        disposables.add(disposable);
    }

    public void insertLocalData(LiveDataRecord liveDataRecord){Completable.fromAction(() -> repository.insertLocalData(liveDataRecord)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe();}

    public void insertVehicle(VehicleList vehiclesEntity) {
        Completable.fromAction(() -> repository.insertVehicle(vehiclesEntity)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void getGetAllVehicles(Context context,String vehicleListSelected,DvirViewModel dvirViewModel) {
        Disposable disposable = repository.getGetAllVehicles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vehicleLists ->{
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.custom_vehicles_dialog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            RecyclerView vehicleListRv = dialog.findViewById(R.id.idVehicleRecyclerview);
                            TextView cancel = dialog.findViewById(R.id.idVehicleCancel);
                            VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(context,vehicleLists);
                            vehicleListRv.setAdapter(vehiclesListAdapter);
                            vehiclesListAdapter.setListener(s -> {
                                if (!vehicleListSelected.equals(s)){
                                    dvirViewModel.getVehicleMutableLiveData().postValue(s);
                                }
                                dialog.dismiss();
                            });
                            cancel.setOnClickListener(view1->{
                                dialog.dismiss();
                            });
                            dialog.show();
                        },
                        throwable -> {
                        });
        disposables.add(disposable);
    }

    public void getGetAllVehiclesGen(ArrayList<VehicleList> vehicleList) {
        Disposable disposable = repository.getGetAllVehicles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vehicleLists ->{
                            if (vehicleList.isEmpty()){
                                vehicleList.addAll(vehicleLists);
                            }
                        },
                        throwable -> {
                        });
        disposables.add(disposable);
    }
//
//    public void insertTrailer(TrailersEntity entity) throws ExecutionException, InterruptedException {
//        repository.insertTrailer(entity);
//    }


    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
        super.onCleared();
    }
}
