package com.iosix.eldblesample.viewModel;

import static com.iosix.eldblesample.enums.Day.stringToDay;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.color.MaterialColors;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.repository.SignatureRepository;

import java.net.ContentHandler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignatureViewModel extends AndroidViewModel {

    private final SignatureRepository repository;
    private final CompositeDisposable disposables;

    public SignatureViewModel(@NonNull Application application) {
        super(application);
        repository = new SignatureRepository(application);
        disposables = new CompositeDisposable();
    }

    public void getMgetAllSignatures(Context context,String currDay, SignaturePad signaturePad) {
        Disposable disposable = repository.getGetAllSignatures()
                .flattenAsObservable(signatureEntities -> signatureEntities)
                .filter(signatureEntity -> signatureEntity.getDay().equals(stringToDay(currDay)))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signatureEntities->{
                    if (signatureEntities.size()>0){
                        signaturePad.setBackgroundColor(MaterialColors.getColor(context, R.attr.customBgColor,Color.WHITE));
                        signaturePad.setSignatureBitmap(signatureEntities.get(signatureEntities.size()-1).getSignatureBitmap());
                    }else {
                        signaturePad.clear();
                    }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void getLastSignatures(String currDay,ImageView imageView) {
        Disposable disposable = repository.getGetAllSignatures()
                .flattenAsObservable(signatureEntities -> signatureEntities)
                .filter(signatureEntity -> signatureEntity.getDay().equals(stringToDay(currDay)))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signatureEntities->{
                    if (signatureEntities.size()>0){
                        imageView.setImageBitmap(signatureEntities.get(signatureEntities.size()-1).getSignatureBitmap());
                    }else {
                        imageView.setImageBitmap(null);
                    }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void getLastSign(Context context,TextView textView,SignaturePad signaturePad) {
        Disposable disposable = repository.getGetAllSignatures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signatureEntities->{
                   if (signatureEntities.size()>0){
                       textView.setOnClickListener(view -> {
                           signaturePad.setBackgroundColor(MaterialColors.getColor(context, R.attr.customBgColor,Color.WHITE));
                           signaturePad.setSignatureBitmap(signatureEntities.get(signatureEntities.size()-1).getSignatureBitmap());
                       });
                   }
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void insertSignature(SignatureEntity entity){
        Completable.fromAction(() -> repository.insertSignature(entity)).subscribeOn(Schedulers.io())
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
