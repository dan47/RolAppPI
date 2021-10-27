package com.example.rolapppi.ui.cropProtection;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.rolapppi.R;
import com.example.rolapppi.ui.cattle.AddCattleDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CropProtectionFragment extends Fragment implements CpAdapter.OnModelListener{


    RecyclerView recyclerView;
    CpAdapter cpAdapter;
    CropProtectionViewModel viewModel;
    FloatingActionButton addBtn;
    Button exportBtn;
    ProgressBar progressBar;
    private Spinner spinner;
    private NavController navController;

    public CropProtectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop_protection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.cropProtectionRecyclerView);
        addBtn = view.findViewById(R.id.addBtn);
        exportBtn = view.findViewById(R.id.exportBtn);
        progressBar = view.findViewById(R.id.progressBarCropProtectionFragment);
        spinner = view.findViewById(R.id.spinner);

        cpAdapter = new CpAdapter( this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cpAdapter);


        navController = Navigation.findNavController(view);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCropProtectionDialog exampleDialog = new AddCropProtectionDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Eksport")
                        .setMessage("Czy chcesz eksportować ewidencje zabiegów ochrony roślin?")
                        .setPositiveButton("Eksportuj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    createPdf();
                                } catch (DocumentException e) {
                                    Log.e("DIPA", e.toString());
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    Log.e("DIPA", e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }).show();
            }
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        viewModel = new ViewModelProvider(requireActivity()).get(CropProtectionViewModel.class);
        viewModel.selected.setValue(null);

        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<CropProtectionModel>>() {
            @Override
            public void onChanged(List<CropProtectionModel> cropProtectionModels) {
                cropProtectionModels.sort((d1, d2) -> LocalDateTime.parse(d1.getTreatmentTime(), formatter).compareTo(LocalDateTime.parse(d2.getTreatmentTime(), formatter)));
                Collections.reverse(cropProtectionModels);
                List<String> years = new ArrayList<>();
                years.add("Wszystko");
                years.addAll(cropProtectionModels.stream().map(e->e.getTreatmentTime().substring(12,16)).distinct().collect(Collectors.toList()));
                cpAdapter.setCropProtectionModelData(cropProtectionModels);
                cpAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if(years.size()!=1){
                    spinner.setVisibility(View.VISIBLE);
                    exportBtn.setVisibility(View.VISIBLE);
                }else{
                    spinner.setVisibility(View.INVISIBLE);
                    exportBtn.setVisibility(View.GONE);
                }
                spinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, years));
//                recyclerView.setAnimation(fadein);
//                progressBar.setAnimation(fadeout);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cpAdapter.getFilter().filter( adapterView.getItemAtPosition(i).toString());

                try {
                    //Your task here
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onModelClick(int position) {
        viewModel.setSelected(cpAdapter.cropProtectionModelList.get(position));
        navController.navigate(R.id.action_nav_cropProtection_to_detailsCropProtectionFragment);
    }

    public void createPdf() throws DocumentException, IOException {

        List<CropProtectionModel> cropProtectionModelListToPdf = viewModel.getLiveDatafromFireStore().getValue();
        Collections.reverse(cropProtectionModelListToPdf);
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "EwidencjaZabiegowOchronyRoslin.pdf");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(file));

        document.open();
        float columnWidth[] = {80f, 240f, 240f, 240f, 240f, 240f, 240f};
        String[] columnsTitle = {"Lp.", "Czas Zabiegu", "Uprawa, na której zastosowano środek",
                "Powierzchnia, na której zastosowano środek", "Pełna nazwa zastostowanego środka ochrony roślin",
                "Dawka zastosowanego środka",
                "Przyczyny zastosowania środka ochrony roślin"};
        String[] columnsTitle2 = {""," (godzina, data)","", " (ha)","", " (l/ha), (kg/ha), stężenie (%)", " (nazwy chorób, szkodników, chwastów, itp.)" };


        PdfPTable pdfPTable = new PdfPTable(columnWidth);
        BaseFont bf = BaseFont.createFont("/res/font/arial.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
        Font bold = new Font(bf,12, Font.BOLD);
        Font normalFont = new Font(bf,12, Font.NORMAL);

        for(int i =0; i<columnsTitle.length; i++ ){
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase(columnsTitle[i],bold));
            paragraph.add(new Phrase(columnsTitle2[i],normalFont));
            PdfPCell cell = new PdfPCell(paragraph);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            pdfPTable.addCell(cell);
        }
        AtomicInteger index = new AtomicInteger();
        index.getAndIncrement();
        cropProtectionModelListToPdf.stream().forEach(e-> {

            pdfPTable.addCell(String.valueOf(index.getAndIncrement()));
            pdfPTable.addCell(new Paragraph(e.getTreatmentTime(),normalFont));
            pdfPTable.addCell(new Paragraph(e.getCrop(), normalFont));
            pdfPTable.addCell(new Paragraph(e.getArea(), normalFont));
            pdfPTable.addCell(new Paragraph(e.getProtectionProduct(), normalFont));
            pdfPTable.addCell(new Paragraph(e.getDose(), normalFont));
            pdfPTable.addCell(new Paragraph(e.getReason(), normalFont));
        });
        pdfPTable.setHeaderRows(1);
        document.add(pdfPTable);

        document.close();
        Log.e("DIPA", exportBtn.toString());
    }
}