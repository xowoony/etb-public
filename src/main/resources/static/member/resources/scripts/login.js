const form = window.document.getElementById('form');

const Warning = {
    getElementById: () => form.querySelector('[rel="warningRow"]'),
    show: (text) => {
        const warningRow = Warning.getElementById();
        warningRow.querySelector('.text').innerText = text;
        warningRow.classList.add('visible');
    },
    hide: () => Warning.getElementById().classList.remove('visible')
};


let text = form.querySelector('.text');
form.onsubmit = (e) => {
    e.preventDefault();
    Warning.hide();
    if (form['email'].value === '') {
        form.querySelector('.warning-row').classList.add('visible');
        text.innerText = '이메일을 입력해주세요.';
        form['email'].focus();
        return false;
    }
    if (form['password'].value === '') {
        form.querySelector('.warning-row').classList.add('visible');
        text.innerText = '비밀번호를 입력해주세요.';
        form['password'].focus();
        return false;
    }

    Cover.show('로그인 중 입니다.\n\n 잠시만 기다려 주세요.')
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    formData.append('password', form['password'].value);

    xhr.open('POST', '/member/login');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case'success':
                        alert('성공적으로 로그인 되었습니다.');
                        window.location.href = '/';
                            // window.location.reload();
                            break;
                    default:
                        alert('로그인에 실패하였습니다.다시 시도해 주세요.');
                }
            } else {
                Warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}