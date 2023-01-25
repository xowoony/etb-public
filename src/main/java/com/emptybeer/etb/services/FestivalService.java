package com.emptybeer.etb.services;

import com.emptybeer.etb.entities.bbs.FestivalArticleEntity;
import com.emptybeer.etb.entities.bbs.FestivalCommentEntity;
import com.emptybeer.etb.entities.bbs.ImageEntity;
import com.emptybeer.etb.entities.member.UserEntity;
import com.emptybeer.etb.enums.CommonResult;
import com.emptybeer.etb.enums.festival.FestivalResult;
import com.emptybeer.etb.interfaces.IResult;
import com.emptybeer.etb.mappers.IBbsMapper;
import com.emptybeer.etb.mappers.IFestivalMapper;
import com.emptybeer.etb.models.PagingModel;
import com.emptybeer.etb.models.PagingModelFestival;
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

    public FestivalArticleEntity[] getFestivalArticleInAdmin(PagingModelFestival paging){

        return this.festivalMapper.selectFestivalArticle(paging.countPerPage, (paging.requestPage - 1)* paging.countPerPage);
    }

    public FestivalArticleEntity[] getFestivalArticle(PagingModelFestival paging){
        return this.festivalMapper.selectFestivalArticleForPaging(paging.countPerPage, (paging.requestPage - 1)* paging.countPerPage);
    }


    public ImageEntity getImage(int index) {return this.festivalMapper.selectImageByIndex(index);}

    public FestivalArticleEntity getFestivalArticleByIndex(int index){
        return this.festivalMapper.selectFestivalArticleByIndex(index);
    }

    public FestivalCommentVo[] getFestivalCommentByArticleIndex(int index, PagingModel paging){
        return this.festivalMapper.selectFestivalCommentByArticleIndex(index, paging.countPerPage, (paging.requestPage - 1)* paging.countPerPage);
    }

    public Enum<? extends IResult> writeFestivalComment(FestivalCommentEntity festivalComment){
        FestivalArticleEntity article = this.festivalMapper.selectFestivalArticleByIndex(festivalComment.getArticleIndex());

        if(article==null){
            return CommonResult.FAILURE;
        }

        return this.festivalMapper.insertFestivalComment(festivalComment) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }

    public Enum<? extends IResult> deleteFestivalComment(FestivalCommentEntity festivalComment, UserEntity user){
//        FestivalCommentEntity existingComment = this.festivalMapper.selectCommentByIndex(comment.getIndex());
        FestivalCommentEntity existingComment = this.festivalMapper.selectFestivalCommentByCommentIndex(festivalComment.getIndex());
        if(existingComment == null){
            return FestivalResult.NO_SUCH_COMMENT;
        }


        if(user==null || !user.getEmail().equals(existingComment.getUserEmail())){
            return FestivalResult.NOT_ALLOWED;
        }

        return this.festivalMapper.deleteFestivalCommentByIndex(festivalComment.getIndex()) > 0  ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }



    //festivalRead관련 메소드
    public Enum<? extends IResult> updateFestivalComment(FestivalCommentEntity festivalComment, UserEntity user){
        //user의 내용이 있고, 앞에서 가져온 Content나 Index를 가지고 값이 수정되었으면 SUCCESS 반환
        System.out.println("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ"+festivalComment.getContent());
        if(this.festivalMapper.updateFestivalCommentByIndex(festivalComment.getContent(), festivalComment.getIndex())>0 && user != null){
            System.out.println("수정완료");
            return CommonResult.SUCCESS;
        }

        //그렇지 않을시 FAILURE 반환
        return CommonResult.FAILURE;

    }



    // festivalModify 관련 메소드
    public int updateFestivalComment(int index, String content){
        if(this.festivalMapper.updateFestivalCommentByIndexFromFestivalModify(index, content)>0){
            FestivalCommentEntity comment = this.festivalMapper.selectFestivalArticleIndexByIndexFromFestivalModify(index);


            //articleIndex를 반환하여 리다이렉팅
            return comment.getArticleIndex();
        }

        return 0;
    }

    public int getALLFestivalCommentCountByFestivalArticleIndex(int index){
        return this.festivalMapper.selectFestivalCommentCountByFestivalArticleIndex(index);
    }




    //페이징용
    public int getALLFestivalArticle(){
        return this.festivalMapper.selectFestivalArticleForCount();
    }


    // festivalAdmin 관련 메소드

    //입력
    public Enum<? extends IResult> festivalWrite(FestivalArticleEntity festivalArticle){


        return this.festivalMapper.insertFestivalArticle(festivalArticle) > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }


    // 수정
    public Enum<? extends IResult> festivalUpdate(FestivalArticleEntity festivalArticle){

        int result;

        // 이미지를 변경하지 않을시와 하지 않을시의 로직을 수행
        if(festivalArticle.getTitleImage() != null){
            System.out.println("1111111111111111111");
            result = this.festivalMapper.updateFestivalArticle(festivalArticle);
        } else{
            System.out.println("222222222222222222222");
            result = this.festivalMapper.updateFestivalArticleExceptImage(festivalArticle);
        }

        System.out.println("result=========="+result);
        return result > 0 ? CommonResult.SUCCESS : CommonResult.FAILURE;
    }



    //삭제

    public Enum<? extends IResult> deleteFestivalArticle(FestivalArticleEntity festivalArticle, UserEntity user){

        if(user==null){
            return CommonResult.FAILURE;
        } else{
            return this.festivalMapper.deleteFestivalArticleByIndex(festivalArticle.getIndex()) > 0  ? CommonResult.SUCCESS : CommonResult.FAILURE;
        }

    }

}
