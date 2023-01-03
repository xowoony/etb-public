package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.BasicArticleEntity;
import com.emptybeer.etb.entities.bbs.BoardEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.bbs.WriteResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBasicBbsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.emptybeer.etb.services.BasicBbsService")
public class BasicBbsService {
    private final IBasicBbsMapper basicBbsMapper;

    @Autowired
    public BasicBbsService(IBasicBbsMapper basicBbsMapper) {
        this.basicBbsMapper = basicBbsMapper;
    }

    public BoardEntity getBoard(String id) {
        return this.basicBbsMapper.selectBoardById(id);
    }

    // 글 적기
    public Enum<? extends IResult> write(BasicArticleEntity basicArticle) {
        BoardEntity board = this.basicBbsMapper.selectBoardById(basicArticle.getBoardId());

        if (board == null) {
            return WriteResult.NO_SUCH_BOARD;
        }
        return this.basicBbsMapper.insertArticle(basicArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 이미지
    public Enum<? extends IResult> addImage(ImageEntity image) {
        return this.basicBbsMapper.insertImage(image) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    // 이미지 넣는 기
    public ImageEntity getImage(int index) {
        return this.basicBbsMapper.selectImageByIndex(index);
    }

}
