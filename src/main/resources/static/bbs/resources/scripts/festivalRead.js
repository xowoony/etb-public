var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
const festivalReviewForm = document.getElementById('festivalReviewForm'); //축제와 관련된 리뷰영역
const festivalDataForm = document.getElementById('festivalDataForm');
const modifyCommentForm = document.getElementById('modify');
const commentForm = window.document.getElementById('comment');

// 지도관련

let longi = festivalDataForm['longitude'].value;
let lati = festivalDataForm['latitude'].value;
let festivalTitle = festivalDataForm['festivalTitle'].value;


var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(lati, longi), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// 마커가 표시될 위치입니다
var markerPosition = new kakao.maps.LatLng(lati, longi);

// 마커를 생성합니다
var marker = new kakao.maps.Marker({
    position: markerPosition
});

// 마커가 지도 위에 표시되도록 설정합니다
marker.setMap(map);


//  댓글 관련


// 댓글 작성 Form
if (festivalReviewForm != null) {
    festivalReviewForm.onsubmit = e => {

        // 입력시 화면이 전환되지 않게 한다.
        // 입력시 지정하지 않아 계속 400 오류가 발생하였는데 이는 해당 값(index, title 등등)이 계속 새로고침되어 사라져서 발생하였다.
        // post 같은 입력형태를 할 시 반드시 preventDefault를 지정하여 이를 방지해야한다.
        e.preventDefault();

        if (festivalReviewForm['content'].value === '') {
            alert('댓글을 입력해주세요.');
            festivalReviewForm['content'].focus();
            return false;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();

        // formData.append('index', festivalReviewForm['index'].value);
        formData.append('articleIndex', festivalReviewForm['aid'].value);
        formData.append('content', festivalReviewForm['content'].value);

        xhr.open('POST', `./festivalRead`);
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            alert('댓글을 작성하였습니다.');
                            window.location.href = `festivalRead?index=${festivalReviewForm['aid'].value}`
                            break;
                        default:
                            alert('적절하지 못한 처리로 댓글을 작성하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                    }
                } else {
                    alert('알 수 없는 이유로 댓글을 작성하지 못하였습니다.\n\n잠시 후 다시 시도해 주세요.');
                }
            }
        }
        xhr.send(formData);
    }
}


// 댓글 삭제


const commentLists = document.querySelectorAll('[rel="commentList"]');


for (let comment of commentLists) {
    const deleteComment = comment.querySelector('[rel="deleteButton"]')
    deleteComment?.addEventListener('click', e => {
            e.preventDefault();
            if (!confirm('정말로 게시글을 삭제하시겠습니까?')) {
                return;
            }

            console.log(deleteComment.dataset.index);

            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('index', deleteComment.dataset.index);
            xhr.open('DELETE', `./festivalComment`);
            xhr.onreadystatechange = () => {


                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject['result']) {
                            case 'success':
                                alert('댓글을 삭제하였습니다.');
                                window.location.href = `festivalRead?index=${festivalReviewForm['aid'].value}`
                                break;
                            default:
                                alert('알 수 없는 이유로 게시글을 삭제하는데 실패하였습니다.');
                        }
                    } else {
                        alert('서버와 통신을 하는데 실패하였습니다. 다시 시도해주세요.');
                    }
                }
            }
            xhr.send(formData);
        }
    )
}


// 댓글 수정

// const modifyForm = document.querySelector('[rel="modifyForm"]');
// const commentContent = document.querySelector('[rel="commentContent"]');
// const commentButton = document.querySelector('[rel="commentButton"]');
// const commentWritten = document.querySelector('[rel="commentWritten"]');


// comment와 modifyButton, cancelButton의 querySelector 지정은 올바르게 되었으나, 상단에서 지정된 modifyForm, commentContent,
// commentButton, commentWritten들은 각 리스트의 첫번째 요소만 반환하게 되었다. 즉 comment에서 따로 다시 지정하지 않아서 리스트의 첫번째
// 부분만 항상 출력이나 취소되게 되어서 문제가 발생하였다.

// 수정 버튼 클릭
for (let comment of commentLists) {
    const modifyComment = comment.querySelector('[rel="modifyButton"]')
    modifyComment?.addEventListener('click', e => {
            e.preventDefault();

            const modifyForm = comment.querySelector('[rel="modifyForm"]');
            const commentContent = comment.querySelector('[rel="commentContent"]');
            const commentButton = comment.querySelector('[rel="commentButton"]');
            const commentWritten = comment.querySelector('[rel="commentWritten"]');

            modifyForm.classList.add('visible');
            commentContent.classList.add('hidden');
            commentButton.classList.add('hidden');
            commentWritten.classList.add('hidden');
        }
    )
}
;


// 취소 버튼 클릭
for (let comment of commentLists) {
    const cancelComment = comment.querySelector('[rel="cancelButton"]')
    cancelComment?.addEventListener('click', e => {
            e.preventDefault();

            const modifyForm = comment.querySelector('[rel="modifyForm"]');
            const commentContent = comment.querySelector('[rel="commentContent"]');
            const commentButton = comment.querySelector('[rel="commentButton"]');
            const commentWritten = comment.querySelector('[rel="commentWritten"]');

            modifyForm.classList.remove('visible');
            commentContent.classList.remove('hidden');
            commentButton.classList.remove('hidden');
            commentWritten.classList.remove('hidden');
        }
    )
}
;

const modifyCheck = document.getElementById('modifyByIndex');

// 수정 확인 클릭

for (let comment of commentLists) {

    const modifyCheck = comment.querySelector('[rel="modifyCheck"]')
    modifyCheck?.addEventListener('click', e => {

            e.preventDefault();

            const modifyContent = comment.querySelector('[rel="modifyContent"]');
            const modifyIndex = comment.querySelector('[rel="modifyIndex"]');


            if (modifyContent.value === '') {
                alert('수정할 댓글을 입력해주세요.');
                modifyContent.focus();
                return false;
            }


            if (!confirm('댓글을 수정하시겠습니까?')) {
                return;
            }


            console.log('11111111111');
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('index', modifyIndex.value);
            formData.append('content', modifyContent.value);


            xhr.open('PATCH', `./festivalComment`);


            console.log('22222222222222222');
            xhr.onreadystatechange = () => {

                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        const responseObject = JSON.parse(xhr.responseText);
                        switch (responseObject['result']) {
                            case 'success':
                                alert('댓글을 수정하였습니다.');
                                window.location.href = `festivalRead?index=${festivalReviewForm['aid'].value}`
                                break;
                            default:
                                alert('알 수 없는 이유로 게시글을 수정하는데 실패하였습니다.');
                        }
                    } else {
                        alert('서버와 통신을 하는데 실패하였습니다. 다시 시도해주세요.');
                    }
                }
            }
            xhr.send(formData);


        }
    );
}
