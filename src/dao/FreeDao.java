package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import vo.FreeVo;

public class FreeDao {
	private static FreeDao instance;

	private FreeDao() {

	}

	public static FreeDao getInstance() {
		if (instance == null) {
			instance = new FreeDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<FreeVo> free() {
		String sql = " SELECT NO, F.NAME, CONTENT, TO_CHAR(REGDATE, 'YYYY.MM.DD') REGDATE, F.MEM_NO, M.NAME WRITER\r\n" + 
					 "FROM JAVA_FREEBOARD F, JAVA_MEMBER M\r\n" + 
					 "WHERE F.MEM_NO = M.MEM_NO\r\n" + 
					 "AND F.DELYN = 'N'\r\n" + 
					 "AND BAN = 'N'";

		// 편의를 위해서 사용하는 방법, Map 대신에 FreeVo.class 호출
		// => 후에 xml에서 다른 방법을 배울 것이기 때문에 깊게 파고들 필요 없음
		// 굳이 내용을 알고 싶다면 ConvertUtils 확인할 것
		return jdbc.selectList(sql, FreeVo.class);
	}
	
	
	public FreeVo freeDetail(List<Object> param) {
		String sql = " SELECT * FROM\r\n" + 
				"(SELECT NO, F.NAME, CONTENT, TO_CHAR(REGDATE, 'YYYY.MM.DD') REGDATE, F.MEM_NO, M.NAME WRITER,\r\n" + 
				"       LAG(NO) OVER(ORDER BY NO)PREVNO,\r\n" + 
				"       LEAD(NO) OVER(ORDER BY NO)NEXTNO\r\n" + 
				"FROM JAVA_FREEBOARD F, JAVA_MEMBER M\r\n" + 
				"WHERE F.MEM_NO = M.MEM_NO\r\n" + 
				"AND F.DELYN = 'N'\r\n" + 
				"AND BAN = 'N')\r\n" + 
				"WHERE NO = ?";
		
		return jdbc.selectOne(sql, param, FreeVo.class);
	}

	public void freeUpdate(List<Object> param) {
		String sql = " UPDATE JAVA_FREEBOARD\r\n" + 
					 "SET CONTENT = ?\r\n" + 
					 "WHERE NO =?";
		
		jdbc.update(sql, param);
	}
	
	
}
