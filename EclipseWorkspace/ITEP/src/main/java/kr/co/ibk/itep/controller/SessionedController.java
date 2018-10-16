﻿package kr.co.ibk.itep.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import kr.co.ibk.itep.dto.Ath001m;
import kr.co.ibk.itep.dto.EduJoinedEcd;
import kr.co.ibk.itep.service.Service;
import kr.co.ibk.itep.dto.JoinForEdulist;



@Controller
@SessionAttributes("login_info")
public class SessionedController {
	private static final Logger logger = LoggerFactory.getLogger(SessionedController.class);
	
	@Autowired
	private Service service;
	
	@Autowired
	private ServletContext servletContext;
	
	
	@RequestMapping("/home")
	public String home( Model model) {
		ArrayList<EduJoinedEcd> top8List = new ArrayList<>();
		ArrayList<EduJoinedEcd> top8List1 = new ArrayList<>();
		ArrayList<EduJoinedEcd> top8List2 = new ArrayList<>();
		ArrayList<EduJoinedEcd> ddayList = new ArrayList<>();
		top8List = service.getTop8Edu();
		
		for(int i=0; i<8; i++){
			if( i/4 < 1){
				top8List1.add(top8List.get(i));
			}else{
				top8List2.add(top8List.get(i));
			}
		}
		
		ddayList = service.getDDayEdu();
		
		model.addAttribute("top8List1", top8List1);
		model.addAttribute("top8List2", top8List2);

		model.addAttribute("ddayList", ddayList);
		

		return "home";
	} 
	
	
	@RequestMapping("/file")
	public void download(String svr_img_file_nm, String img_file_type, HttpServletResponse r,
			@RequestHeader("User-Agent") String userAgent) throws IOException {
		logger.info(svr_img_file_nm);
		String fileName = svr_img_file_nm;
		String encFileName;
		if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
			encFileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			encFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		r.addHeader("Content-Disposition", "attachment; filename=\"" + encFileName + "\"");
		// 2. 파일의 종류
		r.addHeader("Content-Type", img_file_type);
		// 3. 파일의 크기(옵션)
		r.addHeader(fileName, fileName);
		File file = null;

		file = new File(servletContext.getRealPath("/WEB-INF/upload/eduImg/" + fileName));

		long fileSize = file.length();
		r.addHeader("Content-Length", String.valueOf(fileSize));

		OutputStream os = r.getOutputStream();
		FileInputStream fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, os);
		os.flush();
		fis.close();
		os.close();

	}

	@RequestMapping("/admin")
	public String admin(String ssoid, Model model) {
		model.addAttribute("ssoid", ssoid);
		return "admin";
	} 
	
	@RequestMapping("/authority")
	public ModelAndView list(@RequestParam(defaultValue="title")String searchOption, @RequestParam(defaultValue="") String keyword) {
		ModelAndView mv = new ModelAndView();
		
		List<Ath001m> list = service.selectAuthorityList(searchOption, keyword);
		
		HashMap<String, Object> map = new HashMap<String, Object>(); 
		map.put("list", list);
		map.put("searchOption", searchOption);
		map.put("keyword", keyword);
		mv.addObject("map", map);
		mv.setViewName("/authority");
		return mv;
	} 

	@RequestMapping("/EduList")
	public String EduList(String ssoid, Model model) {
		try{
			
			List<JoinForEdulist> joinForEdulist = service.selectEdulist();
				model.addAttribute("edu_list", joinForEdulist);
				model.addAttribute("ssoid", ssoid);
				return "EduList";
		}catch(Exception e){
			logger.error(e.getStackTrace().toString());
			model.addAttribute("result", 1);
			return "error";
		}
	}

	@RequestMapping("/eduUploadExcel")
	public String eduUploadExcel(Model model) {
		return "eduUploadExcel";
	} 
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		return "dashboard";
	} 
	
}
