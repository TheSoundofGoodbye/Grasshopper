package service;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import common.JDBCTemplate;
import dao.OfficialDao;
import dao.OfficialDaoImpl;
import dto.Official;
import util.Paging;

public class OfficialServiceImpl implements OfficialService{
	
	//OfficialDao 객체 생성
	private OfficialDao officialDao = new OfficialDaoImpl();
	
	@Override
	public Paging getPaging(HttpServletRequest req) {
		
		//전달 파라미터 curPage 파싱
		String param = req.getParameter("curPage");
		int curPage = 0;
		if(param != null && !"".equals(param)) {
			curPage = Integer.parseInt(param);
		} else {
			System.out.println("[CAUTION] curPage값이 null 또는 비어있습니다");
		}
		
		//Offical 테이블의 총 데이터 수(레시피 숫자)를 조회
		int totalCount = officialDao.selectCntAll(JDBCTemplate.getConnection());
		
		//Paging 객체 생성
		Paging paging = new Paging(totalCount, curPage);
		
		return paging;				
	}
	
	@Override
	public List<Official> getList(Paging paging) {

		return officialDao.selectAll(JDBCTemplate.getConnection(), paging);
	}
	
	@Override
	public Official getOfficial_no(HttpServletRequest req) {
		
		//Official 객체 생성
		Official officialno = new Official();
		
		//officialno 전달 파라미터 검증 - null or ""
		String param = req.getParameter("official_no");
		if(param != null && !"".equals(param)) {
			//official_no 전달 파라미터 추출
			officialno.setOfficial_cocktail_no( Integer.parseInt(param));
		}
		
		//결과 Official 객체 반환
		return officialno;
	}
	
	@Override
	public Official view(Official official_no) {
		
		Connection connection = JDBCTemplate.getConnection();
		
		//[비활성] 조회수 증가 
		if( officialDao.updateHit(connection, official_no) == 1 ) {
			JDBCTemplate.commit(connection);
		} else {
			JDBCTemplate.rollback(connection);
		}
		
		//레시피 조회
		Official official = officialDao.selectOfficialByOfficialno(connection, official_no);
		
		return official;
	}
	
	
	
	
}
