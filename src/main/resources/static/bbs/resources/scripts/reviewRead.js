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

