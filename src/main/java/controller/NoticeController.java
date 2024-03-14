package controller;

import dto.MsgDTO;
import dto.NoticeDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.INoticeService;
import util.CmmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




/*
 * Controller 선언해야만 Spring 프레임워크에서 Contorller인지 인식가능
 * 자바 서블릿 역할 수행
 *
 * slf4j는 스프링 프레임워크에서 로그 처리하는 인터페이스 기술이며,
 * 로그처리 기술인 log4j와 logback과 인터페이스 역할 수행함
 * 스프링 프레임워크는 기본으로 logback을 채택해서 로그 처리함
 */
@Slf4j
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {

    // @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함
    private final INoticeService noticeService;

    /**
     * 게시판 리스트 보여주기
     * <p>
     * GetMapping(value = "noticeList") => GET방식을 통해 접속되는 URL이 notice/noticeList 경우 아래 함수를 실행함
     */

    @GetMapping(value = "noticeList")
    public String noticeList(HttpSession session, ModelMap model)
        throws Exception {

        log.info(this.getClass().getName() + ".noticeList Start!");

        //로그인된 사용자 아이디는 Session에 저장함
        //교육용으로 아직 로그인을 구현하지 않았기에 Session에 데이터를 저장하지 않았음
        //추후 로그인을 구현할것으로 가정하고, 공지사항 리스트 출력하는 함수에서 로그인 한 것 처럼 Session 값을 생성함
        session.setAttribute("SESSION_USER_ID", "USER01"); //로그인 구현하지 않아 임시로 사용



        //공지사항 리스트 조회하기
         // JAVA 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception 처리
        List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList())
                .orElseGet(ArrayList::new);

        //조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);


        log.info(this.getClass().getName() + "noticeList End!");

        // 함수 처리가 끝나면 보여줄 HTML (Thymeleaf) 파일명
        // templates/notice/noticeList.html
        return "notice/noticeList";

    }

    /**
     * 게시판 작성 페이지 이동
     * <p>
     * 이 함수는 게시판 작성 페이지로 접근하기 위해 만듦
     * <p>
     * GetMapping(value = "notice/noticeReg") => GET방식을 통해 접속되는 URL이 notice/noticeReg 경우아래 함수를 실햄
     */

    @GetMapping(value = "noticeReg")
    public String noticeReg() {

        log.info(this.getClass().getName() + "noticeReg Start!");

        log.info(this.getClass().getName() + "noticeReg End!");

        //함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        // templates/notice/noticeReg.html
        return "notice/noticeReg";

    }

    /**
     * 게시판 글 등록
     * <p>
     * 게시판 등록은 Ajax로 호출되기 때문에 결과는 JSON 구주로 전달해야만 함
     * JSON 구조로 결과 메시지를 전송하기 위해 @ResponseBody 어노테이션 추가함
     */

    @ResponseBody
    @PostMapping(value = "noticeInsert")
    public MsgDTO noticeInsert(HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() + "noticeInsert Start!");

        String msg = ""; //메시지 내용
        MsgDTO dto = null; //결과 메시지 구조

        try {
            // 로그인된 사용자 아이디를 가져오기
            // 로그인을 아직 구현하지 않았기에 공지사항 리스트에서 로그인 한 것처럼 Session 값을 저장함
            String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            /*
             *###########################################################################################
             * 반드시 값을 받았으면 로그를 찍어 값이 제대로 들어오는 파악해야함 반드시 작성할 것
             *###########################################################################################
             */

            log.info(" userId:" + userId);
            log.info(" title:" + title);
            log.info(" noticeYn :" + noticeYn);
            log.info(" contents:" + contents);

            // 데이터 저장하기 위해DTO에 저장하기
            NoticeDTO pDTO = NoticeDTO.builder().userId(userId).title(title)
                    .noticeYn(noticeYn).contents(contents).build();

            /*
             * 게시글 등록하기위한 비즈니스 로직을 호출
             */
            noticeService.insertNoticeInfo(pDTO);

            //서비스 호출이 정상적이면 보여줄, 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다";

        } catch (Exception e) {

            //저장 실패시 사용자에게 보여줄 메시지
            msg = "실패하였습니다. :" + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            //결과 메시지 전달하기
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + "noticeInsert End!");

        }


        //@ReponseBody 어노테이션에 의해 자동으로 JSON 구조로 변경되어 전달됨
        return dto;
    }

    /**
     * 게시판 상세보기
     */
    @GetMapping(value = "noticeInfo")
    public String noticeInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + "noticenfo Start!");

        Long nSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("nSeq"), String.valueOf(0L))); // 공지글 번호(PK)

        /*
         *#############################################################################################
         * 반드시 값을 받았으면, 꼭 로그를 찍어 값이 들어오는지 확인해야함 반드시 작성할 것
         *#############################################################################################
         */

        log.info("nSeq :" + nSeq);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달받은 값을 DTO 객체에 넣는다.
         */

        NoticeDTO pDTO = NoticeDTO.builder().noticeSeq(nSeq).build();

        // 공지사항 상세정보 가져오기
        // JAVA 8부터 제공되는 Optional 활용하여 NPE(Nill Point Exception) 처리
        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                .orElseGet(null);

        //조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".noticeInfo End!");

        return "notice/noticeInfo";

    }


    /**
     * 게시판 수정 보기
     */
    @GetMapping(value = "noticeEditInfo")
    public String noticeEditInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".noticeEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글 번호(PK)

        // 값 받으면 꼭 로그를 찍자

        log.info("nSeq :" + nSeq);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해 처리 전달 받은 값을 DTO객체에 넣는다.
         */

        NoticeDTO pDTO = NoticeDTO.builder().noticeSeq(Long.parseLong(nSeq)).build();


        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, false))
                .orElseGet(() -> NoticeDTO.builder().build());

        //조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO",rDTO);

        log.info(this.getClass().getName() + "noticeEditInfo End!");

        return "notice/noticeEditInfo";

    }


    /**
     * 게시판 글 수정
     */

    @ResponseBody
    @PostMapping(value = "noticeUpdate")
    public MsgDTO noticeUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".noticeUpdate Start!");

        String msg =""; //메시지 내용
        MsgDTO dto =null; //결과 메시지 구조

        try {

            String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String noticeYn = CmmUtil.nvl(request.getParameter("noticeYn"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            /*
             *###########################################################################################
             * 반드시 값을 받았으면 로그를 찍어 값이 제대로 들어오는 파악해야함 반드시 작성할 것
             *###########################################################################################
             */

            log.info(" userId :" + userId);
            log.info(" nSeq :" + nSeq);
            log.info(" title :" + title);
            log.info(" noticeYn :" + noticeYn);
            log.info(" contents:" + contents);

            // 데이터 저장하기 위해DTO에 저장하기
            NoticeDTO pDTO = NoticeDTO.builder().userId(userId).noticeSeq(Long.parseLong(nSeq))
                    .title(title).noticeYn(noticeYn).contents(contents).build();

            //게시글 수정하기 DB
            noticeService.updateNoticeInfo(pDTO);

            msg = "수정되었습니다";

        } catch (Exception e) {
            msg = "실패하였습니다. :" +e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            //결과 메시지 전달
            dto = MsgDTO.builder()
                    .msg(msg)
                    .build();

            log.info(this.getClass().getName() + ".noticeUpdate End!");

        }
            return dto;
    }


    /**
     * 게시판 글 삭제
     */

    @ResponseBody
    @PostMapping(value = "noticeDelete")
    public MsgDTO noticeDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".noticeDelete Start!");

        String msg ="";
        MsgDTO dto = null;

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); //글번호(PK)


            log.info("nSeq" + nSeq);

            NoticeDTO pDTO = NoticeDTO.builder().noticeSeq(Long.parseLong(nSeq)).build();

            noticeService.deleteNoticeInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {

            msg ="실패하였습니다. : " +e.getMessage();
            log.info(e.toString());

        } finally {

            dto = MsgDTO.builder()
                    .msg(msg)
                    .build();

            log.info(this.getClass().getName() + ".noticeDelete End!");
        }

        return dto;
    }

}

