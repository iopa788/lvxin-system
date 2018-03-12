/**
 * Copyright 2013-2033 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.lvxin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.farsunset.lvxin.exception.EmptyExcelFileException;
import com.farsunset.lvxin.exception.IllegalNullArgumentException;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.User;

@SuppressWarnings("resource")
public class TemplateTools {
	public static List<Organization> parseOrganizationList(MultipartFile excelFile) throws IOException {
		List<Organization> list = new ArrayList<Organization>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile.getInputStream());
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		int rowCount = xssfSheet.getLastRowNum();
		if (rowCount < 1) {
			throw new EmptyExcelFileException();
		}
		for (int row = 1; row <= rowCount; row++) {
			XSSFRow xssfRow = xssfSheet.getRow(row);
			Organization model = new Organization();
			if (xssfRow.getCell(0) == null || xssfRow.getCell(1) == null) {
				throw new IllegalNullArgumentException();
			}
			xssfRow.getCell(0).setCellType(CellType.STRING);
			xssfRow.getCell(1).setCellType(CellType.STRING);
			model.setCode(xssfRow.getCell(0).getStringCellValue().trim());
			model.setName(xssfRow.getCell(1).getStringCellValue().trim());
			if (xssfRow.getCell(2) != null) {
				xssfRow.getCell(2).setCellType(CellType.STRING);
				String parentCode = xssfRow.getCell(2).getStringCellValue();
				if (StringUtils.isNotBlank(parentCode)) {
					model.setParentCode(parentCode.trim());
				}
			}
			list.add(model);
		}

		IOUtils.closeQuietly(xssfWorkbook);

		return list;
	}

	public static List<User> parseUserList(MultipartFile excelFile) throws IOException {
		List<User> list = new ArrayList<User>();
		String password = DigestUtils.md5Hex(Constants.DEF_PASSWORD);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile.getInputStream());
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		int rowCount = xssfSheet.getLastRowNum();
		if (rowCount < 1) {
			throw new EmptyExcelFileException();
		}
		for (int row = 1; row <= rowCount; row++) {
			XSSFRow xssfRow = xssfSheet.getRow(row);
			User model = new User();
			if (xssfRow.getCell(0) == null || xssfRow.getCell(1) == null) {
				throw new IllegalNullArgumentException();
			}
			xssfRow.getCell(0).setCellType(CellType.STRING);
			xssfRow.getCell(1).setCellType(CellType.STRING);
			model.setAccount(xssfRow.getCell(0).getStringCellValue().trim());
			model.setName(xssfRow.getCell(1).getStringCellValue().trim());
			if (xssfRow.getCell(2) != null) {
				xssfRow.getCell(2).setCellType(CellType.STRING);
				model.setGender(xssfRow.getCell(2).getStringCellValue().trim());
			}

			if (xssfRow.getCell(3) != null) {
				xssfRow.getCell(3).setCellType(CellType.STRING);
				model.setTelephone(xssfRow.getCell(3).getStringCellValue().trim());
			}

			if (xssfRow.getCell(4) != null) {
				xssfRow.getCell(4).setCellType(CellType.STRING);
				model.setEmail(xssfRow.getCell(4).getStringCellValue().trim());
			}

			if (xssfRow.getCell(5) != null) {
				xssfRow.getCell(5).setCellType(CellType.STRING);

				String orgCode = xssfRow.getCell(5).getStringCellValue();
				if (StringUtils.isNotBlank(orgCode)) {
					model.setOrgCode(orgCode.trim());
				}
			}

			model.setPassword(password);
			list.add(model);
		}

		IOUtils.closeQuietly(xssfWorkbook);

		return list;

	}

}
