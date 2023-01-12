const form = window.document.getElementById('form');



// 페스티벌 내용 입력
form.onsubmit = e =>{

    if (form['beerTitle'].value === '') {
        alert('맥주명을 입력해주세요.');
        form['beerTitle'].focus();
        return false;
    }


    if (form['beerDescription'].value === '') {
        alert('맥주소개 내용을 입력해주세요.');
        form['beerDescription'].focus();
        return false;
    }

    if (form['beerMfr'].value === '') {
        alert('맥주소개 내용을 입력해주세요.');
        form['beerMfr'].focus();
        return false;
    }


    // 맥주 용량이 입력되었는 지를 먼저 확인하고 그다음 올바른 단위가 입력되었는지를 확인
    if (form['beerVolume'].value === '') {
        alert('맥주용량을 입력해주세요.');
        form['beerVolume'].focus();
        return false;
    }


    if( form['beerVolume'].value.includes('ml') ||
        form['beerVolume'].value.includes('l') ||
        form['beerVolume'].value.includes('ML') ||
        form['beerVolume'].value.includes('L')){

    } else{
        alert('올바른 단위를 입력해주세요. (ex: ml, l)');
        form['beerVolume'].focus();
        return false;
    }


    if (form['beerDegree'].value === '') {
        alert('맥주도수를 입력해주세요.');
        form['beerDegree'].focus();
        return false;
    }



    if (form['beerCategory'].value === '') {
        alert('맥주분류를 입력해주세요.');
        form['beerCategory'].focus();
        return false;
    }


    e.preventDefault();
    const xhr = new XMLHttpRequest();
    const formData = new FormData();





    // 서버로 보낼 맥주 데이터
    formData.append('index', form['index'].value);
    formData.append('name', form['beerTitle'].value);
    formData.append('description', form['beerDescription'].value);
    formData.append('mfr', form['beerMfr'].value);
    formData.append('volume', form['beerVolume'].value);
    formData.append('degree', form['beerDegree'].value);
    formData.append('categoryIndex', form['beerCategory'].value);


    // mime 타입을 구하는 코드
    // 참조: https://stackoverflow.com/questions/18299806/how-to-check-file-mime-type-with-javascript-before-upload
    //이미지는 특수하게 바이트형태로 보낸다.
    //수정페이지에서 이미지를 삽입하였으면 아래구문을 실행하고 그렇지 않으면 실행하는 않는 로직
    if(form['beerImage'].value != ''){

        let control = document.getElementById("beerFile");
        let files = control.files;
        let blob

        for (let i = 0; i < files.length; i++) {
            console.log("Filename: " + files[i].name);
            console.log("Type: " + files[i].type);
            console.log("Size: " + files[i].size + " bytes");
            blob = files[i];
        }


        if(blob.type != ''){
            formData.append('imageType', blob.type);
        }



        for (let file of form['beerImage'].files) {
            formData.append('beerImage', file);
        }
    }


    xhr.open('PATCH', './beerAdminModify');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success':
                        alert('수정 완료')
                        window.location.href = './beerAdmin';
                        break;
                    default:
                        alert('알 수 없는 이유로 수정하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);


}