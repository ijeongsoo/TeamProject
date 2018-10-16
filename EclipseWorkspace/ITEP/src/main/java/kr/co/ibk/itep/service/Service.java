package kr.co.ibk.itep.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kr.co.ibk.itep.dto.Ath001m;
import kr.co.ibk.itep.dto.EduJoinedEcd;
import kr.co.ibk.itep.dto.EmpJoinedDep;


@Component
public interface Service {

	public EmpJoinedDep ssoLogin(String emn);

	public void empFileUploader(String originPath, String newPath);

	public void briFileUploader(String originPath, String newPath);

	public ArrayList<EduJoinedEcd> getTop8Edu();

	public ArrayList<EduJoinedEcd> getDDayEdu();

	public List<Ath001m> selectAuthorityList(String searchOption, String keyword);

}
