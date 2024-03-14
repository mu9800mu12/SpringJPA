package service;

import dto.NoticeDTO;

import java.util.List;

public interface INoticeService {

    /**
     * 공지사항 전체 가져오기
     */
    List<NoticeDTO> getNoticeList();

    /**
     * 공지사항 상세 정보가져오기
     *
     * @poram pDTO 공지사항 상세 가져오기 위한 정보
     * @oaram type 조회수 증가여부(true : 증가, false : 증가안함
     */

    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception;

    /**
     * 해당 공지사항 수정하기
     *
     * @param pDTO 공지사항 수정하기 위한 정보
     */

    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;


    /**
     * 해당 공지사항 삭제하기
     *
     * @param pDTO 공지사항 저장하기
     */
    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;



    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;






}
