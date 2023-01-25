// 게시글 정렬
// 신고순
const sortReport = window.document.getElementById('sortReport');
sortReport.addEventListener('click', () => {
    window.location.href = `./review-admin?sort=report`;
    sortGood.scrollIntoView();
});
// 최신순
const sortNew = window.document.getElementById('sortNew');
sortNew.addEventListener('click', () => {
    window.location.href = `./review-admin?sort=new`;
    sortNew.scrollIntoView();
});

function checkSelectAll()  {
    // 전체 체크박스
    const checkboxes
        = document.querySelectorAll('input[name="reported"]');
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="reported"]:checked');
    // select all 체크박스
    const selectAll
        = document.querySelector('input[name="allSelect"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}

function selectAll(selectAll)  {
    const checkboxes
        = document.getElementsByName('reported');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    })
}


// 신고 리뷰 삭제
const deleteButtons = window.document.querySelectorAll('[rel="deleteButton"]');
for (let deleteButton of deleteButtons) {
    deleteButton?.addEventListener('click', e => {
        e.preventDefault();
        if (document.querySelector('input[name="reported"]:checked') === null) {
            alert('삭제할 리뷰를 선택해 주세요.')
        } else {
            if (!confirm('정말로 리뷰를 삭제할까요?')) {
                return;
            }
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            document.querySelectorAll('input[name="reported"]:checked').forEach(item => {
                formData.append('aid', item.dataset.aid);
            });
            xhr.open('DELETE', "./reviewRead");
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject['result']) {
                            case 'success':
                                window.location.reload();
                                alert('리뷰가 삭제되었습니다.');
                                break;
                            default:
                                alert('알 수 없는 이유로 리뷰를 삭제하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                        }
                    } else {
                        alert('서버와 통신하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                }
            };
            xhr.send(formData);
        }
    });
}



// 신고 개수 초기화
const resetButtons = window.document.querySelectorAll('[rel="resetButton"]');
for (let resetButton of resetButtons) {
    resetButton?.addEventListener('click', e => {
        e.preventDefault();
        if (document.querySelector('input[name="reported"]:checked') === null) {
            alert('신고 수를 초기화 할 리뷰를 선택해 주세요.')
        } else {
            if (!confirm('신고 수를 초기화할까요?')) {
                return;
            }
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            document.querySelectorAll('input[name="reported"]:checked').forEach(item => {
                formData.append('aid', item.dataset.aid);
            });
            xhr.open('DELETE', "./reviewDeclaration");
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
}
