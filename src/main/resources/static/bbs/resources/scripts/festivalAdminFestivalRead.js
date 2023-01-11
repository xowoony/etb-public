

//리스트 형태로 입력되기에 리스트 하나만 담는 querySelector가 아닌 querySelectorAll로 지정해야한다.
const festivalLists = document.querySelectorAll('[rel="festivalContainer"]');

for(let festivalList of festivalLists){
    const deleteFestival = festivalList.querySelector('[rel="deleteButton"]')
    deleteFestival?.addEventListener('click', e => {
        e.preventDefault();

        if (!confirm('정말로 게시물을 삭제하시겠습니까?')) {
            return;
        }


        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('index', deleteFestival.dataset.index);
        xhr.open('DELETE', `./festivalAdminFestivalRead`);
        xhr.onreadystatechange = () => {


            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseObject = JSON.parse(xhr.responseText);
                    switch (responseObject['result']) {
                        case 'success':
                            alert('축제게시물을 삭제하였습니다.');
                            window.location.href = `./festivalAdminFestivalRead`;
                            break;
                        default:
                            alert('알 수 없는 이유로 축제게시물을 삭제하는데 실패하였습니다.');
                    }
                } else {
                    alert('서버와 통신을 하는데 실패하였습니다. 다시 시도해주세요.');
                }
            }
        }
        xhr.send(formData);

    })


}