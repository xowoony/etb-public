const form = window.document.getElementById('form');

form.onsubmit = (e)=>{
    e.preventDefault();

    if (form['nicknameModify'].value === '') {
        alert('변경하실 닉네임을 입력해 주세요.');
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('nickname', form['nicknameModify'].value);
    // formData.append('email', form['email'].value);
    xhr.open('PATCH', './changeNickname');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                console.log('아아아아아아아아아아아아앙4');
                const responseObject = JSON.parse(xhr.responseText);
                console.log(xhr.responseText);
                switch (responseObject['result']) {
                    case'success':
                        alert('닉네임이 성공적으로 변경되었습니다.');
                        window.location.href = 'login';
                        break;
                    default:
                        alert('닉네임을 변경하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    }
    xhr.send(formData);
}

