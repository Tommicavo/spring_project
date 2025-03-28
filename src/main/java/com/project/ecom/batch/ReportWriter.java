package com.project.ecom.batch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.ecom.models.entities.ExpenseEntity;
import com.project.ecom.models.entities.ReportEntity;
import com.project.ecom.models.repositories.ReportRepository;

@Component
public class ReportWriter implements ItemWriter<ReportEntity> {

    @Value("${dirOutput}")
    private String dirOutput;

    @Autowired
    private ReportRepository reportRepo;

    @Override
    public void write(Chunk<? extends ReportEntity> reports) throws Exception {
        File directory = new File(dirOutput);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (ReportEntity report : reports) {
            String filePath = dirOutput + File.separator + "Report_" + report.getIdReport() + ".pdf";

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            document.add(new Paragraph("REPORT INFO"));
            document.add(new Paragraph("Report Title: " + report.getTitleReport()));
            document.add(new Paragraph("Email Report Owner: " + report.getUser().getEmailUser()));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("REPORT EXPENSES"));
            for (ExpenseEntity expense : report.getExpenses()) {
                document.add(new Paragraph("    Expense Title: " + expense.getTitleExpense()));
                document.add(new Paragraph("    Expense Price: " + expense.getPriceExpense()));
                document.add(new Paragraph("    Expense Category: " + expense.getCategory().getLabelCategory()));
                document.add(new Paragraph("\n"));
            }

            document.add(new Paragraph("REPORT COSTS"));
            document.add(new Paragraph("Monthly Cost by Price: " + report.getMonthlyCostByPrice() + "€"));
            document.add(new Paragraph("Weekly Cost by Price:"));
            for (Map.Entry<Integer, Long> entry : report.getWeeklyCostByPrice().entrySet()) {
                document.add(new Paragraph("Week " + entry.getKey() + " = " + entry.getValue() + "€"));
            }
            document.add(new Paragraph("Monthly Cost by Category:"));
            for (Map.Entry<Long, Long> entry : report.getMonthlyCostByCategory().entrySet()) {
                document.add(new Paragraph("idCategory " + entry.getKey() + ": " + entry.getValue() + "€"));
            }

            document.close();

            report.setSummaryReport(byteArrayOutputStream.toByteArray());
            reportRepo.save(report);

            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                byteArrayOutputStream.writeTo(outputStream);
            }

            byteArrayOutputStream.close();
            fileOutputStream.close();
        }
    }
}
