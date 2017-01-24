//package com.show.blue;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//
//public class ExcelUtil {
//    //默认单元格内容为数字时格式
//    private DecimalFormat df = new DecimalFormat("0");
//    // 默认单元格格式化日期字符串
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    // 格式化数字
//    private DecimalFormat nf = new DecimalFormat("0");
//
//
//    ArrayList<XSSFSheet> opnexlsx(String xlsxpath) {
//        File file = new File(xlsxpath);
//        XSSFWorkbook wb = null;
//        ArrayList<XSSFSheet> Xss = new ArrayList<XSSFSheet>();
//        try {
//            wb = new XSSFWorkbook(new FileInputStream(file));
//            if (wb == null || !file.getName().endsWith("xlsx")) {
//                return null;
//            }
//            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
//                Xss.add(wb.getSheetAt(i));
//            }
//            return Xss;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /*
//     * @return 将返回结果存储在ArrayList内，存储结构与二位数组类似
//     * lists.get(0).get(0)表示过去Excel中0行0列单元格
//     */
//
//    public final ArrayList<Pack> readExcel2007(XSSFSheet sheet, int st) {
//        ArrayList<Pack> rowList = new ArrayList<Pack>();
//        try {
//            XSSFRow row;
//            XSSFCell cell;
//            String value;
//            row = sheet.getRow(1);
//            int j = -1;
//
//            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
//                value = CellType(row.getCell(i));
//                if (value != null && (value.equals("渠道ID") || value.equals("渠道号"))) {
//                    j = i;
//                    break;
//                }
//            }
//            if (j < 0) {
//                return rowList;
//            }
//
//            for (int i = 2, rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
//                row = sheet.getRow(i);
//
//                if (row == null) {
//                    break;
//                } else {
//                    rowCount++;
//                }
//
////                for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
//                cell = row.getCell(j);
//                if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
//                    break;
//                }
//                value = CellType(cell);
//                if (value != null) {
//                    Pack pack = new Pack(value, "" + st);
//                    rowList.add(pack);
//                    if (rowList.size()==1){
//                        return rowList;
//                    }
//
//                } else {
//                    return rowList;
//                }
//            }//end for i
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("exception");
//        }
//        return rowList;
//    }
//
//    String CellType(Cell cell) {
//        String value = null;
//        if (cell == null) {
//            return value;
//        }
//        switch (cell.getCellType()) {
//            case XSSFCell.CELL_TYPE_STRING:
////            System.out.println(i + "行" + j + " 列 is String type");
//                value = cell.getStringCellValue();
//                break;
//            case XSSFCell.CELL_TYPE_NUMERIC:
//                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
//                    value = df.format(cell.getNumericCellValue());
//                } else if ("General".equals(cell.getCellStyle()
//                        .getDataFormatString())) {
//                    value = nf.format(cell.getNumericCellValue());
//                } else {
//                    value = sdf.format(HSSFDateUtil.getJavaDate(cell
//                            .getNumericCellValue()));
//                }
////            System.out.println(i + "行" + j
////                    + " 列 is Number type ; DateFormt:"
////                    + value.toString());
//                break;
//            case XSSFCell.CELL_TYPE_BOOLEAN:
////            System.out.println(i + "行" + j + " 列 is Boolean type");
////              Boolean.valueOf(cell.getBooleanCellValue());
//                break;
//            case XSSFCell.CELL_TYPE_BLANK:
////            System.out.println(i + "行" + j + " 列 is Blank type");
////                value = "";
//                break;
//            default: {
//
//            }
////            System.out.println(i + "行" + j + " 列 is default type");
////                value = cell.toString();
//        }// end switch
//        return value;
//    }
//
//
//}