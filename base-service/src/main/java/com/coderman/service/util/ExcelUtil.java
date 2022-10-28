package com.coderman.service.util;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ResultVO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class ExcelUtil {

    /**
     * 默认标题行号
     */
    public static final Integer DEFAULT_TITLE_ROW = 0;


    /**
     * 隐藏数据行号
     */
    private static final Integer HIDDEN_DATA_ROW = 0;


    /**
     * 数据列号
     */
    private static final Integer DATA_START_COLUMN = 0;

    /**
     * 模板工作索引
     */
    private static final Integer TEMPLATE_SHEET_INDEX = 0;

    /**
     * 活动工作表索引
     */
    private static final Integer ACTIVE_SHEET_INDEX = 1;

    /**
     * 数据必填标识
     */
    private static final String DATA_REQUIRE_FLAG = "必填";

    /**
     * 后缀名 - xlsx
     */
    private static final String EXCEL_SUFFIX_XLSX = ".xlsx";

    /**
     * 后缀名 - xls
     */
    private static final String EXCEL_SUFFIX_XLS = ".xls";

    /**
     * 模板文件目录
     */
    private static final String TEMPLATE_FILE_DIRECTORY = "template";

    /**
     * 文件最大20M
     */
    public static final Integer EXCEL_MAX_SIZE = 20 * 1024 * 1024;


    /**
     * 是否整数正则
     */
    private final static Pattern integerPattern = Pattern.compile("(^-?[0-9]+$)|(^-?[0-9]+.[0]+$)");


    /**
     * 判断字符串是否是整数
     *
     * @param str 字符串
     * @return
     */
    public static Boolean isInteger(String str) {

        return integerPattern.matcher(str).find();
    }


    /**
     * 解析excel
     *
     * @param excelFile excel文件
     * @return
     * @throws Exception
     */
    public static ResultVO<List<List<String>>> parse(File excelFile) throws Exception {


        String excelType = excelFile.getName().substring(excelFile.getName().lastIndexOf("."));

        if (!EXCEL_SUFFIX_XLSX.equals(excelType) && !EXCEL_SUFFIX_XLS.equals(excelType)) {

            return ResultUtil.getList(null, ResultConstant.RESULT_CODE_402, "文件后缀名不正确", null);
        }

        return ExcelUtil.parse(new FileInputStream(excelFile), excelType);
    }

    /**
     * 解析excel
     *
     * @param fileInputStream 文件输入流
     * @param excelType       文件类型
     * @return
     */
    private static ResultVO<List<List<String>>> parse(FileInputStream fileInputStream, String excelType) throws Exception {

        Workbook workbook = null;

        try {
            if (fileInputStream.getChannel().size() > EXCEL_MAX_SIZE) {

                return ResultUtil.getList(null, ResultConstant.RESULT_CODE_402, "文件大小不能超过20M", null);
            }

            if (EXCEL_SUFFIX_XLS.equals(excelType)) {

                workbook = new HSSFWorkbook(fileInputStream);
            } else {

                workbook = new XSSFWorkbook(fileInputStream);
            }

            Sheet sheet = workbook.getSheetAt(TEMPLATE_SHEET_INDEX);


            // 解析结果集
            List<List<String>> resultList = new ArrayList<>();

            // 最大有效数据列号

            int maxColumnIndex = 0;

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                if (row != null && row.getLastCellNum() > maxColumnIndex) {

                    maxColumnIndex = row.getLastCellNum();
                }
            }

            // 每一行都作为数据内容行
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                Row row = sheet.getRow(rowIndex);
                if (row == null) {

                    continue;
                }

                List<String> rowContent = new ArrayList<>();

                for (int columnIndex = DATA_START_COLUMN; columnIndex < maxColumnIndex; columnIndex++) {

                    rowContent.add(ExcelUtil.getCellStringValue(row.getCell(columnIndex)));
                }
                resultList.add(rowContent);
            }

            return ResultUtil.getSuccess(null, resultList);

        } catch (Exception e) {

            throw e;

        } finally {

            if (workbook != null) {

                workbook.close();
            }
        }

    }

    /**
     * 解析excel
     *
     * @param clazz
     * @param excelFile
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ResultVO<List<T>> parse(Class<T> clazz, File excelFile) throws Exception {
        return ExcelUtil.parse(clazz, excelFile, DEFAULT_TITLE_ROW);
    }

    /**
     * 解析excel
     *
     * @param clazz
     * @param excelFile
     * @param titleRow
     * @param <T>
     * @return
     */
    private static <T> ResultVO<List<T>> parse(Class<T> clazz, File excelFile, Integer titleRow) throws Exception {

        String excelType = excelFile.getName().substring(excelFile.getName().lastIndexOf("."));

        if (!EXCEL_SUFFIX_XLS.equals(excelType) && !EXCEL_SUFFIX_XLSX.equals(excelType)) {

            return ResultUtil.getList(null, ResultConstant.RESULT_CODE_402, "文件后缀名错误", null);
        }

        return ExcelUtil.parse(clazz, new FileInputStream(excelFile), titleRow, excelType);
    }


    /**
     * 解析excel
     *
     * @param clazz
     * @param fileInputStream
     * @param titleRow
     * @param excelType
     * @param <T>
     * @return
     */
    private static <T> ResultVO<List<T>> parse(Class<T> clazz, FileInputStream fileInputStream, Integer titleRow, String excelType) {
        return ExcelUtil.parse(clazz, fileInputStream, titleRow + 1, true, excelType, TEMPLATE_SHEET_INDEX);
    }

    /**
     * 解析excel
     *
     * @param clazz
     * @param fileInputStream
     * @param dataRow
     * @param isHasTitle
     * @param excelType
     * @param sheetNumber
     * @param <T>
     * @return
     */
    private static <T> ResultVO<List<T>> parse(Class<T> clazz, FileInputStream fileInputStream, int dataRow, boolean isHasTitle, String excelType, Integer sheetNumber) {
        return null;
    }


    /**
     * 获取单元格内容
     *
     * @param cell
     * @return
     */
    private static String getCellStringValue(Cell cell) {

        if (cell == null) {
            return StringUtils.EMPTY;
        }

        String cellValue = StringUtils.EMPTY;

        CellType cellType = cell.getCellType();

        // 空白或者错误
        if (cellType == CellType._NONE || cellType == CellType.BLANK) {

            cellValue = StringUtils.EMPTY;
        }

        if (cellType == CellType.NUMERIC) {

            cellValue = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toString();
        }

        if (cellType == CellType.STRING) {

            cellValue = cell.getStringCellValue().trim();
        }

        if (cellType == CellType.BOOLEAN) {

            cellValue = Boolean.toString(cell.getBooleanCellValue()).trim();
        }

        if (cellType == CellType.FORMULA) {

            cellValue = cell.getCellFormula().trim();
        }

        return cellValue;

    }
}
