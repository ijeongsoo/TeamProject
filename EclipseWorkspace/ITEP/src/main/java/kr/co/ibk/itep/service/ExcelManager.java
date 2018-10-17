package kr.co.ibk.itep.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ibk.itep.dto.Edu001m;

public class ExcelManager {
	private String filePath;
	private ArrayList<Edu001m> eduList;
	
	public ExcelManager() {
		this.filePath = "";
		this.eduList = new ArrayList<Edu001m>();
	}

	// 생성자
	public ExcelManager(String filePath) {
		this.filePath = filePath;
		this.eduList = new ArrayList<Edu001m>();
		
		String ext = filePath.substring(filePath.lastIndexOf(".") + 1).trim();
		
		// 확장자별 read함수
        if ("xls".equals(ext)) {
            readExcelXLS();
        } else if ("xlsx".equals(ext)) {
            readExcelXLSX();
        }
	}
	

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ArrayList<Edu001m> getEduList() {
		return eduList;
	}

	public void setEduList(ArrayList<Edu001m> eduList) {
		this.eduList = eduList;
	}
	
	
	public void readExcelXLS() {
		if(this.filePath == null || this.filePath.equals("")) {
			return;
		}
		
		try {
            String[] tempFileName = this.filePath.split("/");
            String fileName = tempFileName[this.filePath.split("/").length - 1];
 
            //ctxRoot = this.filePath.substring(0, this.filePath.lastIndexOf("/"));
            File file = new File(this.filePath.trim());
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
 
            ExcelExtractor extractor = new ExcelExtractor(wb);
            extractor.setFormulasNotResults(true);
            extractor.setIncludeSheetNames(false);
 
            makeExcelToObject(wb.getSheetAt(0), "xls", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void readExcelXLSX() {
		if(this.filePath == null || this.filePath.equals("")) {
			return;
		}
		
		try {
            String[] tempFileName = this.filePath.split("/");
            String fileName = tempFileName[this.filePath.split("/").length - 1];
 
            //ctxRoot = this.filePath.substring(0, this.filePath.lastIndexOf("/"));
            File file = new File(this.filePath.trim());
 
            if (!file.exists() || !file.isFile() || !file.canRead()) {
                throw new IOException(this.filePath.trim());
            }
 
            XSSFWorkbook wb = new XSSFWorkbook(this.filePath.trim());
            XSSFExcelExtractor extractor = new XSSFExcelExtractor(wb);
            extractor.setFormulasNotResults(true);
            extractor.setIncludeSheetNames(false);
 
            makeExcelToObject(wb.getSheetAt(0), "xlsx", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	private void makeExcelToObject(Object sheet, String flag, String fileName) throws IOException {
        Row titles = null;

        // 시트 전체 처리하기 위한 반복문
        for (Row row : (flag.equals("xls") ? (HSSFSheet) sheet : (XSSFSheet) sheet)) {
            if (row.getRowNum() == 0) {
                titles = row;
                continue;
            }
            // 건건이 처리하는 함수
            updateData(titles, row);
        }
    }
	
	@Transactional
	private void updateData(Row titles, Row row) throws IOException {
		// 한 행별로 데이터 처리하는 함수, titles는 컬럼명 로우, row는 처리할 데이터 로우
        String colName = null;
        String colValue = null;
        Edu001m edu = new Edu001m();
 
        // FOR Section (S)
        for (Cell cell : row) {
            if (titles.getCell(cell.getColumnIndex()) == null || titles.getCell(cell.getColumnIndex()).equals("")) {
                break;
            }
 
            colName = titles.getCell(cell.getColumnIndex()).getRichStringCellValue().getString().trim();
 
            switch (cell.getCellType()) {
            case STRING:
                colValue = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    colValue = cell.getDateCellValue().toString();
                } else {
                    Long roundVal = Math.round(cell.getNumericCellValue());
                    Double doubleVal = cell.getNumericCellValue();
                    if (doubleVal.equals(roundVal.doubleValue())) {
                        colValue = String.valueOf(roundVal);
                    } else {
                        colValue = String.valueOf(doubleVal);
                    }
                }
                break;
            case BOOLEAN:
                colValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                colValue = cell.getCellFormula();
                break;
 
            default:
                colValue = "";
            }
 
            colValue = colValue.trim();
            
            if (!colValue.isEmpty()) {
            	// 컬럼명 별로 데이터 처리
            	switch (colName) {
            	case "COURSE_CD":
            		edu.setCOURSE_CD(colValue);
            		break;
            	case "ORG_CD":
            		edu.setORG_CD(colValue);
            		break;
            	case "CLS_CD":
            		edu.setCLS_CD(colValue);
            		break;
            	case "COURSE_NM":
            		edu.setCOURSE_NM(colValue);
            		break;
            	case "EDU_HOUR":
            		edu.setEDU_HOUR(Integer.parseInt(colValue));
            		break;
            	case "REG_ST_DT":
            		edu.setREG_ST_DT(colValue);
            		break;
            	case "REG_ED_DT":
            		edu.setREG_ED_DT(colValue);
            		break;
            	case "EDU_ST_DT":
            		edu.setEDU_ST_DT(colValue);
            		break;
            	case "EDU_ED_DT":
            		edu.setEDU_ED_DT(colValue);
            		break;
            	case "EDU_COST":
            		edu.setEDU_COST(Integer.parseInt(colValue));
            		break;
            	case "LOC":
            		edu.setLOCATION(colValue);
            		break;
            	case "REFUND_YN":
            		edu.setREFUND_YN(colValue);
            		break;
            	}
                // 생성된 객체 추가
                this.eduList.add(edu);
            }
        }
 
    }
}
