const Cover = {
    show: (text) => {
        const cover = window.document.getElementById('cover');
        cover.querySelector('[rel="text"]').innerText = text;
        cover.classList.add('visible');
    },
    hide: () => {
        window.document.getElementById('cover').classList.remove('visible');
    }
};


// 회원탈퇴
