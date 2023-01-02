var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
const festivalReviewForm = document.getElementById('festivalReviewForm'); //축제와 관련된 리뷰영역
const festivalDataForm = document.getElementById('festivalDataForm');

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
var markerPosition  = new kakao.maps.LatLng(lati, longi);

// 마커를 생성합니다
var marker = new kakao.maps.Marker({
    position: markerPosition
});

// 마커가 지도 위에 표시되도록 설정합니다
marker.setMap(map);



                        //  댓글 관련


// 댓글 작성 Form
if(festivalReviewForm != null){
    festivalReviewForm.onsubmit = e =>{

        // 입력시 화면이 전환되지 않게 한다.
        // 입력시 지정하지 않아 계속 400 오류가 발생하였는데 이는 해당 값(index, title 등등)이 계속 새로고침되어 사라져서 발생하였다.
        // post 같은 입력형태를 할 시 반드시 preventDefault를 지정하여 이를 방지해야한다.
        e.preventDefault();

        if(festivalReviewForm['content'].value === '')
        {
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



