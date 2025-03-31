package com.mallik.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class ExcelGenerator {

    public static void main(String[] args) throws IOException {
        // Load JSON from a file
        String outputFileName = "./files/questions_"+new Random().nextInt(1000) +".xlsx";
        File jsonFile = new File("/Users/mgajji/work/personal/gmk_dsa/files/questions.json");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile);
        JsonNode questions = root.at("/data/favoriteQuestionList/questions");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Questions");

        CellStyle mediumStyle = createColoredStyle(workbook, "FFB8E0");
        CellStyle easyStyle = createColoredStyle(workbook, "A0C878");
        CellStyle hardStyle = createColoredStyle(workbook, "E16A54");

        // Header row
        String[] headers = {"ID", "Title", "Difficulty", "Frequency", "AC Rate", "Topic Tags", "date", "timeTaken", "done", "notes"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data rows
        int rowNum = 1;
        for (JsonNode q : questions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(q.get("questionFrontendId").asInt());
            row.createCell(1).setCellValue(q.get("title").asText());

            Cell difficultyCell = row.createCell(2);
            String difficulty = q.get("difficulty").asText();
            difficultyCell.setCellValue(difficulty);

            switch (difficulty.toUpperCase()) {
                case "MEDIUM":
                    difficultyCell.setCellStyle(mediumStyle);
                    break;
                case "EASY":
                    difficultyCell.setCellStyle(easyStyle);
                    break;
                case "HARD":
                    difficultyCell.setCellStyle(hardStyle);
                    break;
            }

            row.createCell(3).setCellValue(q.get("frequency").asDouble());
            row.createCell(4).setCellValue(q.get("acRate").asDouble());

            // Collect topic tag names
            Iterator<JsonNode> tagIterator = q.withArray("topicTags").elements();
            StringBuilder tags = new StringBuilder();
            while (tagIterator.hasNext()) {
                JsonNode tag = tagIterator.next();
                tags.append(tag.get("name").asText());
                if (tagIterator.hasNext()) tags.append(", ");
            }
            row.createCell(5).setCellValue(tags.toString());
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to Excel file

        try (FileOutputStream fileOut = new FileOutputStream(outputFileName)) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("Excel file generated: questions.xlsx");
    }

    // Helper to create a style with background color
    private static CellStyle createColoredStyle(Workbook workbook, String hexColor) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(hexToRgb(hexColor), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    // Convert hex string (like "FFB8E0") to byte array
    private static byte[] hexToRgb(String colorStr) {
        return new byte[]{
                (byte) Integer.parseInt(colorStr.substring(0, 2), 16),
                (byte) Integer.parseInt(colorStr.substring(2, 4), 16),
                (byte) Integer.parseInt(colorStr.substring(4, 6), 16)
        };
    }
}
