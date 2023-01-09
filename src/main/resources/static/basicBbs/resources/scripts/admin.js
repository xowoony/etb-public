// 전체 체크박스
const checkboxes
    = document.querySelectorAll('input[name="reportedArticle"]');
// 선택된 체크박스
const checked
    = document.querySelectorAll('input[name="reportedArticle"]:checked');
// select all 체크박스
const selectAll
    = document.querySelector('input[name="selectAll"]');

function checkSelectAll()  {
    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }
}

function selectAllArticles(selectAll)  {
    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    });
}


// 신고 게시글 삭제
const deleteButton = window.document.querySelector('[rel="deleteButton"]');
deleteButton?.addEventListener('click', e => {
    e.preventDefault();
    if (document.querySelector('input[name="reportedArticle"]:checked') === null) {
        alert('삭제할 게시글을 선택해 주세요.')
    } else {
        if (!confirm('정말로 게시글을 삭제할까요?')) {
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        document.querySelectorAll('input[name="reportedArticle"]:checked').forEach(item => {
            formData.append('aid', item.dataset.aid);
        });
        xhr.open('DELETE', "./read");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            const bid = responseObject['bid'];
                            window.location.href = `./basic-admin?bid=${bid}`;
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
    }
});


// 신고 개수 초기화
const resetButton = window.document.querySelector('[rel="resetButton"]');
resetButton?.addEventListener('click', e => {
    e.preventDefault();
    if (document.querySelector('input[name="reportedArticle"]:checked') === null) {
        alert('신고 수를 초기화 할 게시글을 선택해 주세요.')
    } else {
        if (!confirm('신고 수를 초기화할까요?')) {
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        document.querySelectorAll('input[name="reportedArticle"]:checked').forEach(item => {
            formData.append('aid', item.dataset.aid);
        });
        xhr.open('DELETE', "./article-report");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            window.location.reload();
                            alert('신고 수가 초기화 되었습니다.');
                            break;
                        default:
                            alert('알 수 없는 이유로 신고 수를 초기화하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    }
});