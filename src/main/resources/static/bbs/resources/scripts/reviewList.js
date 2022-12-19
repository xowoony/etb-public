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