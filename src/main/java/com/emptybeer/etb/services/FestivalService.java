package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.FestivalArticleEntity;
import com.emptybeer.etb.entities.bbs.FestivalCommentEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBbsMapper;
import com.emptybeer.etb.mappers.IFestivalMapper;
import com.emptybeer.etb.vos.FestivalCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.emptybeer.etb.services.FestivalService")
public class FestivalService {

    private final IFestivalMapper festivalMapper;

    @Autowired
    public FestivalService(IFestivalMapper festivalMapper) {
        this.festivalMapper = festivalMapper;
    }
    // festival 관련

    public FestivalArticleEntity[] getFestivalArticle(){

        return this.festivalMapper.selectFestivalArticle();
    }

    public ImageEntity getImage(int index) {return this.festivalMapper.selectImageByIndex(index);}

    public FestivalArticleEntity getFestivalArticleByIndex(int index){
        return this.festivalMapper.selectFestivalArticleByIndex(index);
    }

    public FestivalCommentVo[] getFestivalCommentByArticleIndex(int index){
        return this.festivalMapper.selectFestivalCommentByArticleIndex(index);
    }

    public Enum<? extends IResult> writeFestivalComment(FestivalCommentEntity festivalComment){
        FestivalArticleEntity article = this.festivalMapper.selectFestivalArticleByIndex(festivalComment.getArticleIndex());

        if(article==null){
            return CommonResult.FAILURE;
        }

        return this.festivalMapper.insertFestivalComment(festivalComment) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }
}
