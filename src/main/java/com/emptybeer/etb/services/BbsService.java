package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ReviewArticleEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBbsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.emptybeer.etb.services.BbsServices")
public class BbsService {
    private final IBbsMapper bbsMapper;

    @Autowired
    public BbsService(IBbsMapper bbsMapper) {
        this.bbsMapper = bbsMapper;
    }

    public BoardEntity getBoard(String id) {
        return this.bbsMapper.selectBoardById(id);
    }

    public Enum<? extends IResult> reviewWrite(ReviewArticleEntity reviewArticle) {
        BoardEntity board = this.bbsMapper.selectBoardById(reviewArticle.getBoardId());

        if (board == null) {
            return WriteResult.NO_SUCH_BOARD;
        }
        return this.bbsMapper.insertReviewArticle(reviewArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }
}
