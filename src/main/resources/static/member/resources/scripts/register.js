const form = window.document.getElementById('form');
const Warning = {
    show: (text) => {
        form.querySelector('[rel="warningText"]').innerText = text;
        form.querySelector('[rel="warning"]').classList.add('visible');
    },
    hide: () => {
        form.querySelector('[rel="warning"]').classList.remove('visible');
    }
}
const EmailWarning = {
    show: (text) => {
        const emailWarning = form.querySelector('[rel="emailWarning"]');
        emailWarning.innerText = text;
        emailWarning.classList.add('visible');
    },
    hide: () => {
        form.querySelector('[rel="emailWarning"]').classList.remove('visible');
    }
};


form.querySelector('[rel="nextButton"]').addEventListener('click', () => {
    // 다 입력후 다음버튼을 눌렀을 때
    form.querySelector('[rel="warning"]').classList.remove('visible');
    if (form.classList.contains('step1')) {
        if (!form['termAgree'].checked) {
            // 만약 서비스 약관 동의에 체크를 하지 않았다면
            form.querySelector('[rel="warningText"]').innerText = '서비스 이용약관을 읽고 동의해 주세요.';
            //서비스 이용약관 및 개인정보 처리방침을 읽고 동의해 주세요. 자리에
            // innerText로 인해서 글씨가 바뀌게 된다. (경고 메시지 출력)
            form.querySelector('[rel="warning"]').classList.add('visible');
            // 클래스가 추가된다.
            return;
        }
        // form.querySelector('[rel="stepText"]').innerText = '개인정보 입력';
        form.classList.remove('step1')
        // step1 글씨가 사라지게 됨
        form.classList.add('step2');
        // step2 클래스가 추가된다.
    } else if (form.classList.contains('step2')) {
        if (!form['emailSend'].disabled || !form['emailVerify'].disabled) {
            // 만약 email 전송이 비활성화가 안되어 있거나 이메일 확인이 비활성화가 안되어 있을 경우
            Warning.show('이메일 인증을 완료해 주세요.');
            // 이메일 인증을 완료해 주세요 라는 warning 메시지가 출력된다.
            // 위에서 묶어 줬음. Warning.show
            return;
        }
        if (form['password'].value === '') {
            // 만약 password 값이 비었을 경우
            Warning.show('비밀번호를 입력해주세요.')
            // 비밀번호를 입력해주세요 라는 경고 메시지가 나타난다.
            form['password'].focus();
            // password에 포커스
            return;
        }
        if (form['password'].value !== form['passwordCheck'].value) {
            // 만약 비밀번호 입력값과 비밀번호 확인 값이 서로 일치하지 않는다면
            Warning.show('비밀번호가 서로 일치하지 않습니다.')
            // 비밀번호가 서로 일치하지 않습니다 라는 경고 메시지가 나타난다.
            form['password'].focus();
            // password에 포커스
            return;
        }
        if (form['nickname'].value === '') {
            // 만약 닉네임 입력란이 비었다면
            Warning.show('닉네임을 입력해주세요.')
            // 닉네임을 입력해주세요 라는 경고 메시지가 나타난다.
            form['nickname'].focus();
            // nickname에 포커스
            return;
        }

        if (form['name'].value === '') {
            // 만약 이름 입력란이 비었다면
            Warning.show('이름을 입력해주세요.')
            // 이름을 입력해주세요 라는 경고 메시지가 나타난다.
            form['name'].focus();
            // name에 포커스 된다.
            return;
        }
        if (form['contact'].value === '') {
            // 만약 연락처 입력란이 비었다면
            Warning.show('연락처를 입력해주세요.')
            // 연락처를 입력해주세요 라는 경고 메시지가 나타난다.
            form['contact'].focus();
            // contact에 focus 된다.
            return;
        }
        if (form['addressPostal'].value === '' || form['addressPrimary'].value === '') {
            // 우편번호 입력란이 비었거나 또는 주소 입력란이 비었을 경우
            Warning.show('주소 찾기를 통해 주소를 입력해 주세요.')
            // 주소 찾기를 통해 주소를 입력해 주세요 라는 경고 메시지가 나타난다.
            return;
        }

        Cover.show('회원가입 진행중입니다. \n\n잠시만 기다려주세요');
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        //  EmailAuthEntity 가 가지는 모든 멤버에 대응하는 값 Append (index 제외) 불러오도록 한다.
        formData.append('email', form['email'].value);
        formData.append('code', form['emailAuthCode'].value);
        formData.append('salt', form['emailAuthSalt'].value);
        formData.append('password', form['password'].value);
        formData.append('nickname', form['nickname'].value);
        formData.append('name', form['name'].value);
        formData.append('contact', form['contact'].value);
        formData.append('addressPostal', form['addressPostal'].value);
        formData.append('addressPrimary', form['addressPrimary'].value);
        formData.append('addressSecondary', form['addressSecondary'].value);

        // XHR 준비
        xhr.open('POST', './register');
        // 'POST' 방식으로 /member/register로 오픈 .은 현재 위치가 기준이 된다.
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                // 만약 readyState가 완료되었다면.
                Cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    // 상태 코드가 200 이상 300 미만일 경우
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'email_not_verified':
                            // 이메일인증이 확인 되지 않았을 경우
                            Warning.show('이메일 인증이 완료되지 않았습니다.');
                            // 이메일 인증이 완료되지 않았습니다 라는 경고 메시지가 보여진다.
                            break;
                        case 'success':
                            // 이메일 인증이 제대로 되었을 경우
                            // form.querySelector('[rel="stepText"]').innerText = '회원가입 완료';
                            // 오른쪽 이용약관 및 개인정보 처리방침 글씨가 회원가입 완료로 바뀐다.
                            form.querySelector('[rel="nextButton"]').innerText = '로그인하기';
                            // 초록색 다음버튼이 로그인하러가기 라고 바뀐다.
                            form.classList.remove('step2');
                            // step2 클래스가 삭제된다.
                            form.classList.add('step3');
                            // step3 클래스가 추가된다.
                            break;
                        default:
                            Warning.show('알 수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요.');
                        // 그 외의 알 수 없는 이유일 경우 알수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요 라는 경고 메시지가 나타난다.
                    }
                } else {
                    Warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    // 이 모든것에 해당하지 않을 경우 서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요 라는 경고 메시지가 나타난다.
                }
            }
        };
        xhr.send(formData);
        // form.querySelector('[rel="stepText"]').innerText = '회원가입 완료';
        // form.querySelector('[rel="nextButton"]').innerText = '로그인하러 가기';
        // form.classList.remove('step2');
        // form.classList.add('step3');
    } else if (form.classList.contains('step3')) {
        window.location.href = 'login';
    }

});

// 정규식의 경우 이메일 양식을 유저가 올바르게 작성했는지에 대해 확인작업을 해준다
form['emailSend'].addEventListener('click', () => {
    form.querySelector('[rel="emailWarning"]').classList.remove('visible');
    if (form['email'].value === '') {
        // 이메일 양식이 값이 비어있을 경우
        EmailWarning.show('이메일을 입력해주세요')
        // 이메일을 입력해주세요 라는 경고 메시지가 보여진다.
        form['email'].focus();
        return;
    }
    if (!new RegExp('^(?=.{9,50}$)([\\da-zA-Z\\-_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,15})(\\.[a-z]{2})?$').test(form['email'].value)) {
        EmailWarning.show('올바른 이메일 주소를 입력해 주세요.');
        form['email'].focus();
        return;
    }

    // XHR 시작. (위의 작업들을 다 지나쳐 왔을 경우 - 제대로 값들을 다 입력했을 경우)
    Cover.show('인증번호를 전송하고 있습니다.\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    // 앞부분 email은 RequestParam에서 받을 value값이 되고, 뒤에 email은 form 태그안 input 태그 안에 있는
    // email이란 name값을 가진 value이다.
    xhr.open('POST', './email'); // post방식, RequestMapping의 ./email 은 POST를 요청할 도메인이다.
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            Cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'success':
                        form.querySelector('[rel="emailWarning"]').innerText = '인증 번호를 전송하였습니다. 전송된 인증 번호는 5분간만 유효합니다.';
                        form.querySelector('[rel="emailWarning"]').classList.add('visible');
                        form['email'].setAttribute('disabled', 'disabled');
                        form['emailSend'].setAttribute('disabled', 'disabled');
                        form['emailAuthCode'].removeAttribute('disabled');
                        //disabled 풀어주고
                        form['emailAuthCode'].focus();
                        form['emailAuthSalt'].value = responseObject['salt'];
                        form['emailVerify'].removeAttribute('disabled');
                        break;
                    case 'email_duplicated':
                        form.querySelector('[rel="emailWarning"]').innerText = '해당 이메일은 이미 사용 중입니다.';
                        form.querySelector('[rel="emailWarning"]').classList.add('visible');
                        form['email'].focus();
                        form['emial'].select();
                        break;
                    default:
                        form.querySelector('[rel="emailWarning"]').innerText = '알 수 없는 이유로 인증 번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.'
                        form.querySelector('[rel="emailWarning"]').classList.add('visible');
                        form['email'].focus();
                        form['email'].select();
                }
            } else {
                form.querySelector('[rel="emailWarning"]').innerText = '서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.';
                form.querySelector('[rel="emailWarning"]').classList.add('visible');
            }
        }
    };
    xhr.send(formData);
});

form['emailVerify'].addEventListener('click', () => {
    if (form['emailAuthCode'].value === '') {
        EmailWarning.show('인증번호를 입력해 주세요.');
        form['emailAuthCode'].focus();
        return;
    }

    if (!new RegExp('^(\\d{6})$').test(form['emailAuthCode'].value)) {
        EmailWarning.show('올바른 인증 번호를 입력해 주세요.');
        form['emailAuthCode'].focus();
        form['emailAuthCode'].select();
        return;
    }
    Cover.show('인증번호를 확인하고 있습니다.\n\n 잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    formData.append('code', form['emailAuthCode'].value);
    formData.append('salt', form['emailAuthSalt'].value);
    xhr.open('PATCH', 'email');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            Cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'expired':
                        EmailWarning.show('인증 정보가 만료되었습니다. 다시 시도해 주세요');
                        form['email'].removeAttribute('disabled');
                        form['email'].focus();
                        form['email'].select();
                        form['emailSend'].removeAttribute('disabled');
                        form['emailAuthCode'].value = '';
                        form['emailAuthCode'].setAttribute('disabled', 'disabled');
                        form['emailAuthSalt'].value = '';
                        form['emailVerify'].setAttribute('disabled', 'disabled');
                        break;
                    case 'success':
                        EmailWarning.show('이메일이 정상적으로 인증되었습니다.');
                        form['emailAuthCode'].setAttribute('disabled', 'disabled');
                        form['emailVerify'].setAttribute('disabled', 'disabled');
                        form['password'].focus();
                        break;
                    default:
                        EmailWarning.show('인증 번호가 올바르지 않습니다.');
                        form['emailAuthCode'].focus();
                        form['emailAuthCode'].select();
                }
            } else {
                EmailWarning.show(
                    '서버와 통신하지 못했습니다.잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
});

// 우편번호 찾기 기능
form['addressFind'].addEventListener('click', () => {
    new daum.Postcode({
        oncomplete: e => {
            form.querySelector('[rel="addressFindPanel"]').classList.remove('visible');
            form['addressPostal'].value = e['zonecode'];
            form['addressPrimary'].value = e['address'];
            form['addressSecondary'].value = '';
            form['addressSecondary'].focus();
        }
    }).embed(form.querySelector('[rel="addressFindPanelDialog"]'));
    form.querySelector('[rel="addressFindPanel"]').classList.add('visible');
});

form.querySelector('[rel="addressFindPanel"]').addEventListener('click', () => {
    form.querySelector('[rel="addressFindPanel"]').classList.remove('visible');
});