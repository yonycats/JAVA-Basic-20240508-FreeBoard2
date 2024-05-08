package vo;

import lombok.Data;

@Data
public class FreeVo {
	private int no;
	private String name;
	private String content;
	private int mem_no;
	private String regdate;
	private String delyn;
	private String ban;
	
	private String writer;
	
	// DB에 없지만 사용하고 싶은 컬럼은 VO에 얼마든지 추가해서 사용하면 됨
	private int prevno;
	private int nextno;
}
