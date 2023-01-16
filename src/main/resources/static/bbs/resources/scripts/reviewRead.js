const reviewForm = window.document.getElementById('reviewForm');

// 뒤로가기 버튼
reviewForm.querySelector('[rel="backButton"]').addEventListener('click', () => {
    window.history.back();
});

reviewForm['beerSelectButton'].addEventListener('click', () => {
    window.location.href = '/product'
});

// const reviewStarArray = Array.from(reviewForm.querySelector('[rel="starContainer"]').querySelectorAll(':scope > .star'));
// for (let i = 0; i < reviewStarArray.length; i++) {
//     reviewStarArray[i].addEventListener('click', () => {
//         reviewStarArray.forEach(x => x.classList.remove('selected'));
//         for (let j = 0; j<=i; j++) {
//             reviewStarArray[j].classList.add('selected');
//         }
//         reviewForm.querySelector('[rel="score"]').innerText = i + 1;
//         reviewForm['score'].value = i + 1;
//     });
// }


// 리뷰 삭제
function beforeCheck() {
    if(!confirm('정말로 게시글을 삭제할까요?')) {
        return;
    } else {

    }
}

reviewForm.onsubmit = e => {
    e.preventDefault()
    if (!confirm('정말로 게시글을 삭제할까요?')) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('aid', reviewForm['aid'].value);

    xhr.open('DELETE', "./reviewRead");
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success':
                        const beerIndex = reviewForm['beerIndex'].value;
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
}