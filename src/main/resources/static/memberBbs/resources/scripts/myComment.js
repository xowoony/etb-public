function checkSelectAll()  {
    // 전체 체크박스
    const checkboxes
        = document.querySelectorAll('input[name="myLike"]');
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="myLike"]:checked');
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
        = document.getElementsByName('myLike');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    })
}


// 내가 쓴 댓글 삭제
const commentDeleteButton = window.document.querySelector('[rel="commentDeleteButton"]');
commentDeleteButton?.addEventListener('click', e => {
    e.preventDefault();
    if (document.querySelector('input[name="myLike"]:checked') === null) {
        alert('삭제할 댓글을 선택해 주세요.')
    } else {
        if (!confirm('정말로 댓글을 삭제할까요?')) {
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        document.querySelectorAll('input[name="myLike"]:checked').forEach(item => {
            formData.append('index', item.dataset.cid);
        });
        xhr.open('DELETE', "../basicBbs/comment");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            window.location.reload();
                            alert('댓글이 삭제되었습니다.');
                            break;
                        default:
                            alert('알 수 없는 이유로 댓글을 삭제하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
});