const form = window.document.getElementById('form');

const Warning = {
    show: (text) => {
        form.querySelector('[rel="warningText"]').innerText = text;
        form.querySelector('[rel="warningRow"]').classList.add('visible');
    },
    hide: () => {
        form.querySelector('[rel="warningRow"]').classList.remove('visible');
    }
};

const url = new URL(window.location.href);
const searchParams = url.searchParams;  // 이건 comment?aid= 뒤에 있는 숫자를 의미한다.
const aid = searchParams.get('aid');
const xhr = new XMLHttpRequest();

let editor;
ClassicEditor
    .create(form['content'], {
        simpleUpload: {
            uploadUrl: './image'
        }
    })
    .then(e => editor = e);

form['back'].addEventListener('click', () => window.history.length < 2 ? window.close() : window.history.back());


form.onsubmit = e => {
    e.preventDefault();
    Warning.hide();

    if (form['title'].value === '') {
        Warning.show('제목을 입력해 주세요.');
        form['title'].focus();
        return false;
    }
    if (form['content'].value === '') {
        Warning.show('내용을 입력해 주세요.');
        editor.focus();
        return false;
    }
    Cover.show('게시글을 수정하고 있어요.\n\n잠시만 기다려 주세요.')
    const xhr = new XMLHttpRequest();
    const formData = new FormData();

    formData.append('title', form['title'].value);
    formData.append('content', editor.getData());

    xhr.open('PATCH', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            Cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseObject = JSON.parse(xhr.responseText);
                switch (responseObject['result']) {
                    case 'no_such_article':
                        Warning.show('게시글을 수정할 수 없습니다. 게시글이 존재하지 않습니다.');
                        break;
                    case 'not_allowed':
                        Warning.show('게시글을 수정할 수 있는 권한이 없거나 로그아웃 되었습니다. 확인 후 다시 시도해 주세요.');
                        break;
                    case 'success':
                        const aid = responseObject['aid'];
                        window.location.href = `read?aid=${aid}`;
                        break;
                    default:
                        Warning.show('알 수 없는 이유로 게시글을 수정하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                Warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    }
    xhr.send(formData);


};