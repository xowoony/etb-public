
let prevCtrl = document.getElementById("prev");
let nextCtrl = document.getElementById("next");
let current = 1;
let images = document.getElementsByClassName("image");
let tokens = document.getElementsByClassName("token");

function galleryShiftFn(curr) {
    images = document.getElementsByClassName("image");
    let imgLen = images.length;
    let currClassName, currListItem;
    for (let i = 0; i < imgLen; i++) {
        currTokenItem = tokens[i];
        currListItem = images[i];
        currClassName = currListItem.className;
        currTokenClassName = currTokenItem.className;
        currTokenItem.className = '';
        currListItem.className = '';
        currListItem.classList.add('image');
        currTokenItem.classList.add('token');
        let bkgndClass = currClassName.replace(/(image|previous|next|active|hidden)\s?/ig, '').trim();
        console.log('replacing class: ' + bkgndClass);
        currListItem.classList.add(bkgndClass);
        if (curr === i - 1 ) {
            currListItem.classList.add('previous');
        } else if (curr === i + 1) {
            currListItem.classList.add('next');
        } else if (curr === i) {
            currListItem.classList.add('active');
            currTokenItem.classList.add('active');
        } else {
            currListItem.classList.add('hidden');
        }
    }
    if (curr === 0) {
        prevCtrl.classList.add('disabled')
    } else {
        prevCtrl.classList.remove('disabled');
    }
    if (curr + 1 === imgLen) {
        nextCtrl.classList.add('disabled');
    } else {
        nextCtrl.classList.remove('disabled');
    }
}

if (images.length > 1) {
    prevCtrl.onclick = function(e) {
        console.log('prevCtrl class: ' + prevCtrl.className);
        console.log('current: ' + current);
        if (prevCtrl.classList.contains('disabled') || current === 0) {
            prevCtrl.classList.add('disabled');
            return;
        } else {
            prevCtrl.classList.remove('disabled');
        }
        console.log('prev triggered');
        current--;
        if (current < 0) {
            current = 0;
        }
        galleryShiftFn(current);
    };

    nextCtrl.onclick = function(e) {
        console.log('nextCtrl class: ' + nextCtrl.className);
        console.log('current: ' + current);
        if (nextCtrl.classList.contains('disabled') || current + 1 === images.length) {
            nextCtrl.classList.add('disabled');
            return;
        } else {
            nextCtrl.classList.remove('disabled');
        }
        console.log('next triggered');
        current++;
        let imgLen = images.length;
        if (current >= imgLen) {
            current = imgLen - 1;
        }
        galleryShiftFn(current);
    };

    let tokenLen = tokens.length;
    for (let i = 0; i < tokenLen; i++) {
        tokens[i].onclick = function(e) {
            current = i;
            galleryShiftFn(current);
        };
    }
} else {
    nextCtrl.classList.add('disabled');
    prevCtrl.classList.add('disabled');
}
