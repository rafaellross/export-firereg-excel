package au.com.smartplumbingsolutions.fireregister.fireregisterexcel.controller;

import au.com.smartplumbingsolutions.fireregister.fireregisterexcel.entities.Penetration;
import au.com.smartplumbingsolutions.fireregister.fireregisterexcel.service.PenetrationService;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RestController
public class PenetrationController {

    private PenetrationService service;

    public PenetrationController(PenetrationService service) {
        this.service = service;
    }

    @GetMapping("/fire-register/{jobId}")
    public void exportToExcel(@PathVariable Integer jobId, HttpServletResponse response) throws IOException {

        try (final Workbook workbook = new XSSFWorkbook();
             ) {
            List<Penetration> data = this.service.findAllByJob(jobId);

            data.forEach(peno -> {
                if (peno.getFirePhoto() != null) {


                    String image64 =  peno.getFirePhoto().split(",")[1];
                    byte[] imageBytes = Base64.decodeBase64(image64);

                    String filePath = String.format("./photos/%s.jpg", peno.getId());
                    Path path = Path.of(filePath);

                    try {
                        Files.write(path, imageBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });
            Sheet sheet = workbook.createSheet("FireRegister");

            int rowIdx = 0;

            Row headerRow = sheet.createRow(rowIdx++);
            headerRow.createCell(0).setCellValue("Reference");
            headerRow.createCell(1).setCellValue("Drawing");
            headerRow.createCell(2).setCellValue("Floor or wall designation");
            headerRow.createCell(3).setCellValue("Wall (W) Floor (F) Ceiling(C)");
            headerRow.createCell(4).setCellValue("Substrate");

            headerRow.createCell(5).setCellValue("FRL");
            headerRow.createCell(6).setCellValue("Service description size");
            headerRow.createCell(7).setCellValue("Installed By");
            headerRow.createCell(8).setCellValue("Installation Date");
            headerRow.createCell(9).setCellValue("Manufacturer product and system number");
            headerRow.createCell(10).setCellValue("Photo");

            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            for (Penetration obj : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(obj.getFireNumber());
                row.createCell(1).setCellValue(obj.getDrawing());
                row.createCell(2).setCellValue(obj.getLevel());
                if (obj.getFireResistLevel().equals("\"-/90/90\"") || obj.getFireResistLevel().equals("\"-/60/60\"")) {
                    row.createCell(3).setCellValue("W");
                } else {
                    row.createCell(3).setCellValue("F");
                }
                row.createCell(4).setCellValue(obj.getSubstrate());
                row.createCell(5).setCellValue(obj.getFireResistLevel());
                row.createCell(6).setCellValue(obj.getServiceDescription());
                row.createCell(7).setCellValue(obj.getInstalledBy());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                row.createCell(8).setCellValue(simpleDateFormat.format(obj.getInstallDate()));
                row.createCell(9).setCellValue(obj.getManufacturer());

                if (obj.getFirePhoto() != null) {
                    XSSFClientAnchor photo = new XSSFClientAnchor();
                    row.setHeight((short) 2000);
                    updateCellWithImage(workbook, rowIdx, drawing, photo, String.format("./photos/%s.jpg", obj.getId()));
                }
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < 11; i++) {
                sheet.autoSizeColumn(i);
            }

            UUID uuid = UUID.randomUUID();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s.xlsx", uuid.toString()));


            workbook.write(response.getOutputStream());

        }

    }

    private static void updateCellWithImage(Workbook workbook, int rowNum, XSSFDrawing drawing, XSSFClientAnchor inputImageAnchor, String inputImageName) throws IOException {

        FileInputStream inputImageStream = new FileInputStream(inputImageName);

        byte[] inputImageBytes = IOUtils.toByteArray(inputImageStream);
        int inputImagePictureID = workbook.addPicture(inputImageBytes, Workbook.PICTURE_TYPE_JPEG);
        inputImageStream.close();
        inputImageAnchor.setCol1(10);
        inputImageAnchor.setRow1(rowNum - 1);
        inputImageAnchor.setCol2(11);
        inputImageAnchor.setRow2(rowNum);
        drawing.createPicture(inputImageAnchor, inputImagePictureID);
    }
}
