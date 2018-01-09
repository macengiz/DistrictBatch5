package com.districtbatch5.step_definitions;

import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.districtbatch5.pages.HomePage;
import com.districtbatch5.pages.UsedGearPage;
import com.districtbatch5.utilities.ConfigurationReader;
import com.districtbatch5.utilities.Driver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ExcelImportTest {

	WebDriver driver = Driver.getInstance();
	HomePage homepage = new HomePage();
	UsedGearPage gearpage = new UsedGearPage();

	@Given("^The user navigates to URL$")
	public void the_user_navigates_to_URL() throws Throwable {

		driver.get(ConfigurationReader.getProperty("url"));
	}

	@When("^The user clicks -Used Gear- tab$")
	public void the_user_clicks_Used_Gear_tab() throws Throwable {

		homepage.usedGearTab.click();
	}

	@Then("^The user captures all the data and throws into new created Excel sheet each time$")
	public void the_user_captures_all_the_data_and_throws_into_new_created_Excel_sheet_each_time() throws Throwable {

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(gearpage.dataTable));

		String excelPath = "./src/test/resources/com/districtbatch5/test_data/districtTest.xlsx";
		String sheetName = "Sheet1";

		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet workSheet = workBook.createSheet(sheetName);
		XSSFRow row = null;
		XSSFCell cell = null;

		for (int i = 1; i <= gearpage.headerSize.size(); i++) {
			WebElement headers = driver.findElement(By.xpath("//thead/tr/th[" + i + "]"));
			System.out.println(headers.getText());

			row = workSheet.getRow(0);
			if (row == null) {
				row = workSheet.createRow(0);
			}
			cell = row.getCell(i - 1);
			if (cell == null) {
				cell = row.createCell(i - 1);
			}

			cell.setCellValue(headers.getText());
		}

		int rowSizeCount = gearpage.rowSize.size();

		for (int i = 1; i <= rowSizeCount; i++) {

			for (int j = 1; j <= gearpage.headerSize.size(); j++) {

				WebElement allItem = gearpage.dataTable.findElement(By.xpath("//tr[" + i + "]/td[" + j + "]"));

				row = workSheet.getRow(i);
				if (row == null) {
					row = workSheet.createRow(i);
				}
				cell = row.getCell(j - 1);
				if (cell == null) {
					cell = row.createCell(j - 1);
				}

				cell.setCellValue(allItem.getText());
			}

		}
		FileOutputStream output = new FileOutputStream(excelPath);
		workBook.write(output);
		output.close();
		workBook.close();
	}
}
