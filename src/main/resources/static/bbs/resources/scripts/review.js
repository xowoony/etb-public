// for(let i = 0; i < document.querySelectorAll('.declaration').length; i++) {
//     if(document.querySelectorAll('.declaration')[i].value === '신고완료') {
//         document.querySelectorAll('.declaration')[i].style.color = "red";
//     }
// }
const declarations = document.querySelectorAll('.declaration');
for(let declation of declarations) {
    if (declation.value === '신고완료') {
        declation.style.color = "grey";
    }
}



// 리뷰 좋아요
const reviewListForms = document.querySelectorAll('[rel="reviewListForm"]');
// const reviewToggleElements = document.querySelectorAll('[rel = "reviewToggle"]');
// const reviewToggleElement = document.querySelector('[rel="reviewToggle"]');

for (let reviewListForm of reviewListForms) {
    const reviewToggleElement = reviewListForm.querySelector('[rel="reviewToggle"]')
    if (!reviewToggleElement.classList.contains('prohibited')) {
        reviewToggleElement.addEventListener('click', e => {
            e.preventDefault();

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            const method = reviewToggleElement.parentElement.classList.contains('liked') ? 'DELETE' : 'POST';
            formData.append('articleIndex', reviewToggleElement.dataset.aid);
            xhr.open(method, './reviewLike');
            xhr.onreadystatechange = () => {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject['result']) {
                            case 'success' :
                                // 값을 받아와서 innerText
                                if(responseObject['isLiked'] === true) {
                                    reviewToggleElement.parentElement.classList.add('liked');
                                    reviewToggleElement.parentNode.parentNode.querySelector('.review-like-count').innerHTML = responseObject['likeCount'];
                                    reviewToggleElement.value = "추천취소"
                                } else {
                                    reviewToggleElement.parentElement.classList.remove('liked');
                                    reviewToggleElement.parentNode.parentNode.querySelector('.review-like-count').innerHTML = responseObject['likeCount'];
                                    reviewToggleElement.value = "추천하기"
                                }
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

    // 리뷰 신고
    const declaButton = reviewListForm.querySelector('[rel="declaButton"]');
    if (declaButton !== null) {
        if (!declaButton.classList.contains('prohibited') && declaButton.value === '신고하기') {
            declaButton.addEventListener('click', e => {
                e.preventDefault();
                if (declaButton.value === '신고완료') {
                    alert('신고가 완료되었습니다.');
                    return;
                }
                if (!confirm('정말로 게시글을 신고할까요?')) {
                    return;
                }

                const xhr = new XMLHttpRequest();
                const formData = new FormData();
                formData.append('aid', reviewToggleElement.dataset.aid);
                xhr.open('POST', './reviewDeclaration');
                xhr.onreadystatechange = () => {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status >= 200 && xhr.status < 300) {
                            const responseObject = JSON.parse(xhr.responseText);
                            switch (responseObject['result']) {
                                case 'success' :
                                    // 값을 받아와서 innerText
                                    if(responseObject['isDeclared'] === true) {
                                        declaButton.value = '신고완료'
                                        declaButton.style.color = "grey"
                                    }
                                    alert('신고가 완료되었습니다.');
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
        } else  {
            declaButton.addEventListener('click', () => {
                alert('신고가 완료되었습니다.');
            });
        }
    }

    // 본인글 수정 삭제 메뉴
    const menuIcon = reviewListForm.querySelector('[rel="menuIcon"]');
    const xButton = reviewListForm.querySelector('[rel="xButton"]');
    if (menuIcon !== null) {
        menuIcon.addEventListener('click', () => {
            reviewListForm.querySelector('[rel= "menuBox"]').classList.add('visible');
        });

        xButton.addEventListener('click', () => {
            reviewListForm.querySelector('[rel= "menuBox"]').classList.remove('visible');
        });
    }

    // 리뷰 삭제
    const deleteButton = reviewListForm.querySelector('[rel="deleteButton"]');
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
                                window.location.href = `review`;
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
}


// 게시글 정렬
// 추천순
sortGood.addEventListener('click', () => {
    window.location.href = `./review?sort=good`;
    sortGood.scrollIntoView();
});
// 최신순
const sortNew = window.document.getElementById('sortNew');
sortNew.addEventListener('click', () => {
    window.location.href = `./review?sort=new`;
    sortNew.scrollIntoView();
});


// 평점별 보기
const starRank = window.document.getElementById('starRank');
starRank.onchange = e => {
    e.preventDefault();
    const value = (starRank.options[starRank.selectedIndex].value);
    window.location.href = `./review?starRank=${value}`;
}