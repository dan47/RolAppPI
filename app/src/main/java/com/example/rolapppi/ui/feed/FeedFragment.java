package com.example.rolapppi.ui.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.rolapppi.ui.cropProtection.AddCropProtectionDialog;
import com.example.rolapppi.ui.cropProtection.CpAdapter;
import com.example.rolapppi.ui.cropProtection.CropProtectionModel;
import com.example.rolapppi.ui.cropProtection.CropProtectionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Table;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
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


public class FeedFragment extends Fragment implements FAdapter.OnModelListener {

    RecyclerView recyclerView;
    FAdapter fAdapter;
    FeedViewModel viewModel;
    FloatingActionButton addBtn;
    Button exportBtn;
    ProgressBar progressBar;
    private Spinner spinner;
    private NavController navController;


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.feedRecyclerView);
        addBtn = view.findViewById(R.id.addBtn);
        exportBtn = view.findViewById(R.id.exportBtn);
        progressBar = view.findViewById(R.id.progressBarFeedFragment);
        spinner = view.findViewById(R.id.spinner);

        fAdapter = new FAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fAdapter);


        navController = Navigation.findNavController(view);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFeedDialog exampleDialog = new AddFeedDialog();
                exampleDialog.show(getParentFragmentManager(), "example dialog");
            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Eksport")
                        .setMessage("Czy chcesz eksportować rejestr pasz?")
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
        viewModel = new ViewModelProvider(requireActivity()).get(FeedViewModel.class);
        viewModel.selected.setValue(null);

        viewModel.getLiveDatafromFireStore().observe(getViewLifecycleOwner(), new Observer<List<FeedModel>>() {
            @Override
            public void onChanged(List<FeedModel> feedModels) {
                feedModels.sort((d1, d2) -> LocalDate.parse(d1.getPurchaseDate(), formatter).compareTo(LocalDate.parse(d2.getPurchaseDate(), formatter)));
                Collections.reverse(feedModels);
                List<String> years = new ArrayList<>();
                years.add("Wszystko");
                years.addAll(feedModels.stream().map(e -> e.getPurchaseDate().substring(6, 10)).distinct().collect(Collectors.toList()));
                fAdapter.setFeedModelData(feedModels);
                fAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (years.size() != 1) {
                    spinner.setVisibility(View.VISIBLE);
                    exportBtn.setVisibility(View.VISIBLE);
                } else {
                    spinner.setVisibility(View.INVISIBLE);
                    exportBtn.setVisibility(View.GONE);
                }
                spinner.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, years));
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                fAdapter.getFilter().filter(adapterView.getItemAtPosition(i).toString());

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
        viewModel.setSelected(fAdapter.feedModelList.get(position));
        navController.navigate(R.id.action_nav_feed_to_detailsFeedFragment);
    }

    public void createPdf() throws DocumentException, IOException {

        List<FeedModel> feedModelListToPdf = viewModel.getLiveDatafromFireStore().getValue();
        Collections.reverse(feedModelListToPdf);
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "RejestrPasz.pdf");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

        BaseFont bf = BaseFont.createFont("/res/font/arial.ttf", BaseFont.CP1250, BaseFont.EMBEDDED);
        Font bold = new Font(bf, 12, Font.BOLD);
        Font normalFont = new Font(bf, 12, Font.NORMAL);


        HeaderFooter event = new HeaderFooter(normalFont);
//        pdfWriter.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        pdfWriter.setPageEvent(event);
        document.open();


        float columnWidth[] = {240f, 300f, 280f, 280f, 180f, 260f, 420f, 290f, 210f};
        String[] columnsTitle = {"Data zakupu paszy", "Sprzedawca paszy", "Producent paszy",
                "Nazwa i rodzaj paszy", "Nr partii",
                "Ilość zakupionej paszy",
                "Rodzaj opakowania", "Podpis właściciela", "Uwagi"};
        String[] columnsTitle2 = {"", "", "", "", "", "", " (np. worek, silos, BIG-BAG, pryzma)", "", ""};


        PdfPTable pdfPTable = new PdfPTable(columnWidth);
        pdfPTable.setTotalWidth(800f);
        pdfPTable.setLockedWidth(true);


        for (int i = 0; i < columnsTitle.length; i++) {
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase(columnsTitle[i], bold));
            paragraph.add(new Phrase(columnsTitle2[i], normalFont));
            PdfPCell cell = new PdfPCell(paragraph);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            pdfPTable.addCell(cell);
        }
//        AtomicInteger index = new AtomicInteger();
//        index.getAndIncrement();
        feedModelListToPdf.stream().forEach(e -> {


            PdfPCell cell = new PdfPCell(new Paragraph(e.getPurchaseDate(), normalFont));
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getSeller(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getProducer(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getNameFeed(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getBatch(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getCount(), normalFont));
            pdfPTable.addCell(cell);

            cell.setPhrase(new Paragraph(e.getPackageType(), normalFont));
            pdfPTable.addCell(cell);

            pdfPTable.addCell("");
            cell.setPhrase(new Paragraph(e.getRemarks(), normalFont));
            pdfPTable.addCell(cell);
        });
        pdfPTable.setHeaderRows(1);

        document.add(pdfPTable);
        document.close();


        Log.e("DIPA", exportBtn.toString());
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


        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
            normalFont.setStyle(Font.BOLD);
            normalFont.setSize(16);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase("Rejestr pasz z zakupu", normalFont),
                    document.getPageSize().getWidth()/2, 560, 0);
            normalFont.setStyle(Font.NORMAL);
            normalFont.setSize(12);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Nazwisko i imię .................................", normalFont),
                    22, 540, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Adres ................................................", normalFont),
                    22, 520, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase("Nr dostawcy mleka ...........................", normalFont),
                    22, 500, 0);
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(String.format("Nr strony %d", pagenumber)),
                    document.getPageSize().getWidth()-22, 500, 0);
            try {
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
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