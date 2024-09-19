package com.coderman.service.util;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.util.ResultUtil;
import com.coderman.api.vo.ExportPartVO;
import com.coderman.api.vo.ResultVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    private static <T> ResultVO<List<T>> parse(Class<T> clazz, FileInputStream fileInputStream, Integer titleRow, String excelType) throws Exception {
        return ExcelUtil.parse(clazz, fileInputStream, titleRow + 1, true, excelType, TEMPLATE_SHEET_INDEX);
    }


    /**
     * 解析没有标题的excel
     *
     * @param clazz
     * @param excelFile
     * @param dataRow
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ResultVO<List<T>> parseWithoutTitle(Class<T> clazz, File excelFile, Integer dataRow) throws Exception {

        String excelType = excelFile.getName().substring(excelFile.getName().lastIndexOf("."));

        if (!EXCEL_SUFFIX_XLS.equals(excelType) && !EXCEL_SUFFIX_XLSX.equals(excelType)) {

            return ResultUtil.getList(null, ResultConstant.RESULT_CODE_402, "文件后缀名错误", null);
        }

        return ExcelUtil.parseWithoutTitle(clazz, new FileInputStream(excelFile), dataRow, excelType);
    }


    public static <T> ResultVO<List<T>> parseWithoutTitle(Class<T> clazz, FileInputStream fileInputStream, Integer dataRow, String excelType) throws Exception {

        return ExcelUtil.parse(clazz, fileInputStream, dataRow, false, excelType, TEMPLATE_SHEET_INDEX);
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
    private static <T> ResultVO<List<T>> parse(Class<T> clazz, FileInputStream fileInputStream, int dataRow, boolean isHasTitle, String excelType, Integer sheetNumber) throws Exception {

        Workbook workbook = null;

        try {

            if (fileInputStream.getChannel().size() > EXCEL_MAX_SIZE) {

                return ResultUtil.getList(clazz, ResultConstant.RESULT_CODE_402, "文件大小不能超过20m", null);
            }

            // 2003
            if (EXCEL_SUFFIX_XLS.equals(excelType)) {

                workbook = new HSSFWorkbook(fileInputStream);
            } else {

                workbook = new XSSFWorkbook(fileInputStream);
            }

            Sheet sheet = workbook.getSheetAt(sheetNumber);

            // 根据标题行获取类Set方法集合
            List<Method> methodList = ExcelUtil.getClassSetMethodList(clazz, sheet, dataRow - 1, isHasTitle);

            List<Integer> requiredColumnList = new ArrayList<>();

            if (isHasTitle) {

                requiredColumnList.addAll(ExcelUtil.getRequiredColumnList(sheet, dataRow - 1));
            }

            // 根据获取类中时间字段格式化集合
            Map<Integer, String> dateFormatMap = ExcelUtil.getDateFieldPattern(clazz);


            // 解析结果集合
            List<T> resultList = new ArrayList<>();

            for (int rowIndex = dataRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                if (row == null) {

                    continue;
                }

                int columnIndex = DATA_START_COLUMN;

                T object = clazz.newInstance();

                for (Method method : methodList) {


                    String errorMessage = StringUtils.EMPTY;

                    String cellValue = ExcelUtil.getCellStringValue(row.getCell(columnIndex));

                    if (requiredColumnList.contains(columnIndex) && StringUtils.isBlank(cellValue)) {

                        errorMessage = "第" + (rowIndex + 1) + "行第" + (columnIndex + 1) + "列数据不能为空";
                        return ResultUtil.getList(clazz, ResultConstant.RESULT_CODE_402, errorMessage, null);
                    }

                    if (StringUtils.isBlank(cellValue)) {

                        columnIndex++;
                        continue;
                    }

                    Class fieldType = method.getParameterTypes()[0];

                    // 布尔
                    if (fieldType.equals(Boolean.class)) {

                        try {
                            method.invoke(object, new Boolean(cellValue));
                        } catch (Exception e) {
                            errorMessage = "第" + (rowIndex + 1) + "行第" + (columnIndex + 1) + "列数据不是布尔类型";
                        }
                    }

                    // 整型
                    if (fieldType.equals(Integer.class)) {

                        try {
                            if (isInteger(cellValue)) {

                                method.invoke(object, new BigDecimal(cellValue).intValue());
                            } else {
                                errorMessage = "第" + (rowIndex + 1) + "行第" + (columnIndex + 1) + "列数据不是整数类型";
                            }
                        } catch (Exception e) {
                            errorMessage = "第" + (rowIndex + 1) + "行第" + (columnIndex + 1) + "列数据不是整数类型";
                        }
                    }

                    // 小数
                    if (fieldType.equals(BigDecimal.class)) {

                        try {

                            method.invoke(object, new BigDecimal(cellValue));

                        } catch (Exception e) {
                            errorMessage = "第" + (rowIndex + 1) + "行第" + (columnIndex + 1) + "列数据不是小数类型";
                        }
                    }

                    // 字符串
                    if (fieldType.equals(String.class)) {

                        method.invoke(object, cellValue);
                    }

                    // 时间
                    if (fieldType.equals(Date.class)) {

                        try {

                            method.invoke(object, DateUtils.parseDate(cellValue, dateFormatMap.get(columnIndex)));

                        } catch (Exception e) {
                            errorMessage = "第" + (rowIndex + 1) + "行第" + (columnIndex + 1) + "列数据不是时间类型";
                        }
                    }

                    // 有错误直接返回
                    if (StringUtils.isNotBlank(errorMessage)) {

                        return ResultUtil.getList(clazz, ResultConstant.RESULT_CODE_402, errorMessage, null);
                    }

                    columnIndex++;
                }

                resultList.add(object);
            }

            return ResultUtil.getSuccessList(clazz, resultList);

        } catch (Exception e) {

            throw e;
        } finally {

            if (workbook != null) {

                workbook.close();
            }
        }
    }


    public static <T> void export(List<String> hiddenDataList, Integer startRow, List<T> dataList, String templateFileName) throws Exception {
        ExcelUtil.export(hiddenDataList, startRow, dataList, templateFileName, templateFileName);
    }

    public static <T> void export(List<String> hiddenDataList, Integer startRow, List<T> dataList, String templateFileName, String downloadFileName) throws Exception {
        ExcelUtil.export(hiddenDataList, startRow, dataList, templateFileName, downloadFileName, null);
    }

    private static <T> void export(List<String> hiddenDataList, Integer startRow, List<T> ruleDataList, String templateFileName, String downloadFileName, Integer partSheetNumber) throws Exception {

        List<List<T>> partDataList = null;

        if (partSheetNumber != null && partSheetNumber > 0) {

            partDataList = ListUtils.partition(ruleDataList, partSheetNumber);
        } else {

            partDataList = Arrays.asList(ruleDataList);
        }


        List<ExportPartVO<T>> exportPartVOList = new ArrayList<>();

        for (List<T> partData : partDataList) {

            ExportPartVO exportPartVO = new ExportPartVO<>();

            exportPartVO.setHiddenDataList(hiddenDataList);
            exportPartVO.setRuleDataList(partData);
            exportPartVOList.add(exportPartVO);
        }

        ExcelUtil.export(exportPartVOList, startRow, templateFileName, downloadFileName);
    }


    public static <T> void export(List<ExportPartVO<T>> exportPartVOList, Integer startRow, String templateFileName, String downloadFileName) throws Exception {

        Workbook workbook = null;

        try {

            String fileSuffix = EXCEL_SUFFIX_XLS;

            InputStream inputStream = ExcelUtil.class.getResourceAsStream("/" + TEMPLATE_FILE_DIRECTORY + "/" + templateFileName + fileSuffix);

            if (inputStream != null) {

                workbook = new HSSFWorkbook(inputStream);
            } else {

                fileSuffix = EXCEL_SUFFIX_XLSX;
                inputStream = ExcelUtil.class.getResourceAsStream("/" + TEMPLATE_FILE_DIRECTORY + "/" + templateFileName + fileSuffix);

                if (inputStream == null) {

                    throw new NullPointerException("模板文件为空!");
                }

                workbook = new XSSFWorkbook(inputStream);
            }

            int sheetCount = workbook.getNumberOfSheets();

            for (int i = 0; i < sheetCount; i++) {
                workbook.setSheetHidden(i, true);
            }

            int sheetNumber = ACTIVE_SHEET_INDEX;

            for (ExportPartVO<T> exportPartVO : exportPartVOList) {

                Sheet exportSheet = workbook.cloneSheet(TEMPLATE_SHEET_INDEX);

                workbook.setSheetOrder(exportSheet.getSheetName(), sheetNumber);
                workbook.setSheetName(sheetNumber, downloadFileName + "(" + sheetNumber + ")");

                ExcelUtil.writeSheet(exportSheet, startRow, exportPartVO.getHiddenDataList(), exportPartVO.getRuleDataList());

                sheetNumber++;
            }

            workbook.setActiveSheet(ACTIVE_SHEET_INDEX);

            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();

            assert response != null;
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + new String((downloadFileName + fileSuffix).getBytes(), StandardCharsets.ISO_8859_1));

        } catch (Exception e) {

            throw e;
        } finally {

            if (workbook != null) {

                workbook.close();
            }
        }


    }

    private static <T> void writeSheet(Sheet sheet, Integer startDataRow, List<String> hiddenDataList, List<T> ruleDataList) throws Exception {

        CellStyle cellBorderStyle = sheet.getWorkbook().createCellStyle();

        cellBorderStyle.setBorderBottom(BorderStyle.THIN);
        cellBorderStyle.setBorderLeft(BorderStyle.THIN);
        cellBorderStyle.setBorderTop(BorderStyle.THIN);
        cellBorderStyle.setBorderRight(BorderStyle.THIN);

        Font font = ExcelUtil.getSheetFont(sheet);

        if (font != null) {
            cellBorderStyle.setFont(font);
        }

        ExcelUtil.writeRow(sheet, HIDDEN_DATA_ROW, hiddenDataList, cellBorderStyle);

        ExcelUtil.hiddenRow(sheet, HIDDEN_DATA_ROW);

        if (CollectionUtils.isNotEmpty(ruleDataList)) {

            int exportRowNumber = startDataRow;


        }


    }

    private static void hiddenRow(Sheet sheet, Integer row) {

        Row rowContent = sheet.getRow(row);
        if (rowContent != null) {

            rowContent.setZeroHeight(true);
        }
    }

    private static void writeRow(Sheet sheet, Integer row, List<String> contentList, CellStyle cellStyle) {

        if (CollectionUtils.isEmpty(contentList)) {

            return;
        }

        Row rowContent = sheet.getRow(row);
        if (rowContent == null) {

            rowContent = sheet.createRow(row);
        }

        int columnIndex = DATA_START_COLUMN;

        for (String content : contentList) {

            Cell cell = rowContent.getCell(columnIndex);

            if (cell == null) {

                cell = rowContent.createCell(columnIndex);
            }

            cell.setCellType(CellType.STRING);

            if (StringUtils.isNotBlank(content)) {

                cell.setCellValue(content);
            }

            cell.setCellStyle(cellStyle);

            columnIndex++;
        }
    }

    private static Font getSheetFont(Sheet sheet) {

        for (int rowIndex = HIDDEN_DATA_ROW + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            for (int columnIndex = DATA_START_COLUMN; columnIndex < row.getLastCellNum(); columnIndex++) {

                Cell cell = row.getCell(columnIndex);

                if (cell != null) {

                    return sheet.getWorkbook().getFontAt(cell.getCellStyle().getFontIndexAsInt());
                }
            }
        }

        return null;
    }


    private static <T> Map<Integer, String> getDateFieldPattern(Class<T> clazz) {

        Map<Integer, String> dateFormatMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            field.setAccessible(true);

            DateTimeFormat dateTimeFormat = field.getAnnotation(DateTimeFormat.class);

            if (dateTimeFormat != null) {

                dateFormatMap.put(i, dateTimeFormat.pattern());
            }
        }

        return dateFormatMap;
    }

    private static List<Integer> getRequiredColumnList(Sheet sheet, int titleRow) {

        List<Integer> requiredList = new ArrayList<>();

        Row row = sheet.getRow(titleRow);

        if (row != null) {

            for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {

                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    continue;
                }

                if (cell.getCellComment() != null && DATA_REQUIRE_FLAG.equals(cell.getCellComment().getString().getString().trim())) {

                    requiredList.add(columnIndex);
                }
            }
        }

        return requiredList;
    }

    /**
     * 获取字段的set方法
     *
     * @param clazz
     * @param sheet
     * @param titleRow
     * @param isHasTitle
     * @param <T>
     * @return
     */
    private static <T> List<Method> getClassSetMethodList(Class<T> clazz, Sheet sheet, int titleRow, boolean isHasTitle) {

        List<Method> methodList = new ArrayList<>();

        Map<String, Method> methodMap = new HashMap<>();

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {

            methodMap.put(method.getName(), method);
        }

        if (isHasTitle) {

            List<String> titleNameList = new ArrayList<>();

            Row row = sheet.getRow(titleRow);

            if (row != null) {

                for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {

                    Cell cell = row.getCell(columnIndex);

                    String cellValue = ExcelUtil.getCellStringValue(cell);

                    if (cell != null && StringUtils.isNotBlank(cellValue)) {

                        titleNameList.add(cellValue);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(titleNameList)) {

                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {

                    field.setAccessible(true);
                    String titleName = field.getAnnotation(ApiModelProperty.class).value();

                    if (titleNameList.contains(titleName)) {

                        methodList.add(methodMap.get("set" + StringUtils.capitalize(field.getName())));
                    }
                }
            }
        } else {

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                field.setAccessible(true);
                methodList.add(methodMap.get("set" + StringUtils.capitalize(field.getName())));
            }
        }

        return methodList;
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
