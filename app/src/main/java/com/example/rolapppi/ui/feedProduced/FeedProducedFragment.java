package com.example.rolapppi.ui.feedProduced;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
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
import android.widget.Toast;

import com.example.rolapppi.BuildConfig;
import com.example.rolapppi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableFooter;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class FeedProducedFragment extends Fragment implements FpAdapter.OnModelListener {


    private RecyclerView recyclerView;
    private FpAdapter fpAdapter;
    private FeedProducedViewModel viewModel;
    private FloatingActionButton addBtn;
    private Button exportBtn;
    private ProgressBar progressBar;
    private Spinner spinner;
    private NavController navController;


    public FeedProducedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_produced, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.feedProducedRecyclerView);
        addBtn = view.findViewById(R.id.addBtn);
        exportBtn = view.findViewById(R.id.exportBtn);
        progressBar = view.findViewById(R.id.progressBarFeedProducedFragment);
        spinner = view.findViewById(R.id.spinner);

        fpAdapter = new FpAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fpAdapter);


        navController = Navigation.findNavController(view);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFeedProducedDialog exampleDialog = new AddFeedProducedDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Eksport")
                        .setMessage("Czy chcesz eksportować rejestr wyprodukowanych pasz?")
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        viewModel = new ViewModelProvider(requireActivity()).get(FeedProducedViewModel.class);
        viewModel.selected.setValue(null);

        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<FeedProducedModel>>() {
            @Override
            public void onChanged(List<FeedProducedModel> feedProducedModels) {
                feedProducedModels.sort((d1, d2) -> LocalDate.parse(d1.getAcquisition(), formatter).compareTo(LocalDate.parse(d2.getAcquisition(), formatter)));
                Collections.reverse(feedProducedModels);
                List<String> years = new ArrayList<>();
                years.add("Wszystko");
                years.addAll(feedProducedModels.stream().map(e -> e.getAcquisition().substring(6, 10)).distinct().collect(Collectors.toList()));
                fpAdapter.setFeedProducedModelData(feedProducedModels);
                fpAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (years.size() != 1) {
                    exportBtn.setClickable(true);
                } else {
                    exportBtn.setClickable(false);
                }
                spinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, years));
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                fpAdapter.getFilter().filter(adapterView.getItemAtPosition(i).toString());

                try {
                    //Your task here
                } catch (Exception e) {
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
        viewModel.setSelected(fpAdapter.feedProducedModelList.get(position));
        navController.navigate(R.id.action_nav_feedProduced_to_detailsFeedProducedFragment);
    }

    public void createPdf() throws DocumentException, IOException {

        List<FeedProducedModel> feedProducedModelListToPdf = viewModel.getLiveDatafromFireStore().getValue();
        Collections.reverse(feedProducedModelListToPdf);
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "RejestrWyprodukowanychPasz.pdf");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

        BaseFont bf = BaseFont.createFont("/res/font/arial.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
        BaseFont bfPower = BaseFont.createFont("/res/font/arial.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
        Font normalFont = new Font(bf, 12, Font.NORMAL);
        Font bold = new Font(bf, 16, Font.BOLD);
        Font powerFont = new Font(bfPower, 12, Font.BOLD);


        FeedProducedFragment.HeaderFooter event = new FeedProducedFragment.HeaderFooter(bold);
//        pdfWriter.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        pdfWriter.setPageEvent(event);
        document.open();


        float columnWidth[] = {80f, 300f, 280f, 220f, 280f, 280f, 200f, 200f};
        String[] columnsTitle = {"Lp.", "Nazwa paszy", "Pochodzenie własna/zakup", "Ilość",
                "Przeznaczenie", "Gatunek/Kategoria, grupa wiekowa zwierząt",
                "Data zakupu, zbioru", "Uwagi"};


        PdfPTable pdfPTable = new PdfPTable(columnWidth);
        pdfPTable.setTotalWidth(800f);
        pdfPTable.setLockedWidth(true);


        for (String column : columnsTitle) {
            PdfPCell cell = new PdfPCell(new Paragraph(column, bold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            pdfPTable.addCell(cell);
        }
        AtomicInteger index = new AtomicInteger();
        index.getAndIncrement();
        feedProducedModelListToPdf.stream().forEach(e -> {


            PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(index.getAndIncrement()), normalFont));
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getNameFeed(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getOrigin(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getCount(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getDestination(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getCattleType(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getAcquisition(), normalFont));
            pdfPTable.addCell(cell);

            pdfPTable.addCell("");

        });
        pdfPTable.setHeaderRows(1);

        document.add(pdfPTable);
        document.close();


        Log.e("DIPA", exportBtn.toString());

        if (file.exists()) //Checking if the file exists or not
        {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(data, "application/pdf");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {

            Toast.makeText(getActivity(), "Plik nie istnieje! ", Toast.LENGTH_SHORT).show();

        }
    }


    class HeaderFooter extends PdfPageEventHelper {

        Phrase[] header = new Phrase[2];

        int pagenumber;
        Font normalFont;

        public HeaderFooter(Font normalFont) {
            this.normalFont = normalFont;
        }

        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("Movie history");
        }


        public void onChapter(PdfWriter writer, Document document,
                              float paragraphPosition, Paragraph title) {
            header[1] = new Phrase(title.getContent());
            pagenumber = 1;
        }

        @Override
        public void onChapterEnd(PdfWriter writer, Document document, float position) {
            super.onChapterEnd(writer, document, position);

        }

        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;

            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Ewidencja pasz produkowanych/zakupionych i stosowanych w żywieniu zwierząt", normalFont),
                    22, 540, 0);
            normalFont.setSize(12);

            try {
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }


        public void onEndPage(PdfWriter writer, Document document) {

//
//            Rectangle rect = writer.getBoxSize("art");
//
//            ColumnText.showTextAligned(writer.getDirectContent(),
//                    Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber)),
//                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }

}