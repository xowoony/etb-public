const commentForm = window.document.getElementById('commentForm');
const commentContainer = window.document.getElementById('commentContainer');
const reportButton = document.querySelector('[rel="reportButton"]');
if (reportButton !== null) {
    if (reportButton.value === '신고완료') {
        reportButton.style.color = "grey";
    }
}

// 댓글 : aid 와 같은 article_index 찾은 다음에
const loadComments = () => {
    commentContainer.innerHTML = '';    // 초기화 (이거 없으면 모든 댓글이 하나 적을 때 마다 나온다.)

    const url = new URL(window.location.href);
    const searchParams = url.searchParams;  // 이건 comment?aid= 뒤에 있는 숫자를 의미한다.
    const aid = searchParams.get('aid');
    const xhr = new XMLHttpRequest();

    xhr.open('GET', `./comment?aid=${aid}`);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseArray = JSON.parse(xhr.responseText);
                const appendComment = (commentObject) => {
                    const commentHtmlText = `
                    <div class="comment ${commentObject['isMine'] === true ? 'mine': ''}" rel="comment">
                            <div class="head">
                                <span class="writer">${commentObject['userNickname']}</span>
                                <span class="dt">${commentObject['writtenOn']}</span>
                                <span class="spring"></span>
                                <span class="action-container">
                                    ${commentObject['isMine'] === true ? '<a href="#" class="action modify" rel="actionModify">수정</a>' : ''}
                                    ${commentObject['isMine'] === true ? '<a href="#" class="action delete" rel="actionDelete">삭제</a>' : '' }
                                    <a href="#" class="action cancel" rel="actionCancel">취소</a>
                                </span>
                            </div>
                            <div class="body">
                                <div class="content">
                                <span class="text">${commentObject['content']}</span>
                                </div>
                                <form  class="modify-form" rel="modifyForm">
                                    <label class="label">
                                        <span hidden>댓글 수정</span>
                                        <input class="--object-input" maxlength="100" name="content" placeholder="댓글을 입력해주세요." type="text">
                                    </label>
                                    <input class="--object-button" type="submit" value="수정">
                                </form>
                            </div>
                        </div>`;

                    const domParser = new DOMParser(); //위에 적어놓은 html 문자열을 가상구조를 만들어서 html 요소선택이 가능하게 한다.
                    const dom = domParser.parseFromString(commentHtmlText,'text/html');
                    const commentElement = dom.querySelector('[rel="comment"]');//이 dom 구조가 가지고 있는 것 중에 rel을 comment를 가지고 있는 것 선택
                    const modifyFormElement = dom.querySelector('[rel="modifyform"]');

                    dom.querySelector('[rel="actionModify"]')?.addEventListener('click', e => {
                        e.preventDefault();
                        commentElement.classList.add('modifying');
                        modifyFormElement['content'].value = commentObject['content'];
                        modifyFormElement['content'].focus();
                    });

                    modifyFormElement.onsubmit = e => {
                        e.preventDefault();

                        if (modifyFormElement['content'].value === '') {
                            alert('수정할 내용을 입력하세요.');
                            modifyFormElement['content'].focus();
                            return false;
                        }
                        const xhr = new XMLHttpRequest();
                        const formData = new FormData();

                        formData.append('index', commentObject['index']);
                        formData.append('content', modifyFormElement['content'].value);

                        xhr.open('PATCH', './comment');
                        xhr.onreadystatechange = () => {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                Cover.hide();
                                if (xhr.status >= 200 && xhr.status < 300) {
                                    const responseObject = JSON.parse(xhr.responseText);
                                    switch (responseObject['result']) {
                                        case 'success':
                                            loadComments();
                                            break;
                                        case 'no_such_comment':
                                            alert('수정하려는 댓글이 더 이상 존재하지 않습니다.\n\n이미 삭제되었을 수도 있습니다.');
                                            break
                                        case 'not_allowed':
                                            alert('해당 댓글을 수정할 권한이 없습니다.');
                                            break;
                                        default:

                                            alert('알 수 없는 이유로 댓글을 수정하지 못했습니다.\n\n잠시 후 다시 시도해 주세요.');
                                    }
                                } else {
                                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                                }
                            }

                        };
                        xhr.send(formData);
                    };

                    dom.querySelector('[rel="actionCancel"]').addEventListener('click', e => {
                        e.preventDefault();
                        commentElement.classList.remove('modifying');
                    })
                    dom.querySelector('[rel="actionDelete"]')?.addEventListener('click', e => {
                        e.preventDefault();
                        if (!confirm('정말로 댓글을 삭제할까요?')) {
                            return false;
                        }
                        // [DELETE] /bbs/comment
                        // deleteComment
                        // formData 에서 댓글 인덱스를 넘겨줘야함

                        const xhr = new XMLHttpRequest();
                        const formData = new FormData();
                        formData.append('index', commentObject['index']);

                        xhr.open('DELETE', './comment');
                        xhr.onreadystatechange = () => {
                            if (xhr.readyState === XMLHttpRequest.DONE) {
                                Cover.hide();
                                if (xhr.status >= 200 && xhr.status < 300) {
                                    const responseObject = JSON.parse(xhr.responseText);
                                    switch (responseObject['result']) {
                                        case 'success':
                                            loadComments();
                                            break;
                                        case 'no_such_comment':
                                            alert('삭제하려는 댓글이 더 이상 존재하지 않습니다.\n\n이미 삭제되었을 수도 있습니다.');
                                            break
                                        case 'not_allowed':
                                            alert('해당 댓글을 삭제할 권한이 없습니다.');
                                            break;
                                        default:
                                            alert('알 수 없는 이유로 댓글을 삭제하지 못했습니다.\n\n잠시 후 다시 시도해 주세요.');
                                    }
                                } else {
                                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                                }
                            }
                        };
                        xhr.send(formData);
                    });
                    commentContainer.append(commentElement);
                };

                for (let commentObject of responseArray) {
                    appendComment(commentObject);
                }
            } else {

            }
        }
    };
    xhr.send();
};

loadComments();

if (commentForm !== null) {
    commentForm.onsubmit = e => {
        e.preventDefault()

        if (commentForm['content'].value === '') {
            alert('댓글을 입력해 주세요.');
            commentForm['content'].focus();
            return false;
        }
        Cover.show('댓글을 작성하고 있습니다.\n잠시만 기다려 주세요.');
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('articleIndex', commentForm['aid'].value);
        formData.append('boardId', commentForm['bid'].value);
        formData.append('content', commentForm['content'].value);

        xhr.open('POST', './read');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                Cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            loadComments();
                            commentForm['content'].value = '';
                            document.getElementById('commentCount').innerHTML = responseObject['commentCount'] + 1;
                            break;
                        default:
                            alert('알 수 없는 이유로 댓글을 작성하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    };
}

const deleteButton = window.document.getElementById('deleteButton');
deleteButton?.addEventListener('click', e => {
    e.preventDefault();
    if (!confirm('정말로 게시글을 삭제할까요?')) {
        return;
    }
    Cover.show('게시글을 삭제하고 있습니다.\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('aid', commentForm['aid'].value);

    xhr.open('DELETE', "./read");
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            Cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success':
                        const bid = responseObject['bid'];
                        window.location.href = `./list?bid=${bid}`;
                        alert('게시글이 삭제되었습니다.');
                        break;
                    default:
                        alert('알 수 없는 이유로 게시글을 삭제하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
});

const basicToggleElement = document.querySelector('[rel="basicToggle"]');
const basicLikeButton = document.querySelector('[rel="basicLikeButton"]');
if(!basicToggleElement.classList.contains('prohibited')) {
    basicLikeButton.addEventListener('click', e => {
        e.preventDefault();
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        const method = basicLikeButton.classList.contains('liked') ? 'DELETE' : 'POST';
        formData.append('articleIndex', commentForm['aid'].value);
        formData.append('boardId', commentForm['bid'].value);
        xhr.open(method, './basic-like');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success' :
                            // 값을 받아와서 innerText
                            if(responseObject['isLiked'] === true) {
                                basicLikeButton.classList.add('liked');
                                basicToggleElement.value = "추천 취소"
                                basicLikeButton.parentNode.querySelector('.like-count').innerHTML = responseObject['likeCount'];
                            } else {
                                basicLikeButton.classList.remove('liked');
                                basicToggleElement.value = "추천"
                                basicLikeButton.parentNode.querySelector('.like-count').innerHTML = responseObject['likeCount'];
                            }
                            break;
                        default:
                            alert('알 수 없는 이유로 추천결과가 반영되지 못했습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    });
}

const topButton = document.getElementById('topButton');
topButton.addEventListener('click', () => {
    window.scrollTo(0, 0);
});

const goComment = document.getElementById('goComment');
goComment.addEventListener('click', () => {
    commentContainer.scrollIntoView();
})


// 게시글 신고하기
if (reportButton !== null) {
    if (!reportButton.classList.contains('prohibited') && reportButton.value === '신고하기') {
        reportButton.addEventListener('click', e => {
            e.preventDefault();
            if (reportButton.value === '신고완료') {
                alert('신고가 완료되었습니다.');
                return;
            }
            if (!confirm('정말로 게시글을 신고할까요?')) {
                return;
            }

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('aid', commentForm['aid'].value);
            xhr.open('POST', './article-report');
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject['result']) {
                            case 'success' :
                                // 값을 받아와서 innerText
                                // if(responseObject['isReported'] === true) {
                                //     reportButton.value = '신고완료'
                                //     reportButton.style.color = "grey"
                                // }
                                reportButton.value = '신고완료'
                                reportButton.style.color = "grey"
                                alert('신고가 완료되었습니다.');
                                break;
                            default:
                                alert('알 수 없는 이유로 추천결과가 반영되지 못했습니다.\n\n잠시 후 다시 시도해 주세요.');
                        }
                    } else {
                        alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                }
            };
            xhr.send(formData);
        });
    } else {
        reportButton.addEventListener('click', () => {
            alert('신고가 완료되었습니다.');
        });
    }
}