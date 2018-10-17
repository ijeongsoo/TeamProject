package kr.co.ibk.itep.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.ibk.itep.dao.AdminApprovalDao;
import kr.co.ibk.itep.dto.*;

@Component
public class AdminApprovalServiceImpl implements AdminApprovalService{

	//private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);
	@Autowired
	private AdminApprovalDao admindao;

	
	@Override
	public ArrayList<EduApproval> selectAllApprovalList(){
		
		List<EduApproval> eduList = admindao.selectAllApprovalList();
		
		return (ArrayList<EduApproval>)eduList;
	}
	
}
