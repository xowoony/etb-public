const form = window.document.getElementById('form');



// 페스티벌 내용 입력
form.onsubmit = e =>{

    if (form['festivalTitle'].value === '') {
        alert('축제명을 입력해주세요.');
        form['festivalTitle'].focus();
        return false;
    }

    if (form['festivalImage'].value === '') {
        alert('축제이미지를 업로드해주세요.');
        form['festivalImage'].focus();
        return false;
    }

    if (form['festivalDescription'].value === '') {
        alert('축제소개 내용을 입력해주세요.');
        form['festivalDescription'].focus();
        return false;
    }

    if (form['festivalAddress'].value === '') {
        alert('축제주소지를 입력해주세요.');
        form['festivalAddress'].focus();
        return false;
    }

    if (form['festivalDateFrom'].value === '' || form['festivalDateTo'].value === '') {
        alert('축제일정을 입력해주세요.');
        form['festivalDateFrom'].focus();
        form['festivalDateTo'].focus();
        return false;
    }

    if (form['festivalTimeFrom'].value === '' || form['festivalTimeTo'].value === '') {
        alert('축제시간을 입력해주세요.');
        form['festivalTimeFrom'].focus();
        form['festivalTimeTo'].focus();
        return false;
    }

    if (form['festivalLatitude'].value === '' || form['festivalLongitude'].value === '') {
        alert('축제장소를 입력해주세요.');
        form['festivalLatitude'].focus();
        form['festivalLongitude'].focus();
        return false;
    }


    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();



    // mime 타입을 구하는 코드
    // 참조: https://stackoverflow.com/questions/18299806/how-to-check-file-mime-type-with-javascript-before-upload
    let control = document.getElementById("fImage");
    let files = control.files;
    let blob
    for (let i = 0; i < files.length; i++) {
        console.log("Filename: " + files[i].name);
        console.log("Type: " + files[i].type);
        console.log("Size: " + files[i].size + " bytes");
        blob = files[i];
    }



    // 서버로 보낼 페스티벌 데이터
    formData.append('title', form['festivalTitle'].value);
    formData.append('description', form['festivalDescription'].value);
    formData.append('address', form['festivalAddress'].value);
    formData.append('dateFromStr', form['festivalDateFrom'].value);
    formData.append('dateToStr', form['festivalDateTo'].value);
    formData.append('timeFromStr', form['festivalTimeFrom'].value);
    formData.append('timeToStr', form['festivalTimeTo'].value );
    formData.append('latitude', form['festivalLatitude'].value);
    formData.append('longitude', form['festivalLongitude'].value);
    formData.append('titleImageType', blob.type);

    // 이 2가지들은 고정이 되이있기에 단순 문자열로 전송
    formData.append('boardId', 'festival');
    formData.append('userEmail', 'admin@admin');


    //이미지는 특수하게 바이트형태로 보낸다.
    for (let file of form['festivalImage'].files) {
        formData.append('titleImageTemp', file);
    }

    xhr.open('POST', './festivalAdminFestivalWrite');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success':
                        alert('입력 완료')
                        window.location.href = './festivalAdminFestivalRead';
                        break;
                    default:
                        alert('알 수 없는 이유로 입력하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);


}