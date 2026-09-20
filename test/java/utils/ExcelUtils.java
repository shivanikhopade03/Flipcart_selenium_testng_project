package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads tabular test data out of an .xlsx file and hands it back as a
 * simple List<Map> (column header -> cell value) or Object[][] for direct
 * use in a TestNG @DataProvider. Row 1 must be the header row.
 */
public class ExcelUtils {

    /**
     * Reads a sheet and returns every row as a String[] in column order,
     * ready to be returned straight from a TestNG @DataProvider.
     */
    public static Object[][] getTestData(String filePath, String sheetName) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            DataFormatter formatter = new DataFormatter();
            int lastRow = sheet.getLastRowNum();
            int lastCol = sheet.getRow(0).getLastCellNum();

            // Row 0 is the header, so data starts at row 1
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                String[] rowData = new String[lastCol];
                for (int c = 0; c < lastCol; c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[c] = formatter.formatCellValue(cell);
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }

        return data.toArray(new Object[0][0]);
    }
}
