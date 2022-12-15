package com.emptybeer.etb.enums.bbs;

import com.emptybeer.etb.interfaces.IResult;

public enum WriteResult implements IResult {
    NOT_ALLOWED,
    NO_SUCH_BOARD,
    NOT_SIGNED ,
    // 로그인하지 않음.
    NO_MORE_REVIEW
}
