// 뒤로가기 버튼
document.querySelector('[rel="backButton"]').addEventListener('click', () => {
    window.location.href = '/product'
});

// 자세히보기 버튼
const detailButton = window.document.querySelector('[rel="detailButton"]');

detailButton.addEventListener('click', () => {
    if (detailButton.value === '자세히보기') {
        document.querySelector('.page-1').classList.remove('visible');
        document.querySelector('.page-2').classList.add('visible');
        detailButton.value = '접기';
    } else {
        document.querySelector('.page-1').classList.add('visible');
        document.querySelector('.page-2').classList.remove('visible');
        detailButton.value = '자세히보기';
    }
});

// 맥주 좋아요(추천) 버튼
const searchForm = window.document.getElementById('searchForm');
const beerLikeButton = window.document.getElementById('beerLikeButton');
const likeToggleElement = document.querySelector('[rel = "likeToggle"]');

if (!likeToggleElement.classList.contains('prohibited')) {
    likeToggleElement.addEventListener('click', e => {
        e.preventDefault();

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        const method = beerLikeButton.classList.contains('liked') ? 'DELETE' : 'POST';
        formData.append('beerIndex', searchForm['beerIndex'].value);
        xhr.open(method, './beerLike');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success' :
                            // 값을 받아와서 innerText
                            alert('추천이 반영되었습니다.');
                            window.location.reload();
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

// 본인글 수정 삭제 메뉴
const menuIcon = document.getElementById('menuIcon');
const xButton = document.getElementById('xButton');
if (menuIcon !== null) {
    menuIcon.addEventListener('click', () => {
        document.getElementById('menuBox').classList.add('visible');
    });

    xButton.addEventListener('click', () => {
        document.getElementById('menuBox').classList.remove('visible');
    });
}


// 리뷰 삭제
const reviewListForm = document.getElementById('reviewListForm');
const deleteButton = document.getElementById('deleteButton');
if (deleteButton !== null) {
    deleteButton.addEventListener('click', () => {
        if (!confirm('정말로 게시글을 삭제할까요?')) {
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('aid', reviewListForm['aid'].value);

        xhr.open('DELETE', "./reviewRead");
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            const beerIndex = reviewListForm['beerIndex'].value;
                            window.location.href = `reviewList?beerIndex=${beerIndex}`;
                            alert('게시글이 삭제되었습니다.');
                            break;
                        case 'no_such_article':
                            alert('삭제하려는 게시글이 더 이상 존재하지 않습니다.\n\n이미 삭제되었을 수도 있습니다.');
                            break
                        case 'not_allowed':
                            alert('해당 게시글을 삭제할 권한이 없습니다.');
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
}
