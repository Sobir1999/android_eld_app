package com.iosix.eldblesample.viewModel;

import static com.iosix.eldblesample.enums.Day.stringToDay;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.DvirlistAdapter;
import com.iosix.eldblesample.adapters.RecyclerViewLastAdapter;
import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.roomDatabase.repository.DvirRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DvirViewModel extends AndroidViewModel {

    private final DvirRepository dvirRepository;
    private final CompositeDisposable disposables;
    private MutableLiveData<String> stringMutableLiveData;
    private MutableLiveData<Integer> vehiclesMutableLiveData;
    private MutableLiveData<Integer> coDriversMutableLiveData;
    private MutableLiveData<String> vehicleMutableLiveData;
    private MutableLiveData<Integer> trailersMutableLiveData;
    private MutableLiveData<Integer> shippingDocsMutableLiveData;
    private MutableLiveData<Dvir> dvirMutableLiveData;

    public DvirViewModel(@NonNull Application application) {
        super(application);
        dvirRepository = new DvirRepository(application);
        disposables = new CompositeDisposable();
    }

    private MutableLiveData<String> currentName;

    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<>();
        }
        return currentName;
    }

    public MutableLiveData<Dvir> getDvirMutableLiveData() {
        if (dvirMutableLiveData == null) {
            dvirMutableLiveData = new MutableLiveData<>();
        }
        return dvirMutableLiveData;
    }

    public MutableLiveData<String> getStringMutableLiveData() {
        if (stringMutableLiveData == null) {
            stringMutableLiveData = new MutableLiveData<>();
        }
        return stringMutableLiveData;
    }

    public MutableLiveData<Integer> getVehiclesMutableLiveData() {
        if (vehiclesMutableLiveData == null) {
            vehiclesMutableLiveData = new MutableLiveData<>();
        }
        return vehiclesMutableLiveData;
    }

    public MutableLiveData<Integer> getCoDriversMutableLiveData() {
        if (coDriversMutableLiveData == null) {
            coDriversMutableLiveData = new MutableLiveData<>();
        }
        return coDriversMutableLiveData;
    }

    public MutableLiveData<String> getVehicleMutableLiveData() {
        if (vehiclesMutableLiveData == null) {
            vehiclesMutableLiveData = new MutableLiveData<>();
        }
        return vehicleMutableLiveData;
    }

    public MutableLiveData<Integer> getTrailersMutableLiveData() {
        if (trailersMutableLiveData == null) {
            trailersMutableLiveData = new MutableLiveData<>();
        }
        return trailersMutableLiveData;
    }

    public MutableLiveData<Integer> getShippingDocsMutableLiveData() {
        if (shippingDocsMutableLiveData == null) {
            shippingDocsMutableLiveData = new MutableLiveData<>();
        }
        return shippingDocsMutableLiveData;
    }

    public void getCurDateDvirs(Context ctx, String day, Button no_dvir, RecyclerViewLastAdapter.LastDaysRecyclerViewItemClickListener listener){
        Disposable disposable = dvirRepository.getAllDvirs()
//                .flattenAsObservable(dvirs -> dvirs)
//                .filter(dvir -> dvir.getDate().equals(stringToDay(day)))
//                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dvirEntities ->{
                    if (dvirEntities.size() > 0){
                        if (listener != null){
                            listener.onclickDvir(day,true);
                        }
                    }else {
                        if (listener != null){
                            listener.onclickDvir(day,false);
                        }
                    }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void getDvirCurr(Context ctx,String day,Button no_dvir){
        Disposable disposable = dvirRepository.getAllDvirs()
                .flattenAsObservable(dvirs -> dvirs)
                .filter(dvir -> dvir.getDate().equals(stringToDay(day)))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dvirEntities ->{
                    if (dvirEntities.size() > 0){
                        Drawable drawable = no_dvir.getCompoundDrawables()[0];
                        if (drawable != null){
                            int color = ContextCompat.getColor(ctx,R.color.colorPrimary); // replace with the color you want
                            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN; // specifies how the color should be applied to the drawable
                            ColorFilter colorFilter = new PorterDuffColorFilter(color, mode);
                            drawable.setColorFilter(colorFilter);
                            no_dvir.setCompoundDrawables(drawable, null, null, null);
                            no_dvir.setTextColor(color);
                        }
                        no_dvir.setText(R.string.dvir);
                    }else {
                        Drawable drawable = no_dvir.getCompoundDrawables()[0];
                        if (drawable != null) {
                            TypedValue typedValue = new TypedValue();
                            ctx.getTheme().resolveAttribute(R.attr.customTextHintColor, typedValue, true);
                            int color = typedValue.data; // replace with the color you want
                            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN; // specifies how the color should be applied to the drawable
                            ColorFilter colorFilter = new PorterDuffColorFilter(color, mode);
                            drawable.setColorFilter(colorFilter);
                            no_dvir.setCompoundDrawables(drawable, null, null, null);
                            no_dvir.setTextColor(color);
                        }
                        no_dvir.setText(R.string.no_dvir);
                    }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void getMgetDvirs(Context context, String day, ListView dvir_recyclerview, LinearLayout container1) {
        Disposable disposable = dvirRepository.getAllDvirs()
                .flattenAsObservable(dvirs -> dvirs)
                .filter(dvir -> dvir.getDate().equals(stringToDay(day)))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dvirEntities ->{
                    if (dvirEntities.size() > 0){
                        DvirlistAdapter adapter = new DvirlistAdapter(context,dvirEntities);
                        dvir_recyclerview.setAdapter(adapter);
                        container1.setVisibility(View.GONE);
                        dvir_recyclerview.setVisibility(View.VISIBLE);
                        clickDvirButton(context,adapter,day,dvir_recyclerview,container1);
                    }else {
                        container1.setVisibility(View.VISIBLE);
                        dvir_recyclerview.setVisibility(View.GONE);
                    }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    private void clickDvirButton(Context context, DvirlistAdapter adapter, String day, ListView dvir_recyclerview, LinearLayout container1) {
        adapter.setListener(dvirEntity1 -> {

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_delete_dvir_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            dialog.findViewById(R.id.idDvirYes).setOnClickListener(view1 ->{
                deleteDvir(dvirEntity1,context,day,dvir_recyclerview,container1);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.idDvirNo).setOnClickListener(view2 -> {
                dialog.dismiss();
            });

            dialog.show();

        });
    }

    public void deleteDvir(Dvir entity,Context context,String day,ListView dvir_recyclerview,LinearLayout container1) {
         dvirRepository.deleteDvir(entity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 .doOnComplete(()->{
                     getMgetDvirs(context,day,dvir_recyclerview,container1);
                     getDvirMutableLiveData().postValue(entity);
                 })
                .subscribe();
    }


    public void insertDvir(Dvir dvirEntity) {
        Completable.fromAction(() -> dvirRepository.insertDvir(dvirEntity)).subscribeOn(Schedulers.io())
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
