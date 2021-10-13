package custom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.JDBCTemplate;
import custom.dto.Custom;
import custom.dto.Custom;
import util.Paging;

public class CustomDaoImpl implements CustomDao{
	
	PreparedStatement ps = null; //SQL 수행객체 생성
	ResultSet rs = null; //결과값을 담을 객체 생성
	
	@Override
	public int selectCntAll(Connection connection) {
		
		String sql = "";
		sql += "SELECT count(*) FROM custom_board";
		
		//총 레시피 숫자
		int cnt = 0;
		
		try {
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				cnt = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return cnt;
	}
	
	@Override
	public int selectCntSearchAll(Connection connection, String search) {
		
		//총 레시피 숫자
		int cnt = 0;
		
		String sql = "";
		sql += "SELECT count(*) FROM custom_board";
		sql += " WHERE 1=1 AND";
		sql += "      upper(custom_board_title) LIKE upper(?)";
		sql += "    OR upper(custom_board_content) LIKE upper(?)";

		try {
			ps = connection.prepareStatement(sql);

			//변수 채우기
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");

			rs = ps.executeQuery();

			while(rs.next()) {
				cnt = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}

		return cnt;
	}
	
	public int selectCntSearch(Connection connection, String search, String category) {
				
		//총 레시피 숫자
		int cnt = 0;
		
		String sql = "";
		sql += "SELECT count(*) FROM custom_board C";
		sql += " JOIN user_info U ON U.user_no = C.user_no";
		sql += " WHERE 1=1 ";
		if( "title".equals(category) ) {
			sql += "    AND upper(C.custom_board_title) ";
		} else if ("detail".equals(category) ) {
			sql += "    AND upper(C.custom_board_content) ";
		} else { //if (category == "nickname" )
			sql += "    AND upper(U.user_nickname) ";
		}
		sql += " Like upper(?)";
		
		
		try {
			ps = connection.prepareStatement(sql);

			//변수 채우기
			ps.setString(1, "%" + search + "%");

			rs = ps.executeQuery();

			while(rs.next()) {
				cnt = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}

		return cnt;
	}
	
	@Override
	public List<Custom> selectAll(Connection connection, Paging paging) {
		
		String sql = ""; //SQL 작성
		sql += "SELECT * FROM (";
		sql += "  SELECT ROWNUM rnum, O.* FROM (";
		sql += "    SELECT custom_board_no, BOARD_TYPE, C.USER_NO, U.USER_NICKNAME, ATTACH_NO, CUSTOM_BOARD_TITLE, CUSTOM_BOARD_CONTENT, custom_board_date, custom_board_hit, custom_board_vote";
		sql += "	  FROM CUSTOM_BOARD C ";
		sql += "	  JOIN USER_INFO U ON U.USER_NO = C.USER_NO ";
		sql += "	  WHERE 1=1 ";
		sql += "	  ORDER BY custom_board_no ) O ";
		sql += "	) Custom_board ";
		sql += "  WHERE rnum BETWEEN ? AND ?";
	    
		//결과 저장 리스트
		List<Custom> customList = new ArrayList<>();
	    
	    try {
			ps = connection.prepareStatement(sql);
			
			//변수 채우기
			ps.setInt(1, paging.getStartNo());
			ps.setInt(2, paging.getEndNo());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Custom custom = new Custom();
				custom.setCustom_board_no(rs.getInt("custom_board_no"));
				custom.setBoard_type(rs.getString("board_type"));
				custom.setUser_no(rs.getInt("user_no"));
				custom.setUser_nickname(rs.getString("user_nickname"));
				custom.setAttach_no(rs.getInt("attach_no"));
				custom.setCustom_board_title(rs.getString("custom_board_title"));
				custom.setCustom_board_content(rs.getString("custom_board_content"));
				custom.setCustom_board_date(rs.getDate("custom_board_date"));
				custom.setCustom_board_hit(rs.getInt("custom_board_hit"));
				custom.setCustom_board_vote(rs.getInt("custom_board_vote"));
				
				//리스트에 custom 객체로 저장
				customList.add(custom);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
	    		
		return customList;
	}
	
	@Override
	public List<Custom> selectSearchAll(Connection connection, Paging paging, String search) {
		
		String sql = ""; //SQL 작성
		sql += "SELECT * FROM (";
		sql += "  SELECT ROWNUM rnum, O.* FROM (";
		sql += "    SELECT custom_board_no, BOARD_TYPE, C.USER_NO, U.USER_NICKNAME, ATTACH_NO, CUSTOM_BOARD_TITLE, CUSTOM_BOARD_CONTENT, custom_board_date, custom_board_hit, custom_board_vote";
		sql += "	  FROM CUSTOM_BOARD C ";
		sql += "	  JOIN USER_INFO U ON U.USER_NO = C.USER_NO ";
		sql += "		WHERE 1=1";
		sql += "		AND upper(custom_board_title) LIKE upper(?)";
		sql += "		OR upper(custom_board_content) LIKE upper(?)";
		sql += "		ORDER BY custom_board_no ) O";
		sql += " 		) custom_board";
		sql += " WHERE rnum BETWEEN ? AND ?";
		
		//결과 저장 리스트
		List<Custom> customList = new ArrayList<>();
		
		try {
			ps = connection.prepareStatement(sql);
			
			//변수 채우기
			ps.setString(1, "%" + search + "%");
			ps.setString(2, "%" + search + "%");
			ps.setInt(3, paging.getStartNo());
			ps.setInt(4, paging.getEndNo());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Custom custom = new Custom();
				custom.setCustom_board_no(rs.getInt("custom_board_no"));
				custom.setBoard_type(rs.getString("board_type"));
				custom.setUser_no(rs.getInt("user_no"));
				custom.setUser_nickname(rs.getString("user_nickname"));
				custom.setAttach_no(rs.getInt("attach_no"));
				custom.setCustom_board_title(rs.getString("custom_board_title"));
				custom.setCustom_board_content(rs.getString("custom_board_content"));
				custom.setCustom_board_date(rs.getDate("custom_board_date"));
				custom.setCustom_board_hit(rs.getInt("custom_board_hit"));
				custom.setCustom_board_vote(rs.getInt("custom_board_vote"));
				
				//리스트에 custom 객체로 저장
				customList.add(custom);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
		
		return customList;
	}
	
	@Override
	public List<Custom> selectSearch(Connection connection, Paging paging, String search, String category) {
		
		String sql = ""; //SQL 작성
		sql += "SELECT * FROM (";
		sql += "  SELECT ROWNUM rnum, O.* FROM (";
		sql += "    SELECT custom_board_no, BOARD_TYPE, C.USER_NO, U.USER_NICKNAME, ATTACH_NO, CUSTOM_BOARD_TITLE, CUSTOM_BOARD_CONTENT, custom_board_date, custom_board_hit, custom_board_vote";
		sql += "	  FROM CUSTOM_BOARD C ";
		sql += "	  JOIN USER_INFO U ON U.USER_NO = C.USER_NO ";
	    sql += "	  WHERE 1=1";
	    if( "title".equals(category) ) {
			sql += "		AND upper(custom_board_title) ";
	    } else if ( "detail".equals(category) ) {
			sql += "		AND upper(custom_board_content) ";
	    } else { //"nickname" 일 경우
			sql += "		AND upper(user_nickname) ";
		}
	    sql += " 			LIKE upper(?)";
	    sql += "		ORDER BY custom_board_no ) O";
		sql += " 		) custom_board";
		sql += " WHERE rnum BETWEEN ? AND ?";
	    
		//결과 저장 리스트
		List<Custom> customList = new ArrayList<>();
	    
	    try {
			ps = connection.prepareStatement(sql);
			
			//변수 채우기
			ps.setString(1, "%" + search + "%");
			ps.setInt(2, paging.getStartNo());
			ps.setInt(3, paging.getEndNo());
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Custom custom = new Custom();
				custom.setCustom_board_no(rs.getInt("custom_board_no"));
				custom.setBoard_type(rs.getString("board_type"));
				custom.setUser_no(rs.getInt("user_no"));
				custom.setUser_nickname(rs.getString("user_nickname"));
				custom.setAttach_no(rs.getInt("attach_no"));
				custom.setCustom_board_title(rs.getString("custom_board_title"));
				custom.setCustom_board_content(rs.getString("custom_board_content"));
				custom.setCustom_board_date(rs.getDate("custom_board_date"));
				custom.setCustom_board_hit(rs.getInt("custom_board_hit"));
				custom.setCustom_board_vote(rs.getInt("custom_board_vote"));
				
				//리스트에 custom 객체로 저장
				customList.add(custom);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}
	    		
		return customList;
	}
	
	
	@Override
	public int updateHit(Connection connection, Custom custom_no) {

		//SQL 작성
		String sql = "";
		sql += "UPDATE custom_board";
		sql += " SET custom_board_hit = custom_board_hit + 1";
		sql += " WHERE custom_board_no = ?";

		int res = 0;

		try {
			ps = connection.prepareStatement(sql); //SQL수행 객체

			ps.setInt(1, custom_no.getCustom_board_no()); //조회할 게시글 번호 적용

			res = ps.executeUpdate(); //SQL 수행

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//DB객체 닫기
			JDBCTemplate.close(ps);
		}

		return res;
	}
	
	@Override
	public Custom selectCustomByCustomno(Connection connection, Custom custom_no) {
		
		//SQL 작성
		String sql = "";
		sql += "SELECT * FROM custom_board";
		sql += " WHERE custom_board_no = ?";

		//결과 저장할 Board객체
		Custom custom = null;

		try {
			ps = connection.prepareStatement(sql); //SQL수행 객체

			ps.setInt(1, custom_no.getCustom_board_no()); //조회할 게시글 번호 적용

			rs = ps.executeQuery(); //SQL 수행 및 결과집합 저장

			//조회 결과 처리
			while(rs.next()) {
				custom = new Custom(); //결과값 저장 객체

				//결과값 한 행 처리
				custom.setCustom_board_no(rs.getInt("custom_board_no"));
				custom.setBoard_type(rs.getString("board_type"));
				custom.setUser_no(rs.getInt("user_no"));
				custom.setUser_nickname(rs.getString("user_nickname"));
				custom.setAttach_no(rs.getInt("attach_no"));
				custom.setCustom_board_title(rs.getString("custom_board_title"));
				custom.setCustom_board_content(rs.getString("custom_board_content"));
				custom.setCustom_board_date(rs.getDate("custom_board_date"));
				custom.setCustom_board_hit(rs.getInt("custom_board_hit"));
				custom.setCustom_board_vote(rs.getInt("custom_board_vote"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//DB객체 닫기
			JDBCTemplate.close(rs);
			JDBCTemplate.close(ps);
		}

		//최종 결과 반환
		return custom;
	}
}