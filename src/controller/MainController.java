package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.FreeService;
import service.MemberService;
import util.ScanUtil;
import util.View;
import view.Print;
import vo.FreeVo;
import vo.MemberVo;
 
public class MainController extends Print {
	
	static public Map<String, Object> sessionStorage = new HashMap<>();
	
	FreeService freeService = FreeService.getInstance();
	MemberService memberService = MemberService.getInstance();

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.HOME;
		while (true) {
			switch (view) {
			case HOME:
				view = home();
				break;
			case FREE_LIST:
				view = freeList();
				break;
			case FREE_DETAIL:
				view = freeDetail();
				break;
			case FREE_UPDATE:
				view = freeUpdate();
				break;
//			case FREE_DELETE:
//				view = freeDelete();
//				break;
			case MEMBER_LOGIN:
				view = memberLogin();
				break;
			default:
				break;
			}
		}
	}
	
	
	private View memberLogin() {
		String id = ScanUtil.nextLine("ID : ");
		String pw = ScanUtil.nextLine("PW : ");
		
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		param.add(pw);
		param.add(1);
		
		boolean loginChk = memberService.login(param, 1);
		
		if(!loginChk) {
			System.out.println("1. 재로그인");
			System.out.println("2. 회원 가입");
			System.out.println("3. 홈");
			return View.MEMBER_LOGIN;
		}
		
		// 로그인을 한 후, 직전에 있던 페이지로 다시 돌아가게 하는 것
		// sessionStorage의 view값이 어디냐에 따라서 다시 그곳의 페이지로 리턴함
		View view = (View) sessionStorage.get("view");
		if(view == null) return View.FREE_LIST;
		else return view;
	}

	
	private View freeUpdate() {
		if(!sessionStorage.containsKey("member")) {
			// 로그인을 한 후, 직전에 있던 페이지로 다시 돌아가게 하는 것
			sessionStorage.put("view", View.FREE_UPDATE);
		return View.MEMBER_LOGIN;
		}
		
		int boardNo = (int) sessionStorage.get("boardNo");
		List<Object> param = new ArrayList<Object>();
		param.add(boardNo);
		
		FreeVo freeVo = freeService.freeDetail(param);
		MemberVo member = (MemberVo) sessionStorage.get("member");
		
		if(freeVo.getMem_no() != member.getMem_no()) {
			System.out.println("해당 게시글 변경 권한이 없습니다.");
			return View.FREE_DETAIL;
		}
		
		String content = ScanUtil.nextLine("내용 : ");
		List<Object> param2 = new ArrayList<Object>();
		param2.add(content);
		param2.add(boardNo);
		freeService.freeUpdate(param2);
		
		return View.FREE_DETAIL;
	}

	
	private View freeDetail() {
		int boardNo = (int) sessionStorage.get("boardNo");
		List<Object> param = new ArrayList<Object>();
		param.add(boardNo);
		
		FreeVo freeVo = freeService.freeDetail(param);
		System.out.println(freeVo);
		
		System.out.println("<이전글\t\t다음글>");
		
		System.out.println("1. 게시판 변경");
		System.out.println("2. 게시판 삭제");
		System.out.println("3. 전체게시판 조회");
		
		String sel = ScanUtil.nextLine("메뉴 : ");
		
		if(sel.equals("1")) return View.FREE_UPDATE;
		else if(sel.equals("2")) return View.FREE_DELETE;
		else if(sel.equals("3")) return View.FREE_LIST;
		else if(sel.equals("<")) {
			boardNo = freeVo.getPrevno();
			if(boardNo == 0) {
				System.out.println("이전 게시글이 없습니다.");
			} else {
				sessionStorage.put("boardNo", boardNo);
			}
			return View.FREE_DETAIL;
		}
		else if(sel.equals(">")) {
			boardNo = freeVo.getNextno();
			if(boardNo == 0) {
				System.out.println("다음 게시글이 없습니다.");
			} else {
				sessionStorage.put("boardNo", boardNo);
			}
			return View.FREE_DETAIL;
		}
		else return View.FREE_DETAIL;
	}

	
	private View freeList() {
		List<FreeVo> freeList = freeService.freeList();
		
		// Map 대신에 VO 쓰는 것, 메서드 불러오는게 훨씬 편함
		for (FreeVo freeVo : freeList) {
			int no = freeVo.getNo();
			String name = freeVo.getName();
			String content = freeVo.getContent();
			String writer = freeVo.getWriter();
			String regdate = freeVo.getRegdate();
			
			System.out.println(no+"\t"+name+"\t"+content+"\t"+writer+"\t"+regdate);
		}
		System.out.println("1. 게시판 상세 조회");
		System.out.println("2. 홈");
		
		int sel = ScanUtil.menu();
		
		if(sel==1) {
			int boardNo = ScanUtil.nextInt("게시판 번호 입력");
			sessionStorage.put("boardNo", boardNo);
			return View.FREE_DETAIL;
		}
		else if(sel==2) return View.HOME;
		else return View.FREE_LIST;
	}
	
	
	private View home() {
		System.out.println("1. 전체게시판 조회");
		System.out.println("2. 게시판 등록");
		
		int sel = ScanUtil.nextInt("메뉴 선택 : ");
		
		if (sel==1) return View.FREE_LIST;
		else if (sel==2) return View.FREE_INSERT;
		else if (sel==0) return View.ADMIN;
		return View.HOME;
	}
	
	
}