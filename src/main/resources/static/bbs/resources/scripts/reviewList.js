// 뒤로가기 버튼
document.querySelector('[rel="backButton"]').addEventListener('click', () => {
    window.location.href = '/product'
});

// 맥주 좋아요 버튼
// const likeButton = window.document.querySelector('[rel="likeButton"]');

// likeButton.addEventListener('click', e => {
//
// });


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
beerLikeButton.addEventListener('click', e => {
    e.preventDefault();
    beerLikeButton.querySelector('.fa-regular').classList.contains('visible') ? beerLikeButton.querySelector('.fa-regular').classList.remove('visible') : beerLikeButton.querySelector('.fa-regular').classList.add('visible');
    beerLikeButton.querySelector('.fa-solid').classList.contains('visible') ? beerLikeButton.querySelector('.fa-solid').classList.remove('visible') : beerLikeButton.querySelector('.fa-solid').classList.add('visible');
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    const method = beerLikeButton.querySelector('.fa-solid').classList.contains('visible') ? 'POST' : 'DELETE';
    formData.append('beerIndex', searchForm['beerIndex'].value);
    xhr.open(method, './reviewList');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >=200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success' :
                        alert('추천이 반영되었습니다.');
                        break;
                    case 'failure' :
                        alert('로그인을 해주세요.');
                        window.location.href = 'http://localhost:8080/member/login'
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