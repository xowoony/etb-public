const form = window.document.getElementById('form');
const deleteUserButton = window.document.getElementById('deleteUserButton');
deleteUserButton.addEventListener('click', e => {
    e.preventDefault();
    if (!confirm('정말로 탈퇴하실건가요?')) {
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    formData.append('password',form['password'].value);
    xhr.open('DELETE', './user');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success':
                        alert('회원탈퇴가 완료 되었습니다.');
                        alert('우리 다시 만나요');
                        window.location.href = '/';
                        break;
                    default:
                        alert('알 수 없는 이유로 탈퇴하지 못하였습니다. 다시 시도해주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시후 다시 시도해주세요');
            }
        }
    };
    xhr.send(formData);
});


