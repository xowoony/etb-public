function checkSelectAll()  {
    // 전체 체크박스
    const checkboxes
        = document.querySelectorAll('input[name="myArticle"]');
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="myArticle"]:checked');
    // select all 체크박스
    const selectAll
        = document.querySelector('input[name="selectall"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}

function selectAll(selectAll)  {
    const checkboxes
        = document.getElementsByName('myArticle');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    })
}


function checkSelectAll2()  {
    // 전체 체크박스
    const checkboxes
        = document.querySelectorAll('input[name="myReview"]');
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="myReview"]:checked');
    // select all 체크박스
    const selectAll
        = document.querySelector('input[name="selectall2"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}

function selectAll2(selectAll)  {
    const checkboxes
        = document.getElementsByName('myReview');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    })
}


// 선택 글 좋아요 취소
const basicLikeDeleteButton = window.document.querySelector('[rel="basicLikeDeleteButton"]');
basicLikeDeleteButton?.addEventListener('click', e => {
    e.preventDefault();
    if (document.querySelector('input[name="myArticle"]:checked') === null) {
        alert('추천 취소할 게시글을 선택해 주세요.')
    } else {
        if (!confirm('정말로 게시글 추천을 취소할까요?')) {
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        document.querySelectorAll('input[name="myArticle"]:checked').forEach(item => {
            formData.append('articleIndex', item.dataset.aid);
            formData.append('boardId', item.dataset.bid);
        });
        xhr.open('DELETE', "../basicBbs/basic-like");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            window.location.reload();
                            alert('추천을 취소하였습니다.');
                            break;
                        default:
                            alert('알 수 없는 이유로 추천을 취소하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
});


// 선택 리뷰 좋아요 취소
const reviewDeleteButton = window.document.querySelector('[rel="reviewDeleteButton"]');
reviewDeleteButton?.addEventListener('click', e => {
    e.preventDefault();
    if (document.querySelector('input[name="myReview"]:checked') === null) {
        alert('추천 취소할 리뷰를 선택해 주세요.')
    } else {
        if (!confirm('정말로 리뷰 추천을 취소할까요?')) {
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        document.querySelectorAll('input[name="myReview"]:checked').forEach(item => {
            formData.append('articleIndex', item.dataset.aid2);
        });
        xhr.open('DELETE', "../bbs/reviewLike");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            window.location.reload();
                            alert('추천을 취소하였습니다.');
                            break;
                        default:
                            alert('알 수 없는 이유로 추천을 취소하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
});