package com.example.rolapppi.ui.cattle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.rolapppi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCattleDialog extends AppCompatDialogFragment {
    private EditText animal_idE, birthdayE, mother_idE, genderE;

    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addcattle, null);

        builder.setView(view)
                .setTitle("Bydło edycja")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Zatwierdź", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String animal_id = animal_idE.getText().toString();
                        String mother_id = mother_idE.getText().toString();
                        String birthday = birthdayE.getText().toString();
                        String gender = genderE.getText().toString();
                        Map<String, Object> data = new HashMap<>();
                        data.put("birthday", birthday);
                        data.put("gender", gender);
                        data.put("mother_id", mother_id);


                        FirebaseFirestore.getInstance().collection("user_data").document(FirebaseAuth.getInstance().getUid())
                                .collection("cattle").document(animal_id).set(data);
//                        listener.applyTexts(animal_id, birthday, gender);
                    }
                });

        animal_idE = view.findViewById(R.id.animal_id);
        mother_idE = view.findViewById(R.id.mother_id);
        birthdayE = view.findViewById(R.id.birthday);
        genderE = view.findViewById(R.id.gender);

        return builder.create();
    }



    public interface ExampleDialogListener {
        void applyTexts(String animal_id, String birthday, String gender);
    }
}
