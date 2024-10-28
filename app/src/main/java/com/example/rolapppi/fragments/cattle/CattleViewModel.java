package com.example.rolapppi.fragments.cattle;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

public class CattleViewModel extends AndroidViewModel implements CattleRepository.OnFireStoreDataAdded {

    MutableLiveData<List<CattleModel>> cattleModelListData = new MutableLiveData<>();
    MutableLiveData<CattleModel> selected = new MutableLiveData<>();
    private Map<String, Integer> notificationIdsMap;

    CattleRepository firebaseRepo = new CattleRepository(this);


    public CattleViewModel(@NonNull Application application) {
        super(application);
        firebaseRepo.loadData();
        notificationIdsMap = new HashMap<>();
//getSharedPreferences().edit().clear().apply();
        cattleModelListData.observeForever(cattleModels -> {
            updateNotifications(cattleModels);
        });


    }

    private void updateNotifications(List<CattleModel> cattleModels) {
        // Pętla po modelach w liście
        for (CattleModel cattleModel : cattleModels) {
            String animalId = cattleModel.getAnimal_id();
            if (!cattleModel.getCaliving().isEmpty()) {
                // Jeśli animalId nie istnieje w mapie, tworzymy nowe powiadomienie
                if (!getSharedPreferences().contains(animalId)) {
                    createNotification(cattleModel);
                } else {
                    // Jeśli animalId istnieje w mapie, sprawdzamy czy data urodzin się zmieniła
//                    int notificationId = notificationIdsMap.get(animalId);
                    String oldCalving = getSharedPreferences().getString(animalId, "");
                    if (!oldCalving.equals(cattleModel.getCaliving())) {
                        // Jeśli data urodzin się zmieniła, usuwamy stare powiadomienie i tworzymy nowe
                        removeNotification(cattleModel.getAnimal_id().hashCode());
                        createNotification(cattleModel);
                    }
                }
            }else if(getSharedPreferences().contains(animalId)){
                removeNotification(cattleModel.getAnimal_id().hashCode());
                getSharedPreferences().edit().remove(animalId).apply();
            }
        }

        //Dodaj usuwanie powiadomień gdy zwierzę zostanie usunięte/zmieniona płeć itd.
        //sprawdz puste daty zacielenia

        // Usuwamy powiadomienia powiązane z usuniętymi modelami
//        Set<String> animalIdsSet = new HashSet<>();
//        for (CattleModel cattleModel : cattleModels) {
//            animalIdsSet.add(cattleModel.getAnimal_id());
//        }
//        Map<String, ?> allEntries = getSharedPreferences().getAll();
//        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
//            String key = entry.getKey();
//            if (key.startsWith("notification_")) {
//                String animalId = key.substring(13);
//                if (!animalIdsSet.contains(animalId)) {
//                    removeNotification(Integer.parseInt(entry.getValue().toString()));
//                    getSharedPreferences().edit().remove(key).apply();
//                }
//            }
//        }
    }

    private void createNotification(CattleModel cattleModel) {
        // Ustawiamy powiadomienie na 235 dni po zacieleniu modelu
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date calvingDate = dateFormat.parse(cattleModel.getCaliving());
            calendar.setTime(calvingDate);
            calendar.add(Calendar.DAY_OF_MONTH, 235);
            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplication(), com.example.rolapppi.utills.NotificationReceiver.class);
            intent.putExtra("animalId", cattleModel.getAnimal_id());
            intent.putExtra("animalIntent", cattleModel.getAnimal_id().hashCode());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(), cattleModel.getAnimal_id().hashCode(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

         //   if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
         //   }
            // Zapisujemy ID powiadomienia do mapy
            notificationIdsMap.put(cattleModel.getAnimal_id(), intent.hashCode());
            // Zapisujemy datę urodzenia do SharedPreferences, aby móc porównać ją w przyszłości
            getSharedPreferences().edit().putString(cattleModel.getAnimal_id(), cattleModel.getCaliving()).apply();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void removeNotification(int notificationId) {
        Intent notificationIntent = new Intent(getApplication(), com.example.rolapppi.utills.NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplication(),
                notificationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private SharedPreferences getSharedPreferences() {
        return getApplication().getSharedPreferences("notifications", Context.MODE_PRIVATE);
    }

    public LiveData<List<CattleModel>> getLiveDatafromFireStore() {
        return cattleModelListData;
    }

    @Override
    public void cattleDataAdded(List<CattleModel> cattleModelList) {
        cattleModelListData.setValue(cattleModelList);
    }

    public void cattleAdd(CattleModel cattleModel) {
        if (!cattleModelListData.getValue().stream().anyMatch(e -> e.getAnimal_id().equals(cattleModel.getAnimal_id()))) {
            firebaseRepo.addCattle(cattleModel);
        }
    }

    public void addCalving(CattleModel cattleModel, String calving) {
        cattleModel.setCaliving(calving);
        firebaseRepo.addCattle(cattleModel);
    }

    public void cattleDelete(CattleModel cattleModel) {
        firebaseRepo.deleteCattle(cattleModel);
    }

    public void cattleUpdateMother(String cattleMotherId, String previousCaliving) {
        firebaseRepo.updateCattleMother(cattleMotherId,
                previousCaliving);
    }

    public void deleteCalving(String cattleMotherId) {
        firebaseRepo.deleteCalving(cattleMotherId);
    }

    public void setSelected(CattleModel cattleModel) {
        selected.setValue(cattleModel);
    }

    public void cattleEdit(CattleModel cattleModel) {
        selected.setValue(cattleModel);
        firebaseRepo.addCattle(cattleModel);
    }

    public MutableLiveData<CattleModel> getSelected() {
        return selected;
    }

    public List<CattleModel> getFemaleCattleList(List<CattleModel> cattleList) {
        List<CattleModel> femaleCattleList = new ArrayList<>();
        for (CattleModel cattleModel : cattleList) {
            if (cattleModel.getGender().equals("female")) {
                femaleCattleList.add(cattleModel);
            }
        }
        return femaleCattleList;
    }

}