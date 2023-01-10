function checkSelectAll()  {
    // 전체 체크박스
    const checkboxes
        = document.querySelectorAll('input[name="myArticle"]');
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="myArticle"]:checked');
    // select all 체크박스
    const selectAll
        = document.querySelector('input[name="selectall"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}

function selectAll(selectAll)  {
    const checkboxes
        = document.getElementsByName('myArticle');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    })
}


function checkSelectAll2()  {
    // 전체 체크박스
    const checkboxes
        = document.querySelectorAll('input[name="myReview"]');
    // 선택된 체크박스
    const checked
        = document.querySelectorAll('input[name="myReview"]:checked');
    // select all 체크박스
    const selectAll
        = document.querySelector('input[name="selectall2"]');

    if(checkboxes.length === checked.length)  {
        selectAll.checked = true;
    }else {
        selectAll.checked = false;
    }

}

function selectAll2(selectAll)  {
    const checkboxes
        = document.getElementsByName('myReview');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked
    })
}