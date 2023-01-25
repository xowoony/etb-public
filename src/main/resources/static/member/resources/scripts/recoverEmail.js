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

const Email = {
    getElement: () => form.querySelector('[rel="messageRow"]'),
    show: (text) => {
        const messageRow = Email.getElement();
        messageRow.querySelector('.text').innerText = text;
        messageRow.classList.add('visible');
    },
    hide: () => Email.getElement().classList.remove('visible') // 안보이게 하기
};



let text = form.querySelector('.text');
let emailAlertText = form.querySelector('.emailAlertText');
let goLoginText = form.querySelector('.goLoginText');


form.onsubmit = (e) => {
    e.preventDefault();
    Warning.hide();
    if (form['name'].value === '') {
        form.querySelector('.warning-row').classList.add('visible');
        text.innerText = '이름을 입력해 주세요.';
        form['name'].focus();
        return false;
    }
    if (form['contact'].value === '') {
        form.querySelector('.warning-row').classList.add('visible');
        text.innerText = '연락처를 입력해 주세요.';
        form['contact'].focus();
        return false;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('name', form['name'].value);
    formData.append('contact', form['contact'].value);
    xhr.open('POST', '/member/recoverEmail');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            Cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                console.log(responseObject);
                switch (responseObject['result']) {
                    case 'success':
                        // emailAuthIndex = responseObject['index'];
                        form['name'].setAttribute('disabled', 'disabled');
                        form['contact'].setAttribute('disabled', 'disabled');
                        form['loginButton'].setAttribute('disabled', 'disabled');
                        form.querySelector('.emailAlertText').classList.add('visible');
                        emailAlertText.innerText = '찾으시는 회원님의 이메일은\n' + responseObject['email'] + '\n입니다.';
                        form.querySelector('.goLoginText').classList.add('visible');
                        // alert('찾으시는 이메일은\n'+ responseObject['email'] + '입니다.');
                        // window.location.href = `/member/login`;
                        form.querySelector('[rel="messageRow"]').classList.add('visible');
                        form.querySelector('[rel="warningRow"]').classList.add('visible');
                        break;
                    default:
                        Warning.show('입력한 정보와 일치하는 회원이 없습니다.');
                        break;
                }
            } else {
                Warning.show('서버와 통신하지 못하였습니다.\n잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}