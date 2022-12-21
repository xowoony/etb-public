const reviewForm = window.document.getElementById('reviewForm');

// 뒤로가기 버튼
reviewForm.querySelector('[rel="backButton"]').addEventListener('click', () => {
    window.history.back();
});

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


// 실시간 글자수 세기
let inputGood = document.getElementById('inputGood');
let inputBad = document.getElementById('inputBad');
let goodTextCount = document.getElementById('goodTextCount');
let badTextCount = document.getElementById('badTextCount');

inputGood.addEventListener(('input'), ()=> {
    // 좋았던 점 글자수세기(공백포함)
    goodTextCount.innerHTML = `${inputGood.value.length}`;
});
inputBad.addEventListener(('input'), ()=>{
    // 아쉬운 점 글자수세기(공백포함)
    badTextCount.innerHTML = `${inputBad.value.length}`;
});

// 리뷰 등록 버튼 클릭
reviewForm.onsubmit = e => {
    e.preventDefault();
    if (reviewForm['score'].value === '0') {
        alert('별점을 선택해 주세요.');
        return false;
    }
    if (inputGood.value === '' || `${inputGood.value.length}` < 20) {
        alert('내용을 20자 이상 입력해 주세요.');
        reviewForm['contentGood'].focus();
        return false;
    }
    if (inputBad.value === '' || `${inputBad.value.length}` < 20) {
        alert('내용을 20자 이상 입력해 주세요.');
        reviewForm['contentBad'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('beerIndex', reviewForm['beerIndex'].value);
    formData.append('score', reviewForm['score'].value);
    formData.append('contentGood', inputGood.value);
    formData.append('contentBad', inputBad.value);

    xhr.open('POST', './reviewWrite');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >=200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'not_signed':
                        alert('로그인되어있지 않습니다. 로그인 후 다시 시도해 주세요.');
                        break;
                    case 'no_more_review':
                        alert('이미 작성된 리뷰가 있습니다.');
                        break;
                    case 'success':
                        alert('리뷰가 등록되었습니다.')
                        const beerIndex = reviewForm['beerIndex'].value;
                        window.location.href = `reviewList?beerIndex=${beerIndex}`;
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