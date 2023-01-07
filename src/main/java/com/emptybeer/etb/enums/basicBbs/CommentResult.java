package com.emptybeer.etb.enums.basicBbs;

import com.emptybeer.etb.interfaces.IResult;

public enum CommentResult implements IResult {
    NO_SUCH_COMMENT,    // 댓글이 없음
    NOT_ALLOWED         // 권한이 없음
}
