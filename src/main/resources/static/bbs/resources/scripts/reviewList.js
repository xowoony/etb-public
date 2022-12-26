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
