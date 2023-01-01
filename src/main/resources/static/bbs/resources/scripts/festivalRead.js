var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
const festivalReviewForm = document.getElementById('festivalReviewForm'); //축제와 관련된 리뷰영역

                                // 지도관련



var options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(35.85086, 128.558785), //지도의 중심좌표.
    level: 3 //지도의 레벨(확대, 축소 정도)
};

var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴


var markerPosition  = new kakao.maps.LatLng(35.85086, 128.558785);

// 마커를 생성합니다
var marker = new kakao.maps.Marker({
    position: markerPosition
});

// 마커가 지도 위에 표시되도록 설정합니다
marker.setMap(map);



// var iwContent = '<div style="padding:5px; color: black">대구 치맥 페스티벌 </div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
//     iwPosition = new kakao.maps.LatLng(35.85086, 128.558785); //인포윈도우 표시 위치입니다

var iwContent = '<div style="padding:5px; color: black" th:text="${}">대구 치맥 페스티벌 </div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
    iwPosition = new kakao.maps.LatLng(35.85086, 128.558785); //인포윈도우 표시 위치입니다

// 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({
    position : iwPosition,
    content : iwContent
});

// 마커 위에 인포윈도우를 표시합니다. 두번째 파라미터인 marker를 넣어주지 않으면 지도 위에 표시됩니다
infowindow.open(map, marker);




                        //  댓글 관련


// 댓글 작성 Form
if(festivalReviewForm != null){
    festivalReviewForm.onsubmit = e =>{

        if(festivalReviewForm['content'].value === '')
        {
            alert('댓글을 입력해주세요.');
            festivalReviewForm['content'].focus();
            return false;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();

        formData.append('articleIndex', festivalReviewForm['aid'].value);
        formData.append('content', festivalReviewForm['content'].value);

        xhr.open('POST', `./festivalRead`);
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE){
                if(xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']){
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



                    //