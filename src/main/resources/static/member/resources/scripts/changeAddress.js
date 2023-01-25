const form = window.document.getElementById('form');


form.onsubmit = (e)=>{
    e.preventDefault();

    if (form['addressPostal'].value === '') {
        alert('변경하실 주소를 입력해 주세요.');
        return;
    }
    if (form['addressPrimary'].value === '') {
        alert('변경하실 주소를 입력해 주세요.');
        return;
    }


    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('changeAddressPostal', form['addressPostal'].value);
    formData.append('changeAddressPrimary', form['addressPrimary'].value);
    formData.append('changeAddressSecondary', form['addressSecondary'].value);
    xhr.open('PATCH', './changeAddress');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                console.log('주소찾기 자바스크립트까지 잘 됩니당');
                const responseObject = JSON.parse(xhr.responseText);
                console.log(xhr.responseText);
                switch (responseObject['result']) {
                    case'success':
                        alert('주소가 성공적으로 변경되었습니다.');
                        window.location.href = 'myPage';
                        break;
                    default:
                        alert('주소를 변경하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    }
    xhr.send(formData);
}


// 우편번호 변경 기능
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
