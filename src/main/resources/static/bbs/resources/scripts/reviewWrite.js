const reviewForm = window.document.getElementById('reviewForm');

reviewForm.querySelector('[rel="backButton"]').addEventListener('click', () => {
    window.history.back();
})

reviewForm['beerSelectButton'].addEventListener('click', () => {
    window.location.href = '/product'
});

const reviewStarArray = Array.from(reviewForm.querySelector('[rel="starContainer"]').querySelectorAll(':scope > .star'));
for (let i = 0; i < reviewStarArray.length; i++) {
    reviewStarArray[i].addEventListener('click', () => {
        reviewStarArray.forEach(x => x.classList.remove('selected'));
        for (let j = 0; j<=i; j++) {
            reviewStarArray[j].classList.add('selected');
        }
        reviewForm.querySelector('[rel="score"]').innerText = i + 1;
        reviewForm['score'].value = i + 1;
    });
}

reviewForm.onsubmit = e => {
    if (reviewForm['score'].value === '0') {
        alert('별점을 선택해 주세요.');
        return false;
    }
    if (reviewForm['contentGood'].value === '' || reviewForm['contentGood'].value.length < 20) {
        alert('내용을 20자 이상 입력해 주세요.');
        reviewForm['contentGood'].focus();
        return false;
    }
    if (reviewForm['contentBad'].value === '' || reviewForm['contentBad'].value.length < 20) {
        alert('내용을 20자 이상 입력해 주세요.');
        reviewForm['contentBad'].focus();
        return false;
    }

    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('beerIndex', reviewForm['beerIndex'].value);
    formData.append('score', reviewForm['score'].value);
    formData.append('contentGood', reviewForm['contentGood'].value);
    formData.append('contentBad', reviewForm['contentBad'].value);

    xhr.open('POST', './bbs/reviewWrite');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >=200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'not_signed':
                        alert('로그인되어있지 않습니다. 로그인 후 다시 시도해 주세요.');
                        break;
                    case 'success':
                        const aid = responseObject['aid'];
                        window.location.href = `read?aid=${aid}`;
                        break;
                    default:
                        alert('알 수 없는 이유로 리뷰를 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}